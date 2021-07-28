package net.shortninja.staffplus.core;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.freeze.FreezeModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.handler.GadgetHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@IocBean
public class Tasks extends BukkitRunnable {
    private final PermissionHandler permission;


    @ConfigProperty("permissions:member")
    private String permissionMember;
    @ConfigProperty("clock")
    private long clock;

    private final Messages messages;
    private final SessionManagerImpl sessionManager;
    private final FreezeHandler freezeHandler;
    private final GadgetHandler gadgetHandler;
    private final FreezeModeConfiguration freezeModeConfiguration;
    private int saveInterval;
    private int freezeInterval;
    private long now;

    public Tasks(PermissionHandler permission, Options options, Messages messages, SessionManagerImpl sessionManager, FreezeHandler freezeHandler, GadgetHandler gadgetHandler) {
        this.permission = permission;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.freezeHandler = freezeHandler;
        this.gadgetHandler = gadgetHandler;

        freezeModeConfiguration = options.staffItemsConfiguration.getFreezeModeConfiguration();
        saveInterval = 0;
        freezeInterval = 0;
        now = System.currentTimeMillis();
        runTaskTimerAsynchronously(StaffPlus.get(), clock * 20, clock * 20);
    }

    @Override
    public void run() {
        decideAutosave();
        freezeHandler.checkLocations();
        gadgetHandler.updateGadgets();
    }

    private void decideAutosave() {
        long later = System.currentTimeMillis();

        if ((later - now) >= 1000) {
            int addition = (int) ((later - now) / 1000);
            saveInterval += addition;
            freezeInterval += addition;
            now = System.currentTimeMillis();
        }

        if (freezeInterval >= freezeModeConfiguration.getModeFreezeTimer() && freezeInterval > 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerSession user = sessionManager.get(player.getUniqueId());
                if (user.isFrozen() && !permission.has(player, permissionMember)) {
                    freezeModeConfiguration.getModeFreezeSound().ifPresent(s->s.play(player));

                    if (!freezeModeConfiguration.isModeFreezePrompt()) {
                        messages.sendCollectedMessage(player, messages.freeze, messages.prefixGeneral);
                    }
                }
            }

            freezeInterval = 0;
        }
    }
}