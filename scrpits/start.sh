# chmod +x start.sh
# jar包名称
JAR_NAME="sync-data.jar"
# 日志文件名称
LOG_NAME="info.log"

nohup java -Djava.net.preferIPv4Stack=true -jar ${JAR_NAME} --spring.profiles.active=huainan > logs/${LOG_NAME} 2>&1 &
echo "${JAR_NAME}启动成功"
tail -f -n 500 logs/${LOG_NAME}
