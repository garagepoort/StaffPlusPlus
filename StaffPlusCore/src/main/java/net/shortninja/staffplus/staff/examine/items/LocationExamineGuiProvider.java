package net.shortninja.staffplus.staff.examine.items;

import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.examine.ExamineGui;
import net.shortninja.staffplus.staff.examine.ExamineGuiItemProvider;
import net.shortninja.staffplus.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplus.common.gui.IAction;
import net.shortninja.staffplus.common.JavaUtils;
import net.shortninja.staffplus.common.Items;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LocationExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;
    private final ExamineModeConfiguration examineModeConfiguration;
    private final Options options;

    public LocationExamineGuiProvider(Messages messages, Options options) {
        this.messages = messages;
        this.options = options;
        examineModeConfiguration = this.options.modeConfiguration.getExamineModeConfiguration();
    }

    @Override
    public ItemStack getItem(SppPlayer player) {
        return locationItem(player.getPlayer());
    }

    @Override
    public IAction getClickAction(ExamineGui examineGui, Player staff, SppPlayer target) {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                staff.teleport(target.getPlayer());
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        };
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        return examineModeConfiguration.getModeExamineLocation() >= 0 && player.isOnline();
    }

    @Override
    public int getSlot() {
        return examineModeConfiguration.getModeExamineLocation() - 1;
    }

    private ItemStack locationItem(Player player) {
        Location location = player.getLocation();

        ItemStack item = Items.builder()
            .setMaterial(Material.MAP).setAmount(1)
            .setName("&bLocation")
            .addLore(messages.examineLocation.replace("%location%", location.getWorld().getName() + " &8 | &7" + JavaUtils.serializeLocation(location)))
            .build();

        return item;
    }
}
