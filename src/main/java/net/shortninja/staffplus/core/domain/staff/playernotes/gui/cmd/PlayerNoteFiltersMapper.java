package net.shortninja.staffplus.core.domain.staff.playernotes.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.playernotes.PlayerNoteFilters;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.util.Arrays;
import java.util.List;

@IocBean
public class PlayerNoteFiltersMapper {

    private static final String ID = "id";
    private static final String TARGET = "target";
    private static final String NOTED_BY = "notedBy";
    private static final String PRIVATE = "private";
    private static final String NOTE = "note";

    private final PlayerManager playerManager;

    public PlayerNoteFiltersMapper(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }


    public List<String> getFilterKeys() {
        return Arrays.asList(ID, TARGET, NOTED_BY, NOTE, PRIVATE);
    }

    public void map(String key, String value, PlayerNoteFilters.PlayerNoteFiltersBuilder playerNoteFiltersBuilder) {
        if (key.equalsIgnoreCase(ID)) {
            playerNoteFiltersBuilder.id(Integer.parseInt(value));
        }
        if (key.equalsIgnoreCase(TARGET)) {
            SppPlayer reporter = playerManager.getOnOrOfflinePlayer(value).orElseThrow(() -> new PlayerNotFoundException(value));
            playerNoteFiltersBuilder.target(reporter);
        }
        if (key.equalsIgnoreCase(NOTED_BY)) {
            playerNoteFiltersBuilder.notedByName(value);
        }
        if (key.equalsIgnoreCase(PRIVATE)) {
            boolean isPrivate = Boolean.parseBoolean(value);
            if (isPrivate) playerNoteFiltersBuilder.isPrivate();
            else playerNoteFiltersBuilder.isPublic();
        }
        if (key.equalsIgnoreCase(NOTE)) {
            playerNoteFiltersBuilder.note(value);
        }
    }
}
