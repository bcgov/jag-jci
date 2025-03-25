FROM eclipse-temurin:17-jre-alpine

RUN apk update && apk add --upgrade --no-cache libexpat  # fix CVE-2024-8176

WORKDIR /app
RUN chgrp -R 0 /app && \
    chmod -R g=u /app

COPY ./target/jci-test-runner.jar .

ENTRYPOINT ["java","-Xss2048k","-jar","jci-test-runner.jar"]
