#!/bin/bash

###################### docker ######################
HOME="/Users/zhaohongliang/DockerData"
# network
NETWORK="canary-net"
# containers
CONTAINERS=("mysql-1" "mysql-2" "mysql-3")
PORTS=("3306" "3307" "3308")

###################### mysql #######################
MYSQL_HOME="$HOME/mysql"
MYSQL_1_HOME="$MYSQL_HOME/${CONTAINERS[0]}"
MYSQL_2_HOME="$MYSQL_HOME/${CONTAINERS[1]}"
MYSQL_3_HOME="$MYSQL_HOME/${CONTAINERS[2]}"
MYSQL_HOMES=("$MYSQL_1_HOME" "$MYSQL_2_HOME" "$MYSQL_3_HOME")
DB_USER="root"
DB_PASSWORD="123456"
REPLICATION_USER="replicaion.user"
REPLICATION_PASSWORD="123456"
master_ip=""
master_port=""
master_name=""

MGR_PORT="33061"
group_seeds=""
whitelist=""

# 字母表
ALPHABET=("A" "B" "C" "D" "E" "F" "G" "H" "I" "J" "K" "L" "M" "N" "O" "P" "Q" "R" "S" "T" "U" "V" "W" "X" "Y" "Z")

# 定义旋转的光标符号
SPINNER="/-\|"

# ANSI 转义码定义
RED='\033[0;31m'    # 红色字体
GREEN='\033[0;32m'  # 绿色字体
BLUE='\033[0;34m'   # 蓝色字体
BOLD='\033[1m'      # 加粗字体
NC='\033[0m'        # 恢复默认格式

# 选择 master
function choiceMaster() {
	# Run the command and store the output in a variable
	output=$(docker ps -q --filter "name=mysql" | xargs docker inspect --format '{{.Name}}' | sed 's|^/||' | sort)
	
	if [ ! -n "$output" ]; then
		echo "Output is empty"
		exit 0;
	fi
	
	# Split the output into lines
	IFS=$'\n' read -rd '' -a lines <<<"$output"
	# 数量
	number=${#lines[@]}
	# labels A, B, C
	options=(${ALPHABET[@]:0:$number})
	
	# Prompt the user to choose from A、B or C
	for index in ${!options[@]}; do
		echo "${options[index]}. ${lines[index]}"
		option=${options[index]}
		eval "${option}=${lines[index]}"
	done
	
	while [ -z "$master_name" ]; do
		# User selection
		read -p "Enter your choice (A, B or C): " choice
		
		# Check user choice and display the selected line
		case $choice in
			A | a)
				master_ip=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $A)
				master_port=$(docker port $A | grep 3306 | awk '{print $3}' | cut -d ":" -f 2)
				master_name=$A
				;;
			B | b)
				master_ip=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $B)
				master_port=$(docker port $B | grep 3306 | awk '{print $3}' | cut -d ":" -f 2)
				master_name=$B
				;;
			C | c)
				master_ip=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $C)
				master_port=$(docker port $C | grep 3306 | awk '{print $3}' | cut -d ":" -f 2)
				master_name=$C
				;;
			*) echo "Invalid choice";;
		esac
	done
}

# MGR GROUP
function getGroupSeeds() {
	# Run the command and store the output in a variable
	output=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker ps -q --filter "name=mysql") | sort)
	
	if [ ! -n "$output" ]; then
		echo "Output is empty"
		exit 0;
	fi
	
	# Split the output into lines
	IFS=$'\n' read -rd '' -a lines <<<"$output"
	# 数量
	number=${#lines[@]}
	# labels A, B, C
	options=(${ALPHABET[@]:0:$number})
	
	# Loop through the lines and assign them to options A, B, C
	for index in ${!options[@]}; do
		option=${options[index]}
		eval "${option}=${lines[index]}"
	done
	
	group_seeds="$A:$MGR_PORT,$B:$MGR_PORT,$C:$MGR_PORT"
	# whitelist="$A,$B,$C"
	whitelist=$(docker network inspect $NETWORK | jq -r '.[0].IPAM.Config.[0].Subnet')
}

