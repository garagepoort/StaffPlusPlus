package net.shortninja.staffplus.core;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.freeze.FreezeModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.handler.GadgetHandler;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionLoader;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

//TODO: Remove debug.

@IocBean
public class Tasks extends BukkitRunnable {
    private final PermissionHandler permission;

    private final Options options;
    private final Messages messages;
    private final SessionManagerImpl sessionManager;
    private final FreezeHandler freezeHandler;
    private final GadgetHandler gadgetHandler;
    private final FreezeModeConfiguration freezeModeConfiguration;
    private int saveInterval;
    private int freezeInterval;
    private long now;
    private long later;
    private SessionLoader sessionLoader;

    public Tasks(PermissionHandler permission, Options options, Messages messages, SessionManagerImpl sessionManager, FreezeHandler freezeHandler, GadgetHandler gadgetHandler, SessionLoader sessionLoader) {
        this.permission = permission;

        this.options = options;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.freezeHandler = freezeHandler;
        this.gadgetHandler = gadgetHandler;
        this.sessionLoader = sessionLoader;

        freezeModeConfiguration = this.options.staffItemsConfiguration.getFreezeModeConfiguration();
        saveInterval = 0;
        freezeInterval = 0;
        now = System.currentTimeMillis();
        runTaskTimerAsynchronously(StaffPlus.get(), this.options.clock, this.options.clock);
    }

    @Override
    public void run() {
        decideAutosave();
        freezeHandler.checkLocations();
        gadgetHandler.updateGadgets();
    }

    private void decideAutosave() {
        later = System.currentTimeMillis();

        if ((later - now) >= 1000) {
            int addition = (int) ((later - now) / 1000);
            saveInterval += addition;
            freezeInterval += addition;
            now = System.currentTimeMillis();
        }

        if (saveInterval >= options.autoSave && saveInterval > 0) {
            sessionManager.getAll().forEach(sessionLoader::saveSessionSynchronous);
            StaffPlus.get().getLogger().info("Staff++ is now auto saving...");
            saveInterval = 0;
        }

        if (freezeInterval >= freezeModeConfiguration.getModeFreezeTimer() && freezeInterval > 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerSession user = sessionManager.get(player.getUniqueId());
                if (user.isFrozen() && !permission.has(player, options.permissionMember)) {
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