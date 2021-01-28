package net.shortninja.staffplus.staff.mode;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.staff.vanish.VanishService;
import net.shortninja.staffplus.unordered.VanishType;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StaffModeService {

    private final MessageCoordinator message;
    private final Messages messages;
    private final SessionManager sessionManager;
    private final VanishService vanishService;
    private final StaffModeItemsService staffModeItemsService;

    private final GeneralModeConfiguration modeConfiguration;
    private final ModeDataRepository modeDataRepository;
    private final Logger logger = StaffPlus.get().getLogger();

    public StaffModeService(MessageCoordinator message,
                            Options options,
                            Messages messages,
                            SessionManager sessionManager,
                            VanishService vanishService,
                            StaffModeItemsService staffModeItemsService,
                            ModeDataRepository modeDataRepository) {
        this.message = message;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.vanishService = vanishService;

        modeConfiguration = options.modeConfiguration;
        this.staffModeItemsService = staffModeItemsService;
        this.modeDataRepository = modeDataRepository;
    }

    public Set<UUID> getModeUsers() {
        return sessionManager.getAll().stream()
            .filter(p -> p.getPlayer().isPresent() && p.getPlayer().get().isOnline())
            .map(PlayerSession::getUuid).collect(Collectors.toSet());
    }

    public void addMode(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerSession session = sessionManager.get(uuid);

        Optional<ModeData> existingModeData = modeDataRepository.retrieveModeData(player.getUniqueId());
        if (!existingModeData.isPresent()) {
            ModeData modeData = new ModeData(player, session.getVanishType());
            modeDataRepository.saveModeData(modeData);
        }

        staffModeItemsService.setStaffModeItems(player);

        player.setAllowFlight(modeConfiguration.isModeFlight() && !modeConfiguration.isModeCreative());
        if (modeConfiguration.isModeCreative()) player.setGameMode(GameMode.CREATIVE);

        runModeCommands(player, true);
        vanishService.addVanish(player, modeConfiguration.getModeVanish());
        session.setInStaffMode(true);
        message.send(player, messages.modeStatus.replace("%status%", messages.enabled), messages.prefixGeneral);
    }

    public void removeMode(Player player) {
        PlayerSession session = sessionManager.get(player.getUniqueId());

        Optional<ModeData> existingModeData = modeDataRepository.retrieveModeData(player.getUniqueId());
        if (!existingModeData.isPresent()) {
            logger.warning("Player is has no modedata stored. Cannot remove mode.");
            return;
        }

        ModeData modeData = existingModeData.get();
        if (modeConfiguration.isModeOriginalLocation()) {
            player.teleport(modeData.getPreviousLocation().setDirection(player.getLocation().getDirection()));
            message.send(player, messages.modeOriginalLocation, messages.prefixGeneral);
        }

        runModeCommands(player, false);
        JavaUtils.clearInventory(player);
        player.getInventory().setContents(modeData.getPlayerInventory());
        player.updateInventory();
        player.setExp(modeData.getXp());
        player.setAllowFlight(modeData.hasFlight());
        player.setGameMode(modeData.getGameMode());

        if (modeData.getVanishType() == VanishType.NONE) {
            vanishService.removeVanish(player);
        } else {
            vanishService.addVanish(player, modeData.getVanishType());
        }
        modeDataRepository.deleteModeData(player);

        session.setInStaffMode(false);
        message.send(player, messages.modeStatus.replace("%status%", messages.disabled), messages.prefixGeneral);
    }

    private void runModeCommands(Player player, boolean isEnabled) {
        for (String command : isEnabled ? modeConfiguration.getModeEnableCommands() : modeConfiguration.getModeDisableCommands()) {
            if (command.isEmpty()) {
                continue;
            }

            CommandSender target = (command.trim().startsWith("%player%")) ? player : Bukkit.getConsoleSender();
            command = (command.trim().startsWith("%player%)")) ? command.replaceFirst("%player%", "").trim() : command;
            Bukkit.dispatchCommand(target, command.replace("%player%", player.getName()));
        }
    }

    public static ItemStack[] getContents(Player p) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        for (int i = 0; i <= 35; i++) {
            itemStacks.add(p.getInventory().getItem(i));
        }
        return itemStacks.toArray(new ItemStack[]{});
    }

}