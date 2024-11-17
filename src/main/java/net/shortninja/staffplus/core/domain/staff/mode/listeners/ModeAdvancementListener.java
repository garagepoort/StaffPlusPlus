package net.shortninja.staffplus.core.domain.staff.mode;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.advancement.Advancement;
import org.bukkit.GameRule;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.scheduler.BukkitRunnable;

@IocBukkitListener
public class ModeAdvancementListener implements Listener {
    private final OnlineSessionsManager sessionManager;
    
    // Used to prevent race conditions if multiple achievements are awarded
    private boolean announceGameruleDisabledByUs = false;

    public ModeAdvancementListener(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAdvancementCompleted(PlayerAdvancementDoneEvent event)  {
        Player player = event.getPlayer();
        OnlinePlayerSession session = sessionManager.get(player);
        
        if (!session.isInStaffMode()) return;
        if (!(player.getWorld().getGameRuleValue(GameRule.ANNOUNCE_ADVANCEMENTS) || announceGameruleDisabledByUs)) return;
        
        Advancement advancement = event.getAdvancement();
        
        // There is no better way to do it than to disable the gamerule and then enable it
        announceGameruleDisabledByUs = true;
        player.getWorld().setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        
        for (String criteria : advancement.getCriteria()) {
            player.getAdvancementProgress(advancement).revokeCriteria(criteria);
        }
        
        // Enable gamerule after the event
        new BukkitRunnable() {
            @Override
            public void run() {
                player.getWorld().setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, true);
                announceGameruleDisabledByUs = false;
            }
        }.runTask(StaffPlusPlus.get());
    }
}