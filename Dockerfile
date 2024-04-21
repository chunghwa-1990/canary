FROM openjdk:17-jdk

LABEL image.author="zhaohongliang"

WORKDIR /app/canary

COPY ./target/canary-0.0.1-SNAPSHOT.jar /app/canary/canary-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "canary-0.0.1-SNAPSHOT.jar"]

CMD ["-Dspring.profiles.active=standalone"]
