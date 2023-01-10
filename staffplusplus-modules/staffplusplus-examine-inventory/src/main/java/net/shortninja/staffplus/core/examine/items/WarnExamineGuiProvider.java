package net.shortninja.staffplus.core.examine.items;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.tubinggui.GuiActionBuilder;
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
public class WarnExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;
    private final ExamineConfiguration examineConfiguration;

    public WarnExamineGuiProvider(Messages messages, ExamineConfiguration examineConfiguration) {
        this.messages = messages;
        this.examineConfiguration = examineConfiguration;
    }

    @Override
    public ItemStack getItem(Player player1, SppPlayer player) {
        return warnItem();
    }

    @Override
    public String getClickAction(Player staff, SppPlayer targetPlayer) {
        return GuiActionBuilder.builder()
            .action("manage-warnings/view/select-severity")
            .param("targetPlayerName", targetPlayer.getUsername())
            .build();
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        return examineConfiguration.modeExamineWarn >= 0;
    }

    @Override
    public int getSlot() {
        return examineConfiguration.modeExamineWarn - 1;
    }

    private ItemStack warnItem() {
        return Items.builder()
            .setMaterial(Material.PAPER).setAmount(1)
            .setName("&bWarn player")
            .addLore(messages.examineWarn)
            .build();
    }
}
