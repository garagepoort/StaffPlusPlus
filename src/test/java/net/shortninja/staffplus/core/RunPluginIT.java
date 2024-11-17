package net.shortninja.staffplus.core;

import com.github.dockerjava.api.command.CreateContainerCmd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class RunPluginIT {

    private String implementationVersion;
    private String javaVersion;
    private String mcVersion;

    @BeforeEach
    public void beforeEach() throws IOException {
        final Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("project.properties"));
        implementationVersion = properties.getProperty("plugin.version");
        javaVersion = properties.getProperty("java.version");
        mcVersion = properties.getProperty("mc.version");
    }

    @Test
    public void runSqlite() {
        try (
            Network network = Network.newNetwork();
            GenericContainer spigot = createSpigotContainer(network, "sqlite-config.yml");
        ) {
            spigot.start();

            System.out.println(spigot.getLogs());
            assertFalse(spigot.getLogs().contains("ERROR"));
            assertFalse(spigot.getLogs().contains("exception"));
            assertTrue(spigot.getLogs().contains("[StaffPlusPlus] Using SQLITE storage"));
        }
    }

    @Test
    public void runMysql() {
        try (
            Network network = Network.newNetwork();
            GenericContainer spigot = createSpigotContainer(network, "mysql-config.yml");
            GenericContainer mysql = loadMysql(network);
        ) {
            mysql.start();
            spigot.start();

            System.out.println(spigot.getLogs());
            assertFalse(spigot.getLogs().contains("ERROR"));
            assertFalse(spigot.getLogs().contains("exception"));
            assertTrue(spigot.getLogs().contains("[StaffPlusPlus] Using MYSQL storage"));
        }
    }

    private MySQLContainer loadMysql(Network network) {
        return (MySQLContainer) new MySQLContainer("mysql:5.7")
            .withDatabaseName("TEST")
            .withUsername("test")
            .withPassword("test")
            .withEnv("MYSQL_ROOT_HOST", "%")
            .withNetwork(network)
            .withNetworkAliases("mysqlserver")
            .withCreateContainerCmdModifier((Consumer<CreateContainerCmd>) createContainerCmd -> createContainerCmd.withHostName("mysqlserver"))
            .waitingFor(new HttpWaitStrategy().forPort(3306));
    }

    private GenericContainer createSpigotContainer(Network network, String configFile) {
        return new GenericContainer(
            new ImageFromDockerfile()
                .withFileFromClasspath("staffplusplus-core-" + implementationVersion + ".jar", "/docker/staffplusplus-core-" + implementationVersion + ".jar")
                .withFileFromClasspath("spigot-" + mcVersion + ".jar", "/docker/spigot-" + mcVersion + ".jar")
                .withFileFromClasspath("eula.txt", "/docker/eula.txt")
                .withFileFromClasspath("server.properties", "/docker/server.properties")
                .withFileFromClasspath("config.yml", "/docker/" + configFile)
                .withDockerfileFromBuilder(builder ->
                    builder
                        .from("openjdk:" + javaVersion)
                        .copy("spigot-" + mcVersion + ".jar", ".")
                        .copy("eula.txt", ".")
                        .copy("server.properties", ".")
                        .run("chmod", "777", "spigot-" + mcVersion + ".jar")
                        .run("mkdir", "plugins")
                        .run("mkdir", "plugins/StaffPlusPlus")
                        .copy("staffplusplus-core-" + implementationVersion + ".jar", "/plugins")
                        .copy("config.yml", "/plugins/StaffPlusPlus")
                        .cmd("java", "-jar", "-DIReallyKnowWhatIAmDoingISwear", "spigot-" + mcVersion + ".jar", "--nogui")
                        .build()))
            .waitingFor(Wait.forLogMessage(".*Done.*", 1))
            .withStartupTimeout(Duration.ofSeconds(360))
            .withNetwork(network)
            .withNetworkAliases("spigot");
    }
}
