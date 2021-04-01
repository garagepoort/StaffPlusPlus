package net.shortninja.staffplus.core.domain.staff.warn.threshold;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningThresholdConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.database.WarnRepository;
import net.shortninja.staffplusplus.warnings.IWarning;
import net.shortninja.staffplusplus.warnings.WarningThresholdReachedEvent;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class ThresholdService {


    private final WarnRepository warnRepository;
    private final Options options;
    private final ActionService actionService;

    public ThresholdService(WarnRepository warnRepository, Options options, ActionService actionService) {
        this.warnRepository = warnRepository;
        this.options = options;
        this.actionService = actionService;
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

        List<String> executedCommands = actionService.executeActions(warning, configuredAction -> user, threshold.get().getActions()).stream()
            .map(ConfiguredAction::getCommand)
            .collect(Collectors.toList());

        sendEvent(new WarningThresholdReachedEvent(user.getUsername(), user.getId(), threshold.get().getScore(), executedCommands));
    }
}
