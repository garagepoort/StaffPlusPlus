package net.shortninja.staffplus.warn;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.unordered.IWarning;
import org.bukkit.Bukkit;

import java.util.List;

public class WarnService {

    private static WarnService instance;
    private Options options = StaffPlus.get().options;

    private WarnService() {
    }

    public static WarnService getInstance() {
        if(instance == null) {
            instance = new WarnService();
        }
        return instance;
    }

    public void addWarn(IUser user, IWarning warning) {
        user.addWarning(warning);
        IocContainer.getStorage().addWarning(warning);
    }

    public void checkBan(IUser user) {
        if (user.getWarnings().size() >= options.warningsMaximum && options.warningsMaximum > 0) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), options.warningsBanCommand.replace("%player%", user.getName()));
        }
    }

    public void clearWarnings(IUser user) {
        List<IWarning> warnings = user.getWarnings();
        for (IWarning warning : warnings) {
            IocContainer.getStorage().removeWarnings(warning.getUuid());
        }
        user.getWarnings().clear();
    }
}
