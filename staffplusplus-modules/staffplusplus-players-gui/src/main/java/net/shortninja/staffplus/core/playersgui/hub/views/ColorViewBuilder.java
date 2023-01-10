package net.shortninja.staffplus.core.playersgui.hub.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubinggui.model.TubingGui;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.utils.GlassData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@IocBean
public class ColorViewBuilder {

    @ConfigProperty("glass-title")
    private String glassTitle;

    public TubingGui buildGui() {
        TubingGui.Builder builder = new TubingGui.Builder(glassTitle, 27);
        for (short i = 0; i < 15; i++) {
            ItemStack itemStack = glassItem(i);
            builder.addItem("hub/change-color?color=" + itemStack.getType().name(), i, itemStack);
        }
        return builder.build();
    }

    private ItemStack glassItem(short data) {
        return Items.builder()
            .setMaterial(Material.valueOf(GlassData.getName(data)))
            .setName("&bColor #" + data)
            .addLore("&7Click to change your GUI color!")
            .build();
    }
}