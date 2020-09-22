package net.shortninja.staffplus.player.attribute.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ColorGui extends AbstractGui {
    private static final int SIZE = 27;
    private MessageCoordinator message = IocContainer.getMessage();
    private UserManager userManager = IocContainer.getUserManager();

    public ColorGui(Player player, String title) {
        super(SIZE, IocContainer.getMessage().colorize(title));

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
        return Items.builder()
            .setMaterial(Material.STAINED_GLASS_PANE).setAmount(1).setData(data)
            .setName("&bColor #" + data)
            .addLore("&7Click to change your GUI color!")
            .build();

    }
}