FROM ibm-semeru-runtimes:open-17.0.8_7-jre-jammy
LABEL authors="irfan"

ENV SERVER_PORT=8080

WORKDIR /app

COPY target/money-record.jar .

CMD ["java", "-jar", "money-record.jar"]
