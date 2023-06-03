#1.以jdk8为基础镜像
FROM openjdk:8

#2.指明该镜像的作者和其电子邮件
MAINTAINER bq "bq@biuqu.com"

#3.在构建镜像时，指定镜像的工作目录，之后的命令都是基于此工作目录，如果不存在，则会创建目录
WORKDIR /opt/apps/bq-clusters/

#4.挂载路径
VOLUME ~/docker/bq-clusters/

ARG HOST
ARG PORT
ARG SERVICE
ARG SERVICE_NAME
RUN echo ${HOST}
RUN echo ${PORT}
RUN echo ${SERVICE}
RUN echo ${SERVICE_NAME}

#5.配置环境变量
#配置jvm启动参数
ENV JAVA_OPTS='-server -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Xmx512m -Xms256m -Xmn256m -XX:SurvivorRatio=8 -XX:ParallelGCThreads=8 -XX:ConcGCThreads=8 -XX:G1ConcRefinementThreads=4 -XX:-CICompilerCountPerCPU -XX:CICompilerCount=3 -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/logs/heapdump.hprof'
#配置环境变量支持中文
ENV LANG="en_US.UTF-8"
ENV JAR=${SERVICE}
ENV PORT=${PORT}
RUN echo ${JAR}
RUN echo ${PORT}

#6.一个复制命令，把安装文件复制到镜像中
ADD ./${SERVICE_NAME}/target/${SERVICE} ./
ADD ./${SERVICE_NAME}/${SERVICE_NAME}-startup/src/main/resources/application.yaml ./
ADD ./${SERVICE_NAME}/${SERVICE_NAME}-startup/src/main/resources/application-root.yaml ./
ADD ./${SERVICE_NAME}/${SERVICE_NAME}-startup/src/main/resources/application-nacos.yaml ./

RUN sed -i "s/localhost/${HOST}/g" application.yaml
RUN sed -i "s/localhost/${HOST}/g" application-root.yaml
RUN sed -i "s/localhost/${HOST}/g" application-nacos.yaml

RUN sed -i "s/\/Users\/yoyo-studio/\/opt\/apps\/bq-clusters\//g" application-root.yaml

#7.暴露端口(docker内的端口)
EXPOSE ${PORT}

#8.运行jar包
#ENTRYPOINT ["java", "-jar","${SERVICE}"]
#可更接收容器的SIGTERM信号指令，更优雅地实现启停
CMD ["sh", "-ec", "exec java ${JAVA_OPTS} -jar ${JAR} --server.port=${PORT}"]