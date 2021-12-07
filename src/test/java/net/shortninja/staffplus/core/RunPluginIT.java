package net.shortninja.staffplus.core;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Testcontainers
public class RunPluginIT {

    //    @Container
//    public GenericContainer container = new GenericContainer<>(new ImageFromDockerfile()
//        .withFileFromClasspath("staffplusplus-core-1.18.0-SNAPSHOT.jar", "/docker/staffplusplus-core-1.18.0-SNAPSHOT.jar")
//        .withFileFromClasspath("spigot-1.18.jar", "/docker/spigot-1.18.jar")
//        .withFileFromClasspath("Dockerfile", "/docker/Dockerfile"))
//        .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(360)))
//        .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("test")));
    @Container
    public GenericContainer container = new GenericContainer(
        new ImageFromDockerfile()
            .withFileFromClasspath("staffplusplus-core-1.18.0-SNAPSHOT.jar", "/docker/staffplusplus-core-1.18.0-SNAPSHOT.jar")
            .withFileFromClasspath("spigot-1.18.jar", "/docker/spigot-1.18.jar")
            .withFileFromClasspath("eula.txt", "/docker/eula.txt")
            .withFileFromClasspath("server.properties", "/docker/server.properties")
            .withDockerfileFromBuilder(builder ->
                builder
                    .from("openjdk:17")
                    .copy("spigot-1.18.jar", ".")
                    .copy("eula.txt", ".")
                    .copy("server.properties", ".")
                    .run("chmod", "777", "spigot-1.18.jar")
                    .run("mkdir", "plugins")
                    .copy("staffplusplus-core-1.18.0-SNAPSHOT.jar", "/plugins")
                    .cmd("java", "-jar", "spigot-1.18.jar", "nogui")
                    .build()))
        .waitingFor(Wait.forLogMessage(".*Done.*", 1))
        .withStartupTimeout(Duration.ofSeconds(60));

    @Test
    public void run() {
        System.out.println(container.getLogs());

        assertFalse(container.getLogs().contains("ERROR"));
        assertFalse(container.getLogs().contains("exception"));
    }
}
