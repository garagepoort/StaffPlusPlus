package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocMulti;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@IocBukkitListener
public class AsyncPlayerChat implements Listener {
    private final List<ChatInterceptor> chatInterceptors;

    public AsyncPlayerChat(@IocMulti(ChatInterceptor.class) List<ChatInterceptor> chatInterceptors) {
        this.chatInterceptors = chatInterceptors.stream().sorted(Comparator.comparingInt(ChatInterceptor::getPriority)).collect(Collectors.toList());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        for (ChatInterceptor chatInterceptor : chatInterceptors) {
            boolean cancel = chatInterceptor.intercept(event);
            if (cancel) {
                event.setCancelled(true);
                return;
            }
        }
    }
}