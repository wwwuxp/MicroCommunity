FROM registry.cn-beijing.aliyuncs.com/sxd/ubuntu-java8:1.0
MAINTAINER hz <hezongshuju>


ADD bin/start_fee.sh /root/


RUN chmod u+x /root/start_fee.sh

CMD ["/root/start_fee.sh","dev -Dcache -Xms512m -Xmx512m"]