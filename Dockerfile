FROM openjdk:11-jdk
COPY target/basket-microservice-0.0.1-SNAPSHOT.jar basket-microservice-0.0.1.jar
ENTRYPOINT ["java","-jar","basket-microservice-0.0.1.jar"]
EXPOSE 8085
