package net.shortninja.staffplus.core.domain.staff.ban.playerbans.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.Ban;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.database.BansRepository;
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
    command = "commands:bans-migrate",
    permissions = "permissions:bans.migrate",
    description = "Migrates default bans to staff++ bans",
    playerRetrievalStrategy = NONE
)
@IocBean(conditionalOnProperty = "ban-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class MigrateBansCmd extends AbstractCmd {

    private final BukkitUtils bukkitUtils;
    private final BansRepository bansRepository;
    private final PlayerManager playerManager;

    public MigrateBansCmd(PermissionHandler permissionHandler,
                          Messages messages,
                          BukkitUtils bukkitUtils,
                          CommandService commandService, BansRepository bansRepository, PlayerManager playerManager) {
        super(messages, permissionHandler, commandService);
        this.bukkitUtils = bukkitUtils;
        this.bansRepository = bansRepository;
        this.playerManager = playerManager;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);

        Set<BanEntry> banEntries = banList.getBanEntries();
        bukkitUtils.runTaskAsync(sender, () -> {
            AtomicInteger count = new AtomicInteger();
            for (BanEntry banEntry : banEntries) {
                Long endDate = banEntry.getExpiration() == null ? null : banEntry.getExpiration().getTime();

                Optional<SppPlayer> onOrOfflinePlayer = playerManager.getOnOrOfflinePlayer(banEntry.getTarget());
                onOrOfflinePlayer.ifPresent(p -> {
                    Ban ban = new Ban(banEntry.getReason(), banEntry.getCreated().getTime(), endDate, "Console", Constants.CONSOLE_UUID, banEntry.getTarget(), p.getId(), false, null);
                    bansRepository.addBan(ban);
                    count.getAndIncrement();
                });
            }

            bukkitUtils.runTaskLater(() -> {
                for (BanEntry banEntry : banEntries) {
                    banList.pardon(banEntry.getTarget());
                }
            });

            messages.send(sender, "&C" + count.get() + " &6bans have been migrated", messages.prefixBans);
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
