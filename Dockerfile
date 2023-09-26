FROM maven:3.8-amazoncorretto-17
COPY ./ /home
WORKDIR /home
RUN mvn clean install -DskipTests
ENTRYPOINT ["mvn", "exec:java", "-Dexec.mainClass=anoniks.Pipe", "-Dmaven.test.skip"]
