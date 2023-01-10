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

@IocBean
@IocMultiProvider(ExamineGuiItemProvider.class)
public class GamemodeExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;
    private final ExamineConfiguration examineConfiguration;

    public GamemodeExamineGuiProvider(Messages messages, ExamineConfiguration examineConfiguration) {
        this.messages = messages;
        this.examineConfiguration = examineConfiguration;
    }

    @Override
    public ItemStack getItem(Player player1, SppPlayer player) {
        return gameModeItem(player.getPlayer());
    }

    @Override
    public String getClickAction(Player staff, SppPlayer targetPlayer) {
        return TubingGuiActions.NOOP;
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        return examineConfiguration.modeExamineGamemode >= 0 && player.isOnline();
    }

    @Override
    public int getSlot() {
        return examineConfiguration.modeExamineGamemode - 1;
    }

    private ItemStack gameModeItem(Player player) {

        return Items.builder()
            .setMaterial(Material.GRASS).setAmount(1)
            .setName("&bGamemode")
            .addLore(messages.examineGamemode.replace("%gamemode%", player.getGameMode().toString()))
            .build();
    }

}
