package net.shortninja.staffplus.core.domain.player.gui.hub.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.utils.GlassData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@IocBean
public class ColorViewBuilder {

    private final Options options;

    public ColorViewBuilder(Options options) {
        this.options = options;
    }

    public TubingGui buildGui() {
        TubingGui.Builder builder = new TubingGui.Builder(options.glassTitle, 27);
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