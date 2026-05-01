FROM registry.cn-beijing.aliyuncs.com/sxd/ubuntu-java8:1.0
MAINTAINER hz <hezongshuju>


ADD bin/start_store.sh /root/


RUN chmod u+x /root/start_store.sh

CMD ["/root/start_store.sh","dev -Xms512m -Xmx512m"]