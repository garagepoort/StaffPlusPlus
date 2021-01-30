package net.shortninja.staffplus.staff.mute;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.event.mute.MuteEvent;
import net.shortninja.staffplus.event.mute.UnmuteEvent;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.infractions.Infraction;
import net.shortninja.staffplus.staff.infractions.InfractionCount;
import net.shortninja.staffplus.staff.infractions.InfractionProvider;
import net.shortninja.staffplus.staff.mute.database.MuteRepository;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.Permission;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getScheduler;

public class MuteService implements InfractionProvider {

    private final Permission permission;
    private final MuteRepository muteRepository;
    private final Options options;
    private MessageCoordinator message;
    private Messages messages;

    public MuteService(Permission permission, MuteRepository muteRepository, Options options, MessageCoordinator message, Messages messages) {
        this.message = message;
        this.permission = permission;
        this.muteRepository = muteRepository;
        this.options = options;
        this.messages = messages;
    }

    public void permMute(CommandSender issuer, SppPlayer playerToMute, String reason) {
        mute(issuer, playerToMute, reason, null);
    }

    public void tempMute(CommandSender issuer, SppPlayer playerToMute, Long durationInMillis, String reason) {
        mute(issuer, playerToMute, reason, durationInMillis);
    }

    public Optional<Mute> getMuteByMutedUuid(UUID playerUuid) {
        return muteRepository.findActiveMute(playerUuid);
    }

    public Mute getById(int muteId) {
        return muteRepository.findActiveMute(muteId).orElseThrow(() -> new BusinessException("No mute found with this id"));
    }

    public List<Mute> getAllPaged(int offset, int amount) {
        return muteRepository.getActiveMutes(offset, amount);
    }

    public List<Mute> getAllActiveMutes(List<Player> players) {
        if(players.isEmpty()) {
            return Collections.emptyList();
        }
        return muteRepository.getAllActiveMutes(players.stream().map(p -> p.getUniqueId().toString()).collect(Collectors.toList()));
    }

    public void unmute(CommandSender issuer, SppPlayer playerToUnmute, String reason) {
        Mute mute = muteRepository.findActiveMute(playerToUnmute.getId())
            .orElseThrow(() -> new BusinessException("&CCannot unmute, this user is not muted"));

        mute.setUnmutedByName(issuer instanceof Player ? issuer.getName() : "Console");
        mute.setUnmutedByUuid(issuer instanceof Player ? ((Player) issuer).getUniqueId() : StaffPlus.get().consoleUUID);
        mute.setUnmuteReason(reason);
        unmute(mute);
    }

    public void unmute(Player issuer, int muteId, String reason) {
        Mute mute = muteRepository.findActiveMute(muteId)
            .orElseThrow(() -> new BusinessException("&CCannot unmute, this user is not muted"));

        mute.setUnmutedByName(issuer.getName());
        mute.setUnmutedByUuid(issuer.getUniqueId());
        mute.setUnmuteReason(reason);
        unmute(mute);
    }

    private void mute(CommandSender issuer, SppPlayer playerToMute, String reason, Long durationInMillis) {
        if (playerToMute.isOnline() && permission.has(playerToMute.getPlayer(), options.muteConfiguration.getPermissionMuteByPass())) {
            throw new BusinessException("&CThis player bypasses being muted");
        }

        muteRepository.findActiveMute(playerToMute.getId())
            .ifPresent((e) -> {
                throw new BusinessException("&CCannot mute this player, the player is already muted");
            });

        String issuerName = issuer instanceof Player ? issuer.getName() : "Console";
        UUID issuerUuid = issuer instanceof Player ? ((Player) issuer).getUniqueId() : StaffPlus.get().consoleUUID;

        Long endDate = durationInMillis == null ? null : System.currentTimeMillis() + durationInMillis;
        Mute mute = new Mute(reason, endDate, issuerName, issuerUuid, playerToMute.getUsername(), playerToMute.getId());
        mute.setId(muteRepository.addMute(mute));

        notifyPlayers(playerToMute, durationInMillis, issuerName, reason);
        sendEvent(new MuteEvent(mute));
    }

    private void notifyPlayers(SppPlayer playerToMute, Long duration, String issuerName, String reason) {
        if (duration == null) {
            String message = messages.permanentMuted
                .replace("%target%", playerToMute.getUsername())
                .replace("%reason%", reason)
                .replace("%issuer%", issuerName);
            this.message.sendGlobalMessage(message, messages.prefixGeneral);
        } else {
            String message = messages.tempMuted
                .replace("%target%", playerToMute.getUsername())
                .replace("%issuer%", issuerName)
                .replace("%reason%", reason)
                .replace("%duration%", JavaUtils.toHumanReadableDuration(duration));
            this.message.sendGlobalMessage(message, messages.prefixGeneral);
        }
    }

    private void unmute(Mute mute) {
        muteRepository.update(mute);

        String unmuteMessage = messages.unmuted
            .replace("%target%", mute.getPlayerName())
            .replace("%issuer%", mute.getUnmutedByName());
        message.sendGlobalMessage(unmuteMessage, messages.prefixGeneral);
        sendEvent(new UnmuteEvent(mute));
    }


    private void sendEvent(Event event) {
        getScheduler().runTask(StaffPlus.get(), () -> {
            Bukkit.getPluginManager().callEvent(event);
        });
    }

    @Override
    public List<? extends Infraction> getInfractions(Player executor, UUID playerUUID) {
        if(!options.infractionsConfiguration.isShowMutes()) {
            return Collections.emptyList();
        }
        return muteRepository.getMutesForPlayer(playerUUID);
    }


    @Override
    public Optional<InfractionCount> getInfractionsCount() {
        if (!options.infractionsConfiguration.isShowMutes()) {
            return Optional.empty();
        }
        return Optional.of(new InfractionCount("Mutes", muteRepository.getCountByPlayer()));
    }
}
