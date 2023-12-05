package net.shortninja.staffplus.core;

import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;
import net.shortninja.staffplusplus.IStaffPlus;
import net.shortninja.staffplusplus.ban.BanService;
import net.shortninja.staffplusplus.joinmessages.JoinMessageService;
import net.shortninja.staffplusplus.mute.MuteService;
import net.shortninja.staffplusplus.nightvision.NightVisionService;
import net.shortninja.staffplusplus.reports.ReportService;
import net.shortninja.staffplusplus.session.SessionManager;
import net.shortninja.staffplusplus.staffmode.chat.StaffChatService;
import net.shortninja.staffplusplus.warnings.WarningService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class StaffPlusPlus extends TubingBukkitPlugin implements IStaffPlus {

    private static StaffPlusPlus plugin;

    public static StaffPlusPlus get() {
        return plugin;
    }

    @Override
    protected void beforeEnable() {
        try {
            plugin = this;
            File firstInstallFile = new File(StaffPlusPlus.get().getDataFolder(), "installed.txt");
            if (!firstInstallFile.exists()) {
                getLogger().info("First installation detected");
                File oldStaffPlusFolder = new File(getDataFolder().getParentFile(), "StaffPlus");
                File newStaffPlusPlusFolder = new File(getDataFolder().getParentFile(), "StaffPlusPlus");
                if (oldStaffPlusFolder.exists()) {
                    getLogger().info("Copying old staff+ folder");
                    copyFolder(oldStaffPlusFolder.toPath(), newStaffPlusPlusFolder.toPath());
                }
                firstInstallFile.getParentFile().mkdirs();
                firstInstallFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFolder(Path src, Path dest) throws IOException {
        try (Stream<Path> stream = Files.walk(src)) {
            stream.forEach(source -> copy(source, dest.resolve(src.relativize(source))));
        }
    }

    private static void copy(Path source, Path dest) {
        try {
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    protected void enable() {
        try {
            Bukkit.getServicesManager().register(IStaffPlus.class, this, this, ServicePriority.Normal);

            getLogger().info("Staff++ has been enabled!");
            getLogger().info("Plugin created by Shortninja continued by Qball - Revisited by Garagepoort");
        } catch (Exception e) {
            getLogger().severe("Unable to load plugin: " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    protected void disable() {
        getLogger().info("Staff++ is now disabling!");
        if (this.getIocContainer() != null && getIocContainer().getList(PluginDisable.class) != null) {
            getIocContainer().getList(PluginDisable.class).forEach(b -> b.disable(this));
        }
        getLogger().info("Staff++ disabled!");
    }

    @Override
    public StaffChatService getStaffChatService() {
        return StaffPlusPlus.get().getIocContainer().get(StaffChatService.class);
    }

    @Override
    public SessionManager getSessionManager() {
        return StaffPlusPlus.get().getIocContainer().get(SessionManager.class);
    }

    @Override
    public BanService getBanService() {
        return StaffPlusPlus.get().getIocContainer().get(BanService.class);
    }

    @Override
    public MuteService getMuteService() {
        return StaffPlusPlus.get().getIocContainer().get(MuteService.class);
    }

    @Override
    public ReportService getReportService() {
        return StaffPlusPlus.get().getIocContainer().get(ReportService.class);
    }

    @Override
    public WarningService getWarningService() {
        return StaffPlusPlus.get().getIocContainer().get(WarningService.class);
    }

    @Override
    public NightVisionService getNightVisionService() {
        return StaffPlusPlus.get().getIocContainer().get(NightVisionService.class);
    }

    @Override
    public JoinMessageService getJoinMessageService() {
        return StaffPlusPlus.get().getIocContainer().get(JoinMessageService.class);
    }
}