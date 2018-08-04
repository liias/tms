FROM openjdk:8-jdk-alpine

# mount container /tmp to host /var/lib/docker (or whatever is set on runtime)
# /tmp is the default working directory for Tomcat in Sprint Boot application
VOLUME /tmp

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

# Tomcat uses /dev/random on startup by default. Using urandom instead to speed things up
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]