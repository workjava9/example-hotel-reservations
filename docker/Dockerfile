# or ubuntu:lasted
FROM openjdk:17
MAINTAINER Pavel
RUN javac ExampleHotelReservationsApplication.java
COPY target/examplehotelreservations-0.0.1-SNAPSHOT.jar /app/app.jar
WORKDIR /app
CMD ["java", "ExampleHotelReservationsApplication", "-jar", "app.jar"]
