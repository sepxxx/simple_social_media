FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /app
COPY src /app/.
COPY pom.xml /app/pom.xml
EXPOSE 8080
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true


#ENTRYPOINT ["java", "-jar", "target/simple_social_media-0.0.1-SNAPSHOT.jar"]


#FROM eclipse-temurin:17-jre-alpine
#WORKDIR /app
#EXPOSE 8080
#COPY --from=builder /app/target/*.jar /app/*.jar
#ENTRYPOINT ["java", "-jar", "/app/*.jar"]
#COPY --from=builder /app/target/* /app/.
#ENTRYPOINT ["java", "-jar", "app.jar"]