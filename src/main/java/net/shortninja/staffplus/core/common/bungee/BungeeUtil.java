package net.shortninja.staffplus.core.common.bungee;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.domain.staff.broadcast.BungeeBroadcastListener;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.*;
import net.shortninja.staffplus.core.domain.staff.staffchat.BungeeStaffChatListener;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CORD_CHANNEL;

public class BungeeUtil {

    public static void initListeners(StaffPlus staffPlus) {
        staffPlus.getServer().getMessenger().registerOutgoingPluginChannel(staffPlus, BUNGEE_CORD_CHANNEL);
        staffPlus.getServer().getMessenger().registerIncomingPluginChannel(staffPlus, BUNGEE_CORD_CHANNEL, new BungeeStaffChatListener());
        staffPlus.getServer().getMessenger().registerIncomingPluginChannel(staffPlus, BUNGEE_CORD_CHANNEL, new BungeeBroadcastListener());
        staffPlus.getServer().getMessenger().registerIncomingPluginChannel(staffPlus, BUNGEE_CORD_CHANNEL, new ReportCreatedBungeeListener());
        staffPlus.getServer().getMessenger().registerIncomingPluginChannel(staffPlus, BUNGEE_CORD_CHANNEL, new ReportAcceptedBungeeListener());
        staffPlus.getServer().getMessenger().registerIncomingPluginChannel(staffPlus, BUNGEE_CORD_CHANNEL, new ReportClosedBungeeListener());
        staffPlus.getServer().getMessenger().registerIncomingPluginChannel(staffPlus, BUNGEE_CORD_CHANNEL, new ReportReopenBungeeListener());
        staffPlus.getServer().getMessenger().registerIncomingPluginChannel(staffPlus, BUNGEE_CORD_CHANNEL, new ReportDeletedBungeeListener());
    }
}
