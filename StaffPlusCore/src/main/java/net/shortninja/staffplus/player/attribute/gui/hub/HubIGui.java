package net.shortninja.staffplus.player.attribute.gui.hub;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.gui.AbstractIGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HubIGui extends AbstractIGui {
    private static final int SIZE = 27;
    private Options options = StaffPlus.get().options;
    private UserManager userManager = StaffPlus.get().userManager;

    public HubIGui(Player player, String title) {
        super(SIZE, title);

        IUser user = userManager.get(player.getUniqueId());

        if (options.modeGuiReports) {
            setItem(options.modeGuiMiner ? 12 : 13, reportsItem(), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    new ReportsIGui(player, options.modeGuiReportsTitle);
                }

                @Override
                public boolean shouldClose() {
                    return false;
                }

                @Override
                public void execute(Player player, String input) {
                }
            });
        }

        if (options.modeGuiMiner) {
            setItem(options.modeGuiReports ? 14 : 13, minerItem(), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    new MinerIGui(player, options.modeGuiMinerTitle);
                }

                @Override
                public boolean shouldClose() {
                    return false;
                }

                @Override
                public void execute(Player player, String input) {
                }
            });
        }

        setGlass(user);
        player.openInventory(getInventory());
        user.setCurrentGui(this);
    }

    private ItemStack reportsItem() {
        ItemStack item = Items.builder()
                .setMaterial(Material.PAPER).setAmount(1)
                .setName(options.modeGuiReportsName)
                .addLore(options.modeGuiReportsLore)
                .build();

        return item;
    }

    private ItemStack minerItem() {
        ItemStack item = Items.builder()
                .setMaterial(Material.STONE_PICKAXE).setAmount(1)
                .setName(options.modeGuiMinerName)
                .addLore(options.modeGuiMinerLore)
                .build();

        return item;
    }
}