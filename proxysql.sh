#!/bin/bash

###################### docker ######################
HOME="/Users/zhaohongliang/DockerData"
# network
NETWORK="canary-net"
# containers
CONTAINERS=("proxysql-1" "proxysql-2")
PROXYSQL_1=("16032" "16033" "16070")
PROXYSQL_2=("26032" "26033" "26070")

###################### proxysql #####################
PROXYSQL_HOME="$HOME/proxysql"
PROXYSQL_ADMIN_USER="radmin"
PROXYSQL_ADMIN_PASSWORD="radmin"
PROXYSQL_DATA_PATH="/var/lib/proxysql"
PROXYSQL_DATA_FILE=$PROXYSQL_DATA_PATH/proxysql.db

###################### mysql ######################
# 管理员用户名、密码
DB_USER="root"
DB_PASSWORD="Pass!234"
# proxysql管理员用户名、密码
ADMIN_USER="proxy.admin"
ADMIN_PASSWORD="Pass!234"
# proxysql监测用户名、密码
MONITOR_USER="proxy.monitor"
MONITOR_PASSWORD="123456"
# master节点的IP、端口和主机名
master_ip=""
master_port=""
master_name=""

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


# progress
function progress() {
	local i=0
	local b=' '
	local max=100
	local continer_name=$1
	
	while [ $i -le $max  ]; do
		printf "${continer_name}:[%-100s] %d%%\r" $b $i
		sleep 0.1
		i=`expr 1 + $i`
		b=#$b
	done
	printf "\n${continer_name} build successful!\n"
}

# 校验数据库是否启动
function isStart() {
	local continer_name=$1
	
	while ! docker ps | grep ${CONTAINERS[$index]}; do
		for i in $(seq 0 3); do
			printf "\r${continer_name} 正在启动...${SPINNER:$i:1}"
			sleep 0.2
		done
	done
	printf "${continer_name} start successful！\n"
}

# 校验连接是否正常
function isValid() {
	local continer_name=$1
	local port=$2
	
	nc -z -v -w 3 127.0.0.1 ${port}
	if [ $? -eq 0  ]; then
		printf "${CONTAINERS[$index]} connection successful!\n"
	else
		printf "${CONTAINERS[$index]} connection failed!\n"
	fi
	echo ""
}

# create proxysql
function createProxySql() {
	containers=(${CONTAINERS[@]})
	
	echo "构建进程："
	for index in ${!containers[@]}; do
		volume_home=$PROXYSQL_HOME/${containers[index]}
		alias=$(echo "${containers[index]}" | sed 's/-/_/' | tr '[:lower:]' '[:upper:]')[@]
		ports=(${!alias})
		
		mkdir -p $volume_home/conf
		docker run \
			-d \
			--name ${containers[index]} \
			-p ${ports[0]}:6032 \
			-p ${ports[1]}:6033 \
			-p ${ports[2]}:6070 \
			-v $volume_home/conf/proxysql.cnf:/etc/proxysql.cnf \
			-v /etc/localtime:/etc/localtime \
			--restart no \
			--privileged=true \
			--network $NETWORK \
			proxysql/proxysql &> /dev/null
		progress ${containers[index]}
		isStart ${containers[index]}
		sleep 2
		isValid ${containers[index]} ${ports[0]}
		
		# docker run -p 16032:6032 -p 16033:6033 -p 16070:6070 -d -v /path/to/proxysql.cnf:/etc/proxysql.cnf proxysql/proxysql
	done
}

# proxysql 单机配置
function writeProxySqlCnf() {
	for container in ${CONTAINERS[@]}; do
		mkdir -p  $PROXYSQL_HOME/$container/conf
		cat << EOF > $PROXYSQL_HOME/$container/conf/proxysql.cnf
datadir="/var/lib/proxysql"

admin_variables=
{
	admin_credentials="admin:admin;$PROXYSQL_ADMIN_USER:$PROXYSQL_ADMIN_PASSWORD"   # 管理端账号密码
	mysql_ifaces="0.0.0.0:6032"
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
	monitor_username="$MONITOR_USER"                # 监控账号
	monitor_password="$MONITOR_PASSWORD"            # 监控密码
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
EOF
	done
	
}

# 获取 proxysql sever ip
function getProxySqlServer() {
	output=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker ps -q --filter "name=proxysql") | sort)
	if [ ! -n "$output" ]; then
		echo "Output is empty"
		exit 0;
	fi
	
	# Split the output into servers
	IFS=$'\n' read -rd '' -a servers <<<"$output"
	echo ${servers[@]}
}

