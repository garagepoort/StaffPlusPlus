package net.shortninja.staffplus.core.domain.staff.mute.appeals;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;
import net.shortninja.staffplusplus.appeals.AppealApprovedEvent;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.appeals.IAppeal;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean
@IocListener
public class MuteAppealedListener implements Listener {

    private final MuteService muteService;
    private final MuteAppealConfiguration muteAppealConfiguration;

    public MuteAppealedListener(MuteService muteService,
                                MuteAppealConfiguration muteAppealConfiguration) {
        this.muteService = muteService;
        this.muteAppealConfiguration = muteAppealConfiguration;
    }

    @EventHandler
    public void handleAppealApproved(AppealApprovedEvent event) {
        if (event.getAppeal().getType() != AppealableType.MUTE || !muteAppealConfiguration.unmuteOnApprove) {
            return;
        }

        IAppeal appeal = event.getAppeal();
        SppPlayer sppPlayer = new SppPlayer(appeal.getResolverUuid(), appeal.getResolverName(), Bukkit.getOfflinePlayer(appeal.getResolverUuid()));
        muteService.unmute(sppPlayer, appeal.getAppealableId(), "Appeal approved");
    }

}
