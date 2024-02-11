FROM openjdk:17-alpine
# Set working directory
WORKDIR /app
# Copy application jar file
COPY build/libs/hufsting-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "-Duser.timezone=Asia/Seoul", "./app.jar"]

#FROM openjdk:17-alpine
#COPY build/libs/hufsting-0.0.1-SNAPSHOT.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "/app.jar"]