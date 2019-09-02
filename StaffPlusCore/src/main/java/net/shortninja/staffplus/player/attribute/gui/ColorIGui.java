package net.shortninja.staffplus.player.attribute.gui;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.GlassData;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ColorIGui extends AbstractIGui {
    private static final int SIZE = 27;
    private MessageCoordinator message = StaffPlus.get().message;
    private UserManager userManager = StaffPlus.get().userManager;

    public ColorIGui(Player player, String title) {
        super(SIZE, StaffPlus.get().message.colorize(title));

        IAction action = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                userManager.get(player.getUniqueId()).setGlassColor(item.getDurability());
            }

            @Override
            public boolean shouldClose() {
                return true;
            }

            @Override
            public void execute(Player player, String input) {
            }
        };

        for (short i = 0; i < 15; i++) {
            setItem(i, glassItem(i), action);
        }

        player.openInventory(getInventory());
        userManager.get(player.getUniqueId()).setCurrentGui(this);
    }

    private ItemStack glassItem(short data) {
        String[] tmp = Bukkit.getVersion().split("MC: ");
        String version = tmp[tmp.length - 1].substring(0, 4);
        if (!version.contains("1.13")) {
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