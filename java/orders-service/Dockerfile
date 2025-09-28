FROM eclipse-temurin:21-jre
WORKDIR /app
COPY ./target/orders-service-0.0.1-SNAPSHOT.jar /app/app.jar
COPY ./opentelemetry/opentelemetry-javaagent.jar /app/otel-agent.jar

CMD ["java", "-javaagent:/app/otel-agent.jar", "-jar", "/app/app.jar"]
