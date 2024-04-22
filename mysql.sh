#!/bin/bash

###################### docker ######################
DOCKER_HOME="$HOME/DockerData"
# network
NETWORK="canary-net"
# containers
CONTAINERS=("mysql-1" "mysql-2" "mysql-3")
PORTS=("3306" "3307" "3308")

###################### mysql #######################
MYSQL_HOME="$DOCKER_HOME/mysql"
# 管理员用户名、密码
DB_USER="root"
DB_PASSWORD="123456"
DB_NEW_PASSWORD="Pass!234"
# 主从复制用户名、密码
REPLICATION_USER="replicaion.user"
REPLICATION_PASSWORD="123456"
# MGR服务器ip、端口和白名单
MGR_PORT="33061"
group_seeds=""
whitelist=""
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
    printf "\n${continer_name} build successfull!\n"
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

# 网路
function checkNetwork() {
    network_exists=$(docker network ls --format '{{.Name}}' | grep -w "$NETWORK")
    if [ -z "$network_exists" ]; then
        echo "Creating network...(might take a while)"
        docker network create $NETWORK &> /dev/null
        echo "${BLUE}===>${NC}${BOLD} Successfully created network${NC}"
    else
        echo "The network $NETWORK already exists."
        while true; do
            read -p "Do you want to recreate the $NETWORK network? (Y/N): " choice
            if [ "$choice" == "Y"  ] || [ "$choice" == "y" ]; then
                docker network rm $NETWORK &> /dev/null
                docker network create $NETWORK &> /dev/null
                break
            elif [ "$choice" == "N"  ] || [ "$choice" == "n" ]; then
                echo "Continuing to use the existing $NETWORK network"
                break
            else
                echo "Invalid choice"
            fi
        done
    fi
    subnet=$(docker network inspect $NETWORK | jq -r '.[0].IPAM.Config.[0].Subnet')
    gateway=$(docker network inspect $NETWORK | jq -r '.[0].IPAM.Config.[0].Gateway')
    echo "Subnet: $subnet"
    echo "Gateway: $gateway"
}

# 部署模式
function choiceDeploy() {
    # labels A, B
    options=(${ALPHABET[@]:0:2})
    # 架构
    architecture=("standalone" "cluster")

    # Prompt the user to choose from A or B
    echo "部署模式："
    for index in ${!options[@]}; do
        echo "${options[index]}. ${architecture[index]}"
        option=${options[index]}
        eval "${option}=${architecture[index]}"
    done

    # Prompt the user to choose from A, B, or C
    # echo "Please choose from the following options:"
    # echo "A: $A"
    # echo "B: $B"
    # echo "C: $C"

    flag=0
    while [ $flag -ne 1 ]; do
        # User selection
        read -p "Enter your choice (A or B): " choice

        # Check user choice and display the selected line
        case $choice in
            A | a)
                standalone
                flag=1
            ;;
            B | b)
                cluster
                flag=1
            ;;
            *) echo "Invalid choice";;
        esac
    done
}

# standalone
function standalone() {
    createMySql ${CONTAINERS[@]:0:1}
    resetPassword ${PORTS[0]}
}

# cluster
function cluster() {
    choiceFramework
    resetPassword $master_port
}

# 选择框架
function choiceFramework() {
    # labels A, B
    options=(${ALPHABET[@]:0:2})
    # framework
    frameworks=("MGR" "DEFAULT")

    # Prompt the user to choose from A or B
    echo "MySql-Replicaion Framework:"
    for index in ${!options[@]}; do
        echo "${options[index]}. ${frameworks[index]}"
        option=${options[index]}
        eval "${option}=${frameworks[index]}"
    done

    # Prompt the user to choose from A or B
    # echo "Please choose from the following options:"
    # echo "A: $A"
    # echo "B: $B"

    framework=""
    while [ -z "$framework" ]; do
        # User selection
        read -p "Enter your choice (A or B): " choice

        # Check user choice and display the selected line
        case $choice in
            A | a)
                createMySql ${CONTAINERS[@]}
                choiceMaster
                writeMgnCnf
                restarMySql
                startGroupReplication
                framework=$A
            ;;
            B | b)
                choiceMaster
                writeMySqlCnf
                createMySql ${CONTAINERS[@]}
                startReplication
                framework=$B
            ;;
            *) echo "Invalid choice";;
        esac
    done
}

