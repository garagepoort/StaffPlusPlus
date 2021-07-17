package net.shortninja.staffplus.core.domain.staff.investigate.gui;

import be.garagepoort.mcioc.IocBean;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import org.bukkit.Bukkit;

@IocBean(conditionalOnProperty = "investigations-module.notifications.investigated.title-message-enabled=true")
public class InvestigationActionBarService {

    public InvestigationActionBarService(SessionManagerImpl sessionManager, Messages messages) {

        Bukkit.getScheduler().runTaskTimerAsynchronously(StaffPlus.get(), () -> {
            sessionManager.getAll().stream().filter(PlayerSession::isUnderInvestigation)
                .filter(session -> session.getPlayer().isPresent())
                .forEach(session -> session.getPlayer().get().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messages.colorize(messages.underInvestigationTitle))));
        }, 20L, 20L);
    }
}
