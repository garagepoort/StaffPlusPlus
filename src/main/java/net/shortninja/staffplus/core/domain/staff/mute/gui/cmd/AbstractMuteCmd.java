package net.shortninja.staffplus.core.domain.staff.mute.gui.cmd;

import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;
import net.shortninja.staffplus.core.domain.staff.mute.config.MuteConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class AbstractMuteCmd extends AbstractCmd {

    protected final MuteConfiguration muteConfiguration;
    protected final MuteService muteService;
    protected final PlayerManager playerManager;

    public AbstractMuteCmd(
                    PermissionHandler permissionHandler,
                   Messages messages,
                   MuteService muteService,
                   CommandService commandService,
                   PlayerManager playerManager,
                   MuteConfiguration muteConfiguration) {
        super(messages, permissionHandler, commandService);
        this.muteService = muteService;
        this.playerManager = playerManager;
        this.muteConfiguration = muteConfiguration;
    }


    @Override
    protected List<String> getOptionalParameters() {
        return Arrays.asList("-soft", "-hard");
    }

    protected boolean isSoftMute(Map<String, String> optionalParameters) {
        if(optionalParameters.containsKey("-hard")) {
            return false;
        }
        if(optionalParameters.containsKey("-soft")) {
            return true;
        }
        return muteConfiguration.defaultSoftMutes;
    }
}
