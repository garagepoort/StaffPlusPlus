package net.shortninja.staffplus.staff.ban.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.ban.BanService;
import net.shortninja.staffplus.common.time.TimeUnit;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.common.JavaUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TempBanCmd extends AbstractCmd {

    private static final String TEMPLATE_FILE = "-template=";
    private final BanService banService = IocContainer.getBanService();
    private PermissionHandler permissionHandler;
    private Options options;

    public TempBanCmd(String name) {
        super(name, IocContainer.getOptions().banConfiguration.getPermissionBanPlayer());
        permissionHandler = IocContainer.getPermissionHandler();
        options = IocContainer.getOptions();
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if (!JavaUtils.isInteger(args[1])) {
            throw new BusinessException(messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()));
        }

        int amount = Integer.parseInt(args[1]);
        String timeUnit = args[2];

        if (args[3].toLowerCase().startsWith(TEMPLATE_FILE.toLowerCase())) {
            String template = getTemplateName(args[3]);
            String reason = JavaUtils.compileWords(args, 4);
            banService.tempBan(sender, player, TimeUnit.getDuration(timeUnit, amount), reason, template);
            return true;
        }


        String reason = JavaUtils.compileWords(args, 3);
        banService.tempBan(sender, player, TimeUnit.getDuration(timeUnit, amount), reason);
        return true;
    }

    private String getTemplateName(String arg) {
        String[] templateParams = arg.split("=");
        if (templateParams.length != 2) {
            throw new BusinessException("&CInvalid template provided");
        }
        return templateParams[1];
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if (args.length >= 4 && args[3].toLowerCase().contains(TEMPLATE_FILE.toLowerCase())) {
            return 5;
        }
        return 4;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.BOTH;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.ofNullable(args[0]);
    }

    @Override
    protected boolean canBypass(Player player) {
        return permissionHandler.has(player, options.banConfiguration.getPermissionBanByPass());
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {

        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }
        if (args.length == 2) {
            return Stream.of("5", "10", "15", "20")
                .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                .collect(Collectors.toList());
        }
        if (args.length == 3) {
            return Stream.of(
                TimeUnit.YEAR.name(), TimeUnit.MONTH.name(),
                TimeUnit.WEEK.name(), TimeUnit.DAY.name(),
                TimeUnit.HOUR.name(), TimeUnit.MINUTE.name())
                .filter(s -> args[2].isEmpty() || s.contains(args[2]))
                .collect(Collectors.toList());
        }
        if (args.length == 4 && args[3].startsWith("-")) {
            return options.banConfiguration.getTemplates().keySet().stream()
                .map(k -> TEMPLATE_FILE + k)
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
