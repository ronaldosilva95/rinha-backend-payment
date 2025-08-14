# Using Oracle GraalVM for JDK 17
FROM container-registry.oracle.com/graalvm/native-image:21-ol8 AS builder

# Set the working directory to /home/app
WORKDIR /build

# Copy the source code into the image for building
COPY . /build

# Build
RUN ./mvnw --no-transfer-progress native:compile -Pnative

# The deployment Image
FROM container-registry.oracle.com/os/oraclelinux:8-slim

EXPOSE 8080

# Copy the native executable into the containers
COPY --from=builder /build/target/payment app
ENTRYPOINT ["/app"]

#FROM bellsoft/liberica-openjre-debian:24-cds
#WORKDIR /app
#
#COPY target/payment-1.0.0.jar application.jar
#CMD ["java", "-XX:SharedArchiveFile=application.jsa", "-jar", "application.jar"]


#FROM openjdk:21-jdk-slim-buster
#WORKDIR /app
#COPY target/payment-1.0.0.jar app.jar
#EXPOSE 9999
#CMD ["java", "-XX:+UseContainerSupport", "-XX:+UseSerialGC", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]