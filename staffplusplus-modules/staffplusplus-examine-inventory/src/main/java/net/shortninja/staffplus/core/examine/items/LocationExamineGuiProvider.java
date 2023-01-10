package net.shortninja.staffplus.core.examine.items;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.tubinggui.GuiActionBuilder;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.examine.config.ExamineConfiguration;
import net.shortninja.staffplus.core.examine.gui.ExamineGuiItemProvider;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@IocBean
@IocMultiProvider(ExamineGuiItemProvider.class)
public class LocationExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;
    private final ExamineConfiguration examineConfiguration;

    public LocationExamineGuiProvider(Messages messages, ExamineConfiguration examineConfiguration) {
        this.messages = messages;
        this.examineConfiguration = examineConfiguration;
    }

    @Override
    public ItemStack getItem(Player player1, SppPlayer player) {
        return locationItem(player.getPlayer());
    }

    @Override
    public String getClickAction(Player staff, SppPlayer target) {
        return GuiActionBuilder.builder()
            .action("teleport")
            .param("targetPlayerName", target.getUsername())
            .build();
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        return examineConfiguration.modeExamineLocation >= 0 && player.isOnline();
    }

    @Override
    public int getSlot() {
        return examineConfiguration.modeExamineLocation - 1;
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
