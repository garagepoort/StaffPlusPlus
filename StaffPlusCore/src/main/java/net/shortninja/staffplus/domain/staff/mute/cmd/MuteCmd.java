package net.shortninja.staffplus.domain.staff.mute.cmd;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.common.cmd.AbstractCmd;
import net.shortninja.staffplus.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.session.SessionManagerImpl;
import net.shortninja.staffplus.domain.staff.mute.MuteService;
import net.shortninja.staffplus.common.utils.PermissionHandler;
import net.shortninja.staffplus.common.JavaUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MuteCmd extends AbstractCmd {

    private final MuteService muteService = IocContainer.getMuteService();
    private SessionManagerImpl sessionManager = IocContainer.getSessionManager();
    private PermissionHandler permissionHandler;
    private Options options;

    public MuteCmd(String name) {
        super(name, IocContainer.getOptions().muteConfiguration.getPermissionMutePlayer());
        options = IocContainer.getOptions();
        permissionHandler = IocContainer.getPermissionHandler();
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        String reason = JavaUtils.compileWords(args, 1);

        muteService.permMute(sender, player, reason);
        sessionManager.get(player.getId()).setMuted(true);
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 2;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.BOTH;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.ofNullable(args[0]);
    }

    @Override
    protected boolean canBypass(Player player) {
        return permissionHandler.has(player, options.muteConfiguration.getPermissionMuteByPass());
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
