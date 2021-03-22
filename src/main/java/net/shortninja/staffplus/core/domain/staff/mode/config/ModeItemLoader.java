package net.shortninja.staffplus.core.domain.staff.mode.config;

import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.config.ConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static net.shortninja.staffplus.core.common.config.Options.stringToMaterial;

public abstract class ModeItemLoader<T extends ModeItemConfiguration> extends ConfigLoader<T> {

    protected T loadGeneralConfig(FileConfiguration config, T modeItemConfiguration) {
        String prefix = "staff-mode." + getModuleName() + ".";

        boolean enabled = config.getBoolean(prefix + "enabled");
        int slot = config.getInt(prefix + "slot") - 1;
        Material type = stringToMaterial(sanitize(config.getString(prefix + "item")));
        short durability = getMaterialData(config.getString(prefix + "item"));
        String name = config.getString(prefix + "name");
        List<String> lore = JavaUtils.stringToList(config.getString(prefix + "lore"));
        ItemStack item = Items.builder().setMaterial(type).setData(durability).setName(name).setLore(lore).build();

        modeItemConfiguration.setEnabled(enabled);
        modeItemConfiguration.setSlot(slot);
        modeItemConfiguration.setItem(item);

        return modeItemConfiguration;
    }

    protected short getMaterialData(String string) {
        short data = 0;

        if (string.contains(":")) {
            String dataString = string.substring(string.lastIndexOf(':') + 1);

            if (JavaUtils.isInteger(dataString)) {
                data = (short) Integer.parseInt(dataString);
            } else
                Bukkit.getLogger().severe("Invalid material data '" + dataString + "' from '" + string + "'!");
        }

        return data;
    }

    protected abstract String getModuleName();
}
