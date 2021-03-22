package net.shortninja.staffplus.core.domain.staff.freeze;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.shortninja.staffplus.core.StaffPlus;
import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.freeze.FreezeModeConfiguration;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
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
    private final MessageCoordinator message;
    private final Options options;
    private final Messages messages;
    private final SessionManagerImpl sessionManager;
    private final FreezeModeConfiguration freezeModeConfiguration;

    public FreezeHandler(PermissionHandler permission, MessageCoordinator message, Options options, Messages messages, SessionManagerImpl sessionManager) {
        this.permission = permission;
        this.message = message;
        this.options = options;
        this.messages = messages;
        this.sessionManager = sessionManager;
        freezeModeConfiguration = options.modeConfiguration.getFreezeModeConfiguration();

        if (freezeModeConfiguration.isTitleMessageEnabled()) {
            Bukkit.getScheduler().runTaskTimer(StaffPlus.get(), () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    PlayerSession playerSession = sessionManager.get(p.getUniqueId());
                    if (playerSession.isFrozen()) {
                        p.sendTitle(message.colorize(messages.freezeTitle), message.colorize(messages.freezeSubtitle), 1, 25, 1);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message.colorize(messages.vanishEnabled)));
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
            message.sendCollectedMessage(player, messages.freeze, messages.prefixGeneral);
        }

        message.send(sender, messages.staffFroze.replace("%target%", player.getName()), messages.prefixGeneral);

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
            message.send(sender, messages.bypassed, messages.prefixGeneral);
            return;
        }

        if (freezeModeConfiguration.isModeFreezePrompt() && session.getCurrentGui().isPresent()) {
            if (session.getCurrentGui().get() instanceof FreezeGui) {
                player.closeInventory();
            }


        }

        message.send(sender, messages.staffUnfroze.replace("%target%", player.getName()), messages.prefixGeneral);
        message.sendCollectedMessage(player, messages.unfrozen, messages.prefixGeneral);

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

                player.teleport(lastLocation);
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