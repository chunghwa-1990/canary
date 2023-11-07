#!/bin/bash
pid=$(ps -ef | grep java | grep canary | awk '{print $2}')
if [ -n "$pid" ]; then
        kill -9 ${pid}
        sleep 2
        echo "Shutting down, bye..."
fi

