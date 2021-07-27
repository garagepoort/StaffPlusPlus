package net.shortninja.staffplus.core.domain.staff.examine.items;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.chests.config.EnderchestsConfiguration;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGuiItemProvider;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@IocBean
@IocMultiProvider(ExamineGuiItemProvider.class)
public class EnderchestExamineGuiProvider implements ExamineGuiItemProvider {

    private final PermissionHandler permissionHandler;
    private final EnderchestsConfiguration enderchestsConfiguration;

    public EnderchestExamineGuiProvider(PermissionHandler permissionHandler, EnderchestsConfiguration enderchestsConfiguration) {
        this.permissionHandler = permissionHandler;
        this.enderchestsConfiguration = enderchestsConfiguration;
    }

    @Override
    public ItemStack getItem(SppPlayer player) {
        return locationItem();
    }

    @Override
    public String getClickAction(Player staff, SppPlayer target) {
        return GuiActionBuilder.builder()
            .action("manage-enderchest/open")
            .param("targetPlayerName", target.getUsername())
            .build();
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        if (player.isOnline()) {
            return permissionHandler.has(staff, enderchestsConfiguration.permissionViewOnline);
        } else {
            return permissionHandler.has(staff, enderchestsConfiguration.permissionViewOffline);
        }
    }

    @Override
    public int getSlot() {
        return 24;
    }

    private ItemStack locationItem() {
        ItemStack item = Items.builder()
            .setMaterial(Material.ENDER_CHEST).setAmount(1)
            .setName("&bPlayer's Enderchest")
            .addLore("View player's Enderchest")
            .build();

        return item;
    }
}
