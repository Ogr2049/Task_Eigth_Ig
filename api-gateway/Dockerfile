FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/api-gateway-*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]