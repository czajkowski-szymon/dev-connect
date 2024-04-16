FROM openjdk:17-jdk

WORKDIR /app

COPY target/devconnect-api-1.0.0.jar /app/devconnect-api.jar

EXPOSE 8080

CMD ["java", "-jar", "devconnect-api.jar"]