package net.shortninja.staffplus.staff.warn.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.infractions.gui.InfractionGuiProvider;
import net.shortninja.staffplus.staff.warn.Warning;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static net.shortninja.staffplus.util.lib.JavaUtils.formatLines;

public class WarningItemBuilder implements InfractionGuiProvider<Warning> {

    public static ItemStack build(Warning warning) {
        List<String> lore = new ArrayList<String>();

        lore.add("&bId: " + warning.getId());
        if(IocContainer.getOptions().serverSyncConfiguration.isWarningSyncEnabled()) {
            lore.add("&bServer: " + warning.getServerName());
        }
        lore.add("&bSeverity: " + warning.getSeverity());
        lore.add("&bTimeStamp: " + warning.getTimestamp().format(DateTimeFormatter.ofPattern(IocContainer.getOptions().timestampFormat)));
        if (IocContainer.getOptions().warningConfiguration.isShowIssuer()) {
            lore.add("&bIssuer: " + warning.getIssuerName());
        }

        lore.add("&bReason:");
        for (String line : formatLines(warning.getReason(), 30)) {
            lore.add("  &b" + line);
        }

        ItemStack item = Items.editor(Items.createSkull(warning.getName())).setAmount(1)
            .setName("&6Warning")
            .setLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, String.valueOf(warning.getId()));
    }


    @Override
    public String getType() {
        return "WARNING";
    }

    @Override
    public ItemStack getMenuItem(Warning warning) {
        ItemStack itemStack = build(warning);
        itemStack.setType(IocContainer.getOptions().infractionsConfiguration.getWarningsGuiItem());
        return itemStack;
    }
}