# proxysql 集群配置
function writeClusterCnf() {
	servers=($(getProxySqlServer))
	for container in ${CONTAINERS[@]}; do
		mkdir -p $PROXYSQL_HOME/$container/conf
		cat << EOF > $PROXYSQL_HOME/$container/conf/proxysql.cnf
datadir="/var/lib/proxysql"

admin_variables=
{
	admin_credentials="admin:admin;$PROXYSQL_ADMIN_USER:$PROXYSQL_ADMIN_PASSWORD"   # 管理端账号密码
	mysql_ifaces="0.0.0.0:6032"
	cluster_username="$PROXYSQL_ADMIN_USER"				# 集群用户名称,与admin_credentials中配置的相同
	cluster_password="$PROXYSQL_ADMIN_PASSWORD"        	# 集群用户密码,与admin_credentials中配置的相同
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
	monitor_username="$MONITOR_USER"                # 监控账号
	monitor_password="$MONITOR_PASSWORD"            # 监控密码
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
		hostname="${servers[0]}"
		port=6032
		weight=1
		comment="${CONTAINERS[0]}"
	},
	{
		hostname="${servers[1]}"
		port=6032
		weight=1
		comment="${CONTAINERS[1]}"
	}
)
EOF
	done
}

# 删除 proxysql.db
function removeProxySqlData() {
	for container in ${CONTAINERS[@]}; do
		# 删除 proxysql.db
		docker exec $container rm /var/lib/proxysql/proxysql.db
	done
}

# 重启 proxysql
function restartProxySql() {
	echo "Stopping proxsql...(might take a while)"
	docker restart `docker ps -a | grep proxysql | awk -F " " '{print $1}'` &> /dev/null
	sleep 5
	echo "${BLUE}===>${NC}${BOLD} Successfully stopped proxsql${NC}"
	echo "${BLUE}===>${NC}${BOLD} Successfully started proxsql${NC}"
}

# 选择 master
function choiceMySqlMaster() {
	# Run the command and store the output in a variable
	output=$(docker ps -q --filter "name=mysql" | xargs docker inspect --format '{{.Name}}' | sed 's|^/||' | sort)
	
	if [ ! -n "$output" ]; then
		echo "Output is empty"
		exit 0;
	fi
	
	# Split the output into containers
	IFS=$'\n' read -rd '' -a containers <<<"$output"
	
	# 数量
	number=${#containers[@]}
	# labels A, B, C
	options=(${ALPHABET[@]:0:$number})
	
	# Prompt the user to choose from A、B or C
	echo "MySql-Replicaion Master:"
	for index in ${!options[@]}; do
		echo "${options[index]}. ${containers[index]}"
		option=${options[index]}
		eval "${option}=${containers[index]}"
	done
	
	# Prompt the user to choose from A, B, or C
	# echo "Please choose from the following options:"
	# echo "A: $A"
	# echo "B: $B"
	# echo "C: $C"
	
	while [ -z "$master_name" ]; do
		# User selection
		read -p "Enter your choice (A、B or C): " choice
		
		# Check user choice and display the selected line
		case $choice in
			A | a)
				master_name=$A
			;;
			B | b)
				master_name=$B
			;;
			C | c)
				master_name=$C
			;;
			*) echo "Invalid choice";;
		esac
	done
}

# 选择复制模式
function choiceReplicationModel() {
	# labels A, B
	options=(${ALPHABET[@]:0:2})
	model=("master-slave" "group-replication")
	
	for index in ${!options[@]}; do
		option=${options[index]}
		eval "${option}=${model[index]}"
	done
	
	# Prompt the user to choose from A or B
	echo "MySql-Replicaion Model:"
	echo "A: $A"
	echo "B: $B"
	
	model=""
	while [ -z "$model" ]; do
		# User selection
		read -p "Enter your choice (A、B or C): " choice
		
		# Check user choice and display the selected line
		case $choice in
			A | a)
				masterSlaveReplication
				model=$A
			;;
			B | b)
				groupReplication
				# createView
				model=$B
			;;
			*) echo "Invalid choice";;
		esac
	done
}

# 主从复制 模式
function masterSlaveReplication() {
	mysql -h 127.0.0.1 -P ${PROXYSQL_1[0]} -u $PROXYSQL_ADMIN_USER -p$PROXYSQL_ADMIN_PASSWORD --prompt "ProxySQL Admin>" << EOF
INSERT INTO mysql_replication_hostgroups (writer_hostgroup, reader_hostgroup, check_type, COMMENT)
values(10, 20, 'read_only', 'proxy');
load mysql servers to runtime;
save mysql servers to disk;
EOF
}

# 组复制 模式
function groupReplication() {
	mysql -h 127.0.0.1 -P ${PROXYSQL_1[0]} -u $PROXYSQL_ADMIN_USER -p$PROXYSQL_ADMIN_PASSWORD --prompt "ProxySQL Admin>" << EOF
INSERT INTO mysql_group_replication_hostgroups (writer_hostgroup, backup_writer_hostgroup, reader_hostgroup, offline_hostgroup, active, max_writers, writer_is_also_reader, max_transactions_behind, COMMENT)
values(10, 20, 30, 40, 1, 1, 1, 100, 'mgr_cluster_01');load mysql servers to runtime;
save mysql servers to disk;
EOF
}

function createView() {
	master_ip=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $master_name)
	master_port=$(docker port $master_name | grep 3306 | awk '{print $3}' | cut -d ":" -f 2)
	
	mysql -h 127.0.0.1 -P $master_port -u $DB_USER -p$DB_PASSWORD << EOF
		USE sys;
DELIMITER $$
CREATE FUNCTION IFZERO(a INT, b INT)
RETURNS INT
DETERMINISTIC
RETURN IF(a = 0, b, a)$$

CREATE FUNCTION LOCATE2(needle TEXT(10000), haystack TEXT(10000), offset INT)
RETURNS INT
DETERMINISTIC
RETURN IFZERO(LOCATE(needle, haystack, offset), LENGTH(haystack) + 1)$$

CREATE FUNCTION GTID_NORMALIZE(g TEXT(10000))
RETURNS TEXT(10000)
DETERMINISTIC
RETURN GTID_SUBTRACT(g, '')$$

CREATE FUNCTION GTID_COUNT(gtid_set TEXT(10000))
RETURNS INT
DETERMINISTIC
BEGIN
	DECLARE result BIGINT DEFAULT 0;
	DECLARE colon_pos INT;
	DECLARE next_dash_pos INT;
	DECLARE next_colon_pos INT;
	DECLARE next_comma_pos INT;
	SET gtid_set = GTID_NORMALIZE(gtid_set);
	SET colon_pos = LOCATE2(':', gtid_set, 1);
	WHILE colon_pos != LENGTH(gtid_set) + 1 DO
			SET next_dash_pos = LOCATE2('-', gtid_set, colon_pos + 1);
			SET next_colon_pos = LOCATE2(':', gtid_set, colon_pos + 1);
			SET next_comma_pos = LOCATE2(',', gtid_set, colon_pos + 1);
			IF next_dash_pos < next_colon_pos AND next_dash_pos < next_comma_pos THEN
				SET result = result +
					SUBSTR(gtid_set, next_dash_pos + 1,
								LEAST(next_colon_pos, next_comma_pos) - (next_dash_pos + 1)) -
					SUBSTR(gtid_set, colon_pos + 1, next_dash_pos - (colon_pos + 1)) + 1;
			ELSE
				SET result = result + 1;
			END IF;
			SET colon_pos = next_colon_pos;
	END WHILE;
	RETURN result;
END$$

CREATE FUNCTION gr_applier_queue_length()
RETURNS INT
DETERMINISTIC
BEGIN
	RETURN (SELECT sys.gtid_count( GTID_SUBTRACT( (SELECT
Received_transaction_set FROM performance_schema.replication_connection_status
WHERE Channel_name = 'group_replication_applier' ), (SELECT
@@global.GTID_EXECUTED) )));
END$$

CREATE FUNCTION gr_member_in_primary_partition()
RETURNS VARCHAR(3)
DETERMINISTIC
BEGIN
	RETURN (SELECT IF( MEMBER_STATE='ONLINE' AND ((SELECT COUNT(*) FROM
performance_schema.replication_group_members WHERE MEMBER_STATE != 'ONLINE') >=
((SELECT COUNT(*) FROM performance_schema.replication_group_members)/2) = 0),
'YES', 'NO' ) FROM performance_schema.replication_group_members JOIN
performance_schema.replication_group_member_stats USING(member_id));
END$$

CREATE VIEW gr_member_routing_candidate_status AS SELECT
sys.gr_member_in_primary_partition() as viable_candidate,
IF( (SELECT (SELECT GROUP_CONCAT(variable_value) FROM
performance_schema.global_variables WHERE variable_name IN ('read_only',
'super_read_only')) != 'OFF,OFF'), 'YES', 'NO') as read_only,
sys.gr_applier_queue_length() as transactions_behind, Count_Transactions_in_queue as 'transactions_to_cert' from performance_schema.replication_group_member_stats;$$

DELIMITER;
EOF
	
	result1 = $(mysql -h 127.0.0.1 -P $master_port -u $DB_USER -p$DB_PASSWORD -e "select gr_member_in_primary_partition();" sys);
	result2 = $(mysql -h 127.0.0.1 -P $master_port -u $DB_USER -p$DB_PASSWORD -e "select * from sys.gr_member_routing_candidate_status;" sys);
	
	if [ -z "$result" ] || [ -z "$result2" ]; then
		mysql -h 127.0.0.1 -P $master_port -u $DB_USER -p$DB_PASSWORD << EOF
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
	fi
}

# 创建用户(proxy.admin、proxy.monitor)
function createProxyUser() {
	master_ip=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $master_name)
	master_port=$(docker port $master_name | grep 3306 | awk '{print $3}' | cut -d ":" -f 2)
	gateway=$(docker network inspect $NETWORK | jq -r '.[0].IPAM.Config.[0].Gateway')
	monitor_host=$(echo $gateway | sed 's/\.1$/\.%/g')
		
	mysql -h 127.0.0.1 -P $master_port -u $DB_USER -p$DB_PASSWORD << EOF
CREATE USER '$MONITOR_USER'@'$monitor_host' identified BY '$MONITOR_PASSWORD';
GRANT replication client ON *.* TO '$MONITOR_USER'@'$monitor_host';
GRANT SELECT ON sys.* TO '$MONITOR_USER'@'$monitor_host';
CREATE USER '$ADMIN_USER'@'%' identified BY '$ADMIN_PASSWORD';
GRANT ALL privileges ON *.* TO '$ADMIN_USER'@'%' WITH GRANT option;
FLUSH PRIVILEGES;
EOF
}

# 读写分离
function readWriteSeparation() {
	
	# Run the command and store the output in a variable
	output=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker ps -q --filter "name=mysql") | sort)
	
	if [ ! -n "$output" ]; then
		echo "Output is empty"
		exit 0;
	fi
	
	# Split the output into lines
	IFS=$'\n' read -rd '' -a lines <<<"$output"
		
	mysql -h 127.0.0.1 -P ${PROXYSQL_1[0]} -u $PROXYSQL_ADMIN_USER -p$PROXYSQL_ADMIN_PASSWORD --prompt "ProxySQL Admin>" << EOF

INSERT INTO mysql_servers (hostgroup_id, hostname, port)
values(10, '${lines[0]}', 3306), (30, '${lines[1]}', 3306), (30, '${lines[2]}', 3306);
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
values(1, 1, '^select.*for update$', 10, 1),(2, 1, '^select', 30, 1);
load mysql query rules to runtime;
save mysql query rules to disk;

EOF
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
	writeProxySqlCnf 
	createProxySql
	if [ ${#CONTAINERS[@]} -gt 1 ]; then
		writeClusterCnf
		removeProxySqlData
		restartProxySql
	fi
	choiceMySqlMaster
	choiceReplicationModel
	createProxyUser
	readWriteSeparation
	logo
}

########################## main #########################
main
