FROM openjdk:17-jdk
ARG JAR_FILE=/build/libs/Unimeeting2.jar
COPY ${JAR_FILE} app.jar
RUN mkdir /app
ENTRYPOINT ["java","-jar","/app.jar"]