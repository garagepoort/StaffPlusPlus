package net.shortninja.staffplus.core.common.gui;

import be.garagepoort.mcioc.gui.GuiActionBuilder;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplusplus.session.IPlayerSession;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.TimeZone;

public class GuiUtils {


    public static String getPreviousPage(String currentAction, int currentPage) {
        return GuiActionBuilder.fromAction(currentAction)
            .param("page", String.valueOf((currentPage - 1)))
            .build();
    }

    public static String getNextPage(String currentAction, int currentPage) {
        return GuiActionBuilder.fromAction(currentAction)
            .param("page", String.valueOf((currentPage + 1)))
            .build();
    }

    public static String parseTimestamp(long timestamp, String format) {
        LocalDateTime localDateTime = getLocalDateTime(timestamp);
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static String parseTimestampSeconds(long timestamp, String format) {
        LocalDateTime localDateTime = getLocalDateTime(timestamp);
        return localDateTime.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ofPattern(format));
    }

    public static LocalDateTime getLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId());
    }

    public static Optional<IPlayerSession> getSession(SppPlayer sppPlayer) {
        OnlineSessionsManager onlineSessionsManager = StaffPlus.get().getIocContainer().get(OnlineSessionsManager.class);
        if (onlineSessionsManager.has(sppPlayer.getId())) {
            return Optional.ofNullable(onlineSessionsManager.get(sppPlayer.getId()));
        }
        return Optional.empty();
    }

    public static String parseLocation(Location location) {
        return location.getWorld().getName() + " &8 | &7" + JavaUtils.serializeLocation(location);
    }

    public static String getTimePlayed(Player p) {
        int secondsPlayed = p.getPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;
        return JavaUtils.toHumanReadableDuration(secondsPlayed * 1000L);
    }
}
