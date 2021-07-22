package net.shortninja.staffplus.core.domain.staff.alerts.xray;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class XrayTrace {

    private final Material blockType;
    private List<Long> timestamps;

    public XrayTrace(Material blockType) {
        this.blockType = blockType;
        this.timestamps = new ArrayList<>();
    }

    public int getAmount() {
        return timestamps.size();
    }

    public Material getBlockType() {
        return blockType;
    }

    public void addTrace(long timestamp) {
        this.timestamps.add(timestamp);
    }

    public void removeInvalidTimestamps(long minimumValidTimestamp) {
        this.timestamps = timestamps.stream().filter(time -> time >= minimumValidTimestamp).collect(Collectors.toList());
    }

    public long getDuration() {
        if (timestamps.isEmpty()) {
            return 0;
        }
        return timestamps.get(timestamps.size() - 1) - timestamps.get(0);
    }
}
