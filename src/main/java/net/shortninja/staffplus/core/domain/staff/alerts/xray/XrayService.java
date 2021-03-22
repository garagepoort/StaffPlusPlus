package net.shortninja.staffplus.core.domain.staff.alerts.xray;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplusplus.xray.XrayEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@IocBean
public class XrayService {

    private final Options options;

    private static final Map<Player, Map<Material, XrayTrace>> playerTraces = new HashMap<>();

    public XrayService(Options options) {
        this.options = options;
    }

    public void handleBlockBreak(Material blocktype, Player player) {
        List<XrayBlockConfig> alertsXrayBlocks = options.alertsConfiguration.getXrayConfiguration().getAlertsXrayBlocks();
        Optional<XrayBlockConfig> xrayBlockConfigOptional = alertsXrayBlocks.stream()
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
            BukkitUtils.sendEvent(new XrayEvent(player, blockTrace.getAmount(), null, blocktype, lightLevel));
            playerTraces.get(player).remove(blocktype);
            return;
        }

        if (xrayBlockConfig.getAmountOfBlocks() != null && xrayBlockConfig.getDuration() == null) {
            if (blockTrace.getAmount() >= xrayBlockConfig.getAmountOfBlocks()) {
                BukkitUtils.sendEvent(new XrayEvent(player, blockTrace.getAmount(), null, blocktype, lightLevel));
                playerTraces.get(player).remove(blocktype);
            }
            return;
        }

        long minimumValidTimestamp = now - xrayBlockConfig.getDuration();
        blockTrace.removeInvalidTimestamps(minimumValidTimestamp);
        long duration = blockTrace.getDuration();
        if (blockTrace.getAmount() >= xrayBlockConfig.getAmountOfBlocks()) {
            BukkitUtils.sendEvent(new XrayEvent(player, blockTrace.getAmount(), duration, blocktype, lightLevel));
            playerTraces.get(player).remove(blocktype);
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
