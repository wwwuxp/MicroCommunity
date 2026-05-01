FROM registry.cn-beijing.aliyuncs.com/sxd/ubuntu-java8:1.0
MAINTAINER hz <hezongshuju>

ADD bin/start_order.sh /root/


RUN chmod u+x /root/start_order.sh

CMD ["/root/start_order.sh","dev -Dcache"," -Xms512m -Xmx512m"]