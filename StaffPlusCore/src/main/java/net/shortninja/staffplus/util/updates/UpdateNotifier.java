package net.shortninja.staffplus.util.updates;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import net.shortninja.staffplus.StaffPlus;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Optional;

public class UpdateNotifier {

    private static final String RESOURCE_API_URL = "https://api.spiget.org/v2/resources/83562";
    private static final String DOT = ".";
    private final SpigetClient spigetClient;

    public UpdateNotifier() {
        spigetClient = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(SpigetClient.class))
                .logLevel(Logger.Level.FULL)
                .target(SpigetClient.class, RESOURCE_API_URL);
    }

    public void checkUpdate() {
        try {
            List<ResourceVersion> versions = spigetClient.getVersions();
            String currentVersion = StaffPlus.get().getDescription().getVersion();
            String mcVersion = currentVersion.substring(0, currentVersion.lastIndexOf(DOT));

            Optional<ResourceVersion> higherVersion = versions.stream()
                    .filter(r -> r.getName().startsWith(mcVersion))
                    .filter(r -> getMinecraftVersion(r.getName()) > getMinecraftVersion(currentVersion))
                    .findFirst();

            if (higherVersion.isPresent()) {
                Bukkit.getLogger().info("============================================================");
                Bukkit.getLogger().info("=           A new Version of Staff++ is available          =");
                Bukkit.getLogger().info("=                         Visit                            =");
                Bukkit.getLogger().info("=  https://www.spigotmc.org/resources/staff.83562/history  =");
                Bukkit.getLogger().info("=               to update your version!                    =");
                Bukkit.getLogger().info("============================================================");
            } else {
                Bukkit.getLogger().info("============================================================");
                Bukkit.getLogger().info("=           Your Staff++ version is up to date!            =");
                Bukkit.getLogger().info("============================================================");
            }
        } catch (Exception e) {
            Bukkit.getLogger().info("Could not check latest version: " + e.getMessage());
        }
    }

    private int getMinecraftVersion(String name) {
        return Integer.parseInt(name.substring(name.lastIndexOf(DOT) + 1));
    }
}
