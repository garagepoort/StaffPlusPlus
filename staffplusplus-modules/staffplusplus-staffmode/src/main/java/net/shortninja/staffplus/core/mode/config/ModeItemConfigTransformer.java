package net.shortninja.staffplus.core.mode.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import be.garagepoort.mcioc.configuration.yaml.configuration.ConfigurationSection;
import net.shortninja.staffplus.core.common.Items;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static net.shortninja.staffplus.core.application.config.Options.getMaterialData;
import static net.shortninja.staffplus.core.application.config.Options.sanitizeMaterial;
import static net.shortninja.staffplus.core.application.config.Options.stringToMaterial;

public class ModeItemConfigTransformer implements IConfigTransformer<ItemStack, Object> {

    @Override
    public ItemStack mapConfig(Object configurationSection) {

        if (configurationSection instanceof LinkedHashMap) {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) configurationSection;

            Material type = stringToMaterial(sanitizeMaterial(map.get("type")));
            short durability = getMaterialData(map.get("type"));
            String name = map.get("name");
            String commas = map.getOrDefault("lore", "");
            List<String> lore = new ArrayList<>(Arrays.asList(commas.split("\\s*,\\s*")));
            Items.ItemStackBuilder itemStackBuilder = Items.builder().setMaterial(type).setData(durability).setName(name).setLore(lore);

            if (!map.getOrDefault("enchantment", "").equalsIgnoreCase("")) {
                String enchantInfo = map.get("enchantment");
                String[] enchantInfoParts = enchantInfo.split(":");
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantInfoParts[0]));
                if (enchantment == null) {
                    enchantment = Enchantment.DURABILITY;
                }
                int level = Integer.parseInt(enchantInfoParts[1]);
                itemStackBuilder.addEnchantment(enchantment, level).build();
            }
            return itemStackBuilder.build();
        }
        if (configurationSection instanceof ConfigurationSection) {
            ConfigurationSection map = (ConfigurationSection) configurationSection;

            Material type = stringToMaterial(sanitizeMaterial(map.getString("type")));
            short durability = getMaterialData(map.getString("type"));
            String name = map.getString("name");
            String commas = map.getString("lore", "");
            List<String> lore = new ArrayList<>(Arrays.asList(commas.split("\\s*,\\s*")));
            Items.ItemStackBuilder itemStackBuilder = Items.builder().setMaterial(type).setData(durability).setName(name).setLore(lore);

            if (!map.getString("enchantment", "").equalsIgnoreCase("")) {
                String enchantInfo = map.getString("enchantment");
                String[] enchantInfoParts = enchantInfo.split(":");
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantInfoParts[0]));
                if (enchantment == null) {
                    enchantment = Enchantment.DURABILITY;
                }
                int level = Integer.parseInt(enchantInfoParts[1]);
                itemStackBuilder.addEnchantment(enchantment, level).build();
            }
            return itemStackBuilder.build();
        }
        return null;
    }
}
