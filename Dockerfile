FROM openjdk:21
COPY target/cdr-service-0.0.1-SNAPSHOT.jar app.jar
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar /app/opentelemetry-javaagent.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
ENTRYPOINT ["java", "-javaagent:/app/opentelemetry-javaagent.jar", "-Djdk.internal.platform.cgroupv1.mountpoints=/sys/fs/cgroup", "-jar", "app.jar"]
#CMD ["java", "-Djdk.internal.platform.cgroupv1.mountpoints=/sys/fs/cgroup", "-jar", "app.jar"]


