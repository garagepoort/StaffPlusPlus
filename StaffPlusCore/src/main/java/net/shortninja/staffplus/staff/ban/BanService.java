package net.shortninja.staffplus.staff.ban;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.ban.database.BansRepository;
import net.shortninja.staffplus.staff.infractions.Infraction;
import net.shortninja.staffplus.staff.infractions.InfractionCount;
import net.shortninja.staffplus.staff.infractions.InfractionProvider;
import net.shortninja.staffplus.staff.infractions.InfractionType;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.Permission;
import net.shortninja.staffplus.common.JavaUtils;
import net.shortninja.staffplus.util.Message;
import net.shortninja.staffplusplus.ban.BanEvent;
import net.shortninja.staffplusplus.ban.UnbanEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.util.BukkitUtils.sendEvent;

public class BanService implements InfractionProvider {

    private final Permission permission;
    private final BansRepository bansRepository;
    private final Options options;
    private MessageCoordinator message;
    private Messages messages;

    public BanService(Permission permission, BansRepository bansRepository, Options options, MessageCoordinator message, Messages messages) {
        this.message = message;
        this.permission = permission;
        this.bansRepository = bansRepository;
        this.options = options;
        this.messages = messages;
    }

    public void permBan(CommandSender issuer, SppPlayer playerToBan, String reason) {
        ban(issuer, playerToBan, reason, null);
    }

    public void tempBan(CommandSender issuer, SppPlayer playerToBan, Long durationInMillis, String reason) {
        ban(issuer, playerToBan, reason, durationInMillis);
    }

    public Optional<Ban> getBanByBannedUuid(UUID playerUuid) {
        return bansRepository.findActiveBan(playerUuid);
    }

    public Ban getById(int banId) {
        return bansRepository.findActiveBan(banId).orElseThrow(() -> new BusinessException("No ban found with this id"));
    }

    public List<Ban> getAllPaged(int offset, int amount) {
        return bansRepository.getActiveBans(offset, amount);
    }


    public void unban(CommandSender issuer, SppPlayer playerToUnban, String reason) {
        Ban ban = bansRepository.findActiveBan(playerToUnban.getId())
            .orElseThrow(() -> new BusinessException("&CCannot unban, this user is not banned"));

        ban.setUnbannedByName(issuer instanceof Player ? issuer.getName() : "Console");
        ban.setUnbannedByUuid(issuer instanceof Player ? ((Player) issuer).getUniqueId() : StaffPlus.get().consoleUUID);
        ban.setUnbanReason(reason);
        unban(ban);
    }

    public void unban(Player issuer, int banId, String reason) {
        Ban ban = bansRepository.findActiveBan(banId)
            .orElseThrow(() -> new BusinessException("&CCannot unban, this user is not banned"));

        ban.setUnbannedByName(issuer.getName());
        ban.setUnbannedByUuid(issuer.getUniqueId());
        ban.setUnbanReason(reason);
        unban(ban);
    }

    private void ban(CommandSender issuer, SppPlayer playerToBan, String reason, Long durationInMillis) {
        if (playerToBan.isOnline() && permission.has(playerToBan.getPlayer(), options.banConfiguration.getPermissionBanByPass())) {
            throw new BusinessException("&CThis player bypasses being banned");
        }

        bansRepository.findActiveBan(playerToBan.getId())
            .ifPresent((e) -> { throw new BusinessException("&CCannot ban this player, the player is already banned"); });

        String issuerName = issuer instanceof Player ? issuer.getName() : "Console";
        UUID issuerUuid = issuer instanceof Player ? ((Player) issuer).getUniqueId() : StaffPlus.get().consoleUUID;

        Long endDate = durationInMillis == null ? null : System.currentTimeMillis() + durationInMillis;
        Ban ban = new Ban(reason, endDate, issuerName, issuerUuid, playerToBan.getUsername(), playerToBan.getId());
        ban.setId(bansRepository.addBan(ban));

        notifyPlayers(playerToBan, durationInMillis, issuerName, reason);
        kickPlayer(playerToBan, durationInMillis, issuerName, reason);
        sendEvent(new BanEvent(ban));
    }

    private void kickPlayer(SppPlayer playerToBan, Long duration, String issuerName, String reason) {
        if (playerToBan.isOnline()) {
            if (duration != null) {
                String message = messages.tempBannedKick
                    .replace("%target%", playerToBan.getUsername())
                    .replace("%issuer%", issuerName)
                    .replace("%reason%", reason)
                    .replace("%duration%", JavaUtils.toHumanReadableDuration(duration));
                playerToBan.getPlayer().kickPlayer(Message.colorize(message));
            } else {
                String message = messages.permanentBannedKick
                    .replace("%target%", playerToBan.getUsername())
                    .replace("%reason%", reason)
                    .replace("%issuer%", issuerName);
                playerToBan.getPlayer().kickPlayer(Message.colorize(message));
            }
        }
    }

    private void notifyPlayers(SppPlayer playerToBan, Long duration, String issuerName, String reason) {
        if (duration == null) {
            String message = messages.permanentBanned
                .replace("%target%", playerToBan.getUsername())
                .replace("%issuer%", issuerName)
                    .replace("%reason%", reason);
            this.message.sendGlobalMessage(message, messages.prefixGeneral);
        } else {
            String message = messages.tempBanned
                .replace("%target%", playerToBan.getUsername())
                .replace("%issuer%", issuerName)
                .replace("%reason%", reason)
                .replace("%duration%", JavaUtils.toHumanReadableDuration(duration));
            this.message.sendGlobalMessage(message, messages.prefixGeneral);
        }
    }

    private void unban(Ban ban) {
        bansRepository.update(ban);

        String unbanMessage = messages.unbanned
            .replace("%target%", ban.getTargetName())
            .replace("%issuer%", ban.getUnbannedByName());
        message.sendGlobalMessage(unbanMessage, messages.prefixGeneral);
        sendEvent(new UnbanEvent(ban));
    }

    @Override
    public List<? extends Infraction> getInfractions(Player executor, UUID playerUUID) {
        if(!options.infractionsConfiguration.isShowBans()) {
            return Collections.emptyList();
        }
        return bansRepository.getBansForPlayer(playerUUID);
    }

    @Override
    public Optional<InfractionCount> getInfractionsCount() {
        if(!options.infractionsConfiguration.isShowBans()) {
            return Optional.empty();
        }
        return Optional.of(new InfractionCount(InfractionType.BAN, bansRepository.getCountByPlayer()));
    }

    @Override
    public InfractionType getType() {
        return InfractionType.BAN;
    }
}
