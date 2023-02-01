package net.shortninja.staffplus.core.vanish.gui;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;
import net.shortninja.staffplusplus.vanish.VanishOffEvent;
import net.shortninja.staffplusplus.vanish.VanishOnEvent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBukkitListener(conditionalOnProperty = "vanish-module.vanish-bossbar-enabled=true")
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
    public void disable(TubingBukkitPlugin staffPlusPlus) {
        BOSS_BAR.removeAll();
    }
}
