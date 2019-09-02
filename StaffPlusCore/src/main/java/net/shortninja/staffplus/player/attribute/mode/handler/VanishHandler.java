package net.shortninja.staffplus.player.attribute.mode.handler;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.compatibility.IProtocol;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.unordered.VanishType;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VanishHandler {
    private IProtocol versionProtocol = StaffPlus.get().versionProtocol;
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private UserManager userManager = StaffPlus.get().userManager;
    private StaffPlus staffPlus = StaffPlus.get();

    public void addVanish(Player player, VanishType vanishType) {
        IUser user = userManager.get(player.getUniqueId());
        VanishType userVanishType = user.getVanishType();

        if (userVanishType == vanishType) {
            return;
        } else if (userVanishType != VanishType.NONE) {
            unapplyVanish(player, userVanishType, true);
        }

        applyVanish(player, vanishType, true);
        user.setVanishType(vanishType);
    }

    public void removeVanish(Player player) {
        IUser user = userManager.get(player.getUniqueId());
        VanishType vanishType = user.getVanishType();

        if (vanishType == VanishType.NONE) {
            return;
        }

        unapplyVanish(player, vanishType, true);
        user.setVanishType(VanishType.NONE);
    }

    public List<Player> getVanished() {
       return Bukkit.getOnlinePlayers().stream().filter(this::isVanished).collect(Collectors.toList());
    }

    public boolean isVanished(Player player) {
        IUser user = userManager.get(player.getUniqueId());

        return user.getVanishType() != VanishType.NONE;
    }

    public void updateVanish() {
        for (IUser user : userManager.getAll()) {
            Optional<Player> player = user.getPlayer();

            if (player.isPresent() && user.getVanishType() == VanishType.TOTAL) {
                applyVanish(player.get(), user.getVanishType(), false);
            }
        }
    }

    private void applyVanish(Player player, VanishType vanishType, boolean shouldMessage) {
        String message = "";

        switch (vanishType) {
            case TOTAL:
                if (permission.has(player, options.permissionVanishTotal)) {
                    if(options.staffView) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if(!permission.has(p,options.permissionMode))
                                p.hidePlayer(player);
                        }
                    }else{
                        for (Player p : Bukkit.getOnlinePlayers()){
                            p.hidePlayer(player);
                        }
                    }
                }

                message = messages.totalVanish.replace("%status%", "enabled");
                break;
            case LIST:
                if (options.vanishTabList) {
                    versionProtocol.listVanish(player, true);
                }

                message = messages.listVanish.replace("%status%", "enabled");
                break;
            default:
                break;
        }

        if (shouldMessage && !message.isEmpty()) {
            this.message.send(player, message, messages.prefixGeneral);
        }
    }

    private void unapplyVanish(Player player, VanishType vanishType, boolean shouldMessage) {
        String message = "";

        switch (vanishType) {
            case TOTAL:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.showPlayer(player);
                }

                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                message = messages.totalVanish.replace("%status%", "disabled");
                break;
            case LIST:
                versionProtocol.listVanish(player, false);
                message = messages.listVanish.replace("%status%", "disabled");
                break;
            default:
                break;
        }

        if (shouldMessage && !message.isEmpty()) {
            this.message.send(player, message, messages.prefixGeneral);
        }
    }
}