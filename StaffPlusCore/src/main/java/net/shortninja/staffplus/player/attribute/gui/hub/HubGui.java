package net.shortninja.staffplus.player.attribute.gui.hub;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.staff.protect.cmd.ProtectedAreasGui;
import net.shortninja.staffplus.staff.reporting.gui.ClosedReportsGui;
import net.shortninja.staffplus.staff.reporting.gui.MyReportsGui;
import net.shortninja.staffplus.staff.reporting.gui.OpenReportsGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HubGui extends AbstractGui {
    private static final int SIZE = 27;
    private final Options options = IocContainer.getOptions();

    public HubGui(Player player, String title) {
        super(SIZE, title);

        if (options.modeGuiReports) {
            setItem(1, reportsItem(), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    new OpenReportsGui(player, options.modeGuiReportsTitle, 0);
                }

                @Override
                public boolean shouldClose() {
                    return false;
                }
            });
            setItem(2, myReportsItem(), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    new MyReportsGui(player, options.modeGuiMyReportsTitle, 0);
                }

                @Override
                public boolean shouldClose() {
                    return false;
                }
            });
            setItem(3, closedReportsItem(), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    new ClosedReportsGui(player, options.modeGuiClosedReportsTitle, 0);
                }

                @Override
                public boolean shouldClose() {
                    return false;
                }
            });
        }

        if (options.modeGuiMiner) {
            setItem(10, minerItem(), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    new MinerGui(player, options.modeGuiMinerTitle);
                }

                @Override
                public boolean shouldClose() {
                    return false;
                }
            });
        }

        if (options.modeGuiProtectedAreas) {
            setItem(19, shieldItem(), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    new ProtectedAreasGui(player, options.modeGuiProtectedAreasTitle, 0);
                }

                @Override
                public boolean shouldClose() {
                    return false;
                }
            });
        }

        PlayerSession playerSession = IocContainer.getSessionManager().get(player.getUniqueId());
        setGlass(playerSession);
        player.openInventory(getInventory());
        playerSession.setCurrentGui(this);
    }

    private ItemStack reportsItem() {
        ItemStack item = Items.builder()
                .setMaterial(Material.PAPER).setAmount(1)
                .setName(options.modeGuiReportsName)
                .addLore(options.modeGuiReportsLore)
                .build();

        return item;
    }
    private ItemStack myReportsItem() {
        ItemStack item = Items.builder()
                .setMaterial(Material.PAPER).setAmount(1)
                .setName(options.modeGuiMyReportsTitle)
                .addLore(options.modeGuiMyReportsLore)
                .build();

        return item;
    }

    private ItemStack closedReportsItem() {
        ItemStack item = Items.builder()
                .setMaterial(Material.PAPER).setAmount(1)
                .setName(options.modeGuiClosedReportsTitle)
                .addLore(options.modeGuiClosedReportsLore)
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

    private ItemStack shieldItem() {
        ItemStack item = Items.builder()
                .setMaterial(Material.SHIELD).setAmount(1)
                .setName(options.modeGuiProtectedAreasName)
                .addLore(options.modeGuiProtectedAreasLore)
                .build();

        return item;
    }
}