package net.shortninja.staffplus.server.command;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.common.NoPermissionException;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Optional;

public abstract class AbstractCmd extends BukkitCommand {
    private final PermissionHandler permission = IocContainer.getPermissionHandler();
    private final Messages messages = IocContainer.getMessages();
    private final PlayerManager playerManager = IocContainer.getPlayerManager();

    protected AbstractCmd(String name, String permission) {
        super(name);
        setPermission(permission);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        try {
            validateAuthentication(sender);
            validatePermissions(sender);
            validateMinimumArguments(args);

            //delay if possible
            //execute command
            return executeCmd(sender, alias, args);
        } catch (BusinessException e) {
            IocContainer.getMessage().send(sender, e.getMessage(), e.getPrefix());
            return false;
        }
    }

    protected abstract boolean executeCmd(CommandSender sender, String alias, String[] args);

    protected abstract int getMinimumArguments(String[] args);

    protected abstract boolean isAuthenticationRequired();

    protected abstract PlayerRetrievalStrategy getPlayerRetrievalStrategy();

    protected abstract Optional<String> getPlayerName();


    public Optional<SppPlayer> retrievePlayer() {
        PlayerRetrievalStrategy strategy = getPlayerRetrievalStrategy();
        switch (strategy) {
            case NONE:
                return Optional.empty();
            case BOTH:
                Optional<SppPlayer> player = playerManager.getOnOrOfflinePlayer(getPlayerName().get());
                if(!player.isPresent()) {
                    throw new BusinessException(messages.playerNotRegistered, messages.prefixGeneral);
                }
                return player;
            case ONLINE:
        }
        return Optional.empty();
    }


    private void validateAuthentication(CommandSender sender) {
        if(isAuthenticationRequired() && sender instanceof Player) {
            IocContainer.getAuthenticationService().checkAuthentication((Player) sender);
        }
    }

    private void validatePermissions(CommandSender sender) {
        if (!permission.has(sender, getPermission())) {
            throw new NoPermissionException(messages.prefixGeneral);
        }
    }

    private void validateMinimumArguments(String[] args) {
        if (args.length < getMinimumArguments(args)) {
            throw new BusinessException(messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
        }
    }


}
