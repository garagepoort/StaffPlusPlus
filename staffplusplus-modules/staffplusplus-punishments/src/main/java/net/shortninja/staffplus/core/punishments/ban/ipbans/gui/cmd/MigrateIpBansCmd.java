package net.shortninja.staffplus.core.punishments.ban.ipbans.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.punishments.ban.ipbans.IpBan;
import net.shortninja.staffplus.core.punishments.ban.ipbans.database.IpBanRepository;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.NONE;

@Command(
    command = "commands:ipban.migrate",
    permissions = "permissions:ipban.migrate",
    description = "Migrates default ip bans to staff++ ip bans",
    playerRetrievalStrategy = NONE
)
@IocBean(conditionalOnProperty = "ban-module.enabled=true && ban-module.ipban.enabled=true")
@IocMultiProvider(SppCommand.class)
public class MigrateIpBansCmd extends AbstractCmd {

    private final BukkitUtils bukkitUtils;
    private final IpBanRepository bansRepository;
    private final Options options;

    public MigrateIpBansCmd(PermissionHandler permissionHandler,
                            Messages messages,
                            BukkitUtils bukkitUtils,
                            CommandService commandService,
                            IpBanRepository bansRepository,
                            Options options) {
        super(messages, permissionHandler, commandService);
        this.bukkitUtils = bukkitUtils;
        this.bansRepository = bansRepository;
        this.options = options;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        BanList banList = Bukkit.getBanList(BanList.Type.IP);

        Set<BanEntry> banEntries = banList.getBanEntries();
        bukkitUtils.runTaskAsync(sender, () -> {
            AtomicInteger count = new AtomicInteger();
            for (BanEntry banEntry : banEntries) {
                Long endDate = banEntry.getExpiration() == null ? null : banEntry.getExpiration().getTime();
                IpBan ban = new IpBan(banEntry.getTarget(), banEntry.getCreated().getTime(), endDate, "Console", Constants.CONSOLE_UUID, options.serverName, false, null);
                bansRepository.saveBan(ban);
                count.getAndIncrement();
            }

            bukkitUtils.runTaskLater(() -> {
                for (BanEntry banEntry : banEntries) {
                    banList.pardon(banEntry.getTarget());
                }
            });

            messages.send(sender, "&C" + count.get() + " &6Ip bans have been migrated", messages.prefixBans);
        });
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 0;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }
}
