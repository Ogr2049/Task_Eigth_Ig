FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/config-server-*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]