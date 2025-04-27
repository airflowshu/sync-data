#!/bin/bash
#chmod +x stop.sh
PID=$(ps -ef | grep 'sync-data.jar' | grep -v grep | awk '{print $2}')
if [ -z "$PID" ]; then
    echo "No running process found"
else
    kill -15 $PID
    if [ $? -ne 0 ]; then
        kill -9 $PID
    fi
    echo "sync-data process stopped"
fi