package net.shortninja.staffplus.server.compatibility.v1_1x;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_14_R1.PacketPlayOutNamedSoundEffect;
import net.shortninja.staffplus.server.compatibility.AbstractPacketHandler;
import org.bukkit.entity.Player;

public final class PacketHandler_v1_14_R1 extends AbstractPacketHandler {

    public PacketHandler_v1_14_R1(Player player) {
        super(player);
    }

    @Override
    public boolean onSend(ChannelHandlerContext context, Object o, ChannelPromise promise) throws Exception {
        if (o instanceof PacketPlayOutNamedSoundEffect) {
            final PacketPlayOutNamedSoundEffect packet = (PacketPlayOutNamedSoundEffect) o;
        }

        return true;
    }

    @Override
    public boolean onReceive(ChannelHandlerContext context, Object o) throws Exception {
        return true;
    }
}
