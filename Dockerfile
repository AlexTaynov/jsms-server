FROM openjdk:17-jdk-alpine

COPY target/jsms-server-0.0.1-SNAPSHOT.jar jsms-server-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/jsms-server-0.0.1-SNAPSHOT.jar"]