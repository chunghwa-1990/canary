#!/bin/bash

# 命令行工具
COMMAND_TO_DOCKER="docker"
COMMAND_TO_MYSQL="mysql"
COMMAND_TO_WGET="wget"
COMMAND_TO_CURL="curl"

# 检测docker命令是否存在
if !command -v $COMMAND_TO_DOCKER &> /dev/null
then
    echo "$COMMAND_TO_DOCKER 未安装，脚本中断执行"
    exit 1
fi

# 检测mysql命令是否存在
if !command -v $COMMAND_TO_MYSQL &> /dev/null
then
    echo "$COMMAND_TO_DOCKER 未安装，脚本中断执行"
    exit 1
fi

# 镜像名称
IMAGE_NAME_MYSQL="mysql:8.0"

# 如果mysql镜像不存在，则拉取
if !docker image inspect $IMAGE_NAME_MYSQL &> /dev/null
then
    docker pull mysql:8.0
fi

# 创建网络
docker network create canary-net

sleep 5

# 创建mysql-master
docker run \
    -itd \
    --name mysql-master \
    -p 3306:3306 \
    -v /Users/zhaohongliang/DockerData/mysql/mysql-master/conf:/etc/mysql/conf.d \
    -v /Users/zhaohongliang/DockerData/mysql/mysql-master/data:/var/lib/mysql \
    -v /Users/zhaohongliang/DockerData/mysql/mysql-master/log:/var/log/mysql \
    -v /etc/localtime:/etc/localtime \
    -e MYSQL_ROOT_PASSWORD=123456   \
    --restart no \
    --privileged=true \
    --network canary-net \
    mysql

sleep 5

# 创建mysql-slave-1
docker run \
    -itd \
    --name mysql-slave-1 \
    -p 3307:3306 \
    -v /Users/zhaohongliang/DockerData/mysql/mysql-slave-1/conf:/etc/mysql/conf.d \
    -v /Users/zhaohongliang/DockerData/mysql/mysql-slave-1/data:/var/lib/mysql \
    -v /Users/zhaohongliang/DockerData/mysql/mysql-slave-1/log:/var/log/mysql \
    -v /etc/localtime:/etc/localtime \
    -e MYSQL_ROOT_PASSWORD=123456   \
    --restart no \
    --privileged=true \
    --network canary-net \
    mysql

sleep 5

# 创建mysql-slave-2
docker run \
    -itd \
    --name mysql-slave-2 \
    -p 3308:3306 \
    -v /Users/zhaohongliang/DockerData/mysql/mysql-slave-2/conf:/etc/mysql/conf.d \
    -v /Users/zhaohongliang/DockerData/mysql/mysql-slave-2/data:/var/lib/mysql \
    -v /Users/zhaohongliang/DockerData/mysql/mysql-slave-2/log:/var/log/mysql \
    -v /etc/localtime:/etc/localtime \
    -e MYSQL_ROOT_PASSWORD=123456   \
    --restart no \
    --privileged=true \
    --network canary-net \
    mysql

MYSQL_MASTER_CNF="/Users/zhaohongliang/DockerData/mysql/mysql-master/conf/my.cnf"
MYSQL_SLAVE_1_CNF="/Users/zhaohongliang/DockerData/mysql/mysql-slave-1/conf/my.cnf"
MYSQL_SLAVE_2_CNF="/Users/zhaohongliang/DockerData/mysql/mysql-slave-2/conf/my.cnf"

if [ ! -f "$MYSQL_MASTER_CNF" ];
then
    # wget -P /Users/zhaohongliang/DockerData/mysql/mysql-master/conf  https://github.com/hahapigs/canary/blob/main/mysql-master.cnf 
    cp /Users/zhaohongliang/GitHub/canary/mysql-master.cnf /Users/zhaohongliang/DockerData/mysql/mysql-master/conf
    mv /Users/zhaohongliang/DockerData/mysql/mysql-master/conf/mysql-master.cnf   /Users/zhaohongliang/DockerData/mysql/mysql-master/conf/my.cnf
fi

if [ ! -f "$MYSQL_SLAVE_1_CNF" ];
then
    # wget -P /Users/zhaohongliang/DockerData/mysql/mysql-slave-1/conf https://github.com/hahapigs/canary/blob/main/mysql-slave-1.cnf
    cp /Users/zhaohongliang/GitHub/canary/mysql-slave-1.cnf /Users/zhaohongliang/DockerData/mysql/mysql-slave-1/conf
    mv /Users/zhaohongliang/DockerData/mysql/mysql-slave-1/conf/mysql-slave-1.cnf /Users/zhaohongliang/DockerData/mysql/mysql-slave-1/conf/my.cnf
fi

