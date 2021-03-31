package net.shortninja.staffplus.core.domain.staff;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;

import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@IocBean
@IocMultiProvider(SppCommand.class)
public class PersonnelCmd extends AbstractCmd {
    private final PermissionHandler permissionHandler;
    private final SessionManagerImpl sessionManager;

    public PersonnelCmd(PermissionHandler permissionHandler, Messages messages, Options options, SessionManagerImpl sessionManager, CommandService commandService) {
        super(options.commandStaffList, messages, options, commandService);
        this.permissionHandler = permissionHandler;
        this.sessionManager = sessionManager;
        setDescription("Lists all registered staff members.");
        setUsage("{all | online | away | offline}");
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer sppPlayer) {
        String status = "all";

        if (args.length == 1) {
            status = args[0];
        }

        for (String message : messages.staffListStart) {
            this.messages.send(sender, message.replace("%longline%", this.messages.LONG_LINE), message.contains("%longline%") ? "" : messages.prefixGeneral);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerSession session = sessionManager.get(player.getUniqueId());
            if (hasStatus(session, status, player)) {
                messages.send(sender, messages.staffListMember.replace("%player%", player.getName()).replace("%statuscolor%", getStatusColor(session, player)), messages.prefixGeneral);
            }
        }

        for (String message : messages.staffListEnd) {
            this.messages.send(sender, message.replace("%longline%", this.messages.LONG_LINE), message.contains("%longline%") ? "" : messages.prefixGeneral);
        }

        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 0;
    }

    @Override
    protected boolean isAuthenticationRequired() {
        return false;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.NONE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }

    private boolean hasStatus(PlayerSession session, String status, Player player) {
        VanishType vanishType = session.getVanishType();
        if (!permissionHandler.has(player, options.permissionMember)) {
            return false;
        }

        switch (status.toLowerCase()) {
            case "online":
                return vanishType == VanishType.NONE;
            case "offline":
                return vanishType == VanishType.TOTAL || (vanishType == VanishType.LIST && !options.vanishShowAway);
            case "away":
                return vanishType == VanishType.NONE || (vanishType == VanishType.LIST && options.vanishShowAway);
        }
        return true;
    }

    private String getStatusColor(PlayerSession user, Player player) {
        String statusColor = "4";

        if (hasStatus(user, "online", player)) {
            statusColor = "a";
        } else if (hasStatus(user, "away", player)) {
            statusColor = "e";
        }

        return statusColor;
    }
}