FROM bellsoft/liberica-openjre-debian:24-cds
WORKDIR /app

COPY target/payment-1.0.0.jar application.jar
CMD ["java", "-XX:SharedArchiveFile=application.jsa", "-jar", "application.jar"]