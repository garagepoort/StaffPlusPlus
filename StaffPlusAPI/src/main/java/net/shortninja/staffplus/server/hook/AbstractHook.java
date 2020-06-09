package net.shortninja.staffplus.server.hook;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class AbstractHook implements IHook {

<<<<<<< HEAD
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
=======
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
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
}
