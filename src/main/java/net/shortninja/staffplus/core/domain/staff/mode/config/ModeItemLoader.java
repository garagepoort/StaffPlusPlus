package net.shortninja.staffplus.core.domain.staff.mode.config;

import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.application.config.AbstractConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static net.shortninja.staffplus.core.application.config.Options.stringToMaterial;

public abstract class ModeItemLoader<T extends ModeItemConfiguration> extends AbstractConfigLoader<T> {

    protected final IProtocolService protocolService;

    protected ModeItemLoader(IProtocolService protocolService) {
        this.protocolService = protocolService;
    }

    protected T loadGeneralConfig(T modeItemConfiguration) {
        String prefix = "modules." + getModuleName() + ".";

        boolean enabled = staffModeModulesConfig.getBoolean(prefix + "enabled");
        Material type = stringToMaterial(sanitize(staffModeModulesConfig.getString(prefix + "item")));
        short durability = getMaterialData(staffModeModulesConfig.getString(prefix + "item"));
        String name = staffModeModulesConfig.getString(prefix + "name");
        List<String> lore = JavaUtils.stringToList(staffModeModulesConfig.getString(prefix + "lore"));
        ItemStack item = Items.builder().setMaterial(type).setData(durability).setName(name).setLore(lore).build();

        modeItemConfiguration.setEnabled(enabled);
        item = protocolService.getVersionProtocol().addNbtString(item, modeItemConfiguration.getIdentifier());
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
