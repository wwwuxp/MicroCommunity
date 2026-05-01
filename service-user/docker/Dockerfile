FROM registry.cn-beijing.aliyuncs.com/sxd/ubuntu-java8:1.0
MAINTAINER hz <hezongshuju.com>

ADD bin/start_user.sh /root/


RUN chmod u+x /root/start_user.sh

CMD ["/root/start_user.sh","dev -Xms512m -Xmx512m"]