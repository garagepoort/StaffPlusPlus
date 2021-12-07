package net.shortninja.staffplus.core.application.updates;

import be.garagepoort.mcioc.IocBean;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import net.shortninja.staffplus.core.StaffPlus;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

@IocBean
public class UpdateNotifier extends BukkitRunnable {

    private static final String RESOURCE_API_URL = "https://staffplusplus.org";
    private static final String DOT = ".";
    private static final String LINE = "============================================================";

    private final SpigetClient spigetClient;

    public UpdateNotifier() {
        spigetClient = Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(SpigetClient.class))
            .logLevel(Logger.Level.FULL)
            .target(SpigetClient.class, RESOURCE_API_URL);
        runTaskAsynchronously(StaffPlus.get());
    }

    @Override
    public void run() {
        try {
            JSONObject versions = spigetClient.getVersions();
            String currentVersion = StaffPlus.get().getDescription().getVersion();

            String jar = (String) versions.get("1.18");
            if(jar == null) {
                return;
            }
            String latestVersion = jar.substring(jar.lastIndexOf("-") +1).replace(".jar", "");
            boolean outdated = getMinecraftVersion(latestVersion) > getMinecraftVersion(currentVersion);

            if (outdated) {
                Bukkit.getLogger().info(LINE);
                Bukkit.getLogger().info("=           A new Version of Staff++ is available          =");
                Bukkit.getLogger().info("=                         Visit                            =");
                Bukkit.getLogger().info("=  https://www.spigotmc.org/resources/staff.83562/history  =");
                Bukkit.getLogger().info("=               to update your version!                    =");
                Bukkit.getLogger().info(LINE);
            } else {
                Bukkit.getLogger().info(LINE);
                Bukkit.getLogger().info("=           Your Staff++ version is up to date!            =");
                Bukkit.getLogger().info(LINE);
            }
        } catch (Exception e) {
            Bukkit.getLogger().info("Could not check latest version: " + e.getMessage());
        }
    }

    private int getMinecraftVersion(String name) {
        return Integer.parseInt(name.substring(name.lastIndexOf(DOT) + 1));
    }
}
