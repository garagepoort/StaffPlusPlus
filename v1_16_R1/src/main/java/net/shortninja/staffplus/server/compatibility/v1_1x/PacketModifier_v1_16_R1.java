package net.shortninja.staffplus.server.compatibility.v1_1x;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_16_R1.IChatBaseComponent;
import net.minecraft.server.v1_16_R1.PacketPlayOutChat;
import net.shortninja.staffplus.server.compatibility.AbstractPacketModifier;
import org.bukkit.entity.Player;

public final class PacketModifier_v1_16_R1 extends AbstractPacketModifier {

    public PacketModifier_v1_16_R1(Player player) {
        super(player);
    }

    @Override
    public boolean onSend(ChannelHandlerContext context, Object o, ChannelPromise promise) throws Exception {
        if (o instanceof PacketPlayOutChat) {
            final PacketPlayOutChat packet = (PacketPlayOutChat) o;
            final IChatBaseComponent baseComponent = (IChatBaseComponent) PacketPlayOutChat.class.getDeclaredField("a").get(packet);

            player.sendMessage("INTERCEPTED >> Chat Message: " + baseComponent.getText());
        }

        return true;
    }

    @Override
    public boolean onReceive(ChannelHandlerContext context, Object packet) throws Exception {
        return true;
    }
}
