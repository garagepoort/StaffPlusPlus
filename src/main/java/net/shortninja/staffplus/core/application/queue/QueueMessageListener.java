package net.shortninja.staffplus.core.application.queue;

public interface QueueMessageListener<T> {
    String handleMessage(T message);

    String getType();

    Class getMessageClass();
}
