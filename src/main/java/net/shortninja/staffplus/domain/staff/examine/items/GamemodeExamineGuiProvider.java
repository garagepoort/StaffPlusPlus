package net.shortninja.staffplus.domain.staff.examine.items;

import net.shortninja.staffplus.common.Items;
import net.shortninja.staffplus.common.config.Messages;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.common.gui.IAction;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.domain.staff.examine.gui.ExamineGui;
import net.shortninja.staffplus.domain.staff.examine.gui.ExamineGuiItemProvider;
import net.shortninja.staffplus.domain.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GamemodeExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;
    private final ExamineModeConfiguration examineModeConfiguration;
    private final Options options;

    public GamemodeExamineGuiProvider(Messages messages, Options options) {
        this.messages = messages;
        this.options = options;
        examineModeConfiguration = this.options.modeConfiguration.getExamineModeConfiguration();
    }

    @Override
    public ItemStack getItem(SppPlayer player) {
        return gameModeItem(player.getPlayer());
    }

    @Override
    public IAction getClickAction(ExamineGui examineGui, Player staff, SppPlayer targetPlayer) {
        return null;
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        return examineModeConfiguration.getModeExamineGamemode() >= 0 && player.isOnline();
    }

    @Override
    public int getSlot() {
        return examineModeConfiguration.getModeExamineGamemode() - 1;
    }

    private ItemStack gameModeItem(Player player) {
        ItemStack item = Items.builder()
            .setMaterial(Material.GRASS).setAmount(1)
            .setName("&bGamemode")
            .addLore(messages.examineGamemode.replace("%gamemode%", player.getGameMode().toString()))
            .build();

        return item;
    }

}
