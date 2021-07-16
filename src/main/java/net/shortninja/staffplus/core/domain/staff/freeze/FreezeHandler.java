package net.shortninja.staffplus.core.domain.staff.freeze;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.freeze.FreezeModeConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@IocBean
public class FreezeHandler {
    private final static Map<UUID, Location> lastFrozenLocations = new HashMap<>();
    private final PermissionHandler permission;

    private final Options options;
    private final Messages messages;
    private final SessionManagerImpl sessionManager;
    private final FreezeModeConfiguration freezeModeConfiguration;

    public FreezeHandler(PermissionHandler permission, Options options, Messages messages, SessionManagerImpl sessionManager) {
        this.permission = permission;

        this.options = options;
        this.messages = messages;
        this.sessionManager = sessionManager;
        freezeModeConfiguration = options.staffItemsConfiguration.getFreezeModeConfiguration();

        if (freezeModeConfiguration.isTitleMessageEnabled()) {
            Bukkit.getScheduler().runTaskTimer(StaffPlus.get(), () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    PlayerSession playerSession = sessionManager.get(p.getUniqueId());
                    if (playerSession.isFrozen()) {
                        p.sendTitle(messages.colorize(messages.freezeTitle), messages.colorize(messages.freezeSubtitle), 1, 25, 1);
                    }
                }
            }, 20L, 20L);
        }
    }

    public void execute(FreezeRequest freezeRequest) {
        validatePermissions(freezeRequest.getCommandSender(), freezeRequest.getPlayer());
        if (freezeRequest.isEnableFreeze()) {
            addFreeze(freezeRequest.getCommandSender(), freezeRequest.getPlayer());
        } else {
            removeFreeze(freezeRequest.getCommandSender(), freezeRequest.getPlayer());
        }
    }

    public boolean isFrozen(UUID uuid) {
        PlayerSession user = sessionManager.get(uuid);
        if (user == null)
            return false;
        return lastFrozenLocations.containsKey(uuid) || user.isFrozen();
    }


    private void addFreeze(CommandSender sender, Player player) {
        UUID uuid = player.getUniqueId();
        if (freezeModeConfiguration.isModeFreezePrompt()) {
            new FreezeGui(freezeModeConfiguration.getModeFreezePromptTitle()).show(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 128));
        } else {
            messages.sendCollectedMessage(player, messages.freeze, messages.prefixGeneral);
        }

        messages.send(sender, messages.staffFroze.replace("%target%", player.getName()), messages.prefixGeneral);

        sessionManager.get(player.getUniqueId()).setFrozen(true);
        lastFrozenLocations.put(uuid, player.getLocation());
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 128));
        freezeModeConfiguration.getModeFreezeSound().ifPresent(s -> s.play(player));

    }

    public void removeFreeze(CommandSender sender, Player player) {
        UUID uuid = player.getUniqueId();
        PlayerSession session = sessionManager.get(uuid);

        if (permission.has(player, options.permissionFreezeBypass)) {
            messages.send(sender, messages.bypassed, messages.prefixGeneral);
            return;
        }

        if (freezeModeConfiguration.isModeFreezePrompt() && session.getCurrentGui().isPresent()) {
            if (session.getCurrentGui().get() instanceof FreezeGui) {
                player.closeInventory();
            }
        }

        messages.send(sender, messages.staffUnfroze.replace("%target%", player.getName()), messages.prefixGeneral);
        messages.sendCollectedMessage(player, messages.unfrozen, messages.prefixGeneral);

        session.setFrozen(false);
        lastFrozenLocations.remove(uuid);
        player.removePotionEffect(PotionEffectType.JUMP);
        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.BLINDNESS);

    }

    public void checkLocations() {
        for (UUID uuid : lastFrozenLocations.keySet()) {
            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                Location playerLocation = player.getLocation();
                Location lastLocation = lastFrozenLocations.get(uuid).setDirection(playerLocation.getDirection());

                if (compareLocations(playerLocation, lastLocation)) {
                    continue;
                }

                Bukkit.getScheduler().runTaskLater(StaffPlus.get(), () -> player.teleport(lastLocation), 1);
            }
        }
    }

    /*
     * Only making this method because Location#equals checks if direction is the
     * same, which I really don't care for.
     */
    private boolean compareLocations(Location previous, Location current) {
        return previous.getBlockX() == current.getBlockX() && previous.getBlockY() == current.getBlockY() && previous.getBlockZ() == current.getBlockZ();
    }

    public void validatePermissions(CommandSender commandSender, Player target) {
        if (permission.has(target, options.permissionFreezeBypass)) {
            throw new BusinessException(messages.bypassed, messages.prefixGeneral);
        }
    }
}