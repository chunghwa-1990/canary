#!/bin/bash

# 容器名称
CONTAINERS=("mysql-1" "mysql-2" "mysql-3" "proxysql-1" "proxysql-2")
PORTS=("3306" "3307" "3308" "16033" "26033")

HOME="/Users/zhaohongliang/DockerData"

PROXYSQL_HOME="$HOME/proxysql"
PROXYSQL_1_HOME="$PROXYSQL_HOME/${CONTAINERS[3]}"
PROXYSQL_2_HOME="$PROXYSQL_HOME/${CONTAINERS[4]}"
PROXYSQL_HOMES=("$PROXYSQL_1_HOME" "$PROXYSQL_2_HOME")
PROXYSQL_DATA_PATH="/var/lib/proxysql"
PROXYSQL_DATA_FILE=$PROXYSQL_DATA_PATH/proxysql.db

PROXYSQL_1_HOST=""
PROXYSQL_2_HOST=""

MGR_PRIMARY_PORT=""
MGR_PRIMARY_NAME=""
PROXYSQL_PORT=""
PROXYSQL_ADMIN="radmin"
PROXYSQL_ADMIN_PASSWORD="radmin"

HOST_IP=""
HOST_PORT=""
HOST_NAME=""
HOST_LINES=""

DB_USER="root"
DB_PASSWORD="123456"
DB_NEW_PASSWORD="Pass!234"

MONITOR_HOST=""
MONITOR_USER="proxy.monitor"
MONITOR_PASSWORD="123456"
ADMIN_HOST="%"
ADMIN_USER="proxy.admin"
ADMIN_PASSWORD="Pass!234"

# 定义旋转的光标符号
SPINNER="/-\|"

# ANSI 转义码定义
RED='\033[0;31m'    # 红色字体
GREEN='\033[0;32m'  # 绿色字体
BLUE='\033[0;34m'   # 蓝色字体
BOLD='\033[1m'      # 加粗字体
NC='\033[0m'        # 恢复默认格式

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

function choiceProxySql() {
	# Run the command and store the output in a variable
	output=$(docker ps -q --filter "name=proxysql" | xargs docker inspect --format '{{.Name}}' | sed 's|^/||')
	
	if [ ! -n "$output" ]; then
		echo "Output is empty"
		exit 0;
	fi
	
	# Split the output into lines
	IFS=$'\n' read -rd '' -a lines <<<"$output"
	
	# Create an array with labels A, B
	options=("A" "B")
	
	# Loop through the lines and assign them to options A, B
	for index in ${!options[@]}; do
		option=${options[index]}
		eval "${option}=${lines[index]}"
	done
	
	# Prompt the user to choose from A or B
	echo "Please choose from the following options:"
	echo "A: $A"
	echo "B: $B"
	
	while [ -z "$HOST_NAME" ]; do
		# User selection
		read -p "Enter your choice (A or B): " choice
		choice_upper=$(printf "$choice" | tr '[:lower:]' '[:upper:]')
		
		# Check user choice and display the selected line
		case $choice_upper in
			A)
				HOST_IP=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $A)
				HOST_PORT=$(docker port $A | grep 6033 | awk '{print $3}' | cut -d ":" -f 2)
				PROXYSQL_PORT=$(docker port $A | grep 6032 | awk '{print $3}' | cut -d ":" -f 2)
				HOST_NAME=$A
			;;
			B)
				HOST_IP=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $B)
				HOST_PORT=$(docker port $B | grep 6033 | awk '{print $3}' | cut -d ":" -f 2)
				PROXYSQL_PORT=$(docker port $B | grep 6032 | awk '{print $3}' | cut -d ":" -f 2)
				HOST_NAME=$B
			;;
			*) echo "Invalid choice";;
		esac
	done
}

function getProxyServers() {
	# Run the command and store the output in a variable
	output=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker ps -q --filter "name=proxysql"))
	
	if [ ! -n "$output" ]; then
		echo "Output is empty"
		exit 0;
	fi
	
	# Split the output into lines
	IFS=$'\n' read -rd '' -a lines <<<"$output"
	
	# Create an array with labels A, B
	options=("A" "B")
	
	# Loop through the lines and assign them to options A, B
	for index in ${!options[@]}; do
		option=${options[index]}
		eval "${option}=${lines[index]}"
	done
	
	if [[ $A == *-1* ]]; then
		PROXYSQL_1_HOST=$A
	else
		PROXYSQL_2_HOST=$A
	fi
	
	if [[ $B == *-2* ]]; then
		PROXYSQL_2_HOST=$B
	else
		PROXYSQL_1_HOST=$B
	fi
}

function getGroupSeeds() {
	# Run the command and store the output in a variable
	output=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker ps -q --filter "name=mysql"))
	
	if [ ! -n "$output" ]; then
		echo "Output is empty"
		exit 0;
	fi
	
	# Split the output into HOST_LINES
	IFS=$'\n' read -rd '' -a HOST_LINES <<<"$output"
}

function writeProxyConf() {
	local dir=$1
	mkdir -p $dir/conf
	cat << EOF > $dir/conf/proxysql.cnf
datadir="/var/lib/proxysql"

admin_variables=
{
	admin_credentials="admin:admin;$PROXYSQL_ADMIN:$PROXYSQL_ADMIN_PASSWORD"   # 管理端账号密码
	mysql_ifaces="0.0.0.0:6032"
	cluster_username="cluster.admin"				# 集群用户名称,与admin_credentials中配置的相同
	cluster_password="cluster.admin"                # 集群用户密码,与admin_credentials中配置的相同
	cluster_check_interval_ms=200
	cluster_check_status_frequency=100
	cluster_mysql_query_rules_save_to_disk=true
	cluster_mysql_servers_save_to_disk=true
	cluster_mysql_users_save_to_disk=true
	cluster_proxysql_servers_save_to_disk=true
	cluster_mysql_query_rules_diffs_before_sync=3
	cluster_mysql_servers_diffs_before_sync=3
	cluster_mysql_users_diffs_before_sync=3
	cluster_proxysql_servers_diffs_before_sync=3
}

mysql_variables=
{
	threads=4
	max_connections=2048
	default_query_delay=0
	default_query_timeout=36000000
	have_compress=true
	poll_timeout=2000
	interfaces="0.0.0.0:6033"                       # 代理请求端口
	default_schema="information_schema"
	stacksize=1048576
	server_version="8.0.4"                          # 指定数据库版本
	connect_timeout_server=3000
	monitor_username="proxy.monitor"                # 监控账号
	monitor_password="123456"                       # 监控密码
	monitor_history=600000
	monitor_connect_interval=60000
	monitor_ping_interval=10000
	monitor_read_only_interval=1500
	monitor_read_only_timeout=500
	ping_interval_server_msec=120000
	ping_timeout_server=500
	commands_stats=true
	sessions_sort=true
	connect_retries_on_failure=10
}

proxysql_servers =
(
	{
		hostname="$PROXYSQL_1_HOST"
		port=6032
		weight=1
		comment="${CONTAINERS[3]}"
	},
	{
		hostname="$PROXYSQL_2_HOST"
		port=6032
		weight=1
		comment="${CONTAINERS[4]}"
	}
)
EOF
}

function getPrimaryPort() {
	# 注意：当前 127.0.0.1:3306 不一定是 primary，不过我们可以通过 MGR 和 docker 查询出 primary 映射在物理机上的 prot
	result=$(mysql -h 127.0.0.1 -P ${PORTS[0]} -u $DB_USER -p$DB_PASSWORD -e "SELECT MEMBER_HOST FROM performance_schema.replication_group_members WHERE MEMBER_ROLE='PRIMARY';")
	primary_host=$(echo "$result" | grep -v "MEMBER_HOST" | tr -s " " | cut -d " " -f 2)
	
	docker ps --format "{{.ID}}: {{.Names}}" | grep mysql | while read line ; do
		container_id=$(echo $line | cut -d':' -f1)
		container_name=$(echo $line | cut -d':' -f2)
		container_ip=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $container_id)
		if [ "$container_ip" == "$primary_host" ]; then
			MGR_PRIMARY_PORT=$(docker port $container_name | grep 3306 | awk '{print $3}' | cut -d ":" -f 2)
			break
		fi
	done
}

function choiceMgr() {
	# Run the command and store the output in a variable
	output=$(docker ps -q --filter "name=mysql" | xargs docker inspect --format '{{.Name}}' | sed 's|^/||')
	
	if [ ! -n "$output" ]; then
		echo "Output is empty"
		exit 0;
	fi
	
	# Split the output into lines
	IFS=$'\n' read -rd '' -a lines <<<"$output"
	
	# Create an array with labels A, B, C
	options=("A" "B" "C")
	
	# Loop through the lines and assign them to options A, B, C
	for index in ${!options[@]}; do
		option=${options[index]}
		eval "${option}=${lines[index]}"
	done
	
	# Prompt the user to choose from A, B, or C
	echo "Please choose from the following options:"
	echo "A: $A"
	echo "B: $B"
	echo "C: $C"
	
	while [ -z "$MGR_PRIMARY_NAME" ]; do
		# User selection
		read -p "Enter your choice (A, B, or C): " choice
		choice_upper=$(printf "$choice" | tr '[:lower:]' '[:upper:]')
		
		# Check user choice and display the selected line
		case $choice_upper in
			A)
				MGR_PRIMARY_PORT=$(docker port $A | grep 3306 | awk '{print $3}' | cut -d ":" -f 2)
				MGR_PRIMARY_NAME=$A
			;;
			B)
				MGR_PRIMARY_PORT=$(docker port $B | grep 3306 | awk '{print $3}' | cut -d ":" -f 2)
				MGR_PRIMARY_NAME=$B
			;;
			C)
				MGR_PRIMARY_PORT=$(docker port $C | grep 3306 | awk '{print $3}' | cut -d ":" -f 2)
				MGR_PRIMARY_NAME=$C
			;;
			*) echo "Invalid choice";;
		esac
	done
}

