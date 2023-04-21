FROM openjdk:8-jdk-alpine

# copy arthas
COPY --from=hengyunabc/arthas:latest /opt/arthas /opt/arthas

# 设置时区为上海
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 设置工作目录
WORKDIR /usr/myapp/

# 复制 Spring Boot 应用程序到 Docker 镜像中
COPY target/*.jar /usr/myapp/app.jar

#请修改应用名，参考https://www.kdocs.cn/l/ccjjXxdSB9gt【内网规划】J列 - 通用服务KCS集群
ENV applicationName=adaptivelayer-pds

# 定义需要的环境变量
ENV JAVA_OPTS="-Xms256m -Xmx1024m"
ENV ACTIVE_PROFILE="loacl"
ENV SERVER_PORT=8080

# 暴露容器端口
EXPOSE $SERVER_PORT
ENTRYPOINT ["java",\
    "$JAVA_OPTS",\
    "-XX:+PrintClassHistogram -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC ",\
    "-jar","app.jar",\
    " -Dspring.profiles.active=$ACTIVE_PROFILE"]
