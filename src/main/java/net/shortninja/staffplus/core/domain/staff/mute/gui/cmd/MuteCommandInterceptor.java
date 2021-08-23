package net.shortninja.staffplus.core.domain.staff.mute.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;
import net.shortninja.staffplus.core.domain.staff.mute.config.MuteConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static net.shortninja.staffplus.core.domain.staff.mute.MuteMessageStringUtil.replaceMutePlaceholders;

@IocBean(conditionalOnProperty = "mute-module.enabled=true")
@IocListener
public class MuteCommandInterceptor implements Listener {

    private final OnlineSessionsManager sessionManager;
    private final MuteConfiguration muteConfiguration;
    private final MuteService muteService;
    private final Messages messages;
    private final BukkitUtils bukkitUtils;

    public MuteCommandInterceptor(OnlineSessionsManager sessionManager, MuteConfiguration muteConfiguration, MuteService muteService, Messages messages, BukkitUtils bukkitUtils) {
        this.sessionManager = sessionManager;
        this.muteConfiguration = muteConfiguration;
        this.muteService = muteService;
        this.messages = messages;
        this.bukkitUtils = bukkitUtils;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void interceptCommands(PlayerCommandPreprocessEvent event) {
        OnlinePlayerSession playerSession = sessionManager.get(event.getPlayer());
        if (!playerSession.isMuted()) {
            return;
        }

        String command = event.getMessage().toLowerCase();
        boolean executedBlockedCommand = muteConfiguration.blockedCommands.stream().anyMatch((c) -> command.startsWith("/" + c));
        if (executedBlockedCommand) {
            bukkitUtils.runTaskAsync(event.getPlayer(), () ->
                muteService.getMuteByMutedUuid(event.getPlayer().getUniqueId()).ifPresent(mute -> {
                    String message = replaceMutePlaceholders(messages.muted, mute);
                    this.messages.send(event.getPlayer(), message, messages.prefixGeneral);
                }));
            event.setCancelled(true);
        }


    }
}
