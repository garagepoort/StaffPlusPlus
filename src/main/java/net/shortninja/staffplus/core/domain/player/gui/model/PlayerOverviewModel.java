package net.shortninja.staffplus.core.domain.player.gui.model;

import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBan;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.Ban;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import net.shortninja.staffplus.core.domain.staff.reporting.Report;
import net.shortninja.staffplusplus.warnings.IWarning;

import java.util.List;
import java.util.Optional;

public class PlayerOverviewModel {

    private final Optional<Ban> ban;
    private final Optional<Mute> mute;
    private final List<IpBan> ipbans;
    private final List<Report> reports;
    private final List<Report> reported;
    private final List<? extends IWarning> warnings;

    public PlayerOverviewModel(Optional<Ban> ban, Optional<Mute> mute, List<IpBan> ipbans, List<Report> reports, List<Report> reported, List<? extends IWarning> warnings) {
        this.ban = ban;
        this.mute = mute;
        this.ipbans = ipbans;
        this.reports = reports;
        this.reported = reported;
        this.warnings = warnings;
    }

    public Optional<Ban> getBan() {
        return ban;
    }

    public Optional<Mute> getMute() {
        return mute;
    }

    public List<IpBan> getIpbans() {
        return ipbans;
    }

    public List<Report> getReports() {
        return reports;
    }

    public List<Report> getReported() {
        return reported;
    }

    public List<? extends IWarning> getWarnings() {
        return warnings;
    }
}
