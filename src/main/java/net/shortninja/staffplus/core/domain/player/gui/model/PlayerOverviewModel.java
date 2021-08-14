package net.shortninja.staffplus.core.domain.player.gui.model;

import net.shortninja.staffplus.core.domain.staff.ban.playerbans.Ban;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;

import java.util.Optional;

public class PlayerOverviewModel {

    private final Optional<Ban> ban;
    private Optional<Mute> mute;

    public PlayerOverviewModel(Optional<Ban> ban, Optional<Mute> mute) {
        this.ban = ban;
        this.mute = mute;
    }

    public Optional<Ban> getBan() {
        return ban;
    }

    public Optional<Mute> getMute() {
        return mute;
    }
}
