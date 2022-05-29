package net.shortninja.staffplus.core.domain.staff.vanish.gui;

import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;
import net.shortninja.staffplusplus.vanish.VanishOffEvent;
import net.shortninja.staffplusplus.vanish.VanishOnEvent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocListener(conditionalOnProperty = "vanish-module.vanish-bossbar-enabled=true")
public class VanishBossBarTask implements Listener, PluginDisable {

    private final static BossBar BOSS_BAR = Bukkit.createBossBar("Vanished", BarColor.WHITE, BarStyle.SOLID);

    @EventHandler
    private void onVanish(VanishOnEvent vanishOnEvent) {
        BOSS_BAR.addPlayer(vanishOnEvent.getPlayer());
    }

    @EventHandler
    private void onUnVanish(VanishOffEvent vanishOffEvent) {
        BOSS_BAR.removePlayer(vanishOffEvent.getPlayer());
    }

    @Override
    public void disable(StaffPlus staffPlus) {
        BOSS_BAR.removeAll();
    }
}
