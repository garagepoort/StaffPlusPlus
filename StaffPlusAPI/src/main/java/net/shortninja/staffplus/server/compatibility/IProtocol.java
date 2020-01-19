package net.shortninja.staffplus.server.compatibility;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public interface IProtocol {
    String NBT_IDENTIFIER = "StaffPlus";

    ItemStack addNbtString(ItemStack item, String value);

    String getNbtString(ItemStack item);

    void registerCommand(String match, Command command);

    void listVanish(Player player, boolean shouldEnable);

    void sendHoverableJsonMessage(Set<Player> players, String message, String hoverMessage);

    boolean shouldIgnorePacket(Object packetValue);

    String getSound(Object object);

    void inject(Player player);

    void uninject(Player player);
}