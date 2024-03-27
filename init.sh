#!/bin/bash

# docker 挂载本地访问容器内的根目录
HOME="/Users/zhaohongliang/DockerData"
# 项目根目录
PROJECT="/Users/zhaohongliang/Github/canary"
# sql 文件根目录
SQL="/Users/zhaohongliang/Desktop"

# ANSI 转义码定义
RED='\033[0;31m'    # 红色字体
GREEN='\033[0;32m'  # 绿色字体
BOLD='\033[1m'      # 加粗字体
NC='\033[0m'        # 恢复默认格式

# 定义旋转的光标符号
spinner="/-\|"

# 旋转光标（等待）
dynamic_cursor() {
    local j=0
    local max=5

    while [ $j -le $max ]; do
        for i in $(seq 0 3); do
            printf "\r正在执行...${spinner:$i:1}"   # 使用回车符（\r）实现光标回到行首
            sleep 0.2
        done
        j=`expr 1 + $j`
    done
    printf "\r\033[K"
    printf "执行完毕！\n"
}

# 移除容器
remove_containers() {
    # 检查容器是否存在
    while [ $(docker ps -aq --filter "name=sql" | wc -l) -gt 0  ]; do
        for i in $(seq 0 3); do
            printf "\r正在准备安装环境...${spinner:$i:1}"   # 使用回车符（\r）实现光标回到行首
            sleep 0.2
        done
        docker stop `docker ps -a | grep sql | awk -F " " '{print $1}'` &> /dev/null
        docker rm `docker ps -a | grep sql | awk -F " " '{print $1}'` &> /dev/null
    done
    printf "\r\033[K"
    printf "正在准备安装环境..."
    printf "\n准备完成！\n"
}

# 容器名称
containers=("mysql-master" "mysql-slave-1" "mysql-slave-2" "proxysql")
ports=("3306" "3307" "3308" "16033")
# 旋转光标（校验）
is_start() {
    local j=$1

    while ! docker ps | grep ${containers[$j]}; do
        for i in $(seq 0 3); do
            printf "\r${containers[$1]} 正在启动...${spinner:$i:1}"
            sleep 0.2
        done
    done
    printf "${containers[$j]} 启动成功！\n"
    
    local port=${ports[$j]}
    
    nc -z -v -w 3 127.0.0.1 ${port}
    if [ $? -eq 0  ]; then
        printf "${containers[$j]} 数据库连接正常！\n"
    else
        printf "${containers[$j]} 数据库连接失败！\n"
    fi
    
    echo ""
}

# 进度条
progress() {
    local i=0
    local b=' '
    local max=100
    local str=$1
    
    while [ $i -le $max ]; do
        printf "${str}：[%-100s] %d%%\r" $b $i
        sleep 0.1
        i=`expr 1 + $i`
        b=#$b
    done
    printf "\n${str} successfully!\n"
}

# 打印logo
function print_logo()
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

# 命令行工具
COMMAND_DOCKER="docker"
COMMAND_MYSQL="mysql"

# 检测 docker 命令是否存在
if !command -v $COMMAND_DOCKER &> /dev/null
then
    printf "$COMMAND_DOCKER 未安装，脚本中断执行"
    exit 1
fi

# 检测 mysql 命令是否存在
if !command -v $COMMAND_MYSQL &> /dev/null
then
    printf "$COMMAND_DOCKER 未安装，脚本中断执行"
    exit 1
fi

# mysql
IMAGE_NAME_MYSQL="mysql:8.0"
# proxysql 
IMAGE_NAME_PROXYSQL="proxysql/proxysql"

# 如果 mysql 镜像不存在，则拉取
if !docker image inspect $IMAGE_NAME_MYSQL &> /dev/null
then
    docker pull mysql:8.0
    if [ $? -eq 0 ]; then
        printf "$IMAGE_NAME_MYSQL 拉取完成"
    fi
fi

# 如果 proxysql 镜像不存在，则拉取
if !docker image inspect $IMAGE_NAME_PROXYSQL &> /dev/null
then
    docker pull proxysql/proxysql
    if [ $? -eq 0 ]; then
        printf "$IMAGE_NAME_PROXYSQL 拉取完成"
    fi
fi

remove_containers

# network
NETWORK="canary-net"

# 判断 network 是否存在
if docker network ls | grep -q $NETWORK &> /dev/null; then
    docker network rm $NETWORK &> /dev/null
fi
docker network create $NETWORK &> /dev/null

