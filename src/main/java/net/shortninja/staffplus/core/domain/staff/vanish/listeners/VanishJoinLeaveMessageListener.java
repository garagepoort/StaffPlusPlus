package net.shortninja.staffplus.core.domain.staff.vanish.listeners;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.domain.staff.joinmessages.JoinMessageGroup;
import net.shortninja.staffplus.core.domain.staff.joinmessages.JoinMessagesConfiguration;
import net.shortninja.staffplusplus.vanish.VanishOffEvent;
import net.shortninja.staffplusplus.vanish.VanishOnEvent;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatColor;

@IocBukkitListener(conditionalOnProperty = "vanish-module.join-leave-message-enabled=true")
public class VanishJoinLeaveMessageListener implements Listener {

    @ConfigProperty("%lang%:vanish-join-message")
    private String vanishJoinMessage;
    @ConfigProperty("%lang%:vanish-leave-message")
    private String vanishLeaveMessage;
    @ConfigProperty("vanish-module.use-vanilla-translation")
    private boolean useVanillaTranslation;

    private final Messages messages;
    private final JoinMessagesConfiguration joinMessagesConfiguration;

    public VanishJoinLeaveMessageListener(Messages messages, JoinMessagesConfiguration joinMessagesConfiguration) {
        this.messages = messages;
        this.joinMessagesConfiguration = joinMessagesConfiguration;
    }

    @EventHandler
    public void onVanish(VanishOnEvent event) {
        if (!event.isOnJoin() && (event.getType() == VanishType.LIST || event.getType() == VanishType.TOTAL)) {
            if (useVanillaTranslation) {
                TextComponent nameComp = new TextComponent(event.getPlayer().getName());
                nameComp.setColor(ChatColor.YELLOW);
                TranslatableComponent tc = new TranslatableComponent("multiplayer.player.left", nameComp);
                tc.setColor(ChatColor.YELLOW);
                Bukkit.getOnlinePlayers().forEach(p -> ((Player)p).spigot().sendMessage(tc));
            } else {
                messages.sendGlobalMessage(vanishLeaveMessage.replace("%player%", event.getPlayer().getName()), "");
            }
        }
    }

    @EventHandler
    public void onUnvanish(VanishOffEvent event) {
        if (event.getType() == VanishType.LIST || event.getType() == VanishType.TOTAL) {
            if (useVanillaTranslation) {
                TextComponent nameComp = new TextComponent(event.getPlayer().getName());
                nameComp.setColor(ChatColor.YELLOW);
                TranslatableComponent tc = new TranslatableComponent("multiplayer.player.joined", nameComp);
                tc.setColor(ChatColor.YELLOW);
                Bukkit.getOnlinePlayers().forEach(p -> ((Player)p).spigot().sendMessage(tc));
            } else {
                String joinMessage = joinMessagesConfiguration.getJoinMessageGroup(event.getPlayer())
                    .map(JoinMessageGroup::getMessage)
                    .orElse(vanishJoinMessage);

                messages.sendGlobalMessage(joinMessage.replace("%player%", event.getPlayer().getName()), "");
            }
        }
    }
}