package net.shortninja.staffplus.core.application.queue;

import be.garagepoort.mcioc.IocMulti;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.bungee.GsonParser;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Only run when the webui is enabled.
//  Only the web interface will communicate through the queue for now
public abstract class QueueMessageProcessor extends BukkitRunnable {
    private static final long TIMER = 10;
    private final QueueRepository queueRepository;
    private final GsonParser gsonParser;
    protected final ServerSyncConfiguration serverSyncConfiguration;

    private final Map<String, QueueMessageListener> listeners = new HashMap<>();

    public QueueMessageProcessor(QueueRepository queueRepository,
                                 GsonParser gsonParser,
                                 @IocMulti(QueueMessageListener.class) List<QueueMessageListener> listeners,
                                 ServerSyncConfiguration serverSyncConfiguration) {
        this.queueRepository = queueRepository;
        this.gsonParser = gsonParser;
        this.serverSyncConfiguration = serverSyncConfiguration;
        listeners.stream()
            .filter(t -> t.getType().startsWith(getProcessingGroup()))
            .forEach(listener -> this.listeners.put(listener.getType(), listener));
        runTaskTimerAsynchronously(StaffPlus.get(), TIMER * 20, TIMER * 20);
    }

    protected abstract String getProcessingGroup();

    protected abstract ServerSyncConfig getServerSyncConfig();

    @Override
    public void run() {
        Optional<QueueMessage> queueMessage = queueRepository.findNextQueueMessage(getProcessingGroup(), getServerSyncConfig());
        queueMessage.ifPresent(message -> {
            try {
                if (!listeners.containsKey(message.getType())) {
                    StaffPlus.getPlugin().getLogger().warning("No QueueMessageListener for type [" + message.getType() + "]");
                }
                QueueMessageListener listener = listeners.get(message.getType());

                Object payload = null;
                if (listener.getMessageClass() != Void.class) {
                    payload = gsonParser.fromJson(message.getData(), listener.getMessageClass());
                }
                String resultMessage = listener.handleMessage(payload);
                queueRepository.updateStatus(message.getId(), QueueStatus.PROCESSED, resultMessage);
            } catch (Exception e) {
                queueRepository.updateStatus(message.getId(), QueueStatus.FAILED, e.getMessage());
            }
        });
    }
}