# mysql
MYSQL_HOME="$HOME/mysql"
MASTER_HOME="$MYSQL_HOME/mysql-master"
SLAVE_1_HOME="$MYSQL_HOME/mysql-slave-1"
SLAVE_2_HOME="$MYSQL_HOME/mysql-slave-2"

if [ -d "$MYSQL_HOME" ]; then 
    # wget -P $MASTER_HOME/conf  https://github.com/hahapigs/canary/blob/main/mysql-master.cnf 
    rm -rf $MYSQL_HOME
fi
mkdir -p $MASTER_HOME/conf && mkdir -p $SLAVE_1_HOME/conf && mkdir -p $SLAVE_2_HOME/conf
cp -f $PROJECT/mysql-master.cnf $MASTER_HOME/conf/my.cnf
cp -f $PROJECT/mysql-slave-1.cnf $SLAVE_1_HOME/conf/my.cnf
cp -f $PROJECT/mysql-slave-2.cnf $SLAVE_2_HOME/conf/my.cnf

# 创建mysql-master
docker run \
    -itd \
    --name mysql-master \
    -p 3306:3306 \
    -v $MASTER_HOME/conf:/etc/mysql/conf.d \
    -v $MASTER_HOME/data:/var/lib/mysql \
    -v $MASTER_HOME/log:/var/log/mysql \
    -v /etc/localtime:/etc/localtime \
    -e MYSQL_ROOT_PASSWORD=123456   \
    --restart no \
    --privileged=true \
    --network $NETWORK \
    mysql &> /dev/null
echo ""
progress "Build Master"
is_start 0

# 创建mysql-slave-1
docker run \
    -itd \
    --name mysql-slave-1 \
    -p 3307:3306 \
    -v $SLAVE_1_HOME/conf:/etc/mysql/conf.d \
    -v $SLAVE_1_HOME/data:/var/lib/mysql \
    -v $SLAVE_1_HOME/log:/var/log/mysql \
    -v /etc/localtime:/etc/localtime \
    -e MYSQL_ROOT_PASSWORD=123456   \
    --restart no \
    --privileged=true \
    --network $NETWORK \
    mysql &> /dev/null

progress "Build Slave-1"
is_start 1

# 创建mysql-slave-2
docker run \
    -itd \
    --name mysql-slave-2 \
    -p 3308:3306 \
    -v $SLAVE_2_HOME/conf:/etc/mysql/conf.d \
    -v $SLAVE_2_HOME/data:/var/lib/mysql \
    -v $SLAVE_2_HOME/log:/var/log/mysql \
    -v /etc/localtime:/etc/localtime \
    -e MYSQL_ROOT_PASSWORD=123456   \
    --restart no \
    --privileged=true \
    --network $NETWORK \
    mysql &> /dev/null

progress "Build Slave-2"
is_start 2

# proxysql
PROXYSQL_HOME=$HOME/proxysql

# 判断 proxysql 目录是否存在
if [ ! -d "$PROXYSQL_HOME" ]; then
    mkdir -p $PROXYSQL_HOME &&
    cp $PROJECT/proxysql.cnf $PROXYSQL_HOME
else
    while true; do
        read -p "是否需要覆盖 ProxySQL 配置？（y/n）" choice
        choice_lower=$(printf "$choice" | tr '[:upper:]' '[:lower:]')
        if [ "$choice_lower" == "y" ] || [ "$choice_lower" == "yes" ]; then
            rm -rf $PROXYSQL_HOME &&
            mkdir -p $PROXYSQL_HOME &&
            cp $PROJECT/proxysql.cnf $PROXYSQL_HOME
            break
        elif [ "$choice_lower" == "n" ] || [ "$choice_lower" == "no" ]; then
            break
        else
            printf "输入不合法，请重新输入"
        fi
    done
fi

# 创建 proxsql
docker run -p 16032:6032 -p 16033:6033 -p 16070:6070 --name proxysql --network $NETWORK -d -v $PROXYSQL_HOME/proxysql.cnf:/etc/proxysql.cnf proxysql/proxysql &> /dev/null

progress "Build ProxySQL"
is_start 3

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


printf "==>${BOLD} 创建主从复制账号${NC}\n"
mysql -h 127.0.0.1 -P 3306 -u$DB_USER -p$DB_PASS <<EOF
CREATE USER 'replication.user'@'$NEW_MASTER_IP' IDENTIFIED WITH mysql_native_password BY 'Copy!234';
GRANT REPLICATION SLAVE ON *.* TO 'replication.user'@'$NEW_MASTER_IP';
FLUSH PRIVILEGES;
EOF
dynamic_cursor

