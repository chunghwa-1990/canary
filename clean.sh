#!/bin/bash

###################### docker ######################
HOME="/Users/zhaohongliang/DockerData"
# network
NETWORK="canary-net"

###################### mysql #######################
MYSQL_HOME="$HOME/mysql"

##################### proxysql ######################
PROXYSQL_HOME="$HOME/proxysql"

# 定义旋转的光标符号
SPINNER="/-\|"

# ANSI 转义码定义
RED='\033[0;31m'    # 红色字体
GREEN='\033[0;32m'  # 绿色字体
BLUE='\033[0;34m'   # 蓝色字体
BOLD='\033[1m'      # 加粗字体
NC='\033[0m'        # 恢复默认格式

# 移除容器
function removeContainers() {
    container_number=$(docker ps -aq --filter "name=sql" | wc -l)
    container_filter=$(docker ps -a | grep sql | awk -F " " '{print $1}')
    if [ $container_number -gt 0 ]; then
        echo "Stopping mysql and proxysql...(might take a while)"
        docker stop $container_filter &> /dev/null
        sleep 2
        echo "${BLUE}===>${NC}${BOLD} Successfully stopped mysql and proxysql${NC}"
        
        docker rm $container_filter &> /dev/null
        sleep 2
        echo "${BLUE}===>${NC}${BOLD} Successfully removed mysql and proxysql${NC}"
    else
        echo "MySQL and ProxySQL containers stopped and removed."
    fi
}

# 移除网路
function removeNetwork() {
    flag=0
    while [ $flag -ne 1 ]; do
        read -p "Do you want to keep the $NETWORK network?（Y/N）" choice
        if [ "$choice" == "Y"  ] || [ "$choice" == "y" ]; then
            flag=1
        elif [ "$choice" == "N"  ] || [ "$choice" == "n" ]; then
            docker network rm $NETWORK &> /dev/null
            flag=1
        else
            echo "Invalid choice. Please enter 'Y' or 'N'"
        fi
    done
}

# 删除配置文件和数据
function removeData() {
    flag=0
    while [ $flag -ne 1 ]; do
        read -p "Do you want to keep the data and configuration files?（Y/N）" choice
        if [ "$choice" == "Y"  ] || [ "$choice" == "y" ]; then
            flag=1
        elif [ "$choice" == "N"  ] || [ "$choice" == "n" ]; then
            rm -rf $MYSQL_HOME && rm -rf $PROXYSQL_HOME
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
    removeContainers
    removeNetwork
    removeData
    logo
}

########################## main #########################
main