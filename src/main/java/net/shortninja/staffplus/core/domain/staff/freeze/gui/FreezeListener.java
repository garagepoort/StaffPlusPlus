package net.shortninja.staffplus.core.domain.staff.freeze.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeGui;
import net.shortninja.staffplus.core.domain.staff.freeze.config.FreezeConfiguration;
import net.shortninja.staffplusplus.freeze.PlayerFrozenEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@IocBean
@IocListener
public class FreezeListener implements Listener {

    private final FreezeConfiguration freezeConfiguration;
    private final Messages messages;

    public FreezeListener(FreezeConfiguration freezeConfiguration, Messages messages) {
        this.freezeConfiguration = freezeConfiguration;
        this.messages = messages;
    }

    @EventHandler
    public void onFreeze(PlayerFrozenEvent event) {
        Player player = event.getTarget();
        if (freezeConfiguration.prompt) {
            new FreezeGui(freezeConfiguration.promptTitle).show(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 128));
        } else {
            messages.sendCollectedMessage(player, messages.freeze, messages.prefixGeneral);
        }

        messages.send(event.getIssuer(), messages.staffFroze.replace("%target%", player.getName()), messages.prefixGeneral);

        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 128));
        if (freezeConfiguration.sound != null) {
            freezeConfiguration.sound.play(player);
        }
    }
}
