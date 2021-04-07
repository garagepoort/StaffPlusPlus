package net.shortninja.staffplus.core.domain.staff.mode;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.gui.GuiConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.vanish.VanishModeConfiguration;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@IocBean
public class StaffModeItemsService {

    private static final Logger logger = StaffPlus.get().getLogger();
    private final List<ModeItemConfiguration> MODE_ITEMS;

    private final PermissionHandler permissionHandler;
    private final Options options;
    private final SessionManagerImpl sessionManager;
    private final GeneralModeConfiguration modeConfiguration;

    public StaffModeItemsService(PermissionHandler permissionHandler, Options options, SessionManagerImpl sessionManager) {
        this.permissionHandler = permissionHandler;
        this.options = options;
        modeConfiguration = options.modeConfiguration;
        this.sessionManager = sessionManager;

        MODE_ITEMS = Arrays.asList(
            options.staffItemsConfiguration.getCompassModeConfiguration(),
            options.staffItemsConfiguration.getRandomTeleportModeConfiguration(),
            options.staffItemsConfiguration.getVanishModeConfiguration(),
            options.staffItemsConfiguration.getGuiModeConfiguration(),
            options.staffItemsConfiguration.getCounterModeConfiguration(),
            options.staffItemsConfiguration.getFreezeModeConfiguration(),
            options.staffItemsConfiguration.getCpsModeConfiguration(),
            options.staffItemsConfiguration.getExamineModeConfiguration(),
            options.staffItemsConfiguration.getFollowModeConfiguration()
        );
    }

    public void setStaffModeItems(Player player) {
        PlayerSession session = sessionManager.get(player.getUniqueId());
        JavaUtils.clearInventory(player);

        if (permissionHandler.isOp(player) || customGuiConfigurations().isEmpty()) {
            getAllModeItems().forEach(modeItem -> addModeItem(player, session, modeItem, modeItem.getSlot()));
            return;
        }

        Optional<GuiConfiguration> applicableGui = customGuiConfigurations().stream()
            .filter(gui -> permissionHandler.has(player, gui.getPermission()))
            .findFirst();

        if (!applicableGui.isPresent()) {
            logger.warning("No gui configuration found for player " + player.getName() + ". Make sure this player has one of the staff mode rank permissions");
            return;
        }

        applicableGui.get().getItemSlots().forEach((moduleName, slot) -> {
            Optional<ModeItemConfiguration> module = getModule(moduleName);
            if (!module.isPresent()) {
                logger.warning("No module found with name [" + moduleName + "]. Skipping...");
            } else {
                addModeItem(player, session, module.get(), slot);
            }
        });
    }

    private List<GuiConfiguration> customGuiConfigurations() {
        return modeConfiguration.getStaffGuiConfigurations();
    }

    private void addModeItem(Player player, PlayerSession session, ModeItemConfiguration modeItem, int slot) {
        if (!modeItem.isEnabled()) {
            return;
        }

        if (modeItem instanceof VanishModeConfiguration) {
            player.getInventory().setItem(slot, ((VanishModeConfiguration) modeItem).getModeVanishItem(session, modeConfiguration.getModeVanish()));
        } else {
            player.getInventory().setItem(slot, modeItem.getItem());
        }
    }

    private Optional<ModeItemConfiguration> getModule(String name) {
        return getAllModeItems().stream().filter(m -> m.getIdentifier().equals(name)).findFirst();
    }

    private List<ModeItemConfiguration> getAllModeItems() {
        return Stream.of(MODE_ITEMS, options.customModuleConfigurations)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    public static ItemStack[] getContents(Player p) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        for (int i = 0; i <= 35; i++) {
            itemStacks.add(p.getInventory().getItem(i));
        }
        return itemStacks.toArray(new ItemStack[]{});
    }
}
