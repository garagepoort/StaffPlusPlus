package net.shortninja.staffplus.staff.examine.items;

import net.shortninja.staffplus.common.IAction;
import net.shortninja.staffplus.common.Items;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.examine.ExamineGui;
import net.shortninja.staffplus.staff.examine.ExamineGuiItemProvider;
import net.shortninja.staffplus.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.staff.freeze.FreezeRequest;
import net.shortninja.staffplus.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class FreezeExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;
    private final ExamineModeConfiguration examineModeConfiguration;
    private final Options options;
    private final FreezeHandler freezeHandler;

    public FreezeExamineGuiProvider(Messages messages, Options options, FreezeHandler freezeHandler) {
        this.messages = messages;
        this.options = options;
        this.freezeHandler = freezeHandler;
        examineModeConfiguration = this.options.modeConfiguration.getExamineModeConfiguration();
    }

    @Override
    public ItemStack getItem(SppPlayer player) {
        return freezeItem(player.getPlayer());
    }

    @Override
    public IAction getClickAction(ExamineGui examineGui, Player staff, SppPlayer targetPlayer) {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                if (targetPlayer.getPlayer() != null) {
                    freezeHandler.execute(new FreezeRequest(staff, targetPlayer.getPlayer(), !freezeHandler.isFrozen(targetPlayer.getId())));
                }
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        };
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

        ItemStack item = Items.builder()
            .setMaterial(Material.BLAZE_ROD).setAmount(1)
            .setName("&bFreeze player")
            .setLore(Arrays.asList(messages.examineFreeze, "&7Currently " + frozenStatus + "frozen."))
            .build();

        return item;
    }
}
