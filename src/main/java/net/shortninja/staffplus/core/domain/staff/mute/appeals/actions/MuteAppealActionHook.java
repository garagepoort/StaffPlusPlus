package net.shortninja.staffplus.core.domain.staff.mute.appeals.actions;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommandMapper;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import net.shortninja.staffplus.core.domain.staff.mute.appeals.MuteAppealConfiguration;
import net.shortninja.staffplus.core.domain.staff.mute.database.MuteRepository;
import net.shortninja.staffplusplus.appeals.AppealApprovedEvent;
import net.shortninja.staffplusplus.appeals.AppealRejectedEvent;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.appeals.IAppeal;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;

@IocBean
@IocListener
public class MuteAppealActionHook implements Listener {

    private final ActionService actionService;
    private final ConfiguredCommandMapper configuredCommandMapper;
    private final BukkitUtils bukkitUtils;
    private final MuteRepository muteRepository;
    private final PlayerManager playerManager;
    private final MuteAppealConfiguration muteAppealConfiguration;

    public MuteAppealActionHook(ActionService actionService,
                                ConfiguredCommandMapper configuredCommandMapper,
                                BukkitUtils bukkitUtils,
                                MuteRepository muteRepository,
                                PlayerManager playerManager,
                                MuteAppealConfiguration muteAppealConfiguration) {
        this.actionService = actionService;
        this.configuredCommandMapper = configuredCommandMapper;
        this.bukkitUtils = bukkitUtils;
        this.muteRepository = muteRepository;
        this.playerManager = playerManager;
        this.muteAppealConfiguration = muteAppealConfiguration;
    }

    @EventHandler
    public void handleAppealApproved(AppealApprovedEvent event) {
        if (event.getAppeal().getType() != AppealableType.MUTE) {
            return;
        }

        IAppeal appeal = event.getAppeal();
        Mute mute = muteRepository.getMute(appeal.getAppealableId())
            .orElseThrow(() -> new BusinessException("No mute found."));

        executeActions(mute, muteAppealConfiguration.onApprovedCommands);
    }

    @EventHandler
    public void handleAppealRejected(AppealRejectedEvent event) {
        if (event.getAppeal().getType() != AppealableType.MUTE) {
            return;
        }

        IAppeal appeal = event.getAppeal();
        Mute mute = muteRepository.getMute(appeal.getAppealableId())
            .orElseThrow(() -> new BusinessException("No mute found."));

        executeActions(mute, muteAppealConfiguration.onRejectedCommands);
    }

    private void executeActions(Mute mute, List<ConfiguredCommand> concludeInvestigationCommands) {
        Optional<SppPlayer> issuer = playerManager.getOnlinePlayer(mute.getIssuerUuid());
        Optional<SppPlayer> culprit = playerManager.getOnlinePlayer(mute.getTargetUuid());

        bukkitUtils.runTaskAsync(() -> {
            Map<String, String> placeholders = new HashMap<>();
            issuer.ifPresent(sppPlayer -> placeholders.put("%issuer%", sppPlayer.getUsername()));
            culprit.ifPresent(sppPlayer -> placeholders.put("%target%", sppPlayer.getUsername()));
            culprit.ifPresent(sppPlayer -> placeholders.put("%culprit%", sppPlayer.getUsername()));

            Map<String, OfflinePlayer> targets = new HashMap<>();
            issuer.ifPresent(sppPlayer -> targets.put("issuer", sppPlayer.getOfflinePlayer()));
            culprit.ifPresent(sppPlayer -> targets.put("target", sppPlayer.getOfflinePlayer()));
            culprit.ifPresent(sppPlayer -> targets.put("culprit", sppPlayer.getOfflinePlayer()));

            actionService.createCommands(configuredCommandMapper.toCreateRequests(concludeInvestigationCommands, placeholders, targets, emptyList()));
        });
    }
}
