package net.shortninja.staffplus.staff.warn;

import net.shortninja.staffplus.common.actions.ActionFilter;
import net.shortninja.staffplus.common.actions.ExecutableAction;
import net.shortninja.staffplus.player.SppPlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class WarningActionFilter implements ActionFilter {

    private static final String SEVERITY = "severity";
    private Warning warning;

    WarningActionFilter(Warning warning) {
        this.warning = warning;
    }

    @Override
    public boolean isValidAction(CommandSender sender, SppPlayer target, ExecutableAction executableAction) {

        return !executableAction.getFilters().containsKey(SEVERITY) || Arrays.asList(executableAction.getFilters().get(SEVERITY).split(",")).contains(warning.getSeverity());
    }
}
