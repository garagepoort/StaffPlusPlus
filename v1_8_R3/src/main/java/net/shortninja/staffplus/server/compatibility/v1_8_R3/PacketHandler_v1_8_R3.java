package net.shortninja.staffplus.server.compatibility.v1_8_R3;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect;
import net.shortninja.staffplus.IStaffPlus;
import net.shortninja.staffplus.server.compatibility.AbstractPacketHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PacketHandler_v1_8_R3 extends AbstractPacketHandler {

    public PacketHandler_v1_8_R3(Player player) {
        super(player);
    }

    @Override
    public boolean onSend(ChannelHandlerContext context, Object o, ChannelPromise promise) throws Exception {
        if (o instanceof PacketPlayOutNamedSoundEffect) {
            RegisteredServiceProvider<IStaffPlus> provider = Bukkit.getServicesManager().getRegistration(IStaffPlus.class);
            if (provider != null) {
                IStaffPlus api = provider.getProvider();
                if (api.getUserManager().get(player.getUniqueId()).isVanished()) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean onReceive(ChannelHandlerContext context, Object o) throws Exception {
        return false;
    }
}
