FROM openjdk:17.0.1-jdk-slim
EXPOSE 9001
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} tweetapp.jar
ENTRYPOINT ["java", "-jar", "/tweetapp.jar"]