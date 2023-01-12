package net.shortninja.staffplus.core.blacklist.listeners;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.blacklist.BlacklistService;
import net.shortninja.staffplusplus.blacklist.BlacklistCensoredEvent;
import net.shortninja.staffplusplus.blacklist.BlacklistType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBukkitListener(conditionalOnProperty = "blacklist-module.enabled=true && blacklist-module.censor-book=true")
public class BlacklistBookListener implements Listener {

    @ConfigProperty("permissions:blacklist")
    private String permissionBlacklist;

    private final BlacklistService blacklistService;
    private final PermissionHandler permission;
    private final Options options;

    public BlacklistBookListener(BlacklistService blacklistService, PermissionHandler permission, Options options) {
        this.blacklistService = blacklistService;
        this.permission = permission;
        this.options = options;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void bookListener(PlayerEditBookEvent event) {
        Player player = event.getPlayer();
        if (permission.has(player, permissionBlacklist)) {
            return;
        }
        List<String> originalText = new ArrayList<>();
        List<String> censoredText = new ArrayList<>();
        boolean censored = false;

        BookMeta newBookMeta = event.getNewBookMeta();
        String originalTitle = newBookMeta.getTitle();
        if (originalTitle != null) {
            String censoredTitle = blacklistService.censorMessage(originalTitle);
            newBookMeta.setTitle(censoredTitle);
            originalText.add(originalTitle);
            censoredText.add(censoredTitle);
            censored = !originalTitle.equals(censoredTitle);
        }

        for (int i = 1; i <= newBookMeta.getPages().size(); i++) {
            String originalPage = newBookMeta.getPage(i);
            String censoredPage = blacklistService.censorMessage(originalPage);
            newBookMeta.setPage(i, censoredPage);
            originalText.add(originalPage);
            censoredText.add(censoredPage);
            censored = censored || !originalPage.equals(censoredPage);
        }

        if (censored) {
            event.setNewBookMeta(newBookMeta);
            sendEvent(new BlacklistCensoredEvent(options.serverName, player, String.join(" ", censoredText), String.join(" ", originalText), BlacklistType.BOOK));
        }
    }

}