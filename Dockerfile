#FROM openjdk:11-jdk
#
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#
##COPY lib/mssql-jdbc-9.2.1.jre11.jar /app/lib/mssql-jdbc-9.2.1.jre11.jar
#
#
#ENTRYPOINT ["java","-jar","/app.jar"]

# Verwenden Sie das offizielle OpenJDK-Basisimage von Docker Hub
FROM openjdk:11-jdk

# Setzen Sie das Arbeitsverzeichnis im Container
WORKDIR /app

# Kopieren Sie das Spring Boot-App-JAR in das Arbeitsverzeichnis des Containers
COPY target/qrush-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
# Befehl, der ausgeführt wird, wenn der Container gestartet wird
ENTRYPOINT ["java", "-jar", "app.jar"]
