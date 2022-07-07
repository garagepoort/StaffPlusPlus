package net.shortninja.staffplus.core.application.bootstrap;

import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import be.garagepoort.mcioc.tubingbukkit.annotations.BeforeTubingReload;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextManager;
import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeLuckPermsContextCalculator;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeLuckPermsContextCalculator;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishLuckPermsContextCalculator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@IocMultiProvider({PluginDisable.class, BeforeTubingReload.class})
public class LuckPermsHook implements PluginDisable, BeforeTubingReload {

    private ContextManager contextManager;
    private final OnlineSessionsManager sessionManager;
    private final List<ContextCalculator<Player>> registeredCalculators = new ArrayList<>();
    private final boolean luckPermsEnabled;

    public LuckPermsHook(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
        luckPermsEnabled = Bukkit.getPluginManager().getPlugin("LuckPerms") != null;
        if (luckPermsEnabled) {
            StaffPlusPlus.get().getLogger().info("Luckperms enabled");
            LuckPerms luckPerms = StaffPlusPlus.get().getServer().getServicesManager().load(LuckPerms.class);
            if (luckPerms == null) {
                throw new IllegalStateException("LuckPerms API not loaded.");
            }
            this.contextManager = luckPerms.getContextManager();
            this.register("StaffMode", () -> new StaffModeLuckPermsContextCalculator(this.sessionManager));
            this.register("Frozen", () -> new FreezeLuckPermsContextCalculator(this.sessionManager));
            this.register("Vanished", () -> new VanishLuckPermsContextCalculator(this.sessionManager));
        }
    }

    @Override
    public void disable(StaffPlusPlus staffPlusPlus) {
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
        StaffPlusPlus.get().getLogger().info("Registering '" + option + "' calculator.");
        ContextCalculator<Player> calculator = calculatorSupplier.get();
        this.contextManager.registerCalculator(calculator);
        this.registeredCalculators.add(calculator);
    }

}
