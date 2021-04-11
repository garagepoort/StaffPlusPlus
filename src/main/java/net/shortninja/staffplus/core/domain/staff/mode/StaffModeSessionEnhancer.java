package net.shortninja.staffplus.core.session;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.data.DataFile;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.core.session.database.SessionEntity;
import net.shortninja.staffplus.core.session.database.SessionsRepository;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Optional;

@IocBean
@IocMultiProvider(SessionEnhancer.class)
public class DefaultSessionEnhancer implements SessionEnhancer {
    private final SessionsRepository sessionsRepository;
    private final Options options;
    private final FileConfiguration dataFileConfiguration;
    private final StaffModeService staffModeService;

    public DefaultSessionEnhancer(SessionsRepository sessionsRepository, Options options, DataFile dataFile, StaffModeService staffModeService) {
        this.sessionsRepository = sessionsRepository;
        this.options = options;
        this.dataFileConfiguration = dataFile.getConfiguration();
        this.staffModeService = staffModeService;
    }

    @Override
    public void enhance(PlayerSession playerSession) {
        VanishType vanishType = VanishType.valueOf(dataFileConfiguration.getString(playerSession.getUuid() + ".vanish-type", "NONE"));
        boolean staffMode = dataFileConfiguration.getBoolean(playerSession.getUuid() + ".staff-mode", false);
        String staffModeName = dataFileConfiguration.getString(playerSession.getUuid() + ".staff-mode-name", null);

        Optional<SessionEntity> session = sessionsRepository.findSession(playerSession.getUuid());
        if (options.serverSyncConfiguration.isStaffModeSyncEnabled()) {
            playerSession.setInStaffMode(session.map(SessionEntity::getStaffMode).orElse(staffMode));
            playerSession.setModeConfiguration(session.map(SessionEntity::getStaffMode).orElse(staffModeName));
        }
        if (options.serverSyncConfiguration.isVanishSyncEnabled()) {
            playerSession.setVanishType(session.map(SessionEntity::getVanishType).orElse(vanishType));
        }
        playerSession.setStaffChatMuted(session.map(SessionEntity::isStaffChatMuted).orElse(false));
    }
}
