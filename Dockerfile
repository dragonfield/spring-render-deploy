FROM amazoncorretto:17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM amazoncorretto:17-alpine
COPY --from=build /home/app/target/spring-render-sample-0.0.1-SNAPSHOT.jar /usr/local/lib/spring-render-sample-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/spring-render-sample-0.0.1-SNAPSHOT.jar"]
