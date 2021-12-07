package net.shortninja.staffplus.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Testcontainers
public class RunPluginIT {

    private GenericContainer container;

    @BeforeEach
    public void beforeEach() throws IOException {
        final Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("project.properties"));
        String implementationVersion = properties.getProperty("plugin.version");
        String javaVersion = properties.getProperty("java.version");
        String mcVersion = properties.getProperty("mc.version");

        container = new GenericContainer(
            new ImageFromDockerfile()
                .withFileFromClasspath("staffplusplus-core-" + implementationVersion + ".jar", "/docker/staffplusplus-core-" + implementationVersion + ".jar")
                .withFileFromClasspath("spigot-" + mcVersion + ".jar", "/docker/spigot-" + mcVersion + ".jar")
                .withFileFromClasspath("eula.txt", "/docker/eula.txt")
                .withFileFromClasspath("server.properties", "/docker/server.properties")
                .withDockerfileFromBuilder(builder ->
                    builder
                        .from("openjdk:" + javaVersion)
                        .copy("spigot-" + mcVersion + ".jar", ".")
                        .copy("eula.txt", ".")
                        .copy("server.properties", ".")
                        .run("chmod", "777", "spigot-" + mcVersion + ".jar")
                        .run("mkdir", "plugins")
                        .copy("staffplusplus-core-" + implementationVersion + ".jar", "/plugins")
                        .cmd("java", "-jar", "spigot-" + mcVersion + ".jar", "nogui")
                        .build()))
            .waitingFor(Wait.forLogMessage(".*Done.*", 1))
            .withStartupTimeout(Duration.ofSeconds(360));
        container.start();
    }

    @Test
    public void run() {
        System.out.println(container.getLogs());

        assertFalse(container.getLogs().contains("ERROR"));
        assertFalse(container.getLogs().contains("exception"));
    }
}
