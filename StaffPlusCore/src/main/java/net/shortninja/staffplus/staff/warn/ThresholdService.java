package net.shortninja.staffplus.staff.warn;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.actions.ActionService;
import net.shortninja.staffplus.common.actions.ExecutableAction;
import net.shortninja.staffplus.event.warnings.WarningThresholdReachedEvent;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.warn.config.WarningThresholdConfiguration;
import net.shortninja.staffplus.staff.warn.database.WarnRepository;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getScheduler;

public class ThresholdService {


    private final WarnRepository warnRepository;
    private final Options options;
    private final ActionService actionService;

    public ThresholdService(WarnRepository warnRepository, Options options, ActionService actionService) {
        this.warnRepository = warnRepository;
        this.options = options;
        this.actionService = actionService;
    }

    public void handleThresholds(CommandSender sender, SppPlayer user) {
        int totalScore = warnRepository.getTotalScore(user.getId());
        List<WarningThresholdConfiguration> thresholds = options.warningConfiguration.getThresholds();

        Optional<WarningThresholdConfiguration> threshold = thresholds.stream()
            .sorted((o1, o2) -> o2.getScore() - o1.getScore())
            .filter(w -> w.getScore() <= totalScore)
            .findFirst();
        if (!threshold.isPresent()) {
            return;
        }

        List<String> executedCommands = actionService.executeActions(sender, user, threshold.get().getActions()).stream()
            .map(ExecutableAction::getCommand)
            .collect(Collectors.toList());

        sendEvent(new WarningThresholdReachedEvent(user.getUsername(), user.getId(), threshold.get().getScore(), executedCommands));
    }


    private void sendEvent(Event event) {
        getScheduler().runTask(StaffPlus.get(), () -> {
            Bukkit.getPluginManager().callEvent(event);
        });
    }
}
