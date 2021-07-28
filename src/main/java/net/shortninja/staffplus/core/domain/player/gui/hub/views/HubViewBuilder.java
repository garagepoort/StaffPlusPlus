package net.shortninja.staffplus.core.domain.player.gui.hub.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.gui.GuiItemConfig;
import net.shortninja.staffplus.core.common.gui.IGuiItemConfig;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.config.BanConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.gui.GuiModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mute.config.MuteConfiguration;
import net.shortninja.staffplus.core.domain.staff.reporting.config.ManageReportConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static be.garagepoort.mcioc.gui.TubingGuiActions.NOOP;
import static org.bukkit.Material.BANNER;
import static org.bukkit.Material.BOOK;
import static org.bukkit.Material.PAPER;
import static org.bukkit.Material.SHIELD;
import static org.bukkit.Material.SIGN;

@IocBean
public class HubViewBuilder {

    @ConfigProperty("staffmode-modules:modules.gui-module.name")
    private String title;

    private final PermissionHandler permissionHandler;
    private final GuiItemConfig protectGuiItemConfig;
    private final IGuiItemConfig banGuiItemConfig;
    private final IGuiItemConfig muteGuiItemConfig;
    private final IGuiItemConfig investigationGuiItemConfig;
    private final IGuiItemConfig closedReportsGui;
    private final IGuiItemConfig myAssignedReportsGui;
    private final IGuiItemConfig assignedReportsGui;
    private final IGuiItemConfig openReportsGui;
    private final GuiModeConfiguration guiModeConfiguration;
    private final ManageReportConfiguration manageReportConfiguration;
    private final SessionManagerImpl sessionManager;
    private final Messages messages;

    public HubViewBuilder(BanConfiguration banConfiguration, PermissionHandler permissionHandler, MuteConfiguration muteConfiguration, ManageReportConfiguration manageReportConfiguration, Options options, SessionManagerImpl sessionManager, Messages messages) {
        this.permissionHandler = permissionHandler;
        this.manageReportConfiguration = manageReportConfiguration;
        this.sessionManager = sessionManager;
        this.messages = messages;
        banGuiItemConfig = banConfiguration.banGuiItemConfig;
        muteGuiItemConfig = muteConfiguration.guiItemConfig;
        protectGuiItemConfig = options.protectConfiguration.getGuiItemConfig();
        investigationGuiItemConfig = options.investigationConfiguration.getGuiItemConfig();
        openReportsGui = options.reportConfiguration.getOpenReportsGui();
        closedReportsGui = options.reportConfiguration.getClosedReportsGui();
        myAssignedReportsGui = options.reportConfiguration.getMyAssignedReportsGui();
        assignedReportsGui = options.reportConfiguration.getAssignedReportsGui();
        guiModeConfiguration = options.staffItemsConfiguration.getGuiModeConfiguration();
    }


    public TubingGui buildGui(Player player) {
        TubingGui.Builder builder = new TubingGui.Builder(messages.colorize(title), 27);

        if (openReportsGui.isEnabled() && permissionHandler.has(player, manageReportConfiguration.permissionView)) {
            builder.addItem(getAction("manage-reports/view/open"), 1, buildGuiItem(PAPER, openReportsGui));
            builder.addItem(getAction("manage-reports/view/my-assigned"), 2, buildGuiItem(PAPER, myAssignedReportsGui));
            builder.addItem(getAction("manage-reports/view/assigned"), 3, buildGuiItem(PAPER, assignedReportsGui));
            builder.addItem(getAction("manage-reports/view/closed"), 4, buildGuiItem(PAPER, closedReportsGui));
        }

        if (guiModeConfiguration.modeGuiMiner) {
            builder.addItem(getAction("miners/view"), 10, minerItem());
        }

        if (protectGuiItemConfig.isEnabled()) {
            builder.addItem(getAction("protected-areas/view"), 19, buildGuiItem(SHIELD, protectGuiItemConfig));
        }

        if (banGuiItemConfig.isEnabled()) {
            builder.addItem(getAction("manage-bans/view/overview"), 7, buildGuiItem(BANNER, banGuiItemConfig));
        }

        if (muteGuiItemConfig.isEnabled()) {
            builder.addItem(getAction("manage-mutes/view/overview"), 16, buildGuiItem(SIGN, muteGuiItemConfig));
        }

        if (investigationGuiItemConfig.isEnabled()) {
            builder.addItem(getAction("manage-investigations/view/overview"), 25, buildGuiItem(BOOK, investigationGuiItemConfig));
        }

        PlayerSession playerSession = sessionManager.get(player.getUniqueId());
        setGlass(playerSession, builder);
        return builder.build();
    }

    private String getAction(String basicAction) {
        return GuiActionBuilder.builder().action(basicAction)
            .param("backAction", "hub/view")
            .build();
    }

    public void setGlass(PlayerSession user, TubingGui.Builder builder) {
        ItemStack item = glassItem(user.getGlassColor());

        for (int i = 0; i < 3; i++) {
            int slot = 9 * i;
            builder.addItem(NOOP, slot, item);
            builder.addItem(NOOP, slot + 8, item);
        }
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

    private ItemStack glassItem(Material data) {
        return Items.builder()
            .setMaterial(data)
            .setName("&bColor #" + data)
            .addLore("&7Click to change your GUI color!")
            .build();
    }
}
