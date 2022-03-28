FROM openjdk:11

VOLUME /tmp

ADD target/Andrey_Volkov-0.0.1-SNAPSHOT.jar Andrey_Volkov-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/Andrey_Volkov-0.0.1-SNAPSHOT.jar"]