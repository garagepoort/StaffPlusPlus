package net.shortninja.staffplus.core.examine.items;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubinggui.model.TubingGuiActions;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.examine.gui.ExamineGuiItemProvider;
import net.shortninja.staffplus.core.mode.config.StaffModeOptions;
import net.shortninja.staffplus.core.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@IocBean
@IocMultiProvider(ExamineGuiItemProvider.class)
public class IpExamineGuiProvider implements ExamineGuiItemProvider {

    @ConfigProperty("permissions:ipPerm")
    private String ipHidePerm;
    @ConfigProperty("permissions:examine-view-ip")
    private String viewIpPermission;

    private final Messages messages;
    private final ExamineModeConfiguration examineModeConfiguration;
    private final PermissionHandler permissionHandler;

    public IpExamineGuiProvider(Messages messages, StaffModeOptions options, PermissionHandler permissionHandler) {
        this.messages = messages;
        this.permissionHandler = permissionHandler;
        examineModeConfiguration = options.staffItemsConfiguration.getExamineModeConfiguration();
    }

    @Override
    public ItemStack getItem(Player player1, SppPlayer player) {
        return ipItem(player.getPlayer());
    }

    @Override
    public String getClickAction(Player staff, SppPlayer targetPlayer) {
        return TubingGuiActions.NOOP;
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        return examineModeConfiguration.getModeExamineIp() >= 0 && player.isOnline() && permissionHandler.has(staff, viewIpPermission);
    }

    @Override
    public int getSlot() {
        return examineModeConfiguration.getModeExamineIp() - 1;
    }

    private ItemStack ipItem(Player player) {
        String ip = permissionHandler.has(player, ipHidePerm) ? "127.0.0.1" : player.getAddress().getAddress().getHostAddress().replace("/", "");

        return Items.builder()
            .setMaterial(Material.COMPASS).setAmount(1)
            .setName("&bConnection")
            .addLore(messages.examineIp.replace("%ipaddress%", ip))
            .build();
    }

}
