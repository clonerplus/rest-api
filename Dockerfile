FROM openjdk:17-jdk-slim
LABEL authors="clonerplus"
VOLUME /tmp
WORKDIR /app
COPY /ir/sobhan/REST-API/0.0.1-SNAPSHOT/*.jar app.jar
EXPOSE 9090

CMD ["java", "-jar", "-Dserver.port=9090", "app.jar"]
