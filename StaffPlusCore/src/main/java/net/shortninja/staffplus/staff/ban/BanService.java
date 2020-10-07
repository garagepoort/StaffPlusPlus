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

    public void tempBan(CommandSender issuer, SppPlayer playerToBan, Long endDate, String reason) {
        ban(issuer, playerToBan, reason, endDate);
    }

    public Optional<Ban> getBan(UUID playerUuid) {
        return bansRepository.findActiveBan(playerUuid);
    }

    public void unban(CommandSender issuer, SppPlayer playerToUnban, String reason) {
        String issuerName = issuer instanceof Player ? issuer.getName() : "Console";
        UUID issuerUuid = issuer instanceof Player ? ((Player) issuer).getUniqueId() : StaffPlus.get().consoleUUID;
        Optional<Ban> ban = getBan(playerToUnban.getId());
        if(!ban.isPresent()) {
            throw new BusinessException("&CCannot unban, this user is not banned");
        }

        bansRepository.unban(playerToUnban, issuerUuid, reason);
        message.sendGlobalMessage("&6" + playerToUnban.getUsername() + " has been unbanned from the server by " + issuerName, messages.prefixGeneral);
    }

    private void ban(CommandSender issuer, SppPlayer playerToBan, String reason, Long endDate) {
        if (playerToBan.isOnline() && permission.has(playerToBan.getPlayer(), options.banConfiguration.getPermissionBanByPass())) {
            throw new BusinessException("&CThis player bypasses being banned");
        }

        Optional<Ban> existingBan = bansRepository.findActiveBan(playerToBan.getId());
        if (existingBan.isPresent()) {
            throw new BusinessException("&CCannot ban this player, the player is already banned");
        }

        String issuerName = issuer instanceof Player ? issuer.getName() : "Console";
        UUID issuerUuid = issuer instanceof Player ? ((Player) issuer).getUniqueId() : StaffPlus.get().consoleUUID;
        bansRepository.addBan(new Ban(reason, endDate, issuerName, issuerUuid, playerToBan.getUsername(), playerToBan.getId()));
        message.sendGlobalMessage("&6" + playerToBan.getUsername() + " has been banned from the server by " + issuerName, messages.prefixGeneral);

        if (playerToBan.isOnline()) {
            if (endDate != null) {
                long differenceInMinutes = JavaUtils.getDuration(endDate);
                playerToBan.getPlayer().kickPlayer("You have been temporarily banned from this server. Ban time left: " + differenceInMinutes + " minutes");
            } else {
                playerToBan.getPlayer().kickPlayer("You have been permanently banned from this server");
            }
        }
    }

}
