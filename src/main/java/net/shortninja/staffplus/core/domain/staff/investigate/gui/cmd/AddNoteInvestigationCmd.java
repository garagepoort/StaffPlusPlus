package net.shortninja.staffplus.core.domain.staff.investigate.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationNoteService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

@Command(
    command = "commands:commands.investigations.manage.add-note",
    permissions = "permissions:permissions.investigations.manage.add-note",
    description = "Add a note to your investigation",
    usage = "[note]"
)
@IocBean(conditionalOnProperty = "investigations-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class AddNoteInvestigationCmd extends AbstractCmd {

    private final InvestigationNoteService investigationNoteService;

    public AddNoteInvestigationCmd(Messages messages, Options options, CommandService commandService, InvestigationNoteService investigationNoteService, PermissionHandler permissionHandler) {
        super(messages, permissionHandler, commandService);
        this.investigationNoteService = investigationNoteService;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        if(!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }

        investigationNoteService.addNote((Player) sender, args[0]);
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
}
