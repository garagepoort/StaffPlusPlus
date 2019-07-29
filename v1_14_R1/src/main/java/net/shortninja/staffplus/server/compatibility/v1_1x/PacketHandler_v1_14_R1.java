package net.shortninja.staffplus.server.compatibility.v1_1x;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;
import net.shortninja.staffplus.server.compatibility.AbstractPacketHandler;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public final class PacketHandler_v1_14_R1 extends AbstractPacketHandler {

    public PacketHandler_v1_14_R1(Player player) {
        super(player);
    }

    @Override
    public boolean onSend(ChannelHandlerContext context, Object o, ChannelPromise promise) throws Exception {
        if (o instanceof PacketPlayOutChat) {
            player.sendMessage("INTERCEPTED >> Chat Message");
            return false;
        }

        return true;
    }

    @Override
    public boolean onReceive(ChannelHandlerContext context, Object o) throws Exception {
        return true;
    }

    private IChatBaseComponent getChatComponent(PacketPlayOutChat packet) throws NoSuchFieldException, IllegalAccessException {
        Field field = PacketPlayOutChat.class.getDeclaredField("a");
        field.setAccessible(true);

        return (IChatBaseComponent) field.get(packet);
    }
}
