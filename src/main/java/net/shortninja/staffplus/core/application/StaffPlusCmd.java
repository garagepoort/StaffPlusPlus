package net.shortninja.staffplus.core.application;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Command(
    command = "commands:staffplus",
    permissions = "permissions:staffplus",
    description = "Used for reloading config and lang file in use",
    usage = "[reload]"
)
@IocBean
@IocMultiProvider(SppCommand.class)
public class StaffPlusCmd extends AbstractCmd {

    public StaffPlusCmd(Messages messages, Options options, CommandService commandService, PermissionHandler permissionHandler) {
        super( messages, permissionHandler, commandService);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        if (args[0].equalsIgnoreCase("reload")) {
            StaffPlus.get().reload();
            messages.send(sender, "Configuration has been reloaded", messages.prefixGeneral);
        }
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
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
