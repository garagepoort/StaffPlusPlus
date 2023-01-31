package net.shortninja.staffplus.core.freeze.bootstrap;

import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import be.garagepoort.mcioc.tubingbukkit.annotations.BeforeTubingReload;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextManager;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.freeze.FreezeLuckPermsContextCalculator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@IocMultiProvider({PluginDisable.class, BeforeTubingReload.class})
public class LuckPermsHook implements PluginDisable, BeforeTubingReload {

    private ContextManager contextManager;
    private final OnlineSessionsManager sessionManager;
    private final TubingBukkitPlugin tubingPlugin;
    private final List<ContextCalculator<Player>> registeredCalculators = new ArrayList<>();
    private final boolean luckPermsEnabled;

    public LuckPermsHook(OnlineSessionsManager sessionManager, TubingBukkitPlugin tubingPlugin) {
        this.sessionManager = sessionManager;
        this.tubingPlugin = tubingPlugin;
        luckPermsEnabled = Bukkit.getPluginManager().getPlugin("LuckPerms") != null;
        if (luckPermsEnabled) {
            LuckPerms luckPerms = tubingPlugin.getServer().getServicesManager().load(LuckPerms.class);
            if (luckPerms == null) {
                throw new IllegalStateException("LuckPerms API not loaded.");
            }
            this.contextManager = luckPerms.getContextManager();
            this.register("Frozen", () -> new FreezeLuckPermsContextCalculator(this.sessionManager));
        }
    }

    @Override
    public void disable(TubingBukkitPlugin staffPlusPlus) {
        if (luckPermsEnabled) {
            this.registeredCalculators.forEach(c -> this.contextManager.unregisterCalculator(c));
            this.registeredCalculators.clear();
        }
    }

    @Override
    public void execute(TubingBukkitPlugin tubingPlugin) {
        if (luckPermsEnabled) {
            this.registeredCalculators.forEach(c -> this.contextManager.unregisterCalculator(c));
            this.registeredCalculators.clear();
        }
    }

    private void register(String option, Supplier<ContextCalculator< Player >> calculatorSupplier) {
        tubingPlugin.getLogger().info("Registering '" + option + "' calculator.");
        ContextCalculator<Player> calculator = calculatorSupplier.get();
        this.contextManager.registerCalculator(calculator);
        this.registeredCalculators.add(calculator);
    }

}
