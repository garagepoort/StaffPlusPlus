package net.shortninja.staffplus.nms;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.shortninja.staffplus.server.compatibility.AbstractPacketHandler;
import org.bukkit.entity.Player;

public class PacketHandler_v1_9 extends AbstractPacketHandler {

	public PacketHandler_v1_9(Player player) {
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