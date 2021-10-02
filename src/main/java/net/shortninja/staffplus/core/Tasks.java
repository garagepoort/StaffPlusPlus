package net.shortninja.staffplus.core;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.core.domain.staff.freeze.config.FreezeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.handler.GadgetHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@IocBean
public class Tasks extends BukkitRunnable {

    @ConfigProperty("permissions:member")
    private String permissionMember;
    @ConfigProperty("clock")
    private long clock;

    private final PermissionHandler permission;
    private final Messages messages;
    private final OnlineSessionsManager sessionManager;
    private final FreezeHandler freezeHandler;
    private final GadgetHandler gadgetHandler;
    private final FreezeConfiguration freezeConfiguration;
    private int freezeInterval;
    private long now;

    public Tasks(PermissionHandler permission,
                 Messages messages,
                 OnlineSessionsManager sessionManager,
                 FreezeHandler freezeHandler,
                 GadgetHandler gadgetHandler,
                 FreezeConfiguration freezeConfiguration) {
        this.permission = permission;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.freezeHandler = freezeHandler;
        this.gadgetHandler = gadgetHandler;
        this.freezeConfiguration = freezeConfiguration;
        freezeInterval = 0;
        now = System.currentTimeMillis();
        runTaskTimerAsynchronously(StaffPlus.get(), clock * 20, clock * 20);
    }

    @Override
    public void run() {
        decideAutosave();
        gadgetHandler.updateGadgets();
    }

    private void decideAutosave() {
        long later = System.currentTimeMillis();

        if ((later - now) >= 1000) {
            int addition = (int) ((later - now) / 1000);
            freezeInterval += addition;
            now = System.currentTimeMillis();
        }

        if (freezeInterval >= freezeConfiguration.timer && freezeInterval > 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                OnlinePlayerSession session = sessionManager.get(player);
                if (session.isFrozen() && !permission.has(player, permissionMember)) {
                    if (freezeConfiguration.sound != null) {
                        freezeConfiguration.sound.play(player);
                    }

                    if (!freezeConfiguration.prompt) {
                        messages.sendCollectedMessage(player, messages.freeze, messages.prefixGeneral);
                    }
                }
            }

            freezeInterval = 0;
        }
    }
}