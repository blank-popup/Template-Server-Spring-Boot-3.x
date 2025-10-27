#!/bin/bash

echo "STARTING deploy ..."

FILENAME_JAR="project-name-0.0.1-SNAPSHOT.jar"
PATH_SERVER="/path/to/project-name"
FILE_ENV="$PATH_SERVER/.env"

echo "Killing process $FILENAME_JAR ..."
pid=$(pgrep -f $FILENAME_JAR)
echo "PID of server: ${pid}" 

if [ ${pid} > 0 ]
then
        kill -9 ${pid}
        echo "killed process ${pid}"
        sleep 2
else
        echo "No running process"
fi

echo "Starting server $FILENAME_JAR ..."
cd $PATH_SERVER
source ${FILE_ENV}
BUILD_ID=dontKillMe java -jar -Dspring.profiles.active=$1 $FILENAME_JAR > /dev/null 2>&1 &
echo "Started server"

pid=$(pgrep -f $FILENAME_JAR)
echo "PID of new server: ${pid}"

echo "ENDED deploy ..."
