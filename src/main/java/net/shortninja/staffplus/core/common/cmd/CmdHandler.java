package net.shortninja.staffplus.core.common.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.staffplusplus.craftbukkit.common.IProtocol;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import org.bukkit.command.Command;

import java.util.List;
import java.util.stream.Collectors;

@IocBean
@IocMultiProvider(PluginDisable.class)
public class CmdHandler implements PluginDisable {
    private final IProtocol versionProtocol;
    private final List<SppCommand> sppCommands;

    private final MessageCoordinator message;
    private final Messages messages;

    public List<BaseCmd> commands;

    public CmdHandler(IProtocol versionProtocol, @IocMulti(SppCommand.class) List<SppCommand> sppCommands, MessageCoordinator message, Messages messages) {
        this.versionProtocol = versionProtocol;
        this.sppCommands = sppCommands;
        this.message = message;
        this.messages = messages;
        registerCommands();
    }

    private void registerCommands() {
        commands = sppCommands.stream()
            .map(sppCommand -> new BaseCmd(message, messages, (Command) sppCommand))
            .collect(Collectors.toList());

        commands.forEach(baseCmd -> versionProtocol.registerCommand(baseCmd.getMatch(), baseCmd.getCommand()));
    }

    @Override
    public void disable(StaffPlus staffPlus) {
        commands.forEach(baseCmd -> versionProtocol.unregisterCommand(baseCmd.getMatch(), baseCmd.getCommand()));
    }
}