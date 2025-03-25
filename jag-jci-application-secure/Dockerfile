FROM eclipse-temurin:17-jre-alpine

RUN apk update && apk add --upgrade --no-cache libexpat  # fix CVE-2024-8176

COPY ./target/jci-application-secure.jar jci-application-secure.jar

ENTRYPOINT ["java","-Xmx2g", "-jar","/jci-application-secure.jar"]
