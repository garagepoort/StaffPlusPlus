package net.shortninja.staffplus.core.domain.staff.alerts.xray.bungee;

import net.shortninja.staffplus.core.domain.location.SppLocation;
import net.shortninja.staffplus.core.common.bungee.BungeeMessage;
import net.shortninja.staffplusplus.xray.XrayEvent;
import org.bukkit.Location;

import java.util.Optional;
import java.util.UUID;

public class XrayAlertBungeeDto extends BungeeMessage {

    private final String playerName;
    private final UUID playerUuid;
    private final int amount;
    private final Long duration;
    private final String type;
    private final int lightLevel;
    private final SppLocation location;

    public XrayAlertBungeeDto(XrayEvent xrayEvent) {
        super(xrayEvent.getServerName());
        this.playerName = xrayEvent.getPlayer().getName();
        this.playerUuid = xrayEvent.getPlayer().getUniqueId();
        this.amount = xrayEvent.getAmount();
        this.duration = xrayEvent.getDuration().orElse(null);
        this.type = xrayEvent.getType().name();
        this.lightLevel = xrayEvent.getLightLevel();
        Location location = xrayEvent.getLocation();
        this.location = new SppLocation(location, xrayEvent.getServerName());
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public int getAmount() {
        return amount;
    }

    public Optional<Long> getDuration() {
        return Optional.ofNullable(duration);
    }

    public String getType() {
        return type;
    }

    public int getLightLevel() {
        return lightLevel;
    }

    public SppLocation getLocation() {
        return location;
    }
}
