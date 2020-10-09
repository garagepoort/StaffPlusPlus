package net.shortninja.staffplus.player.attribute.gui.hub;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.config.GuiItemConfig;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.staff.ban.gui.BannedPlayersGui;
import net.shortninja.staffplus.staff.protect.cmd.ProtectedAreasGui;
import net.shortninja.staffplus.staff.reporting.gui.ClosedReportsGui;
import net.shortninja.staffplus.staff.reporting.gui.MyReportsGui;
import net.shortninja.staffplus.staff.reporting.gui.OpenReportsGui;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

import static org.bukkit.Material.*;

public class HubGui extends AbstractGui {
    private static final int SIZE = 27;
    private final Options options = IocContainer.getOptions();
    private final GuiItemConfig protectGuiItemConfig;
    private final GuiItemConfig banGuiItemConfig;
    private final GuiItemConfig closedReportsGui;
    private final GuiItemConfig myReportsGui;
    private final GuiItemConfig openReportsGui;

    public HubGui(Player player, String title) {
        super(SIZE, title);
        protectGuiItemConfig = options.protectConfiguration.getGuiItemConfig();
        banGuiItemConfig = options.banConfiguration.getGuiItemConfig();
        openReportsGui = options.reportConfiguration.getOpenReportsGui();
        closedReportsGui = options.reportConfiguration.getClosedReportsGui();
        myReportsGui = options.reportConfiguration.getMyReportsGui();

        if (openReportsGui.isEnabled()) {
            setMenuItem(1, buildGuiItem(PAPER, openReportsGui), (p) -> new OpenReportsGui(p, openReportsGui.getTitle(), 0));
            setMenuItem(2, buildGuiItem(PAPER, myReportsGui), (p) -> new MyReportsGui(p, myReportsGui.getTitle(), 0));
            setMenuItem(3, buildGuiItem(PAPER, closedReportsGui), (p) -> new ClosedReportsGui(p, closedReportsGui.getTitle(), 0));
        }

        if (options.modeGuiMiner) {
            setMenuItem(10, minerItem(), (p) -> new MinerGui(player, options.modeGuiMinerTitle));
        }

        if (protectGuiItemConfig.isEnabled()) {
            setMenuItem(19, buildGuiItem(SHIELD, protectGuiItemConfig), (p) -> new ProtectedAreasGui(player, protectGuiItemConfig.getTitle(), 0));
        }

        if (banGuiItemConfig.isEnabled()) {
            setMenuItem(7, buildGuiItem(BANNER, banGuiItemConfig), (p) -> new BannedPlayersGui(player, banGuiItemConfig.getTitle(), 0));
        }

        PlayerSession playerSession = IocContainer.getSessionManager().get(player.getUniqueId());
        setGlass(playerSession);
        player.closeInventory();
        player.openInventory(getInventory());
        playerSession.setCurrentGui(this);
    }

    private void setMenuItem(int menuSlot, ItemStack menuItem, Consumer<Player> guiFunction) {
        setItem(menuSlot, menuItem, new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                guiFunction.accept(player);
            }

            @Override
            public boolean shouldClose() {
                return false;
            }
        });
    }

    private ItemStack minerItem() {
        return Items.builder()
            .setMaterial(Material.STONE_PICKAXE).setAmount(1)
            .setName(options.modeGuiMinerName)
            .addLore(options.modeGuiMinerLore)
            .build();
    }

    private ItemStack buildGuiItem(Material material, GuiItemConfig config) {
        return Items.builder()
            .setMaterial(material).setAmount(1)
            .setName(config.getItemName())
            .addLore(config.getItemLore())
            .build();
    }
}