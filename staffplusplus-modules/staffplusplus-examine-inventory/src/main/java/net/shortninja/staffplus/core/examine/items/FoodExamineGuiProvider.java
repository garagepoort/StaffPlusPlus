package net.shortninja.staffplus.core.examine.items;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.tubinggui.model.TubingGuiActions;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.examine.config.ExamineConfiguration;
import net.shortninja.staffplus.core.examine.gui.ExamineGuiItemProvider;
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
    private final ExamineConfiguration examineConfiguration;

    public FoodExamineGuiProvider(Messages messages, ExamineConfiguration examineConfiguration) {
        this.messages = messages;
        this.examineConfiguration = examineConfiguration;
    }

    @Override
    public ItemStack getItem(Player player1, SppPlayer player) {
        return foodItem(player.getPlayer());
    }

    @Override
    public String getClickAction(Player staff, SppPlayer targetPlayer) {
        return TubingGuiActions.NOOP;
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        return examineConfiguration.modeExamineFood >= 0 && player.isOnline();
    }

    @Override
    public int getSlot() {
        return examineConfiguration.modeExamineFood - 1;
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
