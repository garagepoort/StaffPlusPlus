package net.shortninja.staffplus.core.domain.staff.alerts.xray;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import net.shortninja.staffplus.core.domain.staff.alerts.config.XrayConfiguration;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@IocBean
public class XrayService {

    private final List<XrayStrategy> xrayStrategies;
    private final XrayConfiguration xrayConfiguration;

    private static final Map<Player, Map<Material, XrayTrace>> playerTraces = new HashMap<>();

    public XrayService(@IocMulti(XrayStrategy.class) List<XrayStrategy> xrayStrategies, XrayConfiguration xrayConfiguration) {
        this.xrayStrategies = xrayStrategies;
        this.xrayConfiguration = xrayConfiguration;
    }

    public void handleBlockBreak(Block block, Player player) {
        Optional<XrayBlockConfig> xrayBlockConfigOptional = xrayConfiguration.getBlockConfig(block.getType());
        if (!xrayBlockConfigOptional.isPresent()) {
            return;
        }

        XrayBlockConfig xrayBlockConfig = xrayBlockConfigOptional.get();
        long now = System.currentTimeMillis();
        addTrace(block.getType(), player, now);

        XrayTrace blockTrace = playerTraces.get(player).get(block.getType());
        Optional<XrayStrategy> xrayStrategy = xrayStrategies.stream().filter(s -> s.shouldHandle(xrayBlockConfig)).findFirst();
        if (xrayStrategy.isPresent() && xrayStrategy.get().handleBlockBreak(xrayBlockConfig, blockTrace, player, block)) {
            playerTraces.get(player).remove(block.getType());
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
