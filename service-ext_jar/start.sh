 nohup java -jar -Dserver.port=8087 -Dspring.profiles.active=prod -Dspring.datasource.url="jdbc:mysql://192.168.0.181:3306/tt?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true" -Xms512m -Xmx512m service-ext.jar > app_$(date +"%Y-%m-%d").log 2>&1 &

