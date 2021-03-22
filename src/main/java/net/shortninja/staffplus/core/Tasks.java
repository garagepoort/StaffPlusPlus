package net.shortninja.staffplus.core;

import be.garagepoort.mcioc.IocContainer;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.freeze.FreezeModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.handler.GadgetHandler;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

//TODO: Remove debug.

public class Tasks extends BukkitRunnable {
    private final PermissionHandler permission = IocContainer.get(PermissionHandler.class);
    private final MessageCoordinator message = IocContainer.get(MessageCoordinator.class);
    private final Options options = IocContainer.get(Options.class);
    private final Messages messages = IocContainer.get(Messages.class);
    private final SessionManagerImpl sessionManager = IocContainer.get(SessionManagerImpl.class);
    private final FreezeHandler freezeHandler = IocContainer.get(FreezeHandler.class);
    private final GadgetHandler gadgetHandler = StaffPlus.get().gadgetHandler;
    private final FreezeModeConfiguration freezeModeConfiguration;
    private int saveInterval;
    private int freezeInterval;
    private long now;
    private long later;

    public Tasks() {
        freezeModeConfiguration = options.modeConfiguration.getFreezeModeConfiguration();
        saveInterval = 0;
        freezeInterval = 0;
        now = System.currentTimeMillis();
        runTaskTimer(StaffPlus.get(), options.clock, options.clock);
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
            StaffPlus.get().saveUsers();
            StaffPlus.get().getLogger().info("Staff++ is now auto saving...");
            saveInterval = 0;
        }

        if (freezeInterval >= freezeModeConfiguration.getModeFreezeTimer() && freezeInterval > 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerSession user = sessionManager.get(player.getUniqueId());
                if (user.isFrozen() && !permission.has(player, options.permissionMember)) {
                    freezeModeConfiguration.getModeFreezeSound().ifPresent(s->s.play(player));

                    if (!freezeModeConfiguration.isModeFreezePrompt()) {
                        message.sendCollectedMessage(player, messages.freeze, messages.prefixGeneral);
                    }
                }
            }

            freezeInterval = 0;
        }
    }
}