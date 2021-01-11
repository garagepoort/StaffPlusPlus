package net.shortninja.staffplus.player.attribute.gui.hub;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.config.GuiItemConfig;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.staff.ban.gui.BannedPlayersGui;
import net.shortninja.staffplus.staff.mode.config.modeitems.gui.GuiModeConfiguration;
import net.shortninja.staffplus.staff.mute.gui.MutedPlayersGui;
import net.shortninja.staffplus.staff.protect.cmd.ProtectedAreasGui;
import net.shortninja.staffplus.staff.reporting.gui.ClosedReportsGui;
import net.shortninja.staffplus.staff.reporting.gui.AssignedReportsGui;
import net.shortninja.staffplus.staff.reporting.gui.OpenReportsGui;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

import static org.bukkit.Material.*;

public class HubGui extends AbstractGui {
    private static final int SIZE = 27;
    private final Options options;
    private final GuiItemConfig protectGuiItemConfig;
    private final GuiItemConfig banGuiItemConfig;
    private final GuiItemConfig muteGuiItemConfig;
    private final GuiItemConfig closedReportsGui;
    private final GuiItemConfig assignedReportsGui;
    private final GuiItemConfig openReportsGui;
    private final GuiModeConfiguration guiModeConfiguration;

    public HubGui(Player player, String title) {
        super(SIZE, title);
        options = IocContainer.getOptions();
        protectGuiItemConfig = options.protectConfiguration.getGuiItemConfig();
        banGuiItemConfig = options.banConfiguration.getGuiItemConfig();
        muteGuiItemConfig = options.muteConfiguration.getGuiItemConfig();
        openReportsGui = options.reportConfiguration.getOpenReportsGui();
        closedReportsGui = options.reportConfiguration.getClosedReportsGui();
        assignedReportsGui = options.reportConfiguration.getMyAssignedReportsGui();
        guiModeConfiguration = options.modeConfiguration.getGuiModeConfiguration();

        PermissionHandler permissionHandler = IocContainer.getPermissionHandler();
        if (openReportsGui.isEnabled() && permissionHandler.has(player, options.manageReportConfiguration.getPermissionView())) {
            setMenuItem(1, buildGuiItem(PAPER, openReportsGui), (p) -> new OpenReportsGui(p, openReportsGui.getTitle(), 0, () -> new HubGui(player, title)));
            setMenuItem(2, buildGuiItem(PAPER, assignedReportsGui), (p) -> new AssignedReportsGui(p, assignedReportsGui.getTitle(), 0, () -> new HubGui(player, title)));
            setMenuItem(3, buildGuiItem(PAPER, closedReportsGui), (p) -> new ClosedReportsGui(p, closedReportsGui.getTitle(), 0, () -> new HubGui(player, title)));
        }

        if (guiModeConfiguration.modeGuiMiner) {
            setMenuItem(10, minerItem(), (p) -> new MinerGui(player, guiModeConfiguration.modeGuiMinerTitle));
        }

        if (protectGuiItemConfig.isEnabled()) {
            setMenuItem(19, buildGuiItem(SHIELD, protectGuiItemConfig), (p) -> new ProtectedAreasGui(player, protectGuiItemConfig.getTitle(), 0, () -> new HubGui(player, title)));
        }

        if (banGuiItemConfig.isEnabled()) {
            setMenuItem(7, buildGuiItem(BANNER, banGuiItemConfig), (p) -> new BannedPlayersGui(player, banGuiItemConfig.getTitle(), 0, () -> new HubGui(player, title)));
        }

        if (muteGuiItemConfig.isEnabled()) {
            setMenuItem(16, buildGuiItem(SIGN, muteGuiItemConfig), (p) -> new MutedPlayersGui(player, muteGuiItemConfig.getTitle(), 0, () -> new HubGui(player, title)));
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
            public boolean shouldClose(Player player) {
                return false;
            }
        });
    }

    private ItemStack minerItem() {
        return Items.builder()
            .setMaterial(Material.STONE_PICKAXE).setAmount(1)
            .setName(guiModeConfiguration.modeGuiMinerName)
            .addLore(guiModeConfiguration.modeGuiMinerLore)
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