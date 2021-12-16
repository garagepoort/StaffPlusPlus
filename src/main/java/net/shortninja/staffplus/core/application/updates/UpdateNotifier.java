package net.shortninja.staffplus.core.application.updates;

import be.garagepoort.mcioc.IocBean;
import com.google.gson.JsonObject;
import net.shortninja.staffplus.core.StaffPlus;
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
        runTaskAsynchronously(StaffPlus.get());
    }

    @Override
    public void run() {
        try {
            spigetClient.getVersions().enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        String currentVersion = StaffPlus.get().getDescription().getVersion();

                        String jar = response.body().get("1.16").getAsString();
                        if (jar == null) {
                            return;
                        }
                        String latestVersion = jar.substring(jar.lastIndexOf("-") + 1).replace(".jar", "");
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
                    } else {
                        Bukkit.getLogger().info("Could not check latest version: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable throwable) {
                    Bukkit.getLogger().info("Could not check latest version: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Bukkit.getLogger().info("Could not check latest version: " + e.getMessage());
        }
    }

    private int getMinecraftVersion(String name) {
        return Integer.parseInt(name.substring(name.lastIndexOf(DOT) + 1));
    }
}
