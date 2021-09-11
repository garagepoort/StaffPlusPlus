package net.shortninja.staffplus.core.domain.staff.warn.threshold;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.CreateStoredCommandRequest;
import net.shortninja.staffplus.core.domain.actions.StoredCommandEntity;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommandMapper;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningThresholdConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.database.WarnRepository;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.warnings.IWarning;
import net.shortninja.staffplusplus.warnings.WarningThresholdReachedEvent;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class ThresholdService {


    private final WarnRepository warnRepository;
    private final Options options;
    private final ActionService actionService;
    private final ConfiguredCommandMapper configuredCommandMapper;
    private final PlayerManager playerManager;

    public ThresholdService(WarnRepository warnRepository, Options options, ActionService actionService, ConfiguredCommandMapper configuredCommandMapper, PlayerManager playerManager) {
        this.warnRepository = warnRepository;
        this.options = options;
        this.actionService = actionService;
        this.configuredCommandMapper = configuredCommandMapper;
        this.playerManager = playerManager;
    }

    public void handleThresholds(IWarning warning, SppPlayer user) {
        int totalScore = warnRepository.getTotalScore(user.getId());
        List<WarningThresholdConfiguration> thresholds = options.warningConfiguration.getThresholds();

        Optional<WarningThresholdConfiguration> threshold = thresholds.stream()
            .sorted((o1, o2) -> o2.getScore() - o1.getScore())
            .filter(w -> w.getScore() <= totalScore)
            .findFirst();
        if (!threshold.isPresent()) {
            return;
        }

        Optional<SppPlayer> culprit = playerManager.getOnOrOfflinePlayer(warning.getTargetUuid());

        List<CreateStoredCommandRequest> commands = configuredCommandMapper.toCreateRequests(
            getPlaceholders(culprit),
            getTargets(culprit),
            threshold.get().getActions(),
            Collections.emptyList());

        List<String> executedCommands = actionService.createCommands(commands)
            .stream()
            .map(StoredCommandEntity::getCommand)
            .collect(Collectors.toList());

        sendEvent(new WarningThresholdReachedEvent(user.getUsername(), user.getId(), threshold.get().getScore(), executedCommands));
    }

    @NotNull
    private Map<String, String> getPlaceholders(Optional<SppPlayer> culprit) {
        Map<String, String> placeholders = new HashMap<>();
        culprit.ifPresent(sppPlayer -> placeholders.put("%culprit%", sppPlayer.getUsername()));
        return placeholders;
    }

    @NotNull
    private Map<String, OfflinePlayer> getTargets(Optional<SppPlayer> culprit) {
        Map<String, OfflinePlayer> targets = new HashMap<>();
        culprit.ifPresent(sppPlayer -> targets.put("culprit", sppPlayer.getOfflinePlayer()));
        return targets;
    }
}
