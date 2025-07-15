#Etapes de d'installation des dependances spring-boot
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


#Application en phase developpement
FROM build as dev
WORKDIR /app

EXPOSE 8181

CMD ./mvwn spring-boot:run

#Application en phase de test ou de production
FROM openjdk:17-jdk-slim as prod
WORKDIR /app
# Installer netcat via apt-get
RUN apt-get update && apt-get install -y netcat && apt-get clean

# Copier le jar et le script wait-for-it
COPY --from=build /app/target/*.jar app.jar

COPY wait-for-it.sh /wait-for-it.sh

RUN chmod +x /wait-for-it.sh

EXPOSE 8181

ENTRYPOINT ["/wait-for-it.sh", "mysql:3306", "--", "java", "-jar", "app.jar"]


