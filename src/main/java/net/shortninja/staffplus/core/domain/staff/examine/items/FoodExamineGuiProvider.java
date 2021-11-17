package net.shortninja.staffplus.core.domain.staff.examine.items;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.gui.model.TubingGuiActions;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGuiItemProvider;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@IocBean
@IocMultiProvider(ExamineGuiItemProvider.class)
public class FoodExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;
    private final ExamineModeConfiguration examineModeConfiguration;

    public FoodExamineGuiProvider(Messages messages, Options options) {
        this.messages = messages;
        examineModeConfiguration = options.staffItemsConfiguration.getExamineModeConfiguration();
    }

    @Override
    public ItemStack getItem(Player player1, SppPlayer player) {
        return foodItem(player.getPlayer());
    }

    @Override
    public String getClickAction(Player staff, SppPlayer targetPlayer, String backAction) {
        return TubingGuiActions.NOOP;
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        return examineModeConfiguration.getModeExamineFood() >= 0 && player.isOnline();
    }

    @Override
    public int getSlot() {
        return examineModeConfiguration.getModeExamineFood() - 1;
    }

    private ItemStack foodItem(Player player) {
        int healthLevel = (int) player.getHealth();
        int foodLevel = player.getFoodLevel();
        List<String> lore = new ArrayList<>();

        for (String string : messages.examineFood) {
            lore.add(string.replace("%health%", healthLevel + "/20").replace("%hunger%", foodLevel + "/20"));
        }

        ItemStack item = Items.builder()
            .setMaterial(Material.BREAD).setAmount(1)
            .setName("&bFood")
            .setLore(lore)
            .build();

        return item;
    }

}
