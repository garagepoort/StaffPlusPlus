package net.shortninja.staffplus.staff.mode.handler;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.mode.config.modeitems.cps.CpsModeConfiguration;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CpsHandler {
    private final static Map<UUID, Integer> currentTests = new HashMap<UUID, Integer>();
    private final MessageCoordinator message = IocContainer.getMessage();
    private final Options options = IocContainer.getOptions();
    private final Messages messages = IocContainer.getMessages();
    private final CpsModeConfiguration cpsModeConfiguration;

    public CpsHandler() {
        cpsModeConfiguration = options.modeConfiguration.getCpsModeConfiguration();
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
        message.send(sender, messages.cpsStart.replace("%target%", targetPlayer.getName()).replace("%seconds%", Integer.toString((int) cpsModeConfiguration.getModeCpsTime() / 20)), messages.prefixGeneral);

        new BukkitRunnable() {
            @Override
            public void run() {
                stopTest(sender, targetPlayer);
            }
        }.runTaskLater(StaffPlus.get(), cpsModeConfiguration.getModeCpsTime());
    }

    public void stopTest(CommandSender sender, Player targetPlayer) {
        UUID uuid = targetPlayer.getUniqueId();
        if (uuid == null)
            return;
        int cps = (int) (currentTests.get(uuid) / (cpsModeConfiguration.getModeCpsTime() / 20));
        String message = cps > cpsModeConfiguration.getModeCpsMax() ? messages.cpsFinishMax : messages.cpsFinishNormal;

        this.message.send(sender, message.replace("%player%", targetPlayer.getName()).replace("%cps%", Integer.toString(cps)), messages.prefixGeneral);
        currentTests.remove(uuid);
    }
}