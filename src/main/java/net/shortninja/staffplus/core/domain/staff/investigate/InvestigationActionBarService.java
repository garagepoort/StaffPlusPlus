package net.shortninja.staffplus.core.domain.staff.investigate;

import be.garagepoort.mcioc.IocBean;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@IocBean(conditionalOnProperty = "investigations-module.title-message-enabled=true")
public class InvestigationActionBarService {

    private final SessionManagerImpl sessionManager;

    public InvestigationActionBarService(Options options, SessionManagerImpl sessionManager, Messages messages) {
        this.sessionManager = sessionManager;

        if (options.investigationConfiguration.isTitleMessageEnabled()) {
            Bukkit.getScheduler().runTaskTimer(StaffPlus.get(), () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    PlayerSession playerSession = this.sessionManager.get(p.getUniqueId());
                    if (playerSession.isUnderInvestigation()) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messages.colorize(messages.underInvestigationTitle)));
                    }
                }
            }, 20L, 20L);
        }
    }
}
