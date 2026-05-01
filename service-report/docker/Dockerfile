FROM registry.cn-beijing.aliyuncs.com/sxd/ubuntu-java8:1.0
MAINTAINER hz <hezongshuju>


ADD bin/start_report.sh /root/


RUN chmod u+x /root/start_report.sh

CMD ["/root/start_report.sh","dev -Dcache -Xms512m -Xmx512m"]