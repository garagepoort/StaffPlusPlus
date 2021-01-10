package net.shortninja.staffplus.staff.mode.config.modeitems.vanish;

import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.mode.config.ModeItemLoader;
import net.shortninja.staffplus.util.lib.JavaUtils;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class VanishModeItemLoader extends ModeItemLoader<VanishModeConfiguration> {
    @Override
    protected String getModuleName() {
        return "vanish-module";
    }

    @Override
    protected VanishModeConfiguration load(FileConfiguration config) {

        Material modeVanishTypeOff = Options.stringToMaterial(sanitize(config.getString("staff-mode.vanish-module.item-off")));
        short modeVanishDataOff = getMaterialData(config.getString("staff-mode.vanish-module.item-off"));
        String modeVanishName = config.getString("staff-mode.vanish-module.name");
        List<String> modeVanishLore = JavaUtils.stringToList(config.getString("staff-mode.vanish-module.lore"));
        ItemStack modeVanishItemOff = Items.builder().setMaterial(modeVanishTypeOff).setData(modeVanishDataOff).setName(modeVanishName).setLore(modeVanishLore).build();

        VanishModeConfiguration modeItemConfiguration = new VanishModeConfiguration(modeVanishItemOff);
        return super.loadGeneralConfig(config, modeItemConfiguration);
    }
}
