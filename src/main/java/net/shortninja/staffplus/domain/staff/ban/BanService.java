package net.shortninja.staffplus.domain.staff.ban;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.JavaUtils;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.common.config.Messages;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.domain.staff.ban.config.BanConfiguration;
import net.shortninja.staffplus.domain.staff.ban.database.BansRepository;
import net.shortninja.staffplus.domain.staff.infractions.Infraction;
import net.shortninja.staffplus.domain.staff.infractions.InfractionInfo;
import net.shortninja.staffplus.domain.staff.infractions.InfractionProvider;
import net.shortninja.staffplus.domain.staff.infractions.InfractionType;
import net.shortninja.staffplus.common.utils.MessageCoordinator;
import net.shortninja.staffplus.common.utils.PermissionHandler;
import net.shortninja.staffplusplus.ban.BanEvent;
import net.shortninja.staffplusplus.ban.UnbanEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

import static java.util.stream.Collectors.toMap;
import static net.shortninja.staffplus.domain.staff.ban.BanMessageStringUtil.replaceBanPlaceholders;
import static net.shortninja.staffplus.domain.staff.ban.BanType.PERM_BAN;
import static net.shortninja.staffplus.domain.staff.ban.BanType.TEMP_BAN;
import static net.shortninja.staffplus.common.utils.BukkitUtils.sendEvent;

public class BanService implements InfractionProvider {

    private final PermissionHandler permission;
    private final BansRepository bansRepository;
    private final Options options;
    private final BanConfiguration banConfiguration;
    private final BanReasonResolver banReasonResolver;
    private final BanTemplateResolver banTemplateResolver;
    private final MessageCoordinator message;
    private final Messages messages;

    public BanService(PermissionHandler permission, BansRepository bansRepository, Options options, MessageCoordinator message, Messages messages, BanReasonResolver banReasonResolver, BanTemplateResolver banTemplateResolver) {
        this.message = message;
        this.permission = permission;
        this.bansRepository = bansRepository;
        this.options = options;
        this.messages = messages;
        banConfiguration = options.banConfiguration;
        this.banReasonResolver = banReasonResolver;
        this.banTemplateResolver = banTemplateResolver;
    }

    public void permBan(CommandSender issuer, SppPlayer playerToBan, String reason, String template) {
        ban(issuer, playerToBan, reason, template, null, PERM_BAN);
    }

    public void permBan(CommandSender issuer, SppPlayer playerToBan, String reason) {
        ban(issuer, playerToBan, reason, null, null, PERM_BAN);
    }

    public void tempBan(CommandSender issuer, SppPlayer playerToBan, Long durationInMillis, String reason, String template) {
        ban(issuer, playerToBan, reason, template, durationInMillis, TEMP_BAN);
    }

    public void tempBan(CommandSender issuer, SppPlayer playerToBan, Long durationInMillis, String reason) {
        ban(issuer, playerToBan, reason, null, durationInMillis, TEMP_BAN);
    }

    public Optional<Ban> getBanByBannedUuid(UUID playerUuid) {
        return bansRepository.findActiveBan(playerUuid);
    }

    public Ban getById(int banId) {
        return bansRepository.findActiveBan(banId).orElseThrow(() -> new BusinessException("&CNo ban found with this id"));
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

    private void ban(CommandSender issuer, SppPlayer playerToBan, final String reason, final String providedTemplateName, Long durationInMillis, BanType banType) {
        String fullReason = banReasonResolver.resolveBanReason(reason, banType);
        String templateMessage = banTemplateResolver.resolveTemplate(issuer, reason, providedTemplateName, banType);


        if (playerToBan.isOnline() && permission.has(playerToBan.getPlayer(), banConfiguration.getPermissionBanByPass())) {
            throw new BusinessException("&CThis player bypasses being banned");
        }

        bansRepository.findActiveBan(playerToBan.getId())
            .ifPresent(ban -> {
                throw new BusinessException("&CCannot ban this player, the player is already banned");
            });

        String issuerName = issuer instanceof Player ? issuer.getName() : "Console";
        UUID issuerUuid = issuer instanceof Player ? ((Player) issuer).getUniqueId() : StaffPlus.get().consoleUUID;

        Long endDate = durationInMillis == null ? null : System.currentTimeMillis() + durationInMillis;
        Ban ban = new Ban(fullReason, endDate, issuerName, issuerUuid, playerToBan.getUsername(), playerToBan.getId());
        ban.setId(bansRepository.addBan(ban));

        notifyPlayers(playerToBan, durationInMillis, issuerName, fullReason);
        kickPlayer(playerToBan, durationInMillis, issuerName, fullReason, templateMessage);
        sendEvent(new BanEvent(ban));
    }

    private void kickPlayer(SppPlayer playerToBan, Long duration, String issuerName, String reason, String templateMessage) {
        if (playerToBan.isOnline()) {
            String banMessage = replaceBanPlaceholders(templateMessage, playerToBan.getUsername(), issuerName, reason, duration);
            playerToBan.getPlayer().kickPlayer(MessageCoordinator.colorize(banMessage));
        }
    }

    private void notifyPlayers(SppPlayer playerToBan, Long duration, String issuerName, String reason) {
        String banMessage = duration == null ?
            replaceBanPlaceholders(messages.permanentBanned, playerToBan.getUsername(), issuerName, reason, duration) :
            replaceBanPlaceholders(messages.tempBanned, playerToBan.getUsername(), issuerName, reason, duration);
        this.message.sendGlobalMessage(banMessage, messages.prefixGeneral);
    }

    private void unban(Ban ban) {
        bansRepository.update(ban);

        String unbanMessage = replaceBanPlaceholders(messages.unbanned, ban.getTargetName(), ban.getUnbannedByName(), ban.getReason(), ban.getEndTimestamp());
        message.sendGlobalMessage(unbanMessage, messages.prefixGeneral);
        sendEvent(new UnbanEvent(ban));
    }

    @Override
    public List<? extends Infraction> getInfractions(Player executor, UUID playerUUID) {
        if (!options.infractionsConfiguration.isShowBans()) {
            return Collections.emptyList();
        }
        return bansRepository.getBansForPlayer(playerUUID);
    }

    @Override
    public Optional<InfractionInfo> getInfractionsInfo() {
        if (!options.infractionsConfiguration.isShowBans()) {
            return Optional.empty();
        }
        Map<UUID, List<String>> banDurationByPlayer = bansRepository.getBanDurationByPlayer().entrySet().stream()
            .collect(toMap(Map.Entry::getKey, e -> Arrays.asList("&bTotal time banned: ", "&6" + JavaUtils.toHumanReadableDuration(e.getValue()))));


        for (UUID permBannedPlayer : bansRepository.getAllPermanentBannedPlayers()) {
            banDurationByPlayer.put(permBannedPlayer, Collections.singletonList("&CPermanently banned"));
        }

        return Optional.of(new InfractionInfo(InfractionType.BAN, bansRepository.getCountByPlayer(), banDurationByPlayer));
    }

    @Override
    public InfractionType getType() {
        return InfractionType.BAN;
    }

}
