package net.shortninja.staffplus.server.compatibility.v1_1x;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.shortninja.staffplus.server.compatibility.IPacketModifier;

public final class PacketModifier_v1_14_R1 implements IPacketModifier {

    @Override
    public boolean onSend(ChannelHandlerContext context, Object packet, ChannelPromise promise) {
        return false;
    }

    @Override
    public boolean onReceive(ChannelHandlerContext context, Object packet) {
        return false;
    }
}
