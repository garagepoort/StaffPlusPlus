package net.shortninja.staffplus.core.domain.staff.examine.items;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGuiItemProvider;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@IocBean
@IocMultiProvider(ExamineGuiItemProvider.class)
public class LocationExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;
    private final ExamineModeConfiguration examineModeConfiguration;

    public LocationExamineGuiProvider(Messages messages, Options options) {
        this.messages = messages;
        examineModeConfiguration = options.staffItemsConfiguration.getExamineModeConfiguration();
    }

    @Override
    public ItemStack getItem(SppPlayer player) {
        return locationItem(player.getPlayer());
    }

    @Override
    public String getClickAction(Player staff, SppPlayer target, String backAction) {
        return GuiActionBuilder.builder()
            .action("teleport")
            .param("targetPlayerName", target.getUsername())
            .build();
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
