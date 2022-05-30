package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.staff.alerts.xray.bungee.XrayAlertBungeeDto;
import net.shortninja.staffplusplus.xray.XrayEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                                 String pickaxeType,
                                 List<String> pickaxeEnchantments) {
        String xrayMessage = "<json><text>" + messages.alertsXray
            .replace("%target%", playerName)
            .replace("%count%", Integer.toString(amount))
            .replace("%server%", serverName)
            .replace("%itemtype%", JavaUtils.formatTypeName(type))
            .replace("%pickaxe-type%",
                "</text>" +
                    "<text>" + JavaUtils.formatTypeName(pickaxeType) + "</text>"
                    + buildTooltip(pickaxeEnchantments) +
                    "<text>")
            .replace("%lightlevel%", Integer.toString(lightLevel)) + "</text></json>";

        if (duration.isPresent()) {
            xrayMessage = xrayMessage + String.format(" in %s seconds", duration.get() / 1000);
        }
        return xrayMessage;
    }

    @NotNull
    private String buildTooltip(List<String> pickaxeEnchantments) {
        return "<text tooltip=\"true\"><json>" + pickaxeEnchantments.stream().map(e -> "<text>\n" + e + "</text>").collect(Collectors.joining()) + "</json></text>";
    }
}
