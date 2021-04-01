package net.shortninja.staffplus.core.domain.staff.investigate.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.InvestigationItemBuilder;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.ManageInvestigationsGui;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@IocBean(conditionalOnProperty = "investigations-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class ManageInvestigationsGuiCmd extends AbstractCmd {

    private final PlayerManager playerManager;
    private final InvestigationService investigationService;
    private final InvestigationItemBuilder investigationItemBuilder;
    private final IProtocolService protocolService;

    public ManageInvestigationsGuiCmd(Messages messages, PlayerManager playerManager, Options options, CommandService commandService, InvestigationService investigationService, InvestigationItemBuilder investigationItemBuilder, IProtocolService protocolService) {
        super(options.investigationConfiguration.getCommandManageInvestigationsGui(), messages, options, commandService);
        this.playerManager = playerManager;
        this.investigationService = investigationService;
        this.investigationItemBuilder = investigationItemBuilder;
        this.protocolService = protocolService;
        setPermission(options.investigationConfiguration.getPermissionView());
        setDescription("Open the manage Investigations GUI.");
        setUsage("[playername?]");
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if(!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }

        new ManageInvestigationsGui((Player) sender, player, "Manage Investigation", 0, investigationService, investigationItemBuilder, protocolService).show((Player) sender);
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 0;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.OPTIONAL;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        if(args.length == 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(args[0]);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
