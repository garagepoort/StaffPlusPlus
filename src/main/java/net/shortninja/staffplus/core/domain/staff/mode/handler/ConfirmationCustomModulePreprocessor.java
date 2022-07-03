package net.shortninja.staffplus.core.domain.staff.mode.handler;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.confirmation.ConfirmationConfig;
import net.shortninja.staffplus.core.domain.confirmation.ConfirmationService;
import net.shortninja.staffplus.core.domain.staff.mode.item.CustomModuleConfiguration;

import java.util.Map;

@IocBean
@IocMultiProvider(CustomModulePreProcessor.class)
public class ConfirmationCustomModulePreprocessor implements CustomModulePreProcessor {

    private final Messages messages;
    private final ConfirmationService confirmationService;
    private final BukkitUtils bukkitUtils;


    public ConfirmationCustomModulePreprocessor(Messages messages, ConfirmationService confirmationService, BukkitUtils bukkitUtils) {
        this.messages = messages;
        this.confirmationService = confirmationService;
        this.bukkitUtils = bukkitUtils;
    }

    @Override
    public CustomModuleExecutor process(CustomModuleExecutor action, CustomModuleConfiguration customModuleConfiguration, Map<String, String> placeholders) {
        if (!customModuleConfiguration.getConfirmationConfig().isPresent()) {
            return action;
        }

        return (player, pl) -> bukkitUtils.runTaskLater(player, () -> {
            ConfirmationConfig confirmationConfig = customModuleConfiguration.getConfirmationConfig().get();
            confirmationService.showConfirmation(player, confirmationConfig, pl,
                player1 -> action.execute(player1, pl),
                p -> messages.send(p, "You have cancelled the action", messages.prefixGeneral));
        });
    }

}
