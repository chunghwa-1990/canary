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
MASTER_NAME=""

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
        printf "${continer_name}：[%-100s] %d%%\r" $b $i
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

# 选择框架
function choiceFramework() {
    # frameworks 
    frameworks=("MGR" "DEFAULT")
    # labels A, B
    options=("A" "B")
    
    # Prompt the user to choose from A or B
    echo "请选择 MySql-Replicaion framework:"
    for ((i=0; i<${#options[@]}; i++)); do
        echo "${options[i]}. ${frameworks[i]}"
    done
    
    FRAMEWORK=""
    while [ -z "$FRAMEWORK" ]; do
        # User selection
        read -p "Enter your choice (A or B): " choice
        choice_upper=$(printf "$choice" | tr '[:lower:]' '[:upper:]')
        
        # Check user choice and display the selected line
        case $choice_upper in
            A)
                FRAMEWORK=${options[0]}
            ;;
            B)
                FRAMEWORK=${options[1]}
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
    options=("A" "B" "C")
    # Prompt the user to choose from A、B or C
    echo "请选择 MySql-Replicaion framework:"
    for ((i=0; i<${#options[@]}; i++)); do
        echo "${options[i]}. ${CONTAINERS[i]}"
    done
    
    while [ -z "$MASTER_NAME" ]; do
        # User selection
        read -p "Enter your choice (A、B or C): " choice
        choice_upper=$(printf "$choice" | tr '[:lower:]' '[:upper:]')
        
        # Check user choice and display the selected line
        case $choice_upper in
            A)
                MASTER_NAME=${CONTAINERS[0]}
            ;;
            B)
                MASTER_NAME=${CONTAINERS[1]}
            ;;
            C)
                MASTER_NAME=${CONTAINERS[1]}
            ;;
            *) echo "Invalid choice";;
        esac
    done
}

# 创建 my.cnf
function writeMySqlCnf() {
    echo $MASTER_NAME
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

# cluster
function createCluster() {
    createMySql1
    createMySql2
    createMySql3
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
    choiceFramework
    echo "构建进程："
    createCluster
    logo
}

########################## main #########################
main

