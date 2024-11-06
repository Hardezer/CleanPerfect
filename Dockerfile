FROM openjdk:17
ARG JAR_FILE=target/CleanPerfectBack-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} CleanPerfectBack-0.0.1-SNAPSHOT.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/CleanPerfectBack-0.0.1-SNAPSHOT.jar"]