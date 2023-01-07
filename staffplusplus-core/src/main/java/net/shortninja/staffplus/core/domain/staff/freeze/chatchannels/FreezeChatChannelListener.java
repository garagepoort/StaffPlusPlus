package net.shortninja.staffplus.core.domain.staff.freeze.chatchannels;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.chatchannels.ChatChannel;
import net.shortninja.staffplus.core.domain.chatchannels.ChatChannelService;
import net.shortninja.staffplusplus.chatchannels.ChatChannelType;
import net.shortninja.staffplusplus.freeze.PlayerFrozenEvent;
import net.shortninja.staffplusplus.freeze.PlayerUnFrozenEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static net.shortninja.staffplusplus.chatchannels.ChatChannelType.FREEZE;

@IocBukkitListener(conditionalOnProperty = "freeze-module.chatchannels.enabled=true")
public class FreezeChatChannelListener implements Listener {

    @ConfigProperty("%lang%:freeze-chatchannel.prefix")
    public String chatChannelPrefix;
    @ConfigProperty("%lang%:freeze-chatchannel.chatline")
    public String chatChannelLine;
    @ConfigProperty("%lang%:freeze-chatchannel.openingmessage")
    public String chatChannelOpeningMessage;

    private final ChatChannelService chatChannelService;
    private final BukkitUtils bukkitUtils;

    public FreezeChatChannelListener(ChatChannelService chatChannelService,
                                     BukkitUtils bukkitUtils) {
        this.chatChannelService = chatChannelService;
        this.bukkitUtils = bukkitUtils;
    }

    @EventHandler
    public void onPlayerFrozen(PlayerFrozenEvent playerFrozenEvent) {
        bukkitUtils.runTaskAsync(() -> {
            Set<UUID> members = new HashSet<>();

            CommandSender issuer = playerFrozenEvent.getIssuer();
            Player target = playerFrozenEvent.getTarget();

            members.add(issuer instanceof Player ? ((Player) issuer).getUniqueId() : Constants.CONSOLE_UUID);
            members.add(target.getUniqueId());

            Optional<ChatChannel> channel = chatChannelService.findChannel(target.getName(), ChatChannelType.FREEZE);
            if (!channel.isPresent()) {
                chatChannelService.create(
                    target.getName(),
                    chatChannelPrefix,
                    chatChannelLine,
                    chatChannelOpeningMessage,
                    members,
                    FREEZE);
            }
        });
    }

    @EventHandler
    public void onUnfreeze(PlayerUnFrozenEvent playerUnFrozenEvent) {
        bukkitUtils.runTaskAsync(() -> chatChannelService.closeChannel(
            playerUnFrozenEvent.getTarget().getName(),
            FREEZE));
    }
}
