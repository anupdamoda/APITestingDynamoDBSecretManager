FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app

RUN apt-get update && \
    apt-get install -y unzip curl && \
    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" \
    -o "awscliv2.zip" && \
    unzip awscliv2.zip && \
    ./aws/install && \
    rm -rf aws awscliv2.zip

COPY pom.xml .
COPY src ./src
COPY entrypoint.sh .

RUN mvn dependency:go-offline

RUN chmod +x entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]