package net.shortninja.staffplus.core.common.cmd.arguments;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

@IocBean
public class ArgumentProcessor {

    private final List<ArgumentExecutor> argumentExecutors;

    public ArgumentProcessor(@IocMulti(ArgumentExecutor.class) List<ArgumentExecutor> argumentExecutors) {
        this.argumentExecutors = argumentExecutors;
    }

    public void parseArguments(CommandSender commandSender, String playerName, List<String> options, List<ArgumentType> validTypes) {
        List<ArgumentExecutor> validExecutors = argumentExecutors.stream()
                .filter(a -> validTypes.contains(a.getType()))
                .collect(Collectors.toList());

        for (String option : options) {
            Optional<ArgumentExecutor> argumentExecutor = validExecutors.stream()
                    .filter(a -> option.startsWith(a.getType().getPrefix()))
                    .findFirst();

            if (argumentExecutor.isPresent()) {
                String value = option.replace(argumentExecutor.get().getType().getPrefix(), "");
                Bukkit.getLogger().log(Level.INFO, "Executing argument: [" + argumentExecutor.get().getType() + "]");
                argumentExecutor.get().execute(commandSender, playerName, value);
            }
        }
    }

    public List<String> getArgumentsSuggestions(CommandSender commandSender, String currentArg, List<ArgumentType> validTypes) {
        List<ArgumentExecutor> validExecutors = argumentExecutors.stream()
                .filter(a -> validTypes.contains(a.getType()))
                .filter(a -> currentArg.length() <= 1 || a.getType().getPrefix().startsWith(currentArg))
                .collect(Collectors.toList());

        return validExecutors.stream().flatMap(e -> e.complete(commandSender, currentArg).stream()).collect(Collectors.toList());
    }

}
