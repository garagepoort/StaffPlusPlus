package net.shortninja.staffplus.core.domain.player.gui.hub;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.GuiItemConfig;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.gui.IGuiItemConfig;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.config.BanConfiguration;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.gui.BannedPlayersGui;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.InvestigationGuiComponent;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.gui.GuiModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mute.gui.MutedPlayersGui;
import net.shortninja.staffplus.core.domain.staff.protect.cmd.ProtectedAreasGui;
import net.shortninja.staffplus.core.domain.staff.reporting.gui.AllAssignedReportsGui;
import net.shortninja.staffplus.core.domain.staff.reporting.gui.ClosedReportsGui;
import net.shortninja.staffplus.core.domain.staff.reporting.gui.MyAssignedReportsGui;
import net.shortninja.staffplus.core.domain.staff.reporting.gui.OpenReportsGui;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

import static org.bukkit.Material.*;

public class HubGui extends AbstractGui {
    private static final int SIZE = 27;
    private final Options options;
    private final BanConfiguration banConfiguration;
    private final GuiItemConfig protectGuiItemConfig;
    private final IGuiItemConfig banGuiItemConfig;
    private final IGuiItemConfig muteGuiItemConfig;
    private final IGuiItemConfig investigationGuiItemConfig;
    private final IGuiItemConfig closedReportsGui;
    private final IGuiItemConfig myAssignedReportsGui;
    private final IGuiItemConfig assignedReportsGui;
    private final IGuiItemConfig openReportsGui;
    private final GuiModeConfiguration guiModeConfiguration;
    private final Player player;

    private final InvestigationGuiComponent investigationGuiComponent = StaffPlus.get().getIocContainer().get(InvestigationGuiComponent.class);

    public HubGui(Player player, String title, BanConfiguration banConfiguration) {
        super(SIZE, title);
        this.player = player;
        this.banConfiguration = banConfiguration;
        options = StaffPlus.get().getIocContainer().get(Options.class);
        protectGuiItemConfig = options.protectConfiguration.getGuiItemConfig();
        banGuiItemConfig = banConfiguration.getGuiItemConfig();
        muteGuiItemConfig = options.muteConfiguration.getGuiItemConfig();
        investigationGuiItemConfig = options.investigationConfiguration.getGuiItemConfig();
        openReportsGui = options.reportConfiguration.getOpenReportsGui();
        closedReportsGui = options.reportConfiguration.getClosedReportsGui();
        myAssignedReportsGui = options.reportConfiguration.getMyReportsGui();
        assignedReportsGui = options.reportConfiguration.getAssignedReportsGui();
        guiModeConfiguration = options.staffItemsConfiguration.getGuiModeConfiguration();
    }

    @Override
    public void buildGui() {
        PermissionHandler permissionHandler = StaffPlus.get().getIocContainer().get(PermissionHandler.class);
        if (openReportsGui.isEnabled() && permissionHandler.has(player, options.manageReportConfiguration.getPermissionView())) {
            setMenuItem(1, buildGuiItem(PAPER, openReportsGui), p -> new OpenReportsGui(p, openReportsGui.getTitle(), 0, () -> new HubGui(player, getTitle(), banConfiguration)).show(p));
            setMenuItem(2, buildGuiItem(PAPER, myAssignedReportsGui), p -> new MyAssignedReportsGui(p, myAssignedReportsGui.getTitle(), 0, () -> new HubGui(player, getTitle(), banConfiguration)).show(p));
            setMenuItem(3, buildGuiItem(PAPER, assignedReportsGui), p -> new AllAssignedReportsGui(p, assignedReportsGui.getTitle(), 0, () -> new HubGui(player, getTitle(), banConfiguration)).show(p));
            setMenuItem(4, buildGuiItem(PAPER, closedReportsGui), p -> new ClosedReportsGui(p, closedReportsGui.getTitle(), 0, () -> new HubGui(player, getTitle(), banConfiguration)).show(p));
        }

        if (guiModeConfiguration.modeGuiMiner) {
            setMenuItem(10, minerItem(), p -> new MinerGui(player, guiModeConfiguration.modeGuiMinerTitle, 0, () -> new HubGui(player, getTitle(), banConfiguration)).show(p));
        }

        if (protectGuiItemConfig.isEnabled()) {
            setMenuItem(19, buildGuiItem(SHIELD, protectGuiItemConfig), p -> new ProtectedAreasGui(player, protectGuiItemConfig.getTitle(), 0, () -> new HubGui(player, getTitle(), banConfiguration)).show(p));
        }

        if (banGuiItemConfig.isEnabled()) {
            setMenuItem(7, buildGuiItem(BANNER, banGuiItemConfig), p -> new BannedPlayersGui(player, banGuiItemConfig.getTitle(), 0, () -> new HubGui(player, getTitle(), banConfiguration)).show(p));
        }

        if (muteGuiItemConfig.isEnabled()) {
            setMenuItem(16, buildGuiItem(SIGN, muteGuiItemConfig), p -> new MutedPlayersGui(player, muteGuiItemConfig.getTitle(), 0, () -> new HubGui(player, getTitle(), banConfiguration)).show(p));
        }

        if (investigationGuiItemConfig.isEnabled()) {
            setMenuItem(25, buildGuiItem(BOOK, investigationGuiItemConfig), p -> investigationGuiComponent.openManageInvestigationsGui(p, null, () -> new HubGui(player, getTitle(), banConfiguration)));
        }

        PlayerSession playerSession = StaffPlus.get().getIocContainer().get(SessionManagerImpl.class).get(player.getUniqueId());
        setGlass(playerSession);
    }

    private void setMenuItem(int menuSlot, ItemStack menuItem, Consumer<Player> guiFunction) {
        setItem(menuSlot, menuItem, new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot, ClickType clickType) {
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

    private ItemStack buildGuiItem(Material material, IGuiItemConfig config) {
        return Items.builder()
            .setMaterial(material).setAmount(1)
            .setName(config.getItemName())
            .addLore(config.getItemLore())
            .build();
    }

}