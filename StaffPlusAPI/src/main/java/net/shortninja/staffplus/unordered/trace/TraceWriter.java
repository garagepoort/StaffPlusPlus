package net.shortninja.staffplus.unordered.trace;

public interface TraceWriter {

    void writeToTrace(String message);

    void stopTrace();

    String getResource();

    TraceOutputChannel getType();
}
