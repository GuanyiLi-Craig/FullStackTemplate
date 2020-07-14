FROM openjdk:11-jre-slim

EXPOSE 8002

RUN mkdir /app

COPY user-manager/build/libs/*.jar /app/user-manager.jar

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/user-manager.jar"]