package net.shortninja.staffplus.core.domain.staff.vanish;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

@IocBean
@IocMultiProvider(VanishStrategy.class)
public class ListVanishStrategy implements VanishStrategy {

    private final VanishConfiguration vanishConfiguration;
    private final Messages messages;
    private final IProtocolService protocolService;

    public ListVanishStrategy(VanishConfiguration vanishConfiguration, Messages messages, IProtocolService protocolService) {
        this.vanishConfiguration = vanishConfiguration;
        this.messages = messages;
        this.protocolService = protocolService;
    }

    @Override
    public void vanish(Player player) {
        if (vanishConfiguration.vanishTabList) {
            protocolService.getVersionProtocol().listVanish(player, true);
        }

        String message = messages.listVanish.replace("%status%", messages.enabled);
        if (StringUtils.isNotEmpty(message)) {
            this.messages.send(player, message, messages.prefixGeneral);
        }
    }

    @Override
    public void unvanish(Player player) {
        protocolService.getVersionProtocol().listVanish(player, false);
        String message = messages.listVanish.replace("%status%", messages.disabled);
        if (StringUtils.isNotEmpty(message)) {
            this.messages.send(player, message, messages.prefixGeneral);
        }
    }

    @Override
    public void updateVanish(Player player) {
        // no implementation for list vanish
    }

    @Override
    public VanishType getVanishType() {
        return VanishType.LIST;
    }

}
