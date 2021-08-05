package net.shortninja.staffplus.core.domain.staff.vanish;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplusplus.vanish.VanishOffEvent;
import net.shortninja.staffplusplus.vanish.VanishOnEvent;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class VanishServiceImpl {

    private final SessionManagerImpl sessionManager;
    private final List<VanishStrategy> vanishStrategies;
    private final VanishConfiguration vanishConfiguration;

    public VanishServiceImpl(Messages messages,
                             SessionManagerImpl sessionManager,
                             @IocMulti(VanishStrategy.class) List<VanishStrategy> vanishStrategies,
                             VanishConfiguration vanishConfiguration) {
        this.sessionManager = sessionManager;
        this.vanishStrategies = vanishStrategies;
        this.vanishConfiguration = vanishConfiguration;
        if (this.vanishConfiguration.vanishMessageEnabled) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(StaffPlus.get(), () -> {
                sessionManager.getAll().stream().filter(PlayerSession::isVanished)
                    .filter(session -> session.getPlayer().isPresent())
                    .forEach(session -> session.getPlayer().get().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messages.colorize(messages.vanishEnabled))));
            }, 20L, 20L);
        }
    }

    public void addVanish(Player player, VanishType vanishType) {
        if (!vanishConfiguration.vanishEnabled) {
            return;
        }

        PlayerSession session = sessionManager.get(player.getUniqueId());
        session.setVanishType(vanishType);
        sessionManager.saveSession(player);
        sendEvent(new VanishOnEvent(vanishType, player));
    }

    public void removeVanish(Player player) {
        if (!vanishConfiguration.vanishEnabled) {
            return;
        }
        PlayerSession session = sessionManager.get(player.getUniqueId());
        VanishType vanishType = session.getVanishType();
        session.setVanishType(VanishType.NONE);
        sessionManager.saveSession(player);

        sendEvent(new VanishOffEvent(vanishType, player));
    }

    public boolean isVanished(Player player) {
        PlayerSession user = sessionManager.get(player.getUniqueId());
        return user.getVanishType() != VanishType.NONE;
    }

    public void updateVanish(Player player) {
        if (!vanishConfiguration.vanishEnabled) {
            return;
        }
        vanishStrategies.forEach(v -> v.updateVanish(player));
    }
}
