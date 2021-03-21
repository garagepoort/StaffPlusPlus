package net.shortninja.staffplus.domain.staff;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.cmd.AbstractCmd;
import net.shortninja.staffplus.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.common.utils.MessageCoordinator;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManagerImpl;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PersonnelCmd extends AbstractCmd {
    private final MessageCoordinator message = IocContainer.getMessage();
    private final SessionManagerImpl sessionManager = IocContainer.getSessionManager();

    public PersonnelCmd(String name) {
        super(name);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer sppPlayer) {
        String status = "all";

        if (args.length == 1) {
            status = args[0];
        }

        for (String message : messages.staffListStart) {
            this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE), message.contains("%longline%") ? "" : messages.prefixGeneral);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerSession session = sessionManager.get(player.getUniqueId());
            if (hasStatus(session, status, player)) {
                message.send(sender, messages.staffListMember.replace("%player%", player.getName()).replace("%statuscolor%", getStatusColor(session, player)), messages.prefixGeneral);
            }
        }

        for (String message : messages.staffListEnd) {
            this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE), message.contains("%longline%") ? "" : messages.prefixGeneral);
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
        if (!permission.has(player, options.permissionMember)) {
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