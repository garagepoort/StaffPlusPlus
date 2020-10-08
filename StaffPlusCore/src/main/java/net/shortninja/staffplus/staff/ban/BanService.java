package net.shortninja.staffplus.staff.ban;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.ban.database.BansRepository;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.Permission;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BanService {


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

    public Optional<Ban> getBan(UUID playerUuid) {
        return bansRepository.findActiveBan(playerUuid);
    }

    public void unban(CommandSender issuer, SppPlayer playerToUnban, String reason) {
        String issuerName = issuer instanceof Player ? issuer.getName() : "Console";
        UUID issuerUuid = issuer instanceof Player ? ((Player) issuer).getUniqueId() : StaffPlus.get().consoleUUID;
        Optional<Ban> ban = getBan(playerToUnban.getId());
        if (!ban.isPresent()) {
            throw new BusinessException("&CCannot unban, this user is not banned");
        }

        bansRepository.unban(playerToUnban, issuerUuid, reason);
        String unbanMessage = messages.unbanned
            .replace("%target%", playerToUnban.getUsername())
            .replace("%issuer%", issuerName);
        message.sendGlobalMessage(unbanMessage, messages.prefixGeneral);
    }

    public void unban(Player issuer, int id, String reason) {
        Ban ban = getById(id);
        bansRepository.unban(id, issuer.getUniqueId(), reason);
        String unbanMessage = messages.unbanned
            .replace("%target%", ban.getPlayerName())
            .replace("%issuer%", issuer.getName());
        message.sendGlobalMessage(unbanMessage, messages.prefixGeneral);
    }

    private void ban(CommandSender issuer, SppPlayer playerToBan, String reason, Long durationInMillis) {
        if (playerToBan.isOnline() && permission.has(playerToBan.getPlayer(), options.banConfiguration.getPermissionBanByPass())) {
            throw new BusinessException("&CThis player bypasses being banned");
        }

        Optional<Ban> existingBan = bansRepository.findActiveBan(playerToBan.getId());
        if (existingBan.isPresent()) {
            throw new BusinessException("&CCannot ban this player, the player is already banned");
        }

        String issuerName = issuer instanceof Player ? issuer.getName() : "Console";
        UUID issuerUuid = issuer instanceof Player ? ((Player) issuer).getUniqueId() : StaffPlus.get().consoleUUID;

        Long endDate = durationInMillis == null ? null : System.currentTimeMillis() + durationInMillis;
        bansRepository.addBan(new Ban(reason, endDate, issuerName, issuerUuid, playerToBan.getUsername(), playerToBan.getId()));

        notifyPlayers(playerToBan, durationInMillis, issuerName);
        kickPlayer(playerToBan, durationInMillis, issuerName);
    }

    private void kickPlayer(SppPlayer playerToBan, Long duration, String issuerName) {
        if (playerToBan.isOnline()) {
            if (duration != null) {
                String message = messages.tempBannedKick
                    .replace("%target%", playerToBan.getUsername())
                    .replace("%issuer%", issuerName)
                    .replace("%duration%", JavaUtils.toHumanReadableDuration(duration));
                playerToBan.getPlayer().kickPlayer(message);
            } else {
                String message = messages.permanentBannedKick
                    .replace("%target%", playerToBan.getUsername())
                    .replace("%issuer%", issuerName);
                playerToBan.getPlayer().kickPlayer(message);
            }
        }
    }

    private void notifyPlayers(SppPlayer playerToBan, Long duration, String issuerName) {
        if (duration == null) {
            String message = messages.permanentBanned
                .replace("%target%", playerToBan.getUsername())
                .replace("%issuer%", issuerName);
            this.message.sendGlobalMessage(message, messages.prefixGeneral);
        } else {
            String message = messages.tempBanned
                .replace("%target%", playerToBan.getUsername())
                .replace("%issuer%", issuerName)
                .replace("%duration%", JavaUtils.toHumanReadableDuration(duration));
            this.message.sendGlobalMessage(message, messages.prefixGeneral);
        }
    }

    public Ban getById(int banId) {
        return bansRepository.findActiveBan(banId).orElseThrow(() -> new BusinessException("No ban found with this id"));
    }

    public List<Ban> getAllPaged(int offset, int amount) {
        return bansRepository.getActiveBans(offset, amount);
    }
}
