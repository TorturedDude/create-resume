FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY build/libs/create-resume.jar main.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "main.jar"]