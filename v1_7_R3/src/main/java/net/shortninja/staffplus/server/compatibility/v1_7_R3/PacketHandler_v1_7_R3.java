package net.shortninja.staffplus.server.compatibility.v1_7_R3;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.shortninja.staffplus.server.compatibility.AbstractPacketHandler;
import org.bukkit.entity.Player;

public class PacketHandler_v1_7_R3 extends AbstractPacketHandler {

	public PacketHandler_v1_7_R3(Player player) {
		super(player);
	}

	@Override
	public boolean onSend(ChannelHandlerContext context, Object o, ChannelPromise promise) throws Exception {
		return false;
	}

	@Override
	public boolean onReceive(ChannelHandlerContext context, Object o) throws Exception {
		return false;
	}

}
