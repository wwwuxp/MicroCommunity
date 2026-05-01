FROM registry.cn-beijing.aliyuncs.com/sxd/ubuntu-java8:1.0
MAINTAINER hz <hezongshuju>

ADD bin/start_pubcar.sh /root/


RUN chmod u+x /root/start_pubcar.sh

CMD ["/root/start_pubcar.sh","dev -Xms512m -Xmx512m"]
