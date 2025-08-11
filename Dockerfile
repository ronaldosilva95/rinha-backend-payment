#FROM bellsoft/liberica-openjre-debian:24-cds AS builder
#WORKDIR /builder
#
#ARG JAR_FILE=target/*.jar
#
#COPY ${JAR_FILE} application.jar
#
#RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted


FROM bellsoft/liberica-openjre-debian:24-cds
WORKDIR /application

#COPY --from=builder /builder/extracted/dependencies/ ./
#COPY --from=builder /builder/extracted/spring-boot-loader/ ./
#COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
#COPY --from=builder /builder/extracted/application/ ./
COPY target/payment-1.0.0.jar application.jar

#RUN java -XX:ArchiveClassesAtExit=application.jsa -Dspring.context.exit=onRefresh -jar application.jar

CMD ["java", "-XX:SharedArchiveFile=application.jsa", "-jar", "application.jar"]



#FROM openjdk:21-jdk-slim-buster
#WORKDIR /app
#COPY target/payment-1.0.0.jar app.jar
#EXPOSE 9999
#CMD ["java", "-XX:+UseContainerSupport", "-XX:+UseSerialGC", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]