package net.shortninja.staffplus.core.domain.staff.protect.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.*;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;

import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.protect.ProtectService;
import net.shortninja.staffplus.core.domain.staff.protect.ProtectedArea;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.ONLINE;

@Command(
    command = "commands:commands.protect-area",
    permissions = "permissions:permissions.protect-area",
    description = "Protect an area around you.",
    usage = "[radius] [area name]",
    playerRetrievalStrategy = ONLINE
)
@IocBean(conditionalOnProperty = "protect-module.area-enabled=true")
@IocMultiProvider(SppCommand.class)
public class ProtectAreaCmd extends AbstractCmd {

    private static final String CREATE = "create";
    private static final String DELETE = "delete";

    private final ProtectService protectService;

    public ProtectAreaCmd(Messages messages,
                          Options options,
                          ProtectService protectService,
                          CommandService commandService,
                          PermissionHandler permissionHandler) {
        super(messages, permissionHandler, commandService);
        this.protectService = protectService;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        if(!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }

        String action = args[0];

        if(action.equalsIgnoreCase(CREATE)) {
            int radius = Integer.parseInt(args[1]);
            String name = JavaUtils.compileWords(args, 2);
            protectService.createProtectedArea(radius, name, (Player) sender);
            return true;
        }

        if(action.equalsIgnoreCase(DELETE)) {
            String name = JavaUtils.compileWords(args, 1);
            protectService.deleteProtectedArea((Player) sender, name);
            return true;
        }

        throw new BusinessException("&CPlease choose a correct action [create|delete]");
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if(args.length == 0) {
            return 3;
        }
        String action = args[0];
        if(action.equalsIgnoreCase(CREATE)) {
            return 3;
        }
        if(action.equalsIgnoreCase(DELETE)) {
            return 2;
        }
        return 3;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> suggestions = new ArrayList<>();
        if (args.length <= 1) {
            suggestions.add(CREATE);
            suggestions.add(DELETE);
            return suggestions.stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }
        if (args.length == 2) {
            String action = args[0];
            if(action.equalsIgnoreCase(CREATE)) {
                suggestions.add("5");
                suggestions.add("10");
                suggestions.add("20");
            }
            if(action.equalsIgnoreCase(DELETE)) {
                suggestions.addAll(protectService.getAllProtectedAreas().stream().map(ProtectedArea::getName).collect(Collectors.toList()));
            }
            return suggestions.stream()
                .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                .collect(Collectors.toList());
        }
        return suggestions;
    }
}
