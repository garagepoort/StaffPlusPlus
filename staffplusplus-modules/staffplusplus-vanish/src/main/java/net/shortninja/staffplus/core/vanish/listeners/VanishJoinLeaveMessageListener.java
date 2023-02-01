package net.shortninja.staffplus.core.vanish.listeners;

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

@IocBukkitListener(conditionalOnProperty = "vanish-module.join-leave-message-enabled=true")
public class VanishJoinLeaveMessageListener implements Listener {

    @ConfigProperty("%lang%:vanish-join-message")
    private String vanishJoinMessage;
    @ConfigProperty("%lang%:vanish-leave-message")
    private String vanishLeaveMessage;

    private final Messages messages;
    private final JoinMessagesConfiguration joinMessagesConfiguration;

    public VanishJoinLeaveMessageListener(Messages messages, JoinMessagesConfiguration joinMessagesConfiguration) {
        this.messages = messages;
        this.joinMessagesConfiguration = joinMessagesConfiguration;
    }

    @EventHandler
    public void onVanish(VanishOnEvent event) {
        if (!event.isOnJoin() && (event.getType() == VanishType.LIST || event.getType() == VanishType.TOTAL)) {
            messages.sendGlobalMessage(vanishLeaveMessage.replace("%player%", event.getPlayer().getName()), "");
        }
    }

    @EventHandler
    public void onUnvanish(VanishOffEvent event) {
        if (event.getType() == VanishType.LIST || event.getType() == VanishType.TOTAL) {
            String joinMessage = joinMessagesConfiguration.getJoinMessageGroup(event.getPlayer())
                .map(JoinMessageGroup::getMessage)
                .orElse(vanishJoinMessage);

            messages.sendGlobalMessage(joinMessage.replace("%player%", event.getPlayer().getName()), "");
        }
    }
}
