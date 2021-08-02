package net.shortninja.staffplus.core.domain.staff.mode.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.AbstractConfigLoader;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.confirmation.ConfirmationConfig;
import net.shortninja.staffplus.core.domain.staff.mode.item.ConfirmationType;
import net.shortninja.staffplus.core.domain.staff.mode.item.CustomModuleConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@IocBean
public class StaffCustomItemsLoader extends AbstractConfigLoader<List<CustomModuleConfiguration>> {
    private static final String CUSTOM_MODULES = "custom-modules.";
    private final IProtocolService protocolService;

    public StaffCustomItemsLoader(IProtocolService protocolService) {
        this.protocolService = protocolService;
    }

    @Override
    protected List<CustomModuleConfiguration> load() {
        return loadCustomModules(staffModeCustomModulesConfig);
    }

    private List<CustomModuleConfiguration> loadCustomModules(FileConfiguration config) {
        List<CustomModuleConfiguration> customModuleConfigurations = new ArrayList<>();
        if (config.getConfigurationSection("custom-modules") == null) {
            StaffPlus.get().getLogger().info("No custom staff mode modules to load");
            return customModuleConfigurations;
        }

        for (String identifier : config.getConfigurationSection("custom-modules").getKeys(false)) {
            boolean enabled = config.getBoolean(CUSTOM_MODULES + identifier + ".enabled");
            if (!enabled) {
                continue;
            }

            CustomModuleConfiguration.ModuleType moduleType = stringToModuleType(sanitize(config.getString(CUSTOM_MODULES + identifier + ".type")));
            Material type = Options.stringToMaterial(sanitize(config.getString(CUSTOM_MODULES + identifier + ".item")));
            short data = getMaterialData(config.getString(CUSTOM_MODULES + identifier + ".item"));
            String name = config.getString(CUSTOM_MODULES + identifier + ".name");

            ConfirmationConfig confirmationConfig = getConfirmationConfig(config, identifier);

            String commas = config.getString(CUSTOM_MODULES + identifier + ".lore");
            if (commas == null) {
                throw new IllegalArgumentException("Commas may not be null.");
            }

            List<String> lore = new ArrayList<>(Arrays.asList(commas.split("\\s*,\\s*")));
            ItemStack item = Items.builder().setMaterial(type).setData(data).setName(name).setLore(lore).build();
            List<String> actions = new ArrayList<>();


            if (!config.getString(CUSTOM_MODULES + identifier + ".enchantment", "").equalsIgnoreCase("")) {
                String enchantInfo = config.getString(CUSTOM_MODULES + identifier + ".enchantment");
                String[] enchantInfoParts = enchantInfo.split(":");
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantInfoParts[0]));
                if (enchantment == null) {
                    enchantment = Enchantment.DURABILITY;
                }
                int level = Integer.parseInt(enchantInfoParts[1]);
                item = Items.builder().setMaterial(type).setData(data).setName(name).setLore(lore)
                    .addEnchantment(enchantment, level).build();
            }


            if (moduleType != CustomModuleConfiguration.ModuleType.ITEM) {
                actions = (List<String>) config.getList(CUSTOM_MODULES + identifier + ".commands");
            }

            boolean requireInput = config.getBoolean(CUSTOM_MODULES + identifier + ".require-input", false);
            String inputPrompt = config.getString(CUSTOM_MODULES + identifier + ".input-prompt", null);
            item = protocolService.getVersionProtocol().addNbtString(item, identifier);
            customModuleConfigurations.add(new CustomModuleConfiguration(true, identifier, moduleType, item, actions, confirmationConfig, requireInput, inputPrompt));
        }
        return customModuleConfigurations;
    }

    private ConfirmationConfig getConfirmationConfig(FileConfiguration config, String identifier) {
        ConfirmationConfig confirmationConfig = null;
        ConfirmationType confirmationType = config.getString(CUSTOM_MODULES + identifier + ".confirmation", null) == null ? null : ConfirmationType.valueOf(config.getString(CUSTOM_MODULES + identifier + ".confirmation"));
        if (confirmationType != null) {
            String confirmationMessage = config.getString(CUSTOM_MODULES + identifier + ".confirmation-message", null);
            confirmationConfig = new ConfirmationConfig(confirmationType, confirmationMessage);
        }
        return confirmationConfig;
    }


    private CustomModuleConfiguration.ModuleType stringToModuleType(String string) {
        CustomModuleConfiguration.ModuleType moduleType = CustomModuleConfiguration.ModuleType.ITEM;
        boolean isValid = JavaUtils.isValidEnum(CustomModuleConfiguration.ModuleType.class, string);

        if (!isValid) {
            Bukkit.getLogger().severe("Invalid module type '" + string + "'!");
        } else moduleType = CustomModuleConfiguration.ModuleType.valueOf(string);

        return moduleType;
    }

    private short getMaterialData(String string) {
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

}
