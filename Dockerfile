FROM openjdk:17-jdk

LABEL image.author="zhaohongliang"

WORKDIR /app/canary

COPY ./target/canary-0.0.1-SNAPSHOT.jar /app/canary/canary-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "-Dspring.profiles.active=cluster", "canary-0.0.1-SNAPSHOT.jar"]
