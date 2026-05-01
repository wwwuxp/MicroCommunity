FROM registry.cn-beijing.aliyuncs.com/sxd/ubuntu-java8:1.0
MAINTAINER hz <hezongshuju>

ADD bin/start_api.sh /root/


RUN chmod u+x /root/start_api.sh

CMD ["/root/start_api.sh","dev -Dcache -Xms512m -Xmx512m"]