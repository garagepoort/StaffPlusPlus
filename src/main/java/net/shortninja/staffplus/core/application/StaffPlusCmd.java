package net.shortninja.staffplus.core.application;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.authentication.AuthenticationService;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@IocBean
@IocMultiProvider(SppCommand.class)
public class StaffPlusCmd extends AbstractCmd {

    public StaffPlusCmd(PermissionHandler permissionHandler, AuthenticationService authenticationService, Messages messages, MessageCoordinator message, PlayerManager playerManager, Options options) {
        super("staffplus", permissionHandler, authenticationService, messages, message, playerManager, options);
        setPermission(options.permissionStaff);
        setDescription("Used for reloading config and lang file in use");
        setUsage("[reload]");
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if (args[0].equalsIgnoreCase("reload")) {
            StaffPlus.get().reload();
            message.send(sender, "Configuration has been reloaded", messages.prefixGeneral);
        }
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.NONE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1 && !args[0].equalsIgnoreCase("reload")) {
            return Collections.singletonList("reload");
        }
        return Collections.emptyList();
    }
}
