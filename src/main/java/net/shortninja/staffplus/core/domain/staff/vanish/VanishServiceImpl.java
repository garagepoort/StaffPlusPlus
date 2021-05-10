package net.shortninja.staffplus.core.domain.staff.vanish;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@IocBean
public class VanishServiceImpl {

    private final SessionManagerImpl sessionManager;
    private final List<VanishStrategy> vanishStrategies;

    public VanishServiceImpl(Options options,
                             Messages messages,
                             SessionManagerImpl sessionManager,
                             @IocMulti(VanishStrategy.class) List<VanishStrategy> vanishStrategies) {
        this.sessionManager = sessionManager;
        this.vanishStrategies = vanishStrategies;

        if (options.vanishMessageEnabled) {
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
        PlayerSession session = sessionManager.get(player.getUniqueId());
        VanishType userVanishType = session.getVanishType();

        if (userVanishType == vanishType) {
            return;
        }

        getVanishStrategy(vanishType).vanish(player);
        session.setVanishType(vanishType);
    }

    public void removeVanish(Player player) {
        PlayerSession session = sessionManager.get(player.getUniqueId());
        VanishType vanishType = session.getVanishType();

        getVanishStrategy(vanishType).unvanish(player);
        session.setVanishType(VanishType.NONE);
    }

    public boolean isVanished(Player player) {
        PlayerSession user = sessionManager.get(player.getUniqueId());
        return user.getVanishType() != VanishType.NONE;
    }

    public void updateVanish(Player player) {
        vanishStrategies.forEach(v -> v.updateVanish(player));
    }

    private VanishStrategy getVanishStrategy(VanishType vanishType) {
        return vanishStrategies.stream().filter(v -> v.getVanishType() == vanishType)
            .findFirst()
            .orElseThrow(() -> new BusinessException("&CNo Suitable vanish strategy found for type [" + vanishType + "]"));
    }
}
