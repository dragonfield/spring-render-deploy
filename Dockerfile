FROM amazoncorretto:17 AS build
COPY ./ /home/app
RUN cd /home/app && ./mvnw clean package

FROM amazoncorretto:17-alpine
COPY --from=build /home/app/target/spring-render-sample-0.0.1-SNAPSHOT.jar /usr/local/lib/spring-render-sample-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/spring-render-sample-0.0.1-SNAPSHOT.jar"]
