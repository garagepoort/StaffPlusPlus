package net.shortninja.staffplus.staff.tracing;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.server.command.cmd.StaffPlusPlusCmd;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TraceCmd extends StaffPlusPlusCmd {

    public static final String START = "start";
    public static final String STOP = "stop";
    private TraceService traceService;

    public TraceCmd(String name) {
        super(name, IocContainer.getOptions().permissionTrace);
        traceService = IocContainer.getTraceService();
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args) {
        String command = args[0];
        if(command.equalsIgnoreCase(START)) {
            String playerName = args[1];
            traceService.startTrace(sender, playerName);
            return true;
        }
        if(command.equalsIgnoreCase(STOP)) {
            traceService.stopTrace(sender);
            return true;
        }
        return true;
    }

    @Override
    protected String getPlayerName(String[] args) {
        if(args.length > 1) {
            return args[1];
        }
        return null;
    }

    @Override
    protected int getMinimumArguments(String[] args) {
        if(args[0].equalsIgnoreCase(START)) {
            return 2;
        }
        return 1;
    }

    @Override
    protected boolean isDelayable() {
        return false;
    }

    @Override
    protected boolean canBypass(Player player) {
        return player.hasPermission(options.permissionTraceBypass);
    }
}
