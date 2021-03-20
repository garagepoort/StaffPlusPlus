package net.shortninja.staffplus.domain.player.gui.hub;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.gui.GuiItemConfig;
import net.shortninja.staffplus.common.gui.AbstractGui;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.domain.staff.ban.gui.BannedPlayersGui;
import net.shortninja.staffplus.domain.staff.mode.config.modeitems.gui.GuiModeConfiguration;
import net.shortninja.staffplus.domain.staff.mute.gui.MutedPlayersGui;
import net.shortninja.staffplus.domain.staff.protect.cmd.ProtectedAreasGui;
import net.shortninja.staffplus.domain.staff.reporting.gui.AllAssignedReportsGui;
import net.shortninja.staffplus.domain.staff.reporting.gui.MyAssignedReportsGui;
import net.shortninja.staffplus.domain.staff.reporting.gui.ClosedReportsGui;
import net.shortninja.staffplus.domain.staff.reporting.gui.OpenReportsGui;
import net.shortninja.staffplus.common.gui.IAction;
import net.shortninja.staffplus.common.utils.PermissionHandler;
import net.shortninja.staffplus.common.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

import static org.bukkit.Material.*;
import static org.bukkit.Material.SPRUCE_SIGN;

public class HubGui extends AbstractGui {
    private static final int SIZE = 27;
    private final Options options;
    private final GuiItemConfig protectGuiItemConfig;
    private final GuiItemConfig banGuiItemConfig;
    private final GuiItemConfig muteGuiItemConfig;
    private final GuiItemConfig closedReportsGui;
    private final GuiItemConfig myAssignedReportsGui;
    private final GuiItemConfig assignedReportsGui;
    private final GuiItemConfig openReportsGui;
    private final GuiModeConfiguration guiModeConfiguration;
    private final Player player;

    public HubGui(Player player, String title) {
        super(SIZE, title);
        this.player = player;
        options = IocContainer.getOptions();
        protectGuiItemConfig = options.protectConfiguration.getGuiItemConfig();
        banGuiItemConfig = options.banConfiguration.getGuiItemConfig();
        muteGuiItemConfig = options.muteConfiguration.getGuiItemConfig();
        openReportsGui = options.reportConfiguration.getOpenReportsGui();
        closedReportsGui = options.reportConfiguration.getClosedReportsGui();
        myAssignedReportsGui = options.reportConfiguration.getMyReportsGui();
        assignedReportsGui = options.reportConfiguration.getAssignedReportsGui();
        guiModeConfiguration = options.modeConfiguration.getGuiModeConfiguration();
    }

    @Override
    public void buildGui() {
        PermissionHandler permissionHandler = IocContainer.getPermissionHandler();
        if (openReportsGui.isEnabled() && permissionHandler.has(player, options.manageReportConfiguration.getPermissionView())) {
            setMenuItem(1, buildGuiItem(PAPER, openReportsGui), p -> new OpenReportsGui(p, openReportsGui.getTitle(), 0, () -> new HubGui(player, getTitle())).show(p));
            setMenuItem(2, buildGuiItem(PAPER, myAssignedReportsGui), p -> new MyAssignedReportsGui(p, myAssignedReportsGui.getTitle(), 0, () -> new HubGui(player, getTitle())).show(p));
            setMenuItem(3, buildGuiItem(PAPER, assignedReportsGui), p -> new AllAssignedReportsGui(p, assignedReportsGui.getTitle(), 0, () -> new HubGui(player, getTitle())).show(p));
            setMenuItem(4, buildGuiItem(PAPER, closedReportsGui), p -> new ClosedReportsGui(p, closedReportsGui.getTitle(), 0, () -> new HubGui(player, getTitle())).show(p));
        }

        if (guiModeConfiguration.modeGuiMiner) {
            setMenuItem(10, minerItem(), p -> new MinerGui(player, guiModeConfiguration.modeGuiMinerTitle, 0, () -> new HubGui(player, getTitle())).show(p));
        }

        if (protectGuiItemConfig.isEnabled()) {
            setMenuItem(19, buildGuiItem(SHIELD, protectGuiItemConfig), p -> new ProtectedAreasGui(player, protectGuiItemConfig.getTitle(), 0, () -> new HubGui(player, getTitle())).show(p));
        }

        if (banGuiItemConfig.isEnabled()) {
            setMenuItem(7, buildGuiItem(PLAYER_HEAD, banGuiItemConfig), p -> new BannedPlayersGui(player, banGuiItemConfig.getTitle(), 0, () -> new HubGui(player, getTitle())).show(p));
        }

        if (muteGuiItemConfig.isEnabled()) {
            setMenuItem(16, buildGuiItem(SPRUCE_SIGN, muteGuiItemConfig), p -> new MutedPlayersGui(player, muteGuiItemConfig.getTitle(), 0, () -> new HubGui(player, getTitle())).show(p));
        }

        PlayerSession playerSession = IocContainer.getSessionManager().get(player.getUniqueId());
        setGlass(playerSession);
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