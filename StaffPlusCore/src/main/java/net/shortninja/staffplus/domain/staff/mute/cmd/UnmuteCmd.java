package net.shortninja.staffplus.domain.staff.mute.cmd;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.domain.player.PlayerManager;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.common.cmd.AbstractCmd;
import net.shortninja.staffplus.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.session.SessionManagerImpl;
import net.shortninja.staffplus.domain.staff.mute.MuteService;
import net.shortninja.staffplus.common.JavaUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UnmuteCmd extends AbstractCmd {

    private final MuteService muteService = IocContainer.getMuteService();
    private final PlayerManager playerManager = IocContainer.getPlayerManager();
    private final SessionManagerImpl sessionManager = IocContainer.getSessionManager();

    public UnmuteCmd(String name) {
        super(name, IocContainer.getOptions().muteConfiguration.getPermissionUnmutePlayer());
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        String reason = JavaUtils.compileWords(args, 1);

        muteService.unmute(sender, player, reason);
        sessionManager.get(player.getId()).setMuted(false);
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
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
