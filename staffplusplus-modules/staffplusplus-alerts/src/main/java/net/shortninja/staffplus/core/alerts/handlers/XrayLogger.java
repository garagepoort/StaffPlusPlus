package net.shortninja.staffplus.core.alerts.handlers;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.alerts.xray.bungee.XrayAlertBungeeDto;
import net.shortninja.staffplusplus.xray.XrayEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@IocBean
public class XrayLogger {

    private final Messages messages;

    public XrayLogger(Messages messages) {
        this.messages = messages;
    }

    public String getLogMessage(XrayEvent event) {

        List<String> enchantments = new ArrayList<>();
        event.getPickaxe().getEnchantments()
            .forEach((k, v) -> enchantments.add(JavaUtils.formatTypeName(k.getKey().getKey()) + " " + v));

        return getLogMessage(event.getPlayer().getName(),
            event.getAmount(),
            event.getType().name(),
            event.getLightLevel(),
            event.getDuration(),
            event.getServerName(),
            event.getPickaxe().getType().name(),
            enchantments);
    }

    public String getLogMessage(XrayAlertBungeeDto xrayAlertBungeeDto) {

        List<String> enchantments = new ArrayList<>();
        xrayAlertBungeeDto.getPickaxeEnchantments()
            .forEach((k, v) -> enchantments.add(JavaUtils.formatTypeName(k) + " " + v));

        return getLogMessage(xrayAlertBungeeDto.getPlayerName(),
            xrayAlertBungeeDto.getAmount(),
            xrayAlertBungeeDto.getType(),
            xrayAlertBungeeDto.getLightLevel(),
            xrayAlertBungeeDto.getDuration(),
            xrayAlertBungeeDto.getServerName(),
            xrayAlertBungeeDto.getPickaxeType(),
            enchantments);
    }

    private String getLogMessage(String playerName,
                                 int amount,
                                 String type,
                                 int lightLevel,
                                 Optional<Long> duration,
                                 String serverName,
                                 String toolType,
                                 List<String> enchantments) {


        String xrayMessage = messages.alertsXray
            .replace("%target%", playerName)
            .replace("%count%", Integer.toString(amount))
            .replace("%server%", serverName)
            .replace("%itemtype%", JavaUtils.formatTypeName(type))
            .replace("%tool-type%", JavaUtils.formatTypeName(toolType))
            .replace("%tool-enchantments%", String.join("\\n", enchantments))
            .replace("%lightlevel%", Integer.toString(lightLevel));

        if (duration.isPresent()) {
            xrayMessage = xrayMessage + String.format(" in %s seconds", duration.get() / 1000);
        }
        return xrayMessage;
    }
}