printf "\n==>${BOLD} 开启 Slave-1 主从复制${NC}\n"
# log-file
MASTER_LOG_FILE=$(mysql -h 127.0.0.1 -P 3306 -u $DB_USER -p$DB_PASS -e "SHOW MASTER STATUS\G" | grep "File" | awk '{print $2}')
# log-pos
MASTER_LOG_POS=$(mysql -h 127.0.0.1 -P 3306 -u $DB_USER -p$DB_PASS -e "SHOW MASTER STATUS\G" | grep "Position" | awk '{print $2}')
mysql -h 127.0.0.1 -P 3307 -u $DB_USER -p$DB_PASS <<EOF
CHANGE MASTER TO MASTER_HOST='$MASTER_IP', MASTER_USER='replication.user', MASTER_PASSWORD='Copy!234', MASTER_LOG_FILE='$MASTER_LOG_FILE', MASTER_LOG_POS=$MASTER_LOG_POS;
START SLAVE;
EOF
dynamic_cursor

printf "\n==>${BOLD} 开启 slave-2 主从复制${NC}\n"
mysql -h 127.0.0.1 -P 3308 -u $DB_USER -p$DB_PASS <<EOF
CHANGE MASTER TO MASTER_HOST='$MASTER_IP', MASTER_USER='replication.user', MASTER_PASSWORD='Copy!234', MASTER_LOG_FILE='$MASTER_LOG_FILE', MASTER_LOG_POS=$MASTER_LOG_POS;
START SLAVE;
EOF
dynamic_cursor

# SLAVE_1_IO=$(mysql -h 127.0.0.1 -P 3307 -u $DB_USER -p$DB_PASS -e "SHOW SLAVE STATUS\G" | grep "Slave_IO_Running" | awk '{print $2}')
# SLAVE_2_IO=$(mysql -h 127.0.0.1 -P 3308 -u $DB_USER -p$DB_PASS -e "SHOW SLAVE STATUS\G" | grep "Slave_IO_Running" | awk '{print $2}')
# SLAVE_1_SQL=$(mysql -h 127.0.0.1 -P 3307 -u $DB_USER -p$DB_PASS -e "SHOW SLAVE STATUS\G" | grep "Slave_SQL_Running" | awk '{print $2}')
# SLAVE_2_SQL=$(mysql -h 127.0.0.1 -P 3308 -u $DB_USER -p$DB_PASS -e "SHOW SLAVE STATUS\G" | grep "Slave_SQL_Running" | awk '{print $2}')
# 
# if [ "$SLAVE_1_IO" != "Yes" ]; then
#     printf "slave-1 io: $SLAVE_1_IO"
#     # exit 1
# fi
# if [ "$SLAVE_1_SQL" != "Yes" ]; then
#     printf "slave-1 sql: $SLAVE_1_SQL"
#     # exit 1
# fi
# if [ "$SLAVE_2_IO" != "Yes" ]; then
#     printf "slave-2 io: $SLAVE_2_IO"
#     # exit 1
# fi
# if [ "$SLAVE_2_SQL" != "Yes" ]; then
#     printf "slave-2 sql: $SLAVE_2_SQL"
#     # exit 1
# fi

printf "\n==>${BOLD} 修改主从数据库 root 账号信息${NC}\n"
mysql -h 127.0.0.1 -P 3306 -u $DB_USER -p$DB_PASS <<EOF
ALTER USER 'root'@'localhost' IDENTIFIED BY 'Pass!234';
ALTER USER 'root'@'%' IDENTIFIED BY 'Pass!234';
FLUSH PRIVILEGES;
EOF
dynamic_cursor

# mysql -h 127.0.0.1 -P 3307 -u $DB_USER -p$DB_PASS <<EOF
# ALTER USER 'root'@'localhost' IDENTIFIED BY 'Pass!234';
# ALTER USER 'root'@'%' IDENTIFIED BY 'Pass!234';
# FLUSH PRIVILEGES;
# EOF

# mysql -h 127.0.0.1 -P 3308 -u $DB_USER -p$DB_PASS <<EOF
# ALTER USER 'root'@'localhost' IDENTIFIED BY 'Pass!234';
# ALTER USER 'root'@'%' IDENTIFIED BY 'Pass!234';
# FLUSH PRIVILEGES;
# EOF

