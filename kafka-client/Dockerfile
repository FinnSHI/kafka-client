FROM java:8

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ADD . /kafkaClient
WORKDIR /kafkaClient
COPY application-dev.properties /kafkaClient
EXPOSE 18098
ENTRYPOINT ["java","-jar","-Duser.timezone=Asia/Shanghai", "kafka-client-0.0.1.jar"]
