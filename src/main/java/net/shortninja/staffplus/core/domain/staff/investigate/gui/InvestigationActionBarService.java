package net.shortninja.staffplus.core.domain.staff.investigate.gui;

import be.garagepoort.mcioc.IocBean;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;

import java.util.stream.Stream;

@IocBean(conditionalOnProperty = "investigations-module.notifications.investigated.title-message-enabled=true")
public class InvestigationActionBarService {

    public InvestigationActionBarService(OnlineSessionsManager sessionManager, Messages messages, PlayerManager playerManager) {

        Bukkit.getScheduler().runTaskTimerAsynchronously(StaffPlus.get(), () -> {
            sessionManager.getAll().stream()
                .filter(OnlinePlayerSession::isUnderInvestigation)
                .map(s -> playerManager.getOnlinePlayer(s.getUuid()))
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .map(SppPlayer::getPlayer)
                .forEach(p -> p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messages.colorize(messages.underInvestigationTitle))));
        }, 20L, 20L);
    }
}
