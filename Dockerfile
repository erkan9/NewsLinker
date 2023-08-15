FROM openjdk:11-jdk-slim

RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY . .

EXPOSE 3000

RUN ["mvn", "clean", "install"]
WORKDIR /app/target

CMD ["java", "-jar", "NewsLinker-3.0.0.jar"]