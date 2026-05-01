FROM registry.cn-beijing.aliyuncs.com/sxd/ubuntu-java8:1.0
MAINTAINER hz <hezongshuju.com>


ADD bin/start_job.sh /root/


RUN chmod u+x /root/start_job.sh

CMD ["/root/start_job.sh","dev -Xms512m -Xmx512m"]
