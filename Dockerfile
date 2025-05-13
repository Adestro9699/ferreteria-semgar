# Etapa de construcción
FROM maven:3.9-eclipse-temurin-23 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:23-jre-jammy
WORKDIR /app
COPY --from=build /app/target/ferreteriaSemGar-0.0.1-SNAPSHOT.war ./app.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"] 