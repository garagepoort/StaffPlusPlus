package net.shortninja.staffplus.core.application.queue;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.bungee.GsonParser;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Only run when the webui is enabled.
//  Only the web interface will communicate through the queue for now
@IocBean(conditionalOnProperty = "webui-module.enabled=true")
public class QueueMessageProcessor extends BukkitRunnable {
    private static final long TIMER = 10;
    private final QueueRepository queueRepository;
    private final GsonParser gsonParser;

    private final Map<String, QueueMessageListener> listeners = new HashMap<>();

    public QueueMessageProcessor(QueueRepository queueRepository,
                                 GsonParser gsonParser,
                                 @IocMulti(QueueMessageListener.class) List<QueueMessageListener> listeners) {
        this.queueRepository = queueRepository;
        this.gsonParser = gsonParser;
        listeners.forEach(listener -> this.listeners.put(listener.getType(), listener));
        runTaskTimerAsynchronously(StaffPlus.get(), TIMER * 20, TIMER * 20);
    }

    @Override
    public void run() {
        Optional<QueueMessage> queueMessage = queueRepository.findNextQueueMessage();
        queueMessage.ifPresent(message -> {
            try {

                if (!listeners.containsKey(message.getType())) {
                    StaffPlus.getPlugin().getLogger().warning("No QueueMessageListener for type [" + message.getType() + "]");
                }
                QueueMessageListener listener = listeners.get(message.getType());
                listener.handleMessage(gsonParser.fromJson(message.getData(), listener.getMessageClass()));
            } catch (Exception e) {

            }
        });
    }

}
