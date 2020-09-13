package net.shortninja.staffplus.player.attribute.infraction;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.unordered.IWarning;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.warn.WarnService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class InfractionCoordinator {
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = IocContainer.getMessages();
    private UserManager userManager = IocContainer.getUserManager();
    private WarnService warnService = WarnService.getInstance();

    public Set<IWarning> getWarnings() {
        Set<IWarning> warnings = new HashSet<>();

        for (IUser user : userManager.getAll()) {
            warnings.addAll(user.getWarnings());
        }

        return warnings;
    }

    public void sendWarning(CommandSender sender, String playerName, String reason) {
        IUser user = userManager.getOnOrOfflineUser(playerName);
        if (user == null) {
            message.send(sender, messages.playerOffline, messages.prefixGeneral);
            return;
        }

        String issuerName = sender instanceof Player ? sender.getName() : "Console";
        UUID issuerUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : StaffPlus.get().consoleUUID;
        Warning warning = new Warning(user.getUuid(), playerName, reason, issuerName, issuerUuid, System.currentTimeMillis());


        // Offline users cannot bypass being warned this way. Permissions are taken away upon logging out
        if (user.isOnline() && permission.has(user.getPlayer().get(), options.permissionWarnBypass)) {
            message.send(sender, messages.bypassed, messages.prefixGeneral);
            return;
        }

        warnService.addWarn(warning);
        message.send(sender, messages.warned.replace("%target%", warning.getName()).replace("%reason%", warning.getReason()), messages.prefixWarnings);

        if(user.isOnline()) {
            Optional<Player> p = user.getPlayer();
            message.send(p.get(), messages.warn.replace("%reason%", warning.getReason()), messages.prefixWarnings);
            options.warningsSound.play(p.get());
            warnService.checkBan(user);
        }
    }
}