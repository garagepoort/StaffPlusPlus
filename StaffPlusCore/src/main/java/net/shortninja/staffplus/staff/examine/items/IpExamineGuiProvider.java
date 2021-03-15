package net.shortninja.staffplus.staff.examine.items;

import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.examine.ExamineGui;
import net.shortninja.staffplus.staff.examine.ExamineGuiItemProvider;
import net.shortninja.staffplus.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplus.common.gui.IAction;
import net.shortninja.staffplus.common.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IpExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;
    private final ExamineModeConfiguration examineModeConfiguration;
    private final Options options;

    public IpExamineGuiProvider(Messages messages, Options options) {
        this.messages = messages;
        this.options = options;
        examineModeConfiguration = this.options.modeConfiguration.getExamineModeConfiguration();
    }

    @Override
    public ItemStack getItem(SppPlayer player) {
        return ipItem(player.getPlayer());
    }

    @Override
    public IAction getClickAction(ExamineGui examineGui, Player staff, SppPlayer targetPlayer) {
        return null;
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        return examineModeConfiguration.getModeExamineIp() >= 0 && player.isOnline();
    }

    @Override
    public int getSlot() {
        return examineModeConfiguration.getModeExamineIp() - 1;
    }

    private ItemStack ipItem(Player player) {
        String ip = player.hasPermission(options.ipHidePerm) ? "127.0.0.1" : player.getAddress().getAddress().getHostAddress().replace("/", "");

        ItemStack item = Items.builder()
            .setMaterial(Material.COMPASS).setAmount(1)
            .setName("&bConnection")
            .addLore(messages.examineIp.replace("%ipaddress%", ip))
            .build();

        return item;
    }

}
