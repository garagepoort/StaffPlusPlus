package net.shortninja.staffplus.core.domain.staff.examine.items;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.examine.config.ExamineConfiguration;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGuiItemProvider;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@IocBean
@IocMultiProvider(ExamineGuiItemProvider.class)
public class InventoryExamineGuiProvider implements ExamineGuiItemProvider {

    private final PermissionHandler permissionHandler;
    private final ExamineConfiguration examineConfiguration;

    public InventoryExamineGuiProvider(PermissionHandler permissionHandler, ExamineConfiguration examineConfiguration) {
        this.permissionHandler = permissionHandler;
        this.examineConfiguration = examineConfiguration;
    }

    @Override
    public ItemStack getItem(Player player1, SppPlayer player) {
        return locationItem();
    }

    @Override
    public String getClickAction(Player staff, SppPlayer target) {
        return GuiActionBuilder.builder()
            .action("manage-inventory/open")
            .param("targetPlayerName", target.getUsername())
            .build();
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        if (player.isOnline()) {
            return permissionHandler.has(staff, examineConfiguration.getPermissionExamineViewInventory());
        } else {
            return permissionHandler.has(staff, examineConfiguration.getPermissionExamineViewInventoryOffline());
        }
    }

    @Override
    public int getSlot() {
        return 20;
    }

    private ItemStack locationItem() {
        return Items.builder()
            .setMaterial(Material.CHEST).setAmount(1)
            .setName("&bPlayer's inventory")
            .addLore("View player's inventory")
            .build();
    }
}
