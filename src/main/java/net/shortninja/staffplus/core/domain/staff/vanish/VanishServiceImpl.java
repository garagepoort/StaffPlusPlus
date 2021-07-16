package net.shortninja.staffplus.core.domain.staff.vanish;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

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
            Bukkit.getScheduler().runTaskTimer(StaffPlus.get(), () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    PlayerSession playerSession = sessionManager.get(p.getUniqueId());
                    if (playerSession.isVanished()) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messages.colorize(messages.vanishEnabled)));
                    }
                }
            }, 20L, 20L);
        }
    }

    public void addVanish(Player player, VanishType vanishType) {
        if (!vanishConfiguration.vanishEnabled) {
            return;
        }

        PlayerSession session = sessionManager.get(player.getUniqueId());
        getVanishStrategy(vanishType).vanish(player);
        session.setVanishType(vanishType);
    }

    public void removeVanish(Player player) {
        if (!vanishConfiguration.vanishEnabled) {
            return;
        }
        PlayerSession session = sessionManager.get(player.getUniqueId());
        vanishStrategies.stream()
            .filter(s -> s.getVanishType() == session.getVanishType())
            .forEach(v -> v.unvanish(player));
        session.setVanishType(VanishType.NONE);
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

    private VanishStrategy getVanishStrategy(VanishType vanishType) {
        return vanishStrategies.stream().filter(v -> v.getVanishType() == vanishType)
            .findFirst()
            .orElseThrow(() -> new BusinessException("&CNo Suitable vanish strategy found for type [" + vanishType + "]"));
    }
}
