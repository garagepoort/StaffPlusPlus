package net.shortninja.staffplus.core.common.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.ReflectionUtils;
import be.garagepoort.mcioc.configuration.ConfigurationLoader;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import be.garagepoort.mcioc.tubingbukkit.annotations.BeforeTubingReload;
import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.exceptions.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@IocBean
@IocMultiProvider({PluginDisable.class, BeforeTubingReload.class})
public class CmdHandler implements PluginDisable, BeforeTubingReload {
    private final ConfigurationLoader configurationLoader;
    private final List<SppCommand> sppCommands;
    private final Messages messages;
    public List<BaseCmd> commands;

    public CmdHandler(ConfigurationLoader configurationLoader, @IocMulti(SppCommand.class) List<SppCommand> sppCommands, Messages messages) {
        this.configurationLoader = configurationLoader;
        this.sppCommands = sppCommands;
        this.messages = messages;
        registerCommands();
    }

    private void registerCommands() {
        commands = sppCommands.stream()
            .peek(this::processCommandAnnotation)
            .filter(s -> StringUtils.isNotBlank(((BukkitCommand) s).getName()))
            .map(sppCommand -> new BaseCmd(messages, (org.bukkit.command.Command) sppCommand))
            .collect(Collectors.toList());

        CommandMap commandMap = getCommandMap();
        commands.forEach(baseCmd -> commandMap.register(baseCmd.getMatch(), baseCmd.getCommand()));

    }

    private static CommandMap getCommandMap() {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            return commandMap;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void processCommandAnnotation(SppCommand s) {
        Command command = s.getClass().getAnnotation(Command.class);
        if (command != null) {
            Set<String> permissions = Arrays.stream(command.permissions())
                .filter(StringUtils::isNotBlank)
                .map(p -> (String) ReflectionUtils.getConfigValue(p, configurationLoader.getConfigurationFiles()).orElseThrow(() -> new ConfigurationException("Invalid permission: " + p)))
                .collect(Collectors.toSet());

            AbstractCmd abstractCmd = (AbstractCmd) s;
            abstractCmd.setPermissions(permissions);
            abstractCmd.setDescription(command.description());
            abstractCmd.setUsage(command.usage());
            abstractCmd.setDelayable(command.delayable());
            abstractCmd.setReplaceDoubleQoutesEnabled(command.replaceDoubleQuotes());
            abstractCmd.setPlayerRetrievalStrategy(command.playerRetrievalStrategy());
            if (StringUtils.isNotBlank(command.command())) {
                List<String> commandNames = (List<String>) ReflectionUtils.getConfigValue(command.command(), configurationLoader.getConfigurationFiles())
                    .orElseThrow(() -> new ConfigurationException("Invalid command name: " + command.command()));
                if (commandNames.size() > 0) {
                    abstractCmd.setName(commandNames.get(0));
                    abstractCmd.setAliases(commandNames);
                }
            }
        }
    }

    @Override
    public void disable(StaffPlusPlus staffPlusPlus) {
        CommandMap commandMap = getCommandMap();
        commands.forEach(baseCmd -> baseCmd.getCommand().unregister(commandMap));
    }

    @Override
    public void execute(TubingBukkitPlugin tubingPlugin) {
        CommandMap commandMap = getCommandMap();
        commands.forEach(baseCmd -> baseCmd.getCommand().unregister(commandMap));
    }
}