#!/bin/bash

# docker 挂载本地访问容器内的根目录
HOME="/Users/zhaohongliang/DockerData"
# 项目根目录
PROJECT="/Users/zhaohongliang/Github/canary"
# sql 文件根目录
SQL="/Users/zhaohongliang/Desktop"

# 命令行工具
COMMAND_DOCKER="docker"
COMMAND_MYSQL="mysql"
COMMAND_WGET="wget"
COMMAND_CURL="curl"

# 定义旋转的光标符号
spinner="/-\|"
# 旋转光标
dynamic_cursor() {
    local j=0
    local max=$1
    local str=$2

    while [ $j -le $max ]; do
        for i in $(seq 0 3); do
            printf "\r${str}${spinner:$i:1}"   # 使用回车符（\r）实现光标回到行首
            sleep 0.2
        done
        j=`expr 1 + $j`
    done
    printf "\r\033[K"
    printf "\n执行完毕！\n"
}

# 进度条
progress() {
    local i=0
    local b=' '
    local max=$1
    local str=$2

    while [ $i -le $max  ]; do
        printf "${str}：[%-100s] %d%%\r" $b $i
        sleep 0.1
        i=`expr 1 + $i`
        b=#$b
    done
    printf "\n${str} build successfully!\n"
}



if !command -v $COMMAND_DOCKER &> /dev/null
then
    echo "$COMMAND_DOCKER 未安装，脚本中断执行"
    exit 1
fi

# 检测mysql命令是否存在
if !command -v $COMMAND_MYSQL &> /dev/null
then
    echo "$COMMAND_DOCKER 未安装，脚本中断执行"
    exit 1
fi

# 镜像名称
IMAGE_NAME_MYSQL="mysql:8.0"
IMAGE_NAME_PROXYSQL="proxysql/proxysql"

# 如果mysql镜像不存在，则拉取
if !docker image inspect $IMAGE_NAME_MYSQL &> /dev/null
then
    docker pull mysql:8.0
    if [ $? -eq 0 ]; then
        echo "$IMAGE_NAME_MYSQL 拉取完成"
    fi
fi

# 如果proxy镜像不存在，则拉取
if !docker image inspect $IMAGE_NAME_PROXYSQL &> /dev/null
then
    docker pull proxysql/proxysql
    if [ $? -eq 0 ]; then
        echo "$IMAGE_NAME_PROXYSQL 拉取完成"
    fi
fi

# 网络
NETWORK_NAME="canary-net"
if docker network ls | grep -q $NETWORK_NAME &> /dev/null; then
    while true; do
        read -p "$NETWORK_NAME 已存在，如需覆盖请确认？（y/n）" choice
        choice_lower=$(echo "$choice" | tr '[:upper:]' '[:lower:]')
        if [ "$choice_lower" == "y" ] || [ "$choice_lower" == "yes" ]; then
            docker network rm $NETWORK_NAME &> /dev/null && docker network create $NETWORK_NAME &> /dev/null
            break
        elif [ "$choice_lower" == "n" ] || [ "$choice_lower" == "no" ]; then
            break
        else
            echo "输入不合法，请重新输入"
        fi
    done
else
    docker network create $NETWORK_NAME &> /dev/null
fi

MYSQL_HOME="$HOME/mysql"
MASTER_HOME="$MYSQL_HOME/mysql-master"
MASTER_CNF="$MASTER_HOME/conf/my.cnf"
SLAVE_1_HOME="$MYSQL_HOME/mysql-slave-1"
SLAVE_1_CNF="$SLAVE_1_HOME/conf/my.cnf"
SLAVE_2_HOME="$MYSQL_HOME/mysql-slave-2"
SLAVE_2_CNF="$SLAVE_2_HOME/conf/my.cnf"

if [ ! -d "$MYSQL_HOME" ]; then 
    # wget -P $MASTER_HOME/conf  https://github.com/hahapigs/canary/blob/main/mysql-master.cnf 
    mkdir -p $MASTER_HOME/conf
    mkdir -p $SLAVE_1_HOME/conf
    mkdir -p $SLAVE_2_HOME/conf
    cp -f $PROJECT/mysql-master.cnf $MASTER_HOME/conf
    cp -f $PROJECT/mysql-slave-1.cnf $SLAVE_1_HOME/conf
    cp -f $PROJECT/mysql-slave-2.cnf $SLAVE_2_HOME/conf
else
    if [ ! -f "$MASTER_CNF" ]; then
        # wget -P $MASTER_HOME/conf  https://github.com/hahapigs/canary/blob/main/mysql-master.cnf 
        mkdir -p $MASTER_HOME/conf
        cp -f $PROJECT/mysql-master.cnf $MASTER_HOME/conf/my.cnf
    else
        while true; do
            read -p "mysql-master 的 my.cnf 文件已存在，如需覆盖请确认？（y/n）" choice
            choice_lower=$(echo "$choice" | tr '[:upper:]' '[:lower:]')
            if [ "$choice_lower" == "y" ] || [ "$choice_lower" == "yes" ]; then
                cp -f $PROJECT/mysql-master.cnf $MASTER_HOME/conf/my.cnf
                break
            elif [ "$choice_lower" == "n" ] || [ "$choice_lower" == "no" ]; then
                break
            else
                echo "输入不合法，请重新输入"
            fi
        done
    fi

    if [ ! -f "$SLAVE_1_CNF" ]; then
        mkdir -p $SLAVE_1_HOME/conf
        cp -f $PROJECT/mysql-slave-1.cnf $SLAVE_1_HOME/conf/my.cnf
    else
        while true; do
            read -p "mysql-slave-1 的 my.cnf 文件已存在，如需覆盖请确认？（y/n）" choice
            choice_lower=$(echo "$choice" | tr '[:upper:]' '[:lower:]')
            if [ "$choice_lower" == "y" ] || [ "$choice_lower" == "yes" ]; then
                cp -f $PROJECT/mysql-slave-1.cnf $SLAVE_1_HOME/conf/my.cnf
                break
            elif [ "$choice_lower" == "n" ] || [ "$choice_lower" == "no" ]; then
                break
            else
                echo "输入不合法，请重新输入"
            fi
        done
    fi

    if [ ! -f "$SLAVE_2_CNF" ]; then
        mkdir -p $SLAVE_2_HOME/conf
        cp -f $PROJECT/mysql-slave-2.cnf $SLAVE_2_HOME/conf/my.cnf
    else
        while true; do
            read -p "mysql-slave-2 的 my.cnf 文件已存在，如需覆盖请确认？（y/n）" choice
            choice_lower=$(echo "$choice" | tr '[:upper:]' '[:lower:]')
            if [ "$choice_lower" == "y" ] || [ "$choice_lower" == "yes" ]; then
                cp -f $PROJECT/mysql-slave-2.cnf $SLAVE_2_HOME/conf/my.cnf
                break
            elif [ "$choice_lower" == "n" ] || [ "$choice_lower" == "no" ]; then
                break
            else
                echo "输入不合法，请重新输入"
            fi
        done
    fi
fi

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
    --network canary-net \
    mysql &> /dev/null

progress 100 "MySQL(Master)"

while ! docker ps | grep mysql-master; do
    for i in $(seq 0 3); do
        printf "\rmysql-master 正在启动...${spinner:$i:1}"
        sleep 0.2
    done
done
printf "Master 启动成功！\n"

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
    --network canary-net \
    mysql &> /dev/null

progress 100 "MySQL(Slave-1)"

while ! docker ps | grep mysql-slave-1; do
    for i in $(seq 0 3); do
        printf "\rmysql-slave-2 正在启动...${spinner:$i:1}"
        sleep 0.2
    done
done
printf "Slave-1 启动成功！\n"

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
    --network canary-net \
    mysql &> /dev/null

progress 100 "MySQL(Slave-2)"

while ! docker ps | grep mysql-slave-2; do
    for i in $(seq 0 3); do
        printf "\rmysql-slave-2 正在启动...${spinner:$i:1}"
        sleep 0.2
    done
done
printf "Slave-2 启动成功！\n"

# docker restart mysql-master && \
# docker restart mysql-slave-1 && \
# docker restart mysql-slave-2

PROXYSQL_HOME=$HOME/proxysql
if [ ! -f "$PROXYSQL_HOME/proxsql.cnf" ];
then
    # wget -P $PROXYSQL_HOME https://github.com/hahapigs/canary/blob/main/proxysql.cnf
    mkdir -p $PROXYSQL_HOME
    cp $PROJECT/proxysql.cnf $PROXYSQL_HOME
fi

docker run -p 16032:6032 -p 16033:6033 -p 16070:6070 --name proxysql --network canary-net -d -v $PROXYSQL_HOME/proxysql.cnf:/etc/proxysql.cnf proxysql/proxysql &> /dev/null

progress 100 "MySQL(ProxySQL)"

while ! docker ps | grep proxysql; do
    for i in $(seq 0 3); do
        printf "\rproxysql 正在启动...${spinner:$i:1}"
        sleep 0.2
    done
done
printf "ProxySQL 启动成功！\n"

nc -z -v -w 3 127.0.0.1 3306
# 检查连接状态
if [ $? -eq 0  ]; then
    echo "Master 数据库连接正常！"
else
    echo "Master 数据库连接失败！"
fi

nc -z -v -w 3 127.0.0.1 3307
# 检查连接状态
if [ $? -eq 0  ]; then
    echo "Slave-1 数据库连接正常！"
else
    echo "Slave-1 数据库连接失败！"
fi

nc -z -v -w 3 127.0.0.1 3308
# 检查连接状态
if [ $? -eq 0  ]; then
    echo "Slave-2 数据库连接正常！"
else
    echo "Slave-2 数据库连接失败！"
fi

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

dynamic_cursor 10 "Master 正在创建主从复制账号..."

