package net.shortninja.staffplus.core.domain.staff.freeze.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeGui;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.freeze.FreezeModeConfiguration;
import net.shortninja.staffplusplus.freeze.PlayerFrozenEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@IocBean
@IocListener
public class FreezeListener implements Listener {

    private final FreezeModeConfiguration freezeModeConfiguration;
    private final Messages messages;

    public FreezeListener(Options options, Messages messages) {
        this.freezeModeConfiguration = options.staffItemsConfiguration.getFreezeModeConfiguration();
        this.messages = messages;
    }

    @EventHandler
    public void onFreeze(PlayerFrozenEvent event) {
        Player player = event.getTarget();
        if (freezeModeConfiguration.isModeFreezePrompt()) {
            new FreezeGui(freezeModeConfiguration.getModeFreezePromptTitle()).show(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 128));
        } else {
            messages.sendCollectedMessage(player, messages.freeze, messages.prefixGeneral);
        }

        messages.send(event.getIssuer(), messages.staffFroze.replace("%target%", player.getName()), messages.prefixGeneral);

        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 128));
        freezeModeConfiguration.getModeFreezeSound().ifPresent(s -> s.play(player));
    }
}
