package net.shortninja.staffplus.core.domain.staff.staffchat.cmd;

import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.staffchat.StaffChatChannelConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

public class StaffChatMuteChannelCmd extends AbstractCmd {
    private final OnlineSessionsManager sessionManager;
    private final PlayerSettingsRepository playerSettingsRepository;
    private final StaffChatChannelConfiguration channelConfiguration;
    private final BukkitUtils bukkitUtils;

    public StaffChatMuteChannelCmd(Messages messages, OnlineSessionsManager sessionManager, CommandService commandService, PlayerSettingsRepository playerSettingsRepository, StaffChatChannelConfiguration channelConfiguration, PermissionHandler permissionHandler, BukkitUtils bukkitUtils) {
        super(channelConfiguration.getCommand() + "-mute", messages, permissionHandler, commandService);
        this.sessionManager = sessionManager;
        this.playerSettingsRepository = playerSettingsRepository;
        this.channelConfiguration = channelConfiguration;
        this.bukkitUtils = bukkitUtils;
        setDescription("Mutes all staff chat for channel. You can still send messages to staff chat but you won't see anything.");
        if(channelConfiguration.getPermission().isPresent()) {
            setPermission(channelConfiguration.getPermission().get() + ".mute");
        }
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        validateIsPlayer(sender);
        PlayerSettings session = playerSettingsRepository.get(((Player) sender));

        if (session.isStaffChatMuted(channelConfiguration.getName())) {
            messages.send(sender, messages.staffChatUnmuted, channelConfiguration.getPrefix());
        } else {
            messages.send(sender, messages.staffChatMuted, channelConfiguration.getPrefix());
        }

        updateSession(sender);
        return true;
    }

    private void updateSession(CommandSender sender) {
        bukkitUtils.runTaskAsync(sender, () -> {
            PlayerSettings session = playerSettingsRepository.get((OfflinePlayer) sender);
            session.setStaffChatMuted(channelConfiguration.getName(), !session.isStaffChatMuted(channelConfiguration.getName()));
            playerSettingsRepository.save(session);
        });
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.NONE;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 0;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }

}