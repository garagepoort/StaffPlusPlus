package net.shortninja.staffplus.core.common.utils;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.PlayerCommandPreprocess;
import net.shortninja.staffplus.core.domain.player.listeners.*;
import net.shortninja.staffplus.core.domain.staff.alerts.handlers.*;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.AltDetectionListener;
import net.shortninja.staffplus.core.domain.staff.ban.BanListener;
import net.shortninja.staffplus.core.domain.staff.chests.ChestGuiMove;
import net.shortninja.staffplus.core.domain.staff.protect.ProtectListener;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportListener;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.AppealNotifierListener;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarningListener;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarningNotifierListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import static org.bukkit.Bukkit.getScheduler;

public class BukkitUtils {
    public static void sendEvent(Event event) {
        getScheduler().runTask(StaffPlus.get(), () -> {
            Bukkit.getPluginManager().callEvent(event);
        });
    }

    public static int getInventorySize(int amountOfItems) {
        int division = amountOfItems / 9;
        int rest = amountOfItems % 9;
        if (rest != 0) {
            division++;
            return division * 9;
        }
        return amountOfItems;
    }

    public static void initListeners() {
        new EntityDamage();
        new EntityDamageByEntity();
        new EntityTarget();
        new AsyncPlayerChat();
        new PlayerCommandPreprocess();
        new PlayerDeath();
        new PlayerDropItem();
        new PlayerInteract();
        new PlayerJoin();
        new PlayerPickupItem();
        new PlayerQuit();
        new BlockBreak();
        new BlockPlace();
        new FoodLevelChange();
        new InventoryClick();
        new InventoryClose();
        new InventoryOpen();
        new PlayerWorldChange();
        new EntityChangeBlock();
        new ProtectListener();
        new BanListener();
        new AltDetectAlertHandler();
        new NameChangeAlertHandler();
        new ChatPhraseDetectedAlertHandler();
        new PlayerMentionAlertHandler();
        new XrayAlertHandler();
        new AltDetectionListener();
        new WarningNotifierListener();
        new ReportListener();
        new ChestGuiMove();
        new AppealNotifierListener();
        new WarningListener();
    }
}
