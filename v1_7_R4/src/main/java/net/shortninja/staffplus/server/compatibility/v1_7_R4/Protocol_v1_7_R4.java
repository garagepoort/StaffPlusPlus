package net.shortninja.staffplus.server.compatibility.v1_7_R4;

import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.channel.ChannelHandler;
import net.minecraft.util.io.netty.channel.ChannelPipeline;
import net.shortninja.staffplus.IStaffPlus;
import net.shortninja.staffplus.server.compatibility.AbstractProtocol;
import net.shortninja.staffplus.server.compatibility.IProtocol;
import net.shortninja.staffplus.util.lib.json.JsonMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Set;

public class Protocol_v1_7_R4 extends AbstractProtocol implements IProtocol {
    public Protocol_v1_7_R4(IStaffPlus staffPlus) {
        super(staffPlus);
    }

    @Override
    public org.bukkit.inventory.ItemStack addNbtString(org.bukkit.inventory.ItemStack item, String value) {
        ItemStack craftItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtCompound = craftItem.getTag() == null ? new NBTTagCompound() : craftItem.getTag();

        nbtCompound.setString(NBT_IDENTIFIER, value);
        craftItem.setTag(nbtCompound);

        return CraftItemStack.asCraftMirror(craftItem);
    }

    @Override
    public String getNbtString(org.bukkit.inventory.ItemStack item) {
        ItemStack craftItem = CraftItemStack.asNMSCopy(item);

        if (craftItem == null) {
            return "";
        }

        NBTTagCompound nbtCompound = craftItem.getTag() == null ? new NBTTagCompound() : craftItem.getTag();

        return nbtCompound.getString(NBT_IDENTIFIER);
    }

    @Override
    public void registerCommand(String match, Command command) {
        ((CraftServer) Bukkit.getServer()).getCommandMap().register(match, command);
    }

    @Override
    public void listVanish(Player player, boolean shouldEnable) {
        if (shouldEnable) {
            player.setPlayerListName("");
        } else player.setPlayerListName(player.getName());
    }

    @Override
    public void sendHoverableJsonMessage(Set<Player> players, String message, String hoverMessage) {
        JsonMessage json = new JsonMessage().append(message).setHoverAsTooltip(hoverMessage).save();
        PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(json.getMessage()));

        for (Player player : players) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public String getSound(Object object) {
        return object instanceof String ? (String) object : null;
    }

    @Override
    public void inject(Player player) {
        final ChannelPipeline pipeline = this.getChannel(player).pipeline();

        // Probably will go wrong at runtime but I have no clue how to fix it. - Ronald.
        pipeline.addBefore("packet_handler", player.getUniqueId().toString(), (ChannelHandler) new PacketHandler_v1_7_R4(player));
    }

    @Override
    public void uninject(Player player) {
        final Channel channel = this.getChannel(player);
        channel.eventLoop().submit(() -> channel.pipeline().remove(player.getUniqueId().toString()));
    }

    private Channel getChannel(Player player) {
        try {
            Field field = NetworkManager.class.getDeclaredField("m");
            field.setAccessible(true);

            return (Channel) field.get(((CraftPlayer) player).getHandle().playerConnection.networkManager);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
