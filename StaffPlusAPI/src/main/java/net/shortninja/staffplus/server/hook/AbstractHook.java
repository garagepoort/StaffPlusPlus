package net.shortninja.staffplus.server.hook;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class AbstractHook implements IHook {

    private final Plugin plugin;

    protected AbstractHook(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canHook() {
        Plugin hook = Bukkit.getPluginManager().getPlugin(getPluginName());

        if (hook != null) {
            if (hook.getDescription().getVersion().equals(getPluginVersion())) {
                return true;
            }
        }

        return false;
    }
}
