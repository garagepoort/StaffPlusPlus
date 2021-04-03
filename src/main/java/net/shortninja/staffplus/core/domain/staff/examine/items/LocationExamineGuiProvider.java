package net.shortninja.staffplus.core.domain.staff.examine.items;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGui;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGuiItemProvider;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@IocBean
@IocMultiProvider(ExamineGuiItemProvider.class)
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
            public void click(Player player, ItemStack item, int slot, ClickType clickType) {
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
