package net.shortninja.staffplus.core.domain.staff.vanish.gui;

import be.garagepoort.mcioc.IocBean;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.IPlayerSession;
import org.bukkit.Bukkit;

import java.util.Optional;

@IocBean(conditionalOnProperty = "vanish-module.vanish-message-enabled=true")
public class VanishActionBarTask {

    public VanishActionBarTask(OnlineSessionsManager onlineSessionsManager,
                               PlayerManager playerManager,
                               Messages messages) {
        Bukkit.getScheduler().runTaskTimer(StaffPlus.get(), () ->
            onlineSessionsManager.getAll().stream()
                .filter(IPlayerSession::isVanished)
                .map(s -> playerManager.getOnlinePlayer(s.getUuid()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(p -> p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messages.colorize(messages.vanishEnabled)))), 20L, 20L);
    }
}
