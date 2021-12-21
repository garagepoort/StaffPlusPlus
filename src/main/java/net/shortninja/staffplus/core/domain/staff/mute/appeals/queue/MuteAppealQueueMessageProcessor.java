package net.shortninja.staffplus.core.domain.staff.mute.appeals.queue;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import net.shortninja.staffplus.core.application.queue.QueueMessageListener;
import net.shortninja.staffplus.core.application.queue.QueueMessageProcessor;
import net.shortninja.staffplus.core.application.queue.QueueRepository;
import net.shortninja.staffplus.core.common.bungee.GsonParser;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfiguration;

import java.util.List;

// Only run when the webui is enabled.
//  Only the web interface will communicate through the queue for now
@IocBean(conditionalOnProperty = "webui-module.enabled=true && storage.type=mysql")
public class MuteAppealQueueMessageProcessor extends QueueMessageProcessor {
    private static final String PROCESSING_GROUP = "mute/";

    public MuteAppealQueueMessageProcessor(QueueRepository queueRepository,
                                           GsonParser gsonParser,
                                           @IocMulti(QueueMessageListener.class) List<QueueMessageListener> listeners,
                                           ServerSyncConfiguration serverSyncConfiguration) {
        super(queueRepository, gsonParser, listeners, serverSyncConfiguration);
    }

    @Override
    protected String getProcessingGroup() {
        return PROCESSING_GROUP;
    }

    @Override
    protected ServerSyncConfig getServerSyncConfig() {
        return serverSyncConfiguration.muteSyncServers;
    }

}