if [ ! -f "$MYSQL_SLAVE_2_CNF" ];
then
    # wget -P /Users/zhaohongliang/DockerData/mysql/mysql-slave-2/conf https://github.com/hahapigs/canary/blob/main/mysql-slave-2.cnf
    cp /Users/zhaohongliang/GitHub/canary/mysql-slave-2.cnf /Users/zhaohongliang/DockerData/mysql/mysql-slave-2/conf
    mv /Users/zhaohongliang/DockerData/mysql/mysql-slave-2/conf/mysql-slave-2.cnf /Users/zhaohongliang/DockerData/mysql/mysql-slave-2/conf/my.cnf
fi

sleep 2
docker restart mysql-master
sleep 2
docker restart mysql-slave-1
sleep 2
docker restart mysql-slave-2
sleep 2
 
# 镜像名称
IMAGE_NAME_PROXYSQL="proxysql/proxysql"

# 如果proxy镜像不存在，则拉取
if !docker image inspect $IMAGE_NAME_PROXYSQL &> /dev/null
then
    docker pull proxysql/proxysql
fi

if [ ! -f "$MYSQL_SLAVE_2_CNF" ];
then
    # wget -P /Users/zhaohongliang/DockerData/proxysql https://github.com/hahapigs/canary/blob/main/proxysql.cnf
    cp /Users/zhaohongliang/GitHub/canary/proxysql.cnf /Users/zhaohongliang/DockerData/proxysql
fi

docker run -p 16032:6032 -p 16033:6033 -p 16070:6070 --name proxysql --network canary-net -d -v /Users/zhaohongliang/DockerData/proxysql/proxysql.cnf:/etc/proxysql.cnf proxysql/proxysql

sleep 5

# 数据库连接信息
DB_USER="root"
DB_PASS="123456"
DB_NEW_PASS="Pass!234"
DB_NAME="canary"