# 创建主从同步复制用户
mysql -h 127.0.0.1 -P 3306 -u$DB_USER -p$DB_PASS <<EOF
CREATE USER 'replication.user'@'$NEW_MASTER_IP' IDENTIFIED WITH mysql_native_password BY 'Copy!234';
GRANT REPLICATION SLAVE ON *.* TO 'replication.user'@'$NEW_MASTER_IP';
FLUSH PRIVILEGES;
EOF

dynamic_cursor 10 "Slave-1 开启复制..."

# log-file
MASTER_LOG_FILE=$(mysql -h 127.0.0.1 -P 3306 -u $DB_USER -p$DB_PASS -e "SHOW MASTER STATUS\G" | grep "File" | awk '{print $2}')
# log-pos
MASTER_LOG_POS=$(mysql -h 127.0.0.1 -P 3306 -u $DB_USER -p$DB_PASS -e "SHOW MASTER STATUS\G" | grep "Position" | awk '{print $2}')
# 开启 slave-1 主从复制
mysql -h 127.0.0.1 -P 3307 -u $DB_USER -p$DB_PASS <<EOF
CHANGE MASTER TO MASTER_HOST='$MASTER_IP', MASTER_USER='replication.user', MASTER_PASSWORD='Copy!234', MASTER_LOG_FILE='$MASTER_LOG_FILE', MASTER_LOG_POS=$MASTER_LOG_POS;
START SLAVE;
EOF

dynamic_cursor 10 "Slave-2 开启复制..."

# 开启 slave-2 主从复制
mysql -h 127.0.0.1 -P 3308 -u $DB_USER -p$DB_PASS <<EOF
CHANGE MASTER TO MASTER_HOST='$MASTER_IP', MASTER_USER='replication.user', MASTER_PASSWORD='Copy!234', MASTER_LOG_FILE='$MASTER_LOG_FILE', MASTER_LOG_POS=$MASTER_LOG_POS;
START SLAVE;
EOF

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

dynamic_cursor 10 "修改主从数据库root账号信息..."

mysql -h 127.0.0.1 -P 3306 -u $DB_USER -p$DB_PASS <<EOF
ALTER USER 'root'@'localhost' IDENTIFIED BY 'Pass!234';
ALTER USER 'root'@'%' IDENTIFIED BY 'Pass!234';
FLUSH PRIVILEGES;
EOF

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

dynamic_cursor 10 "正在创建监测用户和外部访问用户..."

mysql -h 127.0.0.1 -P 3306 -u $DB_USER -p$DB_NEW_PASS <<EOF
create user 'proxy.monitor'@'$NEW_MASTER_IP' identified by '123456';
grant replication client on *.* to 'proxy.monitor'@'$NEW_MASTER_IP';
create user 'proxy.admin'@'%' identified by 'Pass!234';
grant all privileges on *.* to 'proxy.admin'@'%' with grant option;
FLUSH PRIVILEGES;
EOF

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

dynamic_cursor 10 "正在添加ProxySQL主从分组信息..."

mysql -h 127.0.0.1 -P 16032 -uradmin -pradmin --prompt "ProxySQL Admin>" <<EOF
insert into mysql_replication_hostgroups(writer_hostgroup,reader_hostgroup,check_type, COMMENT) values(10, 20,'read_only', 'proxy');
load mysql servers to runtime;
save mysql servers to disk;
EOF

dynamic_cursor 10 "正在添加ProxySQL主从服务器的节点..."

SLAVE_1_IP=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mysql-slave-1)
SLAVE_2_IP=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mysql-slave-2)
mysql -h 127.0.0.1 -P 16032 -uradmin -pradmin --prompt "ProxySQL Admin>" <<EOF
insert into mysql_servers(hostgroup_id,hostname,port)  values(10,'$MASTER_IP',3306),(20,'$SLAVE_1_IP',3306),(20,'$SLAVE_2_IP',3306);
load mysql servers to runtime;
save mysql servers to disk;
EOF

dynamic_cursor 10 "正在配置ProxySQL监测账号..."

mysql -h 127.0.0.1 -P 16032 -uradmin -pradmin --prompt "ProxySQL Admin>" <<EOF
UPDATE global_variables SET variable_value='proxy.monitor' WHERE variable_name='mysql-monitor_username';
UPDATE global_variables SET variable_value='123456' WHERE variable_name='mysql-monitor_password';
load mysql variables to runtime;
save mysql variables to disk;
EOF

dynamic_cursor 10 "正在配置ProxySQL访问账号..."

mysql -h 127.0.0.1 -P 16032 -uradmin -pradmin --prompt "ProxySQL Admin>" << EOF
insert into mysql_users(username,password,default_hostgroup) values('proxy.admin','Pass!234',10);
load mysql users to runtime;
save mysql users to disk;
EOF

dynamic_cursor 10 "正在创建路由规则..."

mysql -h 127.0.0.1 -P 16032 -uradmin -pradmin --prompt "ProxySQL Admin>" << EOF
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply) VALUES (1, 1, '^select', 20, 1),(2, 1, '^select.*for update$', 10, 1),(3, 1, '.*', 10, 1);
load mysql query rules to runtime;
save mysql query rules to disk;
EOF

