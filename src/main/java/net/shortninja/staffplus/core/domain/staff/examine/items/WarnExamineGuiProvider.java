package net.shortninja.staffplus.core.domain.staff.examine.items;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGuiItemProvider;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@IocBean
@IocMultiProvider(ExamineGuiItemProvider.class)
public class WarnExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;
    private final ExamineModeConfiguration examineModeConfiguration;

    public WarnExamineGuiProvider(Messages messages, Options options) {
        this.messages = messages;
        examineModeConfiguration = options.staffItemsConfiguration.getExamineModeConfiguration();
    }

    @Override
    public ItemStack getItem(Player player1, SppPlayer player) {
        return warnItem();
    }

    @Override
    public String getClickAction(Player staff, SppPlayer targetPlayer, String backAction) {
        return GuiActionBuilder.builder()
            .action("manage-warnings/view/select-severity")
            .param("targetPlayerName", targetPlayer.getUsername())
            .param("backAction", backAction)
            .build();
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        return examineModeConfiguration.getModeExamineWarn() >= 0;
    }

    @Override
    public int getSlot() {
        return examineModeConfiguration.getModeExamineWarn() - 1;
    }

    private ItemStack warnItem() {
        return Items.builder()
            .setMaterial(Material.PAPER).setAmount(1)
            .setName("&bWarn player")
            .addLore(messages.examineWarn)
            .build();
    }
}
