package net.shortninja.staffplus.core.domain.staff.alerts.xray;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplusplus.xray.XrayEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
@IocMultiProvider(XrayStrategy.class)
public class AmountAndDurationXrayStrategy implements XrayStrategy {
    private final Options options;

    public AmountAndDurationXrayStrategy(Options options) {
        this.options = options;
    }

    @Override
    public boolean handleBlockBreak(XrayBlockConfig xrayBlockConfig, XrayTrace blockTrace, Player player, Block block) {
        int lightLevel = player.getLocation().getBlock().getLightLevel();
        long minimumValidTimestamp = System.currentTimeMillis() - xrayBlockConfig.getDuration();
        blockTrace.removeInvalidTimestamps(minimumValidTimestamp);

        long duration = blockTrace.getDuration();
        if (blockTrace.getAmount() >= xrayBlockConfig.getAmountOfBlocks()) {
            sendEvent(new XrayEvent(player, blockTrace.getAmount(), duration, block.getType(), lightLevel, block.getLocation(), options.serverName));
            return true;
        }
        return false;

    }

    @Override
    public boolean shouldHandle(XrayBlockConfig xrayBlockConfig) {
        return xrayBlockConfig.getAmountOfBlocks() != null && xrayBlockConfig.getDuration() != null;
    }
}
