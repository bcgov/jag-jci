FROM eclipse-temurin:17-jre-alpine

RUN apk update && apk add --upgrade --no-cache libexpat  # fix CVE-2024-8176

COPY ./target/jci-application.jar jci-application.jar

ENTRYPOINT ["java","-Xmx2g", "-jar","/jci-application.jar"]
