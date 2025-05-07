FROM maven:3.9.6-eclipse-temurin-21 as builder
WORKDIR /usr/src/app
COPY pom.xml pom.xml
RUN mvn dependency:resolve-plugins dependency:resolve package -Dmaven.test.skip=true -DskipTests -DskipChecks
COPY src/ src/
RUN mvn package -Dmaven.test.skip=true -DskipTests -DskipChecks

FROM azul/zulu-openjdk-alpine:21-jre-headless
COPY --from=builder /usr/src/app/target/zbctl.jar /usr/src/app/zbctl.jar

ENTRYPOINT ["/bin/sh"]