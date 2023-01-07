package net.shortninja.staffplus.core.domain.blacklist.listeners;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.blacklist.BlacklistService;
import net.shortninja.staffplusplus.blacklist.BlacklistCensoredEvent;
import net.shortninja.staffplusplus.blacklist.BlacklistType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBukkitListener(conditionalOnProperty = "blacklist-module.enabled=true && blacklist-module.censor-signs=true")
public class BlacklistSignListener implements Listener {

    @ConfigProperty("permissions:blacklist")
    private String permissionBlacklist;

    private final BlacklistService blacklistService;
    private final PermissionHandler permission;
    private final Options options;

    public BlacklistSignListener(BlacklistService blacklistService, PermissionHandler permission, Options options) {
        this.blacklistService = blacklistService;
        this.permission = permission;
        this.options = options;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (permission.has(player, permissionBlacklist)) {
            return;
        }
        List<String> originalLines = Arrays.stream(event.getLines()).collect(Collectors.toList());
        List<String> censoredLines = originalLines.stream().map(blacklistService::censorMessage).collect(Collectors.toList());
        boolean hasBeenCensored = !originalLines.equals(censoredLines);
        IntStream.range(0, censoredLines.size()).forEach(i -> event.setLine(i, censoredLines.get(i)));

        if (hasBeenCensored) {
            sendEvent(new BlacklistCensoredEvent(options.serverName, player, String.join(" ", censoredLines), String.join(" ", originalLines), BlacklistType.SIGN));
        }
    }
}