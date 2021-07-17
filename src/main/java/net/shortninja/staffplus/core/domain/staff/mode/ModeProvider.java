package net.shortninja.staffplus.core.domain.staff.mode;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

@IocBean
public class ModeProvider {

    private final Map<String, GeneralModeConfiguration> modeConfigurations;
    private final PermissionHandler permissionHandler;

    public ModeProvider(Options options, PermissionHandler permissionHandler) {
        this.modeConfigurations = options.modeConfigurations;
        this.permissionHandler = permissionHandler;
    }

    public Optional<GeneralModeConfiguration> getConfiguration(String modeName) {
        return Optional.ofNullable(modeConfigurations.get(modeName));
    }

    public GeneralModeConfiguration getMode(Player player, String modeName) {
        if(!modeConfigurations.containsKey(modeName)) {
            throw new BusinessException("&CCan't turn on staff mode. Mode ["+modeName+"] does not exist");
        }

        GeneralModeConfiguration modeConfiguration = modeConfigurations.get(modeName);
        if(!modeConfiguration.isModeValidInWorld(player.getWorld())) {
            throw new BusinessException("&CMode can not be actived in this world");
        }
        if(!permissionHandler.has(player, modeConfiguration.getPermission())) {
            throw new BusinessException("&CYou don't have permission to access this mode");
        }

        return modeConfiguration;
    }

    public Optional<GeneralModeConfiguration> findMode(Player player, String modeName) {
        return modeConfigurations.values().stream()
            .filter(m -> m.getName().equals(modeName))
            .filter(g -> permissionHandler.has(player, g.getPermission()))
            .filter(g -> g.isModeValidInWorld(player.getWorld()))
            .findFirst();
    }

    public Optional<GeneralModeConfiguration> calculateMode(Player player) {
        return modeConfigurations.values().stream()
            .sorted(Comparator.comparingInt(GeneralModeConfiguration::getWeight).reversed())
            .filter(g -> permissionHandler.has(player, g.getPermission()))
            .filter(g -> g.isModeValidInWorld(player.getWorld()))
            .findFirst();
    }
}
