package net.shortninja.staffplus.core.application.updates;

import be.garagepoort.mcioc.IocBean;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import net.shortninja.staffplus.core.StaffPlusPlus;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@IocBean
public class UpdateNotifier extends BukkitRunnable {

    private static final String RESOURCE_API_URL = "https://staffplusplus.org/";
    private static final String DOT = ".";
    private static final String LINE = "============================================================";

    private final SpigetClient spigetClient;

    public UpdateNotifier() {
        spigetClient = new Retrofit.Builder()
            .baseUrl(RESOURCE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpigetClient.class);
        runTaskAsynchronously(StaffPlusPlus.get());
    }

    @Override
    public void run() {
        spigetClient.getVersions().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (!response.isSuccessful()) {
                        Bukkit.getLogger().warning("Could not check latest version: " + response.message());
                        return;
                    }
                    
                    String currentPluginVersion = StaffPlusPlus.get().getDescription().getVersion();
                    String currentServerVersion = StaffPlusPlus.get().getServer().getBukkitVersion().replace("-R0.1-SNAPSHOT", "");
                    
                    if (currentPluginVersion.endsWith("-SNAPSHOT")) {
                        currentPluginVersion = currentPluginVersion.substring(0, currentPluginVersion.length() - 9);
                    }
                    
                    int currentPluginReleaseVersion = getPluginMcVersion(currentPluginVersion);
                    int currentServerReleaseVersion = getPluginMcVersion(currentServerVersion);
                    
                    String targetMcVersion = currentPluginReleaseVersion > currentServerReleaseVersion ? currentPluginVersion : currentServerVersion;
                    targetMcVersion = targetMcVersion.substring(0, targetMcVersion.lastIndexOf(DOT));
                    
                    JsonElement mcVersionElement = response.body().get(targetMcVersion);
                    if (mcVersionElement == null) {
                        if (currentServerReleaseVersion > currentPluginReleaseVersion) {
                            Bukkit.getLogger().severe("This version of minecraft is not yet supported - please wait for an update to release (proceeding anyway - expect errors to follow)");
                            return;
                        }
                        
                        // User is running an unreleased mayor mc version
                        Bukkit.getLogger().warning("YOU ARE RUNNING A DEVELOPER VERSION OF Staff++ - Issues may occur");
                        return;
                    }
                    
                    String jar = mcVersionElement.getAsString();
                    String latestVersion = jar.substring(jar.lastIndexOf("-") + 1).replace(".jar", "");
                    
                    int latestPluginPatch = getPluginPatch(latestVersion);
                    int currentPluginPatch = getPluginPatch(currentPluginVersion);
                    
                    if (latestPluginPatch > currentPluginPatch || currentServerReleaseVersion > currentPluginReleaseVersion) {
                        Bukkit.getLogger().info(LINE);
                        Bukkit.getLogger().info("=           A new Version of Staff++ is available          =");
                        Bukkit.getLogger().info("=                         Visit                            =");
                        Bukkit.getLogger().info("=  https://www.spigotmc.org/resources/staff.83562/history  =");
                        Bukkit.getLogger().info("=               to update your version!                    =");
                        Bukkit.getLogger().info(LINE);
                    } else if (latestPluginPatch < currentPluginPatch) {
                        Bukkit.getLogger().warning("YOU ARE RUNNING A DEVELOPER VERSION OF Staff++ - Issues may occur");
                    } else {
                        Bukkit.getLogger().info("Your Staff++ version is up to date!");
                    }
                } catch (Exception e) {
                    Bukkit.getLogger().warning("Could not check latest version: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                Bukkit.getLogger().warning("Could not check latest version: " + throwable.getMessage());
            }
        });
    }
    
    private int getPluginMcVersion(String name) {
        int firstDotIndex = name.indexOf(DOT) + 1;
        return Integer.parseInt(name.substring(firstDotIndex, name.indexOf(DOT, firstDotIndex)));
    }
    
    private int getPluginPatch(String name) {
        return Integer.parseInt(name.substring(name.lastIndexOf(DOT) + 1));
    }
}