function createProxyUser() {
	local HOST_IP_LENGTH=${#HOST_IP}
	local FRONT_PART=${HOST_IP:0:$(($HOST_IP_LENGTH-1))}
	local LAST_CHAR=${HOST_IP:-1}
	MONITOR_HOST="${FRONT_PART}%"

	mysql -h 127.0.0.1 -P $MGR_PRIMARY_PORT -u $DB_USER -p$DB_PASSWORD << EOF
USE sys;

DELIMITER $$
DROP FUNCTION gr_member_in_primary_partition$$
DROP VIEW gr_member_routing_candidate_status$$

CREATE FUNCTION gr_member_in_primary_partition()
RETURNS VARCHAR(3)
DETERMINISTIC
BEGIN
	RETURN (SELECT IF( MEMBER_STATE='ONLINE' AND ((SELECT COUNT(*) FROM
performance_schema.replication_group_members WHERE MEMBER_STATE != 'ONLINE') >=
((SELECT COUNT(*) FROM performance_schema.replication_group_members)/2) = 0),
'YES', 'NO' ) FROM performance_schema.replication_group_members JOIN
performance_schema.replication_group_member_stats rgms USING(member_id) WHERE rgms.MEMBER_ID=@@SERVER_UUID);
END$$


CREATE VIEW gr_member_routing_candidate_status AS SELECT
sys.gr_member_in_primary_partition() as viable_candidate,
IF( (SELECT (SELECT GROUP_CONCAT(variable_value) FROM
performance_schema.global_variables WHERE variable_name IN ('read_only',
'super_read_only')) != 'OFF,OFF'), 'YES', 'NO') as read_only,
sys.gr_applier_queue_length() as transactions_behind, Count_Transactions_in_queue as 'transactions_to_cert' 
from performance_schema.replication_group_member_stats rgms 
where rgms.MEMBER_ID=(select gv.VARIABLE_VALUE 
	from `performance_schema`.global_variables gv where gv.VARIABLE_NAME='server_uuid');$$

DELIMITER ;
EOF
	
	mysql -h 127.0.0.1 -P $MGR_PRIMARY_PORT -u $DB_USER -p$DB_PASSWORD << EOF
CREATE USER '$MONITOR_USER'@'$MONITOR_HOST' identified BY '$MONITOR_PASSWORD';
GRANT replication client ON *.* TO '$MONITOR_USER'@'$MONITOR_HOST';
GRANT SELECT ON sys.* TO '$MONITOR_USER'@'$MONITOR_HOST';
CREATE USER '$ADMIN_USER'@'$ADMIN_HOST' identified BY '$ADMIN_PASSWORD';
GRANT ALL privileges ON *.* TO '$ADMIN_USER'@'$ADMIN_HOST' WITH GRANT option;
FLUSH PRIVILEGES;
EOF
}

# 读写分离
function readWriteSeparation() {
		
	mysql -h 127.0.0.1 -P $PROXYSQL_PORT -u $PROXYSQL_ADMIN -p$PROXYSQL_ADMIN_PASSWORD --prompt "ProxySQL Admin>" << EOF

-- INSERT INTO mysql_replication_hostgroups (writer_hostgroup, reader_hostgroup, check_type, comment)
-- values (10, 20, 'read_only', 'proxy');
-- load mysql servers to runtime;
-- save mysql servers to disk;

INSERT INTO mysql_servers (hostgroup_id, hostname, port) 
values(10, '${HOST_LINES[0]}', 3306), (20, '${HOST_LINES[1]}', 3306), (20, '${HOST_LINES[2]}', 3306);
load mysql servers to runtime;
save mysql servers to disk;

UPDATE global_variables SET variable_value='$ADMIN_USER' WHERE variable_name='mysql-monitor_username';
UPDATE global_variables SET variable_value='$ADMIN_PASSWORD' WHERE variable_name='mysql-monitor_password';
load mysql variables to runtime;
save mysql variables to disk;

INSERT INTO mysql_users (username, PASSWORD, default_hostgroup)
values('$ADMIN_USER', '$ADMIN_PASSWORD', 10);
load mysql users to runtime;
save mysql users to disk;

INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply) 
values(1, 1, '^select', 20, 1),(2, 1, '^select.*for update$', 10, 1);
load mysql query rules to runtime;
save mysql query rules to disk;

EOF
}

# 重置密码
function resetPassword() {
	mysql -h 127.0.0.1 -P $MGR_PRIMARY_PORT -u$DB_USER -p$DB_PASSWORD << EOF
ALTER USER 'root'@'localhost' IDENTIFIED BY '$DB_NEW_PASSWORD';
ALTER USER 'root'@'%' IDENTIFIED BY '$DB_NEW_PASSWORD';
FLUSH PRIVILEGES;
EOF
}

# main
function main() {
	choiceProxySql
	
	getProxyServers
	
	# 更改 proxysql.cnf
	for index in "${!PROXYSQL_DIRS[@]}"; do
		writeProxyConf ${PROXYSQL_DIRS[index]}
	done
	
	# 删除 proxysql.db
	for container_name in "${CONTAINERS[@]:3}"; do
		docker exec $container_name rm $PROXYSQL_DATA_FILE
	done
	
	printf "\nStopping \`proxsql\`...(might take a while)\n"
	docker restart `docker ps -a | grep proxysql | awk -F " " '{print $1}'` &> /dev/null
	sleep 5
	printf "${BLUE}===>${NC}${BOLD} Successfully stopped \`proxsql\`!${NC}\n"
	printf "${BLUE}===>${NC}${BOLD} Successfully started \`proxsql\`!${NC}\n"
	
	# getPrimaryPort
	choiceMgr
	createProxyUser
	
	
	getGroupSeeds
	readWriteSeparation
	
	# resetPassword
}

########################## main #########################
main
