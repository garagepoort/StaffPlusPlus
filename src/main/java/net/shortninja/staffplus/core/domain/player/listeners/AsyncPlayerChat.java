package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocMulti;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.domain.staff.tracing.TraceType.CHAT;

@IocBukkitListener
public class AsyncPlayerChat implements Listener {
    private final List<ChatInterceptor> chatInterceptors;
    private final TraceService traceService;

    public AsyncPlayerChat(@IocMulti(ChatInterceptor.class) List<ChatInterceptor> chatInterceptors,
                           TraceService traceService) {
        this.chatInterceptors = chatInterceptors.stream().sorted(Comparator.comparingInt(ChatInterceptor::getPriority)).collect(Collectors.toList());
        this.traceService = traceService;
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

        traceService.sendTraceMessage(CHAT, player.getUniqueId(), event.getMessage());
    }
}