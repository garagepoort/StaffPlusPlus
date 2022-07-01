package net.shortninja.staffplus.core.domain.blacklist.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.blacklist.BlacklistService;
import net.shortninja.staffplusplus.blacklist.BlacklistCensoredEvent;
import net.shortninja.staffplusplus.blacklist.BlacklistType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBukkitListener(conditionalOnProperty = "blacklist-module.enabled=true && blacklist-module.censor-anvil=true")
public class BlacklistAnvilListener implements Listener {

    @ConfigProperty("permissions:blacklist")
    private String permissionBlacklist;

    private final BlacklistService blacklistService;
    private final PermissionHandler permission;
    private final Options options;

    public BlacklistAnvilListener(BlacklistService blacklistService, PermissionHandler permission, Options options) {
        this.blacklistService = blacklistService;
        this.permission = permission;
        this.options = options;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void anvilRenameListener(InventoryClickEvent event) {
        if (!(event.getInventory().getType().equals(InventoryType.ANVIL)) || event.getSlot() != 2) {
            return;
        }

        HumanEntity entity = event.getWhoClicked();
        if(!(entity instanceof Player) || permission.has(entity, permissionBlacklist)) {
            return;
        }

        Player player = (Player) entity;
        AnvilInventory anvilInventory = (AnvilInventory) event.getInventory();
        ItemStack item = anvilInventory.getItem(0);
        ItemStack newItem = anvilInventory.getItem(2);

        if (item != null && newItem != null && nameIsChanged(anvilInventory, item)) {
            String nameCensored = blacklistService.censorMessage(anvilInventory.getRenameText());
            if(nameCensored.equals(anvilInventory.getRenameText())) {
                return;
            }
            ItemMeta resultMeta = newItem.getItemMeta();
            resultMeta.setDisplayName(nameCensored);
            newItem.setItemMeta(resultMeta);
            sendEvent(new BlacklistCensoredEvent(options.serverName, player, nameCensored, anvilInventory.getRenameText(), BlacklistType.ANVIL));
        }
    }

    private boolean nameIsChanged(AnvilInventory anvilInventory, ItemStack item) {
        if(StringUtils.isEmpty(anvilInventory.getRenameText())) {
            return false;
        }
        String oldName = getOldName(item);
        return !anvilInventory.getRenameText().equals(oldName);
    }

    @NotNull
    private String getOldName(ItemStack item) {
        if (item.getItemMeta() == null) {
            return "";
        }
        return item.getItemMeta().getDisplayName();
    }
}