package net.shortninja.staffplus.core.domain.player.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.utils.GlassData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ColorGui extends AbstractGui {
    private static final int SIZE = 27;

    public ColorGui(String title) {
        super(SIZE, StaffPlus.get().getIocContainer().get(Messages.class).colorize(title));
    }

    @Override
    public void buildGui() {
        IAction action = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot, ClickType clickType) {
                sessionManager.get(player.getUniqueId()).setGlassColor(item.getType());
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        };

        for (short i = 0; i < 15; i++) {
            setItem(i, glassItem(i), action);
        }
    }

    private ItemStack glassItem(short data) {
        String[] tmp = Bukkit.getVersion().split("MC: ");
        String version = tmp[tmp.length - 1].substring(0, 4);
        if (JavaUtils.parseMcVer(version) < 13) {
            return Items.builder()
                .setMaterial(Material.valueOf("STAINED_GLASS_PANE")).setAmount(1).setData(data)
                .setName("&bColor #" + data)
                .addLore("&7Click to change your GUI color!")
                .build();
        } else {
            return Items.builder()
                .setMaterial(Material.valueOf(GlassData.getName(data)))
                .setName("&bColor #" + data)
                .addLore("&7Click to change your GUI color!")
                .build();
        }

    }
}