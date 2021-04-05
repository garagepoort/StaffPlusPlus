package net.shortninja.staffplus.core.application.bootstrap;

import be.garagepoort.mcioc.IocConditional;
import be.garagepoort.mcioc.IocMultiProvider;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextManager;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeLuckPermsContextCalculator;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@IocMultiProvider(PluginDisable.class)
public class LuckPermsHook implements PluginDisable {

    private ContextManager contextManager;
    private final SessionManagerImpl sessionManager;
    private final List<ContextCalculator<Player>> registeredCalculators = new ArrayList<>();
    private boolean luckPermsEnabled;

    public LuckPermsHook(SessionManagerImpl sessionManager) {
        this.sessionManager = sessionManager;
        luckPermsEnabled = Bukkit.getPluginManager().getPlugin("LuckPerms") != null;
        if (luckPermsEnabled) {
            StaffPlus.get().getLogger().info("Luckperms enabled");
            LuckPerms luckPerms = StaffPlus.get().getServer().getServicesManager().load(LuckPerms.class);
            if (luckPerms == null) {
                throw new IllegalStateException("LuckPerms API not loaded.");
            }
            this.contextManager = luckPerms.getContextManager();
            this.register("StaffMode", () -> new StaffModeLuckPermsContextCalculator(this.sessionManager));
        }
    }

    @IocConditional
    public static boolean shouldInitialize() {
        return Bukkit.getPluginManager().getPlugin("LuckPerms") != null;
    }

    @Override
    public void disable(StaffPlus staffPlus) {
        if (luckPermsEnabled) {
            this.registeredCalculators.forEach(c -> this.contextManager.unregisterCalculator(c));
            this.registeredCalculators.clear();
        }
    }

    private void register(String option, Supplier<ContextCalculator< Player >> calculatorSupplier) {
        StaffPlus.get().getLogger().info("Registering '" + option + "' calculator.");
        ContextCalculator<Player> calculator = calculatorSupplier.get();
        this.contextManager.registerCalculator(calculator);
        this.registeredCalculators.add(calculator);
    }

}