# 创建 my.cnf
function writeMysqlCnf() {
	getGroupSeeds
	for index in "${!CONTAINERS[@]}"; do
		local_ip=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' ${CONTAINERS[$index]})
		mkdir -p $MYSQL_HOME/${CONTAINERS[$index]}/conf
		cat << EOF >> $MYSQL_HOME/${CONTAINERS[$index]}/conf/my.cnf
[mysqld]
server-id = $((index+1))
# 启用二进制日志
log_bin = mysql-bin
# 将执行的二进制日志事件也记录到自己的二进制日志中，会增加从库写负载和二进制文件大小
log_slave_updates = 1
# 设置binlog格式 STATEMENT(同步SQL脚本) / ROW(同步行数据) / MIXED(混合同步)
binlog_format = ROW
# 考虑到后期故障切换，增加slave的中继日志
relay-log = relay-log-bin

# 全局事务
gtid_mode = ON
# 强制GTID的一致性
enforce_gtid_consistency = ON
# 将master.info元数据保存在系统表中
master_info_repository = TABLE
# 将relay.info元数据保存在系统表中
relay_log_info_repository = TABLE
# 禁用二进制日志事件校验
binlog_checksum = NONE

# 记录事务的算法，官网建议使用 XXHASH64
transaction_write_set_extraction = XXHASH64
# 启动时加载group_replication插件
plugin_load_add='group_replication.so'
# GROUP的名字，是UUID值，可以使用select uuid()生成
loose-group_replication_group_name = '558edd3c-02ec-11ea-9bb3-080027e39bd2'
# 是否随服务器启动而自动启动组复制，不建议直接启动，怕故障恢复时有扰乱数据准确性的特殊情况
loose-group_replication_start_on_boot = OFF
# 本地MGR的IP地址和端口，host:port，是MGR的端口,不是数据库的端口
loose-group_replication_local_address = '$local_ip:$MGR_PORT'
# 需要接受本MGR实例控制的服务器IP地址和端口，是MGR的端口，不是数据库的端口
loose-group_replication_group_seeds = '$group_seeds'
# 开启引导模式，添加组成员，用于第一次搭建MGR或重建MGR的时候使用，只需要在集群内的其中一台开启
loose-group_replication_bootstrap_group = OFF
# 白名单
loose-group_replication_ip_whitelist = '$whitelist'
# 本机ip
report-host = '$local_ip'
# 本机port
report-port = 3306
EOF
	done
}

# 重启mysql集群
function restarMySqlCluster() {
	container_filter=$(docker ps -a | grep sql | awk -F " " '{print $1}')
	echo "Stopping mysql...(might take a while)"
	docker restart $container_filter &> /dev/null
	sleep 10
	echo "${BLUE}===>${NC}${BOLD} Successfully stopped mysql${NC}"
	echo "${BLUE}===>${NC}${BOLD} Successfully started mysql${NC}"
}

# 启动组复制
function startGroupReplication() {
	master_ip_length=${#master_ip}
	font_part=${master_ip:0:$(($master_ip_length-1))}
	last_char=${master_ip:-1}
	replication_host="${font_part}%"
	
	mysql -h 127.0.0.1 -P $master_port -u$DB_USER -p$DB_PASSWORD << EOF
SET session sql_log_bin = 0;
CREATE USER '$REPLICATION_USER'@'$replication_host' IDENTIFIED WITH mysql_native_password BY '$REPLICATION_PASSWORD';
GRANT REPLICATION SLAVE ON *.* TO '$REPLICATION_USER'@'$replication_host';
FLUSH PRIVILEGES;
SET session sql_log_bin = 1;
CHANGE MASTER TO MASTER_USER='$REPLICATION_USER', MASTER_PASSWORD='$REPLICATION_PASSWORD' FOR CHANNEL 'group_replication_recovery';
SET global group_replication_bootstrap_group = ON;
START group_replication;
SET global group_replication_bootstrap_group = OFF;
EOF
	for ((i=0; i<${#CONTAINERS[@]}; i++)); do
		if [ "$master_name" != "${CONTAINERS[i]}" ]; then
			mysql -h 127.0.0.1 -P ${PORTS[i]} -u$DB_USER -p$DB_PASSWORD << EOF
SET session sql_log_bin = 0;
CREATE USER '$REPLICATION_USER'@'$replication_host' IDENTIFIED WITH mysql_native_password BY '$REPLICATION_PASSWORD';
GRANT REPLICATION SLAVE ON *.* TO '$REPLICATION_USER'@'$replication_host';
FLUSH PRIVILEGES;
SET session sql_log_bin = 1;
CHANGE MASTER TO MASTER_USER='$REPLICATION_USER', MASTER_PASSWORD='$REPLICATION_PASSWORD' FOR CHANNEL 'group_replication_recovery';
START group_replication;
EOF
		fi
	done
}
		
# 打印logo
function logo()
{
	color="$(tput setaf 6)"
	normal="$(tput sgr0)"
	printf "${color}"
	echo '  _____  ____ _   ____   ____ _   _____   __  __ ' 
	echo ' / ___/ / __ `/  / __ \ / __ `/  / ___/  / / / / '
	echo '/ /__  / /_/ /  / / / // /_/ /  / /     / /_/ /  '
	echo '\___/  \__,_/  /_/ /_/ \__,_/  /_/      \__, /   '
	echo '                                       /____/   ...is now finished!'
	echo ''
	echo 'Just enjoy it!'
	echo 'p.s. Follow me at https://github.com/hahapigs/canary'
	echo ''
	printf "${normal}"
}

# main
function main() {
	choiceMaster
	writeMysqlCnf
	restarMySqlCluster 
	startGroupReplication
	logo
}

########################## main #########################
main
		
