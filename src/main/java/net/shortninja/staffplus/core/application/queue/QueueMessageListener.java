package net.shortninja.staffplus.core.application.queue;

public interface QueueMessageListener<T> {
    void handleMessage(T message);

    String getType();

    Class getMessageClass();
}
