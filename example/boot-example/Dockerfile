FROM registry.cn-hangzhou.aliyuncs.com/lh0811/lh0811-docer:lh-jdk1.8-0.0.1
MAINTAINER lh0811
ADD  ./target/${JAR_FILE} /opt
RUN chmod +x /opt/boot-example-0.0.1-SNAPSHOT.jar
CMD java -jar /opt/boot-example-0.0.1-SNAPSHOT.jar
