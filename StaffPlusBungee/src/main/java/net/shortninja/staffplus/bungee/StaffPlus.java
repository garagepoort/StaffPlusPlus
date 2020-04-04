package net.shortninja.staffplus.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Arrays;

public final class StaffPlus extends Plugin implements Listener {

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterListener(this);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void on(ChatEvent e) {

        if (e.isCommand()) {
            final String[] split = e.getMessage().split(" ");
            final String command = split[0].substring(1);

            // TODO: Allow configurable command.
            if (command.equalsIgnoreCase("report")) {
                if (split.length > 1) {
                    final String[] args = Arrays.copyOfRange(split, 1, split.length);

                    if (args.length > 0) {
                        final String name = args[0];
                        final String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

                        ProxyServer.getInstance().getPlayers().forEach(p -> {
                            // TODO: Allow configurable message.
                            p.sendMessage(TextComponent.fromLegacyText(String.format("§b%s§e is reported by §b%s§e with reason \"§b%s§e\".",
                                    name,
                                    ((ProxiedPlayer) e.getSender()).getName(),
                                    reason
                            )));
                        });
                    } else {
                        // TODO: Allow configurable message.
                        ((ProxiedPlayer) e.getSender()).sendMessage(TextComponent.fromLegacyText("§cPlease specify a reason."));
                    }
                } else {
                    // TODO: Allow configurable message.
                    ((ProxiedPlayer) e.getSender()).sendMessage(TextComponent.fromLegacyText("§cPlease specify a player."));
                }
            }
        }
    }
}