MASTER_IP=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mysql-master)
MASTER_IP_LENGTH=${#MASTER_IP}
FRONT_PART=${MASTER_IP:0:$(($MASTER_IP_LENGTH-1))}
LAST_CHAR=${MASTER_IP:-1}
NEW_MASTER_IP="${FRONT_PART}%"

mysql -h 127.0.0.1 -P 3306 -u$DB_USER -p$DB_PASS <<EOF
CREATE USER 'replication.user'@'$NEW_MASTER_IP' IDENTIFIED WITH mysql_native_password BY 'Copy!234';
GRANT REPLICATION SLAVE ON *.* TO 'replication.user'@'$NEW_MASTER_IP';
FLUSH PRIVILEGES;
EOF
sleep 2

MASTER_LOG_FILE=$(mysql -h 127.0.0.1 -P 3306 -u $DB_USER -p$DB_PASS -e "SHOW MASTER STATUS\G" | grep "File" | awk '{print $2}')
MASTER_LOG_POS=$(mysql -h 127.0.0.1 -P 3306 -u $DB_USER -p$DB_PASS -e "SHOW MASTER STATUS\G" | grep "Position" | awk '{print $2}')
mysql -h 127.0.0.1 -P 3307 -u $DB_USER -p$DB_PASS <<EOF
CHANGE MASTER TO MASTER_HOST='$MASTER_IP', MASTER_USER='replication.user', MASTER_PASSWORD='Copy!234', MASTER_LOG_FILE='$MASTER_LOG_FILE', MASTER_LOG_POS=$MASTER_LOG_POS;
START SLAVE;
EOF
sleep 2

mysql -h 127.0.0.1 -P 3308 -u $DB_USER -p$DB_PASS <<EOF
CHANGE MASTER TO MASTER_HOST='$MASTER_IP', MASTER_USER='replication.user', MASTER_PASSWORD='Copy!234', MASTER_LOG_FILE='$MASTER_LOG_FILE', MASTER_LOG_POS=$MASTER_LOG_POS;
START SLAVE;
EOF
sleep 2

# SLAVE_1_IO=$(mysql -h 127.0.0.1 -P 3307 -u $DB_USER -p$DB_PASS -e "SHOW SLAVE STATUS\G" | grep "Slave_IO_Running" | awk '{print $2}')
# SLAVE_2_IO=$(mysql -h 127.0.0.1 -P 3308 -u $DB_USER -p$DB_PASS -e "SHOW SLAVE STATUS\G" | grep "Slave_IO_Running" | awk '{print $2}')
# SLAVE_1_SQL=$(mysql -h 127.0.0.1 -P 3307 -u $DB_USER -p$DB_PASS -e "SHOW SLAVE STATUS\G" | grep "Slave_SQL_Running" | awk '{print $2}')
# SLAVE_2_SQL=$(mysql -h 127.0.0.1 -P 3308 -u $DB_USER -p$DB_PASS -e "SHOW SLAVE STATUS\G" | grep "Slave_SQL_Running" | awk '{print $2}')
# 
# if [ "$SLAVE_1_IO" != "Yes" ]; then
#     echo "slave-1 io: $SLAVE_1_IO"
#     # exit 1
# fi
# if [ "$SLAVE_1_SQL" != "Yes" ]; then
#     echo "slave-1 sql: $SLAVE_1_SQL"
#     # exit 1
# fi
# if [ "$SLAVE_2_IO" != "Yes" ]; then
#     echo "slave-2 io: $SLAVE_2_IO"
#     # exit 1
# fi
# if [ "$SLAVE_2_SQL" != "Yes" ]; then
#     echo "slave-2 sql: $SLAVE_2_SQL"
#     # exit 1
# fi


 

mysql -h 127.0.0.1 -P 3306 -u $DB_USER -p$DB_PASS <<EOF
ALTER USER 'root'@'localhost' IDENTIFIED BY 'Pass!234';
ALTER USER 'root'@'%' IDENTIFIED BY 'Pass!234';
FLUSH PRIVILEGES;
EOF
sleep 5
# mysql -h 127.0.0.1 -P 3307 -u $DB_USER -p$DB_PASS <<EOF
# ALTER USER 'root'@'localhost' IDENTIFIED BY 'Pass!234';
# ALTER USER 'root'@'%' IDENTIFIED BY 'Pass!234';
# FLUSH PRIVILEGES;
# EOF
# sleep 5
# mysql -h 127.0.0.1 -P 3308 -u $DB_USER -p$DB_PASS <<EOF
# ALTER USER 'root'@'localhost' IDENTIFIED BY 'Pass!234';
# ALTER USER 'root'@'%' IDENTIFIED BY 'Pass!234';
# FLUSH PRIVILEGES;
# EOF
# sleep 5

echo "====="

mysql -h 127.0.0.1 -P 3306 -u $DB_USER -p$DB_NEW_PASS <<EOF
create user 'proxy.monitor'@'$NEW_MASTER_IP' identified by '123456';
grant replication client on *.* to 'proxy.monitor'@'$NEW_MASTER_IP';
create user 'proxy.admin'@'%' identified by 'Pass!234';
grant all privileges on *.* to 'proxy.admin'@'%' with grant option;
FLUSH PRIVILEGES;
EOF
sleep 5

# mysql -h 127.0.0.1 -P 3307 -u $DB_USER -p$DB_NEW_PASS <<EOF
# create user 'proxy.monitor'@'$NEW_MASTER_IP' identified by '123456';
# grant replication client on *.* to 'proxy.monitor'@'$NEW_MASTER_IP';
# create user 'proxy.admin'@'%' identified by 'Pass!234';
# grant all privileges on *.* to 'proxy.admin'@'%' with grant option;
# FLUSH PRIVILEGES;
# EOF
# sleep 5
# 
# mysql -h 127.0.0.1 -P 3308 -u $DB_USER -p$DB_NEW_PASS <<EOF 
# create user 'proxy.monitor'@'$NEW_MASTER_IP' identified by '123456';
# grant replication client on *.* to 'proxy.monitor'@'$NEW_MASTER_IP';
# create user 'proxy.admin'@'%' identified by 'Pass!234';
# grant all privileges on *.* to 'proxy.admin'@'%' with grant option;
# FLUSH PRIVILEGES;
# EOF
# sleep 5
echo "======"

mysql -h 127.0.0.1 -P 16032 -uradmin -pradmin --prompt "ProxySQL Admin>" <<EOF
insert into mysql_replication_hostgroups(writer_hostgroup,reader_hostgroup,check_type, COMMENT) values(10,20,'read_only', 'proxy');
load mysql servers to runtime;
save mysql servers to disk;
EOF
sleep 5

SLAVE_1_IP=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mysql-slave-1)
SLAVE_2_IP=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mysql-slave-2)
mysql -h 127.0.0.1 -P 16032 -uradmin -pradmin --prompt "ProxySQL Admin>" <<EOF
insert into mysql_servers(hostgroup_id,hostname,port)  values(10,'$MASTER_IP',3306),(20,'$SLAVE_1_IP',3306),(20,'$SLAVE_2_IP',3306);
load mysql servers to runtime;
save mysql servers to disk;
EOF
sleep 5


mysql -h 127.0.0.1 -P 16032 -uradmin -pradmin --prompt "ProxySQL Admin>" <<EOF
UPDATE global_variables SET variable_value='proxy.monitor' WHERE variable_name='mysql-monitor_username';
UPDATE global_variables SET variable_value='123456' WHERE variable_name='mysql-monitor_password';
load mysql variables to runtime;
save mysql variables to disk;
EOF
sleep 5

mysql -h 127.0.0.1 -P 16032 -uradmin -pradmin --prompt "ProxySQL Admin>" << EOF
insert into mysql_users(username,password,default_hostgroup) values('proxy.admin','Pass!234',10);
load mysql users to runtime;
save mysql users to disk;
EOF

mysql -h 127.0.0.1 -P 16032 -uradmin -pradmin --prompt "ProxySQL Admin>" << EOF
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply) VALUES (1, 1, '^select', 20, 1),(2, 1, '^select.*for update$', 10, 1),(3, 1, '.*', 10, 1);
load mysql query rules to runtime;
save mysql query rules to disk;
EOF



