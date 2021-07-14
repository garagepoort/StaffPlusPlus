package net.shortninja.staffplus.core.common.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.ReflectionUtils;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.IProtocolService;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@IocBean
@IocMultiProvider(PluginDisable.class)
public class CmdHandler implements PluginDisable {
    private final IProtocolService versionProtocol;
    private final List<SppCommand> sppCommands;


    private final Messages messages;

    public List<BaseCmd> commands;

    public CmdHandler(IProtocolService protocolService, @IocMulti(SppCommand.class) List<SppCommand> sppCommands, Messages messages) {
        this.versionProtocol = protocolService;
        this.sppCommands = sppCommands;

        this.messages = messages;
        registerCommands();
    }

    private void registerCommands() {
        commands = sppCommands.stream()
            .filter(s -> StringUtils.isNotBlank(((BukkitCommand) s).getName()))
            .peek(this::injectPermissions)
            .map(sppCommand -> new BaseCmd(messages, (org.bukkit.command.Command) sppCommand))
            .collect(Collectors.toList());

        commands.forEach(baseCmd -> versionProtocol.getVersionProtocol().registerCommand(baseCmd.getMatch(), baseCmd.getCommand()));
    }

    private void injectPermissions(SppCommand s) {
        Command command = s.getClass().getAnnotation(Command.class);
        if (command != null) {
            Set<String> permissions = Arrays.stream(command.permissions())
                .map(p -> ReflectionUtils.getConfigValue(p, StaffPlus.get().getFileConfigurations()))
                .filter(Optional::isPresent)
                .map(p -> (String) p.get())
                .collect(Collectors.toSet());

            AbstractCmd abstractCmd = (AbstractCmd) s;
            permissions.forEach(abstractCmd::setPermission);
            abstractCmd.setDescription(command.description());
            abstractCmd.setUsage(command.usage());
        }
    }

    @Override
    public void disable(StaffPlus staffPlus) {
        commands.forEach(baseCmd -> versionProtocol.getVersionProtocol().unregisterCommand(baseCmd.getMatch(), baseCmd.getCommand()));
    }
}