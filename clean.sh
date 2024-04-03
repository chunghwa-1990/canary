#!/bin/bash

HOME="/Users/zhaohongliang/DockerData"

NETWORK="canary-net"

MYSQL_HOME="$HOME/mysql"

PROXYSQL_HOME="$HOME/proxysql"

# 定义旋转的光标符号
SPINNER="/-\|"

# ANSI 转义码定义
RED='\033[0;31m'    # 红色字体
GREEN='\033[0;32m'  # 绿色字体
BLUE='\033[0;34m'   # 蓝色字体
BOLD='\033[1m'      # 加粗字体
NC='\033[0m'        # 恢复默认格式

# 停止服务并清理
function clean() {
    while [ $(docker ps -aq --filter "name=sql" | wc -l) -gt 0 ]; do
        for i in $(seq 0 3); do
            printf "\rStopping \`mysql\`...${SPINNER:$i:1}"   # 使用回车符（\r）实现光标回到行首
            sleep 0.2
        done
        docker stop `docker ps -a | grep sql | awk -F " " '{print $1}'` &> /dev/null
        docker rm `docker ps -a | grep sql | awk -F " " '{print $1}'` &> /dev/null
        docker network rm $NETWORK &> /dev/null
    done

    printf "\r\033[K"
    printf "Stopping \`mysql\`..."
    printf "\n${BLUE}===>${NC}${BOLD} Successfully stopped \`mysql\`!${NC}\n"
    
    while true; do
        read -p "Do you want to keep the data and configuration files?（y/n）" choice
        choice_lower=$(printf "$choice" | tr '[:upper:]' '[:lower:]')
        if [ "$choice_lower" == "y"  ] || [ "$choice_lower" == "yes"  ]; then
            break
        elif [ "$choice_lower" == "n"  ] || [ "$choice_lower" == "no"  ]; then
            rm -rf $MYSQL_HOME && rm -rf $PROXYSQL_HOME
            break
        else
            printf "Invalid choice. Please enter 'yes' or 'no'\n"
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
    clean
    logo
}

########################## main #########################
main