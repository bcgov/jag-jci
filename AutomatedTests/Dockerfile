FROM eclipse-temurin:11-jre-jammy

WORKDIR /app
RUN chgrp -R 0 /app && \
    chmod -R g=u /app

COPY ./target/jci-test-runner.jar .

ENTRYPOINT ["java","-Xss2048k","-jar","jci-test-runner.jar"]
