FROM openjdk:11-jdk
ARG JAR_FILE=target/basket-microservice-*.jar
COPY ${JAR_FILE} basket-microservice.jar
ENTRYPOINT ["java","-jar","basket-microservice.jar"]
EXPOSE 8085
