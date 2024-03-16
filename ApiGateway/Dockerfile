FROM adoptopenjdk:11-jre-hotspot

WORKDIR /app

COPY target/ApiGateway.jar /app/ApiGateway.jar

ENTRYPOINT ["java", "-jar", "/app/ApiGateway.jar"]
