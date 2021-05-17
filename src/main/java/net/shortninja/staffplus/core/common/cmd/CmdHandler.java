package net.shortninja.staffplus.core.common.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.config.Messages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.List;
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
            .map(sppCommand -> new BaseCmd(messages, (Command) sppCommand))
            .collect(Collectors.toList());

        commands.forEach(baseCmd -> versionProtocol.getVersionProtocol().registerCommand(baseCmd.getMatch(), baseCmd.getCommand()));
    }

    @Override
    public void disable(StaffPlus staffPlus) {
        commands.forEach(baseCmd -> versionProtocol.getVersionProtocol().unregisterCommand(baseCmd.getMatch(), baseCmd.getCommand()));
    }
}