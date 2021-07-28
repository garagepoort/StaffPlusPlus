package net.shortninja.staffplus.core.domain.staff.examine.items;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGuiItemProvider;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@IocBean
@IocMultiProvider(ExamineGuiItemProvider.class)
public class FreezeExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;
    private final ExamineModeConfiguration examineModeConfiguration;
    private final FreezeHandler freezeHandler;

    public FreezeExamineGuiProvider(Messages messages, Options options, FreezeHandler freezeHandler) {
        this.messages = messages;
        this.freezeHandler = freezeHandler;
        examineModeConfiguration = options.staffItemsConfiguration.getExamineModeConfiguration();
    }

    @Override
    public ItemStack getItem(SppPlayer player) {
        return freezeItem(player.getPlayer());
    }

    @Override
    public String getClickAction(Player staff, SppPlayer targetPlayer, String backAction) {
        return GuiActionBuilder.builder()
            .action("manage-frozen/freeze")
            .param("targetPlayerUuid", String.valueOf(targetPlayer.getId()))
            .build();
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        return examineModeConfiguration.getModeExamineFreeze() >= 0 && player.isOnline();
    }

    @Override
    public int getSlot() {
        return examineModeConfiguration.getModeExamineFreeze() - 1;
    }

    private ItemStack freezeItem(Player player) {
        String frozenStatus = freezeHandler.isFrozen(player.getUniqueId()) ? "" : "not ";

        return Items.builder()
            .setMaterial(Material.BLAZE_ROD).setAmount(1)
            .setName("&bFreeze player")
            .setLore(Arrays.asList(messages.examineFreeze, "&7Currently " + frozenStatus + "frozen."))
            .build();
    }
}