# 选择 master
function choiceMaster() {
    # labels A, B, C
    options=(${ALPHABET[@]:0:3})
    # mysql-1, mysql-2, mysql-3
    containers=(${CONTAINERS[@]})

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

# MGR group
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
    number=${#containers[@]}
    # labels A, B, C
    options=(${ALPHABET[@]:0:$number})

    # Loop through the containers and assign them to options A, B, C
    for index in ${!options[@]}; do
        option=${options[index]}
        eval "${option}=${lines[index]}"
    done

    group_seeds="$A:$MGR_PORT,$B:$MGR_PORT,$C:$MGR_PORT"
    # whitelist="$A,$B,$C"
    whitelist=$(docker network inspect $NETWORK | jq -r '.[0].IPAM.Config.[0].Subnet')
}

# MySQL 组复制配置
function writeMgnCnf() {
    getGroupSeeds
    for index in ${!CONTAINERS[@]}; do
        local_ip=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' ${CONTAINERS[index]})
        mkdir -p $MYSQL_HOME/${CONTAINERS[index]}/conf
        cat << EOF > $MYSQL_HOME/${CONTAINERS[index]}/conf/my.cnf
[mysqld]
server-id = $((index+1))
# 启用二进制日志功能
log_bin = mysql-bin
# 指定错误日志文件的路径
log_error = "/var/log/mysql/error.log"
# 指定二进制日志中记录的内容限制为行级别的更改
binlog_format = ROW

# 指定将告警信息写入错误日志 1:只记录错误信息 2:记录错误和警告信息 3:记录所有错误和警告信息 默认:3
log_error_verbosity = 1

# 指定数据文件的存储路径
datadir="/var/lib/mysql"

# 启用慢查询日志
slow_query_log = 1
# 指定查询执行时间超过多少秒才被记录到慢查询日志中（例如1表示超过1秒的查询）
long_query_time = 1
# 慢查询日志的输出方式为写入文件
log_output = FILE
# 指定慢查询日志文件的保存路径
slow_query_log_file = /var/log/mysql/slow-query.log

# 启用全局查询日志
general_log = 1
# 全局查询日志保存路径
general_log_file = /var/log/mysql/general-query.log

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

# MySQL 主从复制配置
function writeMySqlCnf() {
    for index in ${!CONTAINERS[@]}; do
        mkdir -p $MYSQL_HOME/${CONTAINERS[index]}/conf
        if [ "${CONTAINERS[index]}" == "$master_name" ]; then
            cat << EOF > $MYSQL_HOME/${CONTAINERS[index]}/conf/my.cnf
[mysqld]
server-id = $((index+1))
# 启用二进制日志功能
log_bin = mysql-bin
# 指定错误日志文件的路径
log_error = "/var/log/mysql/error.log"
# 指定二进制日志中记录的内容限制为行级别的更改
binlog_format = ROW

# 指定将告警信息写入错误日志 1:只记录错误信息 2:记录错误和警告信息 3:记录所有错误和警告信息 默认:3
log_error_verbosity = 1

# 指定数据文件的存储路径
datadir="/var/lib/mysql"

# 启用慢查询日志
slow_query_log = 1
# 指定查询执行时间超过多少秒才被记录到慢查询日志中（例如1表示超过1秒的查询）
long_query_time = 1
# 慢查询日志的输出方式为写入文件
log_output = FILE
# 指定慢查询日志文件的保存路径
slow_query_log_file = /var/log/mysql/slow-query.log

# 启用全局查询日志
general_log = 1
# 全局查询日志保存路径
general_log_file = /var/log/mysql/general-query.log
EOF
        else
            cat << EOF > $MYSQL_HOME/${CONTAINERS[$index]}/conf/my.cnf
[mysqld]
server-id = $((index+1))
# 启用二进制日志功能
log_bin = mysql-bin
# 指定错误日志文件的路径
log_error = "/var/log/mysql/error.log"
# 指定二进制日志中记录的内容限制为行级别的更改
binlog_format = ROW

# 指定将告警信息写入错误日志 1:只记录错误信息 2:记录错误和警告信息 3:记录所有错误和警告信息 默认:3
log_error_verbosity = 1

# 指定数据文件的存储路径
datadir="/var/lib/mysql"

# 启用慢查询日志
slow_query_log = 1
# 指定查询执行时间超过多少秒才被记录到慢查询日志中（例如1表示超过1秒的查询）
long_query_time = 1
# 慢查询日志的输出方式为写入文件
log_output = FILE
# 指定慢查询日志文件的保存路径
slow_query_log_file = /var/log/mysql/slow-query.log

# 启用全局查询日志
general_log = 1
# 全局查询日志保存路径
general_log_file = /var/log/mysql/general-query.log

# 将执行的二进制日志事件也记录到自己的二进制日志中，会增加从库写负载和二进制文件大小
log_slave_updates = 1
# 考虑到后期故障切换，增加slave的中继日志
relay-log = relay-log-bin
# 0-读写 1-只读
read-only = 1
EOF
        fi
    done
}

# 创建 mysql
function createMySql() {
    local containers=(${@})

    echo "构建进程："
    for index in ${!containers[@]}; do
        container_home=$MYSQL_HOME/${containers[index]}

        mkdir -p $container_home/conf
        docker run \
            -itd \
            --name ${containers[index]} \
            -p ${PORTS[index]}:3306 \
            -v $container_home/conf:/etc/mysql/conf.d \
            -v $container_home/data:/var/lib/mysql \
            -v $container_home/logs:/var/log/mysql \
            -v /etc/localtime:/etc/localtime \
            -e MYSQL_ROOT_PASSWORD=$DB_PASSWORD \
            --restart no \
            --privileged=true \
            --network $NETWORK \
            mysql &> /dev/null
            
        progress ${containers[index]}
        isStart ${containers[index]}
        sleep 2
        isValid ${containers[index]} ${PORTS[index]}
    done
}

# 重启 mysql
function restarMySql() {
    container_filter=$(docker ps -a | grep sql | awk -F " " '{print $1}')
    echo "Stopping mysql...(might take a while)"
    docker restart $container_filter &> /dev/null
    sleep 10
    echo "${BLUE}===>${NC}${BOLD} Successfully stopped mysql${NC}"
    echo "${BLUE}===>${NC}${BOLD} Successfully started mysql${NC}"
}

# 启动组复制
function startGroupReplication() {
    master_ip=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $master_name)
    master_port=$(docker port $master_name | grep 3306 | awk '{print $3}' | cut -d ":" -f 2)
    gateway=$(docker network inspect $NETWORK | jq -r '.[0].IPAM.Config.[0].Gateway')
    replication_host=$(echo $gateway | sed 's/\.1$/\.%/g')
    
    echo ""
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
            sleep 2
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

# 启动复制
function startReplication() {
    master_ip=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $master_name)
    master_port=$(docker port $master_name | grep 3306 | awk '{print $3}' | cut -d ":" -f 2)
    gateway=$(docker network inspect $NETWORK | jq -r '.[0].IPAM.Config.[0].Gateway')
    replication_host=$(echo $gateway | sed 's/\.1$/\.%/g')
    master_log_file=$(mysql -h 127.0.0.1 -P $master_port -u $DB_USER -p$DB_PASSWORD -e "SHOW MASTER STATUS\G" | grep "File" | awk '{print $2}')
    master_log_pos=$(mysql -h 127.0.0.1 -P $master_port -u $DB_USER -p$DB_PASSWORD -e "SHOW MASTER STATUS\G" | grep "Position" | awk '{print $2}')
    
    mysql -h 127.0.0.1 -P $master_port -u$DB_USER -p$DB_PASSWORD << EOF
CREATE USER '$REPLICATION_USER'@'$replication_host' IDENTIFIED WITH mysql_native_password BY '$REPLICATION_PASSWORD';
GRANT REPLICATION SLAVE ON *.* TO '$REPLICATION_USER'@'$replication_host';
FLUSH PRIVILEGES;
EOF

    for ((i=0; i<${#CONTAINERS[@]}; i++)); do
        if [ "$master_name" != "${CONTAINERS[i]}" ]; then
            sleep 2
            mysql -h 127.0.0.1 -P ${PORTS[i]} -u $DB_USER -p$DB_PASSWORD << EOF
CHANGE MASTER TO MASTER_HOST='$master_ip', MASTER_USER='$REPLICATION_USER', MASTER_PASSWORD='$REPLICATION_PASSWORD', master_log_file='$master_log_file', master_log_pos=$master_log_pos;
START SLAVE;
EOF
        fi
        
    done
}

# 重置密码
function resetPassword() {
    local port=$1
    flag=0
    while [ $flag -ne 1 ]; do
        echo ""
        read -p "Do you want to reset the root password?（Y/N）" choice
        if [ "$choice" == "Y"  ] || [ "$choice" == "y" ]; then
            mysql -h 127.0.0.1 -P $port -u $DB_USER -p$DB_PASSWORD << EOF
ALTER USER 'root'@'localhost' IDENTIFIED BY '$DB_NEW_PASSWORD';
ALTER USER 'root'@'%' IDENTIFIED BY '$DB_NEW_PASSWORD';
FLUSH PRIVILEGES;
EOF
            flag=1
        elif [ "$choice" == "N"  ] || [ "$choice" == "n" ]; then
            flag=1
        else
            echo "Invalid choice. Please enter 'Y' or 'N'"
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
    checkNetwork
    choiceDeploy
    logo
}

########################## main #########################
main

