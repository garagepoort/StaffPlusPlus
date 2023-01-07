package net.shortninja.staffplus.core.mode.handler;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.mode.config.StaffModeOptions;
import net.shortninja.staffplus.core.mode.config.modeitems.cps.CpsModeConfiguration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@IocBean
public class CpsHandler {
    private final static Map<UUID, Integer> currentTests = new HashMap<UUID, Integer>();

    private final Messages messages;
    private final CpsModeConfiguration cpsModeConfiguration;

    public CpsHandler(StaffModeOptions options, Messages messages) {
        this.messages = messages;
        cpsModeConfiguration = options.staffItemsConfiguration.getCpsModeConfiguration();
    }

    public boolean isTesting(UUID uuid) {
        return currentTests.containsKey(uuid);
    }

    public void updateCount(UUID uuid) {
        int count = currentTests.get(uuid);

        currentTests.put(uuid, count + 1);
    }

    public void startTest(final CommandSender sender, final Player targetPlayer) {
        if(currentTests.containsKey(targetPlayer.getUniqueId()))
            return;
        currentTests.put(targetPlayer.getUniqueId(), 0);
        messages.send(sender, messages.cpsStart.replace("%target%", targetPlayer.getName()).replace("%seconds%", Integer.toString((int) cpsModeConfiguration.getModeCpsTime() / 20)), messages.prefixGeneral);

        new BukkitRunnable() {
            @Override
            public void run() {
                stopTest(sender, targetPlayer);
            }
        }.runTaskLater(TubingBukkitPlugin.getPlugin(), cpsModeConfiguration.getModeCpsTime());
    }

    public void stopTest(CommandSender sender, Player targetPlayer) {
        UUID uuid = targetPlayer.getUniqueId();
        if (uuid == null)
            return;
        int cps = (int) (currentTests.get(uuid) / (cpsModeConfiguration.getModeCpsTime() / 20));
        String message = cps > cpsModeConfiguration.getModeCpsMax() ? messages.cpsFinishMax : messages.cpsFinishNormal;

        this.messages.send(sender, message.replace("%player%", targetPlayer.getName()).replace("%cps%", Integer.toString(cps)), messages.prefixGeneral);
        currentTests.remove(uuid);
    }
}