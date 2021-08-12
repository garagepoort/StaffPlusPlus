package net.shortninja.staffplus.core.domain.staff.freeze;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.freeze.FreezeModeConfiguration;
import net.shortninja.staffplusplus.freeze.PlayerFrozenEvent;
import net.shortninja.staffplusplus.freeze.PlayerUnFrozenEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class FreezeHandler {
    private final static Map<UUID, Location> lastFrozenLocations = new HashMap<>();

    @ConfigProperty("permissions:freeze-bypass")
    private String permissionFreezeBypass;

    private final PermissionHandler permission;
    private final Messages messages;

    public FreezeHandler(PermissionHandler permission,
                         Options options,
                         Messages messages,
                         OnlineSessionsManager sessionManager,
                         PlayerManager playerManager) {
        this.permission = permission;
        this.messages = messages;
        FreezeModeConfiguration freezeModeConfiguration = options.staffItemsConfiguration.getFreezeModeConfiguration();

        if (freezeModeConfiguration.isTitleMessageEnabled()) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(StaffPlus.get(), () -> {
                sessionManager.getAll().stream().filter(OnlinePlayerSession::isFrozen)
                    .map(s -> playerManager.getOnlinePlayer(s.getUuid()))
                    .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                    .map(SppPlayer::getPlayer)
                    .forEach(player -> player.sendTitle(messages.colorize(messages.freezeTitle), messages.colorize(messages.freezeSubtitle), 1, 25, 1));
            }, 20L, 20L);
        }
    }

    public void execute(FreezeRequest freezeRequest) {
        if (freezeRequest.isEnableFreeze()) {
            validatePermissions(freezeRequest.getPlayer());
            addFreeze(freezeRequest.getCommandSender(), freezeRequest.getPlayer());
        } else {
            removeFreeze(freezeRequest.getCommandSender(), freezeRequest.getPlayer());
        }
    }

    private void addFreeze(CommandSender sender, Player player) {
        lastFrozenLocations.put(player.getUniqueId(), player.getLocation());
        sendEvent(new PlayerFrozenEvent(sender, player));
    }

    public void removeFreeze(CommandSender sender, Player player) {
        lastFrozenLocations.remove(player.getUniqueId());
        sendEvent(new PlayerUnFrozenEvent(sender, player));
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

    public void validatePermissions(Player target) {
        if (permission.has(target, permissionFreezeBypass)) {
            throw new BusinessException(messages.bypassed, messages.prefixGeneral);
        }
    }
}