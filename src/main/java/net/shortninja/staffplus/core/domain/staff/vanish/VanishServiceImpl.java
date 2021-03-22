package net.shortninja.staffplus.core.domain.staff.vanish;

import be.garagepoort.staffplusplus.craftbukkit.common.IProtocol;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.shortninja.staffplus.core.StaffPlus;
import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionLoader;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

@IocBean
public class VanishServiceImpl {
    private final IProtocol versionProtocol;
    private final PermissionHandler permission;
    private final MessageCoordinator message;
    private final Options options;
    private final Messages messages;
    private final SessionManagerImpl sessionManager;
    private final SessionLoader sessionLoader;

    public VanishServiceImpl(IProtocol versionProtocol, PermissionHandler permission, MessageCoordinator message, Options options, Messages messages, SessionManagerImpl sessionManager, SessionLoader sessionLoader) {
        this.versionProtocol = versionProtocol;
        this.permission = permission;
        this.message = message;
        this.options = options;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.sessionLoader = sessionLoader;

        if (options.vanishMessageEnabled) {
            Bukkit.getScheduler().runTaskTimer(StaffPlus.get(), () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    PlayerSession playerSession = sessionManager.get(p.getUniqueId());
                    if (playerSession.isVanished()) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message.colorize(messages.vanishEnabled)));
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
        } else if (userVanishType != VanishType.NONE) {
            unapplyVanish(player, userVanishType, true);
        }

        applyVanish(player, vanishType, true);
        session.setVanishType(vanishType);
        sessionLoader.saveSession(session);
    }

    public void removeVanish(Player player) {
        PlayerSession session = sessionManager.get(player.getUniqueId());
        VanishType vanishType = session.getVanishType();

        if (vanishType == VanishType.NONE) {
            return;
        }

        unapplyVanish(player, vanishType, true);
        session.setVanishType(VanishType.NONE);
        sessionLoader.saveSession(session);
    }

    public boolean isVanished(Player player) {
        PlayerSession user = sessionManager.get(player.getUniqueId());
        return user.getVanishType() != VanishType.NONE;
    }

    public void updateVanish() {
        for (PlayerSession user : sessionManager.getAll()) {
            Optional<Player> player = user.getPlayer();

            if (player.isPresent() && user.getVanishType() == VanishType.TOTAL) {
                applyVanish(player.get(), user.getVanishType(), false);
            }
        }
    }

    private void applyVanish(Player player, VanishType vanishType, boolean shouldMessage) {
        String message = "";

        switch (vanishType) {
            case TOTAL:
                Bukkit.getOnlinePlayers().stream()
                    .filter(p -> !options.staffView || !permission.has(p, options.permissionMode))
                    .forEach(p -> p.hidePlayer(player));
                message = messages.totalVanish.replace("%status%", messages.enabled);
                break;
            case LIST:
                if (options.vanishTabList) {
                    versionProtocol.listVanish(player, true);
                }

                message = messages.listVanish.replace("%status%", messages.enabled);
                break;
        }

        if (shouldMessage && !message.isEmpty()) {
            this.message.send(player, message, messages.prefixGeneral);
        }
    }

    private void unapplyVanish(Player player, VanishType vanishType, boolean shouldMessage) {

        String message = "";

        switch (vanishType) {
            case TOTAL:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.showPlayer(player);
                }
                message = messages.totalVanish.replace("%status%", messages.disabled);
                break;
            case LIST:
                versionProtocol.listVanish(player, false);
                message = messages.listVanish.replace("%status%", messages.disabled);
                break;
            default:
                break;
        }

        if (shouldMessage && !message.isEmpty()) {
            this.message.send(player, message, messages.prefixGeneral);
        }
    }
}
