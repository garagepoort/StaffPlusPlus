package net.shortninja.staffplus.ui;

import net.shortninja.staffplus.teleport.TeleportArgumentExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class ArgumentProcessor {

    private final List<ArgumentExecutor> argumentExecutors = Arrays.asList(new TeleportArgumentExecutor());

    private static ArgumentProcessor instance;

    public static ArgumentProcessor getInstance() {
        if (instance == null) {
            instance = new ArgumentProcessor();
        }
        return instance;
    }

    public void parseArguments(CommandSender commandSender, String playerName, List<String> options) {
        for (String option : options) {
            Optional<ArgumentExecutor> argumentExecutor = argumentExecutors.stream()
                    .filter(a -> option.startsWith(a.getArgsPrefix()))
                    .findFirst();


            if (argumentExecutor.isPresent()) {
                String value = option.replace(argumentExecutor.get().getArgsPrefix(), "");
                Bukkit.getLogger().log(Level.INFO, "Executing argument: [" + argumentExecutor.get().getArgsPrefix() + "]");
                argumentExecutor.get().execute(commandSender, playerName, value);
            }
        }
    }

}
