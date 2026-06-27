FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY entrypoint.sh .

RUN mvn dependency:go-offline

RUN chmod +x entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]