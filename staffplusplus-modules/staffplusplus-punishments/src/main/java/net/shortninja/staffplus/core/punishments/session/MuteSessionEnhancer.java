package net.shortninja.staffplus.core.punishments.session;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionEnhancer;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.punishments.mute.MuteService;

@IocBean(conditionalOnProperty = "mute-module.enabled=true")
@IocMultiProvider(SessionEnhancer.class)
public class MuteSessionEnhancer implements SessionEnhancer {
    private final MuteService muteService;
    private final BukkitUtils bukkitUtils;

    public MuteSessionEnhancer(MuteService muteService, BukkitUtils bukkitUtils) {
        this.muteService = muteService;
        this.bukkitUtils = bukkitUtils;
    }

    @Override
    public void enhance(PlayerSession playerSession) {
        bukkitUtils.runTaskAsync(() -> muteService.getMuteByMutedUuid(playerSession.getUuid()).ifPresent((m) -> playerSession.setMuted(true)));
    }
}
