package net.shortninja.staffplus.core.domain.staff.alerts.xray;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface XrayStrategy {

    boolean handleBlockBreak(XrayBlockConfig xrayBlockConfig, XrayTrace xrayTrace, Player player, Block block);

    boolean shouldHandle(XrayBlockConfig xrayBlockConfig);

}
