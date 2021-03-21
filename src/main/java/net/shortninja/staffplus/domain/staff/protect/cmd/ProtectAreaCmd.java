package net.shortninja.staffplus.domain.staff.protect.cmd;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.JavaUtils;
import net.shortninja.staffplus.common.cmd.AbstractCmd;
import net.shortninja.staffplus.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.domain.staff.protect.ProtectService;
import net.shortninja.staffplus.domain.staff.protect.ProtectedArea;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProtectAreaCmd extends AbstractCmd {

    private static final String CREATE = "create";
    private static final String DELETE = "delete";
    private final ProtectService protectService = IocContainer.getProtectService();

    public ProtectAreaCmd(String name) {
        super(name, IocContainer.getOptions().protectConfiguration.getPermissionProtectArea());
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
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
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.NONE;
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
