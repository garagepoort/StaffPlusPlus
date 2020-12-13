package net.shortninja.staffplus.staff.alerts.xray;

import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.alerts.AlertCoordinator;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class XrayService {

    private final Options options;

    private static final Map<Player, Map<Material, XrayTrace>> playerTraces = new HashMap<>();
    private final AlertCoordinator alertCoordinator;

    public XrayService(Options options, AlertCoordinator alertCoordinator) {
        this.options = options;
        this.alertCoordinator = alertCoordinator;
    }

    public void handleBlockBreak(Material blocktype, Player player) {
        Optional<XrayBlockConfig> xrayBlockConfigOptional = options.alertsXrayBlocks.stream()
            .filter(b -> b.getMaterial() == blocktype)
            .findFirst();

        if (!xrayBlockConfigOptional.isPresent()) {
            return;
        }
        XrayBlockConfig xrayBlockConfig = xrayBlockConfigOptional.get();

        long now = System.currentTimeMillis();
        addTrace(blocktype, player, now);

        int lightLevel = player.getLocation().getBlock().getLightLevel();
        XrayTrace blockTrace = playerTraces.get(player).get(blocktype);

        if (xrayBlockConfig.getAmountOfBlocks() == null) {
            alertCoordinator.onXray(player.getName(), blockTrace.getAmount(), null, blocktype, lightLevel);
            playerTraces.get(player).remove(blocktype);
        } else if (xrayBlockConfig.getAmountOfBlocks() != null && xrayBlockConfig.getDuration() == null) {
            if (blockTrace.getAmount() >= xrayBlockConfig.getAmountOfBlocks()) {
                alertCoordinator.onXray(player.getName(), blockTrace.getAmount(), null, blocktype, lightLevel);
                playerTraces.get(player).remove(blocktype);
            }
        } else {
            long minimumValidTimestamp = now - xrayBlockConfig.getDuration();
            blockTrace.removeInvalidTimestamps(minimumValidTimestamp);
            long duration = blockTrace.getDuration();
            if (blockTrace.getAmount() >= xrayBlockConfig.getAmountOfBlocks()) {
                alertCoordinator.onXray(player.getName(), blockTrace.getAmount(), duration, blocktype, lightLevel);
                playerTraces.get(player).remove(blocktype);
            }
        }
    }

    private void addTrace(Material blocktype, Player player, long now) {
        if (!playerTraces.containsKey(player)) {
            playerTraces.put(player, new HashMap<>());
        }
        Map<Material, XrayTrace> playerTrace = playerTraces.get(player);
        if (!playerTrace.containsKey(blocktype)) {
            playerTrace.put(blocktype, new XrayTrace(blocktype));
        }

        XrayTrace blockTrace = playerTrace.get(blocktype);
        blockTrace.addTrace(now);
    }

    public void clearTrace(Player player) {
        playerTraces.remove(player);
    }
}
