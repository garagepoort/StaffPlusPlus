package net.shortninja.staffplus.core.domain.staff.ban.appeals;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanService;
import net.shortninja.staffplusplus.appeals.AppealApprovedEvent;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.appeals.IAppeal;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean
@IocListener
public class BanAppealedListener implements Listener {

    private final BanService banService;
    private final BanAppealConfiguration banAppealConfiguration;

    public BanAppealedListener(BanService banService,
                               BanAppealConfiguration banAppealConfiguration) {
        this.banService = banService;
        this.banAppealConfiguration = banAppealConfiguration;
    }

    @EventHandler
    public void handleAppealApproved(AppealApprovedEvent event) {
        if (event.getAppeal().getType() != AppealableType.BAN || !banAppealConfiguration.unbanOnApprove) {
            return;
        }

        IAppeal appeal = event.getAppeal();
        SppPlayer sppPlayer = new SppPlayer(appeal.getResolverUuid(), appeal.getResolverName(), Bukkit.getOfflinePlayer(appeal.getResolverUuid()));
        banService.unban(sppPlayer, appeal.getAppealableId(), "Appeal approved");
    }

}
