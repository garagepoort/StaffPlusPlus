package net.shortninja.staffplus.core.domain.staff.investigate.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@IocBean(conditionalOnProperty = "investigations-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class ConcludeInvestigationCmd extends AbstractCmd {

    private final InvestigationService investigationService;

    public ConcludeInvestigationCmd(Messages messages, Options options, CommandService commandService, InvestigationService investigationService) {
        super(options.investigationConfiguration.getConcludeInvestigationCmd(), messages, options, commandService);
        this.investigationService = investigationService;
        setDescription("Conclude your investigation");
        setPermission(options.investigationConfiguration.getInvestigatePermission());
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if(!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }

        investigationService.concludeInvestigation((Player) sender);
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 0;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.NONE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }
}
