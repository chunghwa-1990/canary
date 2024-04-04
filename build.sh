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
FRAMEWORK=""
MASTER_IP=""
MASTER_NAME=""
REPLICATION_USER="replicaion.user"
REPLICATION_PASSWORD="123456"

alphabet=("A" "B" "C" "D" "E" "F" "G" "H" "I" "J" "K" "L" "M" "N" "O" "P" "Q" "R" "S" "T" "U" "V" "W" "X" "Y" "Z")

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
    local index=$1
    local continer_name=${CONTAINERS[$1]}
    
    while [ $i -le $max  ]; do
        printf "${continer_name}:[%-100s] %d%%\r" $b $i
        sleep 0.1
        i=`expr 1 + $i`
        b=#$b
    done
    printf "\n${continer_name} build successfull!\n"
    isStart ${index}
}

# 校验数据库是否启动
function isStart() {
    local index=$1
    
    while ! docker ps | grep ${CONTAINERS[$index]}; do
        for i in $(seq 0 3); do
            printf "\r${CONTAINERS[$1]} 正在启动...${SPINNER:$i:1}"
            sleep 0.2
        done
    done
    printf "${CONTAINERS[$index]} start successful！\n"
    isValid ${index}
}

# 校验连接是否正常
function isValid() {
    local index=$1
    local port=${PORTS[$1]}
    
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
    local network_exists=$(docker network ls --format '{{.Name}}' | grep -w "$NETWORK")
    if [ -z "$network_exists" ]; then
        echo "Creating network...(might take a while)"
        docker network create $NETWORK &> /dev/null
        sleep 2
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
}

function choiceDeploy() {
    # labels A, B
    options=(${alphabet[@]:0:2})
    # 架构
    architecture=("standalone" "cluster")
    
    
    # Prompt the user to choose from A or B
    echo "部署模式："
    for index in ${!options[@]}; do
        echo "${options[index]}. ${architecture[index]}"
        option=${options[index]}
        eval "${option}=${architecture[index]}"
    done
    
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

# 选择框架
function choiceFramework() {
    # labels A, B
    options=(${alphabet[@]:0:2})
    # framework
    frameworks=("MGR" "DEFAULT")
    
    # Prompt the user to choose from A or B
    echo "MySql-Replicaion Framework:"
    for index in ${!options[@]}; do
        echo "${options[index]}. ${frameworks[index]}"
        option=${options[index]}
        eval "${option}=${frameworks[index]}"
    done
    
    while [ -z "$FRAMEWORK" ]; do
        # User selection
        read -p "Enter your choice (A or B): " choice
        choice_upper=$(printf "$choice" | tr '[:lower:]' '[:upper:]')
        
        # Check user choice and display the selected line
        case $choice_upper in
            A)
                FRAMEWORK=$A
            ;;
            B)
                FRAMEWORK=$B
                choiceMaster
                writeMySqlCnf
            ;;
            *) echo "Invalid choice";;
        esac
    done
}

# 选择 master
function choiceMaster() {
    # labels A, B, C
    options=(${alphabet[@]:0:3})
    # mysql-1, mysql-2, mysql-3
    containers=(${CONTAINERS[@]})
    
    # Prompt the user to choose from A、B or C
    echo "MySql-Replicaion Master:"
    for index in ${!options[@]}; do
        echo "${options[index]}. ${containers[index]}"
        option=${options[index]}
        eval "${option}=${containers[index]}"
    done
    
    while [ -z "$MASTER_NAME"  ]; do
        # User selection
        read -p "Enter your choice (A、B or C): " choice
        choice_upper=$(printf "$choice" | tr '[:lower:]' '[:upper:]')
        
        # Check user choice and display the selected line
        case $choice_upper in
            A)
                MASTER_NAME=$A
            ;;
            B)
                MASTER_NAME=$B
            ;;
            C)
                MASTER_NAME=$C
            ;;
            *) echo "Invalid choice";;
        esac
    done
}

# 创建 my.cnf
function writeMySqlCnf() {
    for index in "${!CONTAINERS[@]}"; do
        mkdir -p $MYSQL_HOME/${CONTAINERS[$index]}/conf
        if [ "${CONTAINERS[$index]}" == "$MASTER_NAME" ]; then
            cat << EOF >> $MYSQL_HOME/${CONTAINERS[$index]}/conf/my.cnf
[mysqld]
server-id = $((index+1))
# 启用二进制日志
log_bin = mysql-bin
# 设置binlog格式 STATEMENT(同步SQL脚本) / ROW(同步行数据) / MIXED(混合同步)
binlog_format = ROW
EOF
        else
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
# 0-读写 1-只读
read-only = 1
EOF
        fi
    done
}

