package net.shortninja.staffplus.core.common.cmd.arguments;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Collections;
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

    public void parseArguments(CommandSender commandSender, String playerName, String[] options, List<ArgumentType> validTypes) {
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

    public List<String> getTabCompletion(String currentArg, ArgumentType argumentType) {
        Optional<ArgumentExecutor> argumentExecutor = argumentExecutors.stream().filter(a -> a.getType() == argumentType).findFirst();
        if(argumentExecutor.isPresent()) {
            return argumentExecutor.get().complete(currentArg);
        }
        return Collections.emptyList();
    }
}
