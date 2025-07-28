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
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.exceptions.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@IocBean
@IocMultiProvider({PluginDisable.class, BeforeTubingReload.class})
public class CmdHandler implements PluginDisable, BeforeTubingReload {
    private final IProtocolService versionProtocol;
    private final ConfigurationLoader configurationLoader;
    private final List<SppCommand> sppCommands;
    private final Messages messages;
    public List<BaseCmd> commands;

    public CmdHandler(IProtocolService protocolService, ConfigurationLoader configurationLoader, @IocMulti(SppCommand.class) List<SppCommand> sppCommands, Messages messages) {
        this.versionProtocol = protocolService;
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

        commands.forEach(baseCmd -> {
            versionProtocol.getVersionProtocol().registerCommand(baseCmd.getMatch(), baseCmd.getCommand());
            for (String permission : baseCmd.getPermissions()) {
                Bukkit.getServer().getPluginManager().addPermission(new Permission(permission, PermissionDefault.FALSE));
            }
        });
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
            abstractCmd.setAsync(command.async());
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
        commands.forEach(baseCmd -> versionProtocol.getVersionProtocol().unregisterCommand(baseCmd.getMatch(), baseCmd.getCommand()));
    }

    @Override
    public void execute(TubingBukkitPlugin tubingPlugin) {
        commands.forEach(baseCmd -> versionProtocol.getVersionProtocol().unregisterCommand(baseCmd.getMatch(), baseCmd.getCommand()));
    }
}