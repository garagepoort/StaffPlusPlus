package net.shortninja.staffplus.core.domain.player;

import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StripService {

    private static StripService instance;

    private MessageCoordinator message = IocContainer.get(MessageCoordinator.class);
    private Messages messages = IocContainer.get(Messages.class);

    public static StripService getInstance() {
        if(instance == null) {
            instance = new StripService();
        }
        return instance;
    }

    public void strip(CommandSender sender,  Player player) {
        strip(player, 0);

        message.send(sender, messages.strip.replace("%player%", player.getName()), messages.prefixGeneral);
    }

    private void strip(Player player, int index) {
        ItemStack[] armor = player.getInventory().getArmorContents();

        switch (index) {
            case 0:
                if (armor[0] != null) {
                    player.getInventory().addItem(armor[0].clone());
                    player.getInventory().setBoots(null);
                }
                break;
            case 1:
                if (armor[1] != null) {
                    player.getInventory().addItem(armor[1].clone());
                    player.getInventory().setLeggings(null);
                }
                break;
            case 2:
                if (armor[2] != null) {
                    player.getInventory().addItem(armor[2].clone());
                    player.getInventory().setChestplate(null);
                }
                break;
            case 3:
                if (armor[3] != null) {
                    player.getInventory().addItem(armor[3].clone());
                    player.getInventory().setHelmet(null);
                }
                break;
            default:
                return;
        }

        if (JavaUtils.hasInventorySpace(player) && index < 3) {
            strip(player, index + 1);
        }
    }
}