# mysql-1
function createMySql1() {
    if [ ! -d "$MYSQL_1_HOME/conf" ]; then
        mkdir -p $MYSQL_1_HOME/conf
    fi
    
    docker run \
        -itd \
        --name ${CONTAINERS[0]} \
        -p ${PORTS[0]}:3306 \
        -v $MYSQL_1_HOME/conf:/etc/mysql/conf.d \
        -v $MYSQL_1_HOME/data:/var/lib/mysql \
        -v $MYSQL_1_HOME/log:/var/log/mysql \
        -v /etc/localtime:/etc/localtime \
        -e MYSQL_ROOT_PASSWORD=$DB_PASSWORD   \
        --restart no \
        --privileged=true \
        --network $NETWORK \
        mysql &> /dev/null
    progress 0
}

# mysql-2
function createMySql2() {
    if [ ! -d "$MYSQL_2_HOME/conf" ]; then
        mkdir -p $MYSQL_2_HOME/conf
    fi
    
    docker run \
    -itd \
        --name ${CONTAINERS[1]} \
        -p ${PORTS[1]}:3306 \
        -v $MYSQL_2_HOME/conf:/etc/mysql/conf.d \
        -v $MYSQL_2_HOME/data:/var/lib/mysql \
        -v $MYSQL_2_HOME/log:/var/log/mysql \
        -v /etc/localtime:/etc/localtime \
        -e MYSQL_ROOT_PASSWORD=$DB_PASSWORD   \
        --restart no \
        --privileged=true \
        --network $NETWORK \
        mysql &> /dev/null
    progress 1
}

# mysql-3
function createMySql3() {
    if [ ! -d "$MYSQL_3_HOME/conf" ]; then
        mkdir -p $MYSQL_3_HOME/conf
    fi
    
    docker run \
        -itd \
        --name ${CONTAINERS[2]} \
        -p ${PORTS[2]}:3306 \
        -v $MYSQL_3_HOME/conf:/etc/mysql/conf.d \
        -v $MYSQL_3_HOME/data:/var/lib/mysql \
        -v $MYSQL_3_HOME/log:/var/log/mysql \
        -v /etc/localtime:/etc/localtime \
        -e MYSQL_ROOT_PASSWORD=$DB_PASSWORD   \
        --restart no \
        --privileged=true \
        --network $NETWORK \
        mysql &> /dev/null
    progress 2
}

# 启动复制
function startSlave() {
    MASTER_IP=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $MASTER_NAME)
    MASTER_IP_LENGTH=${#MASTER_IP}
    FONT_PART=${MASTER_IP:0:$(($MASTER_IP_LENGTH-1))}
    LAST_CHAR=${MASTER_IP:-1}
    REPLICATION_HOST="${FONT_PART}%"
    
    MASTER_PORT=$(docker port $MASTER_NAME | grep 3306 | awk '{print $3}' | cut -d ":" -f 2)
    MASTER_LOG_FILE=$(mysql -h 127.0.0.1 -P $MASTER_PORT -u $DB_USER -p$DB_PASSWORD -e "SHOW MASTER STATUS\G" | grep "File" | awk '{print $2}')
    MASTER_LOG_POS=$(mysql -h 127.0.0.1 -P $MASTER_PORT -u $DB_USER -p$DB_PASSWORD -e "SHOW MASTER STATUS\G" | grep "Position" | awk '{print $2}')
    
    mysql -h 127.0.0.1 -P $MASTER_PORT -u$DB_USER -p$DB_PASSWORD << EOF
CREATE USER '$REPLICATION_USER'@'$REPLICATION_HOST' IDENTIFIED WITH mysql_native_password BY '$REPLICATION_PASSWORD';
GRANT REPLICATION SLAVE ON *.* TO '$REPLICATION_USER'@'$REPLICATION_HOST';
FLUSH PRIVILEGES;
EOF
    
    for ((i=0; i<${#CONTAINERS[@]}; i++)); do
        if [ "$MASTER_NAME" != "${CONTAINERS[i]}" ]; then
            mysql -h 127.0.0.1 -P ${PORTS[i]} -u$DB_USER -p$DB_PASSWORD << EOF
CHANGE MASTER TO MASTER_HOST='$MASTER_IP', MASTER_USER='$REPLICATION_USER', MASTER_PASSWORD='$REPLICATION_PASSWORD', MASTER_LOG_FILE='$MASTER_LOG_FILE', MASTER_LOG_POS=$MASTER_LOG_POS;
START SLAVE;
EOF
        fi
    done
}

# standalone
function standalone() {
    echo "构建进程："
    createMySql1
}

# cluster
function cluster() {
    choiceFramework
    echo "构建进程："
    createMySql1
    createMySql2
    createMySql3
    if [ "$FRAMEWORK" != "MGR" ]; then
        startSlave
    fi
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

