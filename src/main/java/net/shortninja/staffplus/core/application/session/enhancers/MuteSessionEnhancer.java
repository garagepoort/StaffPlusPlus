package net.shortninja.staffplus.core.application.session.enhancers;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionEnhancer;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;

@IocBean
@IocMultiProvider(SessionEnhancer.class)
public class MuteSessionEnhancer implements SessionEnhancer {
    private final MuteService muteService;

    public MuteSessionEnhancer(MuteService muteService) {
        this.muteService = muteService;
    }

    @Override
    public void enhance(PlayerSession playerSession) {
        muteService.getMuteByMutedUuid(playerSession.getUuid()).ifPresent((m) -> playerSession.setMuted(true));
    }
}