printf "\n==>${BOLD} 创建监测用户和外部访问用户${NC}\n"
mysql -h 127.0.0.1 -P 3306 -u $DB_USER -p$DB_NEW_PASS <<EOF
create user 'proxy.monitor'@'$NEW_MASTER_IP' identified by '123456';
grant replication client on *.* to 'proxy.monitor'@'$NEW_MASTER_IP';
create user 'proxy.admin'@'%' identified by 'Pass!234';
grant all privileges on *.* to 'proxy.admin'@'%' with grant option;
FLUSH PRIVILEGES;
EOF
dynamic_cursor

# mysql -h 127.0.0.1 -P 3307 -u $DB_USER -p$DB_NEW_PASS <<EOF
# create user 'proxy.monitor'@'$NEW_MASTER_IP' identified by '123456';
# grant replication client on *.* to 'proxy.monitor'@'$NEW_MASTER_IP';
# create user 'proxy.admin'@'%' identified by 'Pass!234';
# grant all privileges on *.* to 'proxy.admin'@'%' with grant option;
# FLUSH PRIVILEGES;
# EOF

# mysql -h 127.0.0.1 -P 3308 -u $DB_USER -p$DB_NEW_PASS <<EOF 
# create user 'proxy.monitor'@'$NEW_MASTER_IP' identified by '123456';
# grant replication client on *.* to 'proxy.monitor'@'$NEW_MASTER_IP';
# create user 'proxy.admin'@'%' identified by 'Pass!234';
# grant all privileges on *.* to 'proxy.admin'@'%' with grant option;
# FLUSH PRIVILEGES;
# EOF

# mysql -h 127.0.0.1 -P 3306 -u $DB_USER -p$DB_NEW_PASS -e "CREATE DATABASE IF NOT EXISTS canary DEFAULT CHARACTER SET = utf8mb4;"
# 
# if [ $? -eq 0 ]; then
#     mysql -h 127.0.0.1 -P 3306 -u $DB_USER -p$DB_NEW_PASS canary < $SQL/canary.sql
# fi

printf "\n==>${BOLD} 添加 ProxySQL 主从分组信息${NC}\n"
mysql -h 127.0.0.1 -P 16032 -uradmin -pradmin --prompt "ProxySQL Admin>" <<EOF
insert into mysql_replication_hostgroups(writer_hostgroup,reader_hostgroup,check_type, COMMENT) values(10, 20,'read_only', 'proxy');
load mysql servers to runtime;
save mysql servers to disk;
EOF
dynamic_cursor

SLAVE_1_IP=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mysql-slave-1)
SLAVE_2_IP=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mysql-slave-2)

printf "\n==>${BOLD} 添加 ProxySQL 主从服务器的节点${NC}\n"
mysql -h 127.0.0.1 -P 16032 -uradmin -pradmin --prompt "ProxySQL Admin>" <<EOF
insert into mysql_servers(hostgroup_id,hostname,port)  values(10,'$MASTER_IP',3306),(20,'$SLAVE_1_IP',3306),(20,'$SLAVE_2_IP',3306);
load mysql servers to runtime;
save mysql servers to disk;
EOF
dynamic_cursor

printf "\n==>${BOLD} 配置 ProxySQL 监测账号${NC}\n"
mysql -h 127.0.0.1 -P 16032 -uradmin -pradmin --prompt "ProxySQL Admin>" <<EOF
UPDATE global_variables SET variable_value='proxy.monitor' WHERE variable_name='mysql-monitor_username';
UPDATE global_variables SET variable_value='123456' WHERE variable_name='mysql-monitor_password';
load mysql variables to runtime;
save mysql variables to disk;
EOF
dynamic_cursor

printf "\n==>${BOLD} 配置 ProxySQL 访问账号${NC}\n"
mysql -h 127.0.0.1 -P 16032 -uradmin -pradmin --prompt "ProxySQL Admin>" << EOF
insert into mysql_users(username,password,default_hostgroup) values('proxy.admin','Pass!234',10);
load mysql users to runtime;
save mysql users to disk;
EOF
dynamic_cursor

printf "\n==>${BOLD} 创建路由规则${NC}\n"
mysql -h 127.0.0.1 -P 16032 -uradmin -pradmin --prompt "ProxySQL Admin>" << EOF
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply) VALUES (1, 1, '^select', 20, 1),(2, 1, '^select.*for update$', 10, 1),(3, 1, '.*', 10, 1);
load mysql query rules to runtime;
save mysql query rules to disk;
EOF
dynamic_cursor

print_logo


