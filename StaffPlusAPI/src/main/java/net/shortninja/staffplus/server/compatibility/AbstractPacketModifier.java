package net.shortninja.staffplus.server.compatibility;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public abstract class AbstractPacketModifier extends ChannelDuplexHandler implements IPacketModifier {

    @Override
    public final void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (onSend(ctx, msg, promise)) {
            super.write(ctx, msg, promise);
        }
    }

    @Override
    public final void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (onReceive(ctx, msg)) {
            super.channelRead(ctx, msg);
        }
    }
}

