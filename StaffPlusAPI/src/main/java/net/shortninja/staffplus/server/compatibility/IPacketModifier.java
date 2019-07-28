package net.shortninja.staffplus.server.compatibility;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public interface IPacketModifier {

    boolean onSend(ChannelHandlerContext context, Object packet, ChannelPromise promise);

    boolean onReceive(ChannelHandlerContext context, Object packet);
}
