package net.shortninja.staffplus.core.domain.staff.vanish;

import be.garagepoort.mcioc.IocBean;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplusplus.vanish.VanishOffEvent;
import net.shortninja.staffplusplus.vanish.VanishOnEvent;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class VanishServiceImpl {

    private final PlayerSettingsRepository playerSettingsRepository;
    private final VanishConfiguration vanishConfiguration;

    public VanishServiceImpl(Messages messages,
                             PlayerSettingsRepository playerSettingsRepository,
                             VanishConfiguration vanishConfiguration) {
        this.playerSettingsRepository = playerSettingsRepository;
        this.vanishConfiguration = vanishConfiguration;
        if (this.vanishConfiguration.vanishMessageEnabled) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(StaffPlus.get(), () -> {
                Bukkit.getOnlinePlayers().stream()
                    .filter(p -> playerSettingsRepository.get(p).isVanished())
                    .forEach(p -> p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messages.colorize(messages.vanishEnabled))));
            }, 20L, 20L);
        }
    }

    public void addVanish(Player player, VanishType vanishType) {
        if (!vanishConfiguration.vanishEnabled) {
            return;
        }

        PlayerSettings settings = playerSettingsRepository.get(player);
        settings.setVanishType(vanishType);
        playerSettingsRepository.save(settings);
        sendEvent(new VanishOnEvent(vanishType, player));
    }

    public void removeVanish(Player player) {
        if (!vanishConfiguration.vanishEnabled) {
            return;
        }
        PlayerSettings session = playerSettingsRepository.get(player);
        VanishType vanishType = session.getVanishType();
        session.setVanishType(VanishType.NONE);
        playerSettingsRepository.save(session);

        sendEvent(new VanishOffEvent(vanishType, player));
    }

    public boolean isVanished(Player player) {
        PlayerSettings user = playerSettingsRepository.get(player);
        return user.getVanishType() != VanishType.NONE;
    }
}
