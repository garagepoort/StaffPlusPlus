package net.shortninja.staffplus.core.domain.staff.warn.warnings.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.authentication.AuthenticationService;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.gui.MyWarningsGui;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@IocBean(conditionalOnProperty = "warnings-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class MyWarningsCmd extends AbstractCmd {

    private final WarnService warnService;

    public MyWarningsCmd(PermissionHandler permissionHandler, AuthenticationService authenticationService, Messages messages, MessageCoordinator message, PlayerManager playerManager, Options options, WarnService warnService) {
        super(options.warningConfiguration.getMyWarningsCmd(), permissionHandler, authenticationService, messages, message, playerManager, options);
        this.warnService = warnService;
        setPermission(options.warningConfiguration.getMyWarningsPermission());
        setDescription("Open my warnings gui");
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if(!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }

        new MyWarningsGui((Player) sender, "My Warnings", 0).show((Player) sender);
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), ()-> warnService.markWarningsRead(((Player) sender).getUniqueId()));
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
