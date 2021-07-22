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
public class DefaultXrayStrategy implements XrayStrategy {
    private final Options options;

    public DefaultXrayStrategy(Options options) {
        this.options = options;
    }

    @Override
    public boolean handleBlockBreak(XrayBlockConfig xrayBlockConfig, XrayTrace blockTrace, Player player, Block block) {
        int lightLevel = player.getLocation().getBlock().getLightLevel();
        sendEvent(new XrayEvent(player, blockTrace.getAmount(), null, block.getType(), lightLevel, block.getLocation(), options.serverName));
        return true;
    }

    @Override
    public boolean shouldHandle(XrayBlockConfig xrayBlockConfig) {
        return xrayBlockConfig.getAmountOfBlocks() == null;
    }
}
