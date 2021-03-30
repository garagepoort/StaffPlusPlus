package net.shortninja.staffplus.core.domain.staff.mode.handler;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;

import net.shortninja.staffplus.core.domain.staff.mode.item.CustomModuleConfiguration;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.Bukkit;

import java.util.Map;

@IocBean
@IocMultiProvider(CustomModulePreProcessor.class)
public class InputCustomModulePreprocessor implements CustomModulePreProcessor {

    private static final String CANCEL = "cancel";

    private final Messages messages;

    public InputCustomModulePreprocessor(Messages messages) {
        this.messages = messages;
    }

    @Override
    public CustomModuleExecutor process(CustomModuleExecutor action, CustomModuleConfiguration customModuleConfiguration, Map<String, String> placeholders) {
        if (!customModuleConfiguration.isRequireInput()) {
            return action;
        }
        return (player, pl) -> {
            String inputPrompt = customModuleConfiguration.getInputPrompt();
            for (Map.Entry<String, String> entry : pl.entrySet()) {
                inputPrompt = inputPrompt.replace(entry.getKey(), entry.getValue());
            }

            PlayerSession playerSession = StaffPlus.get().getIocContainer().get(SessionManagerImpl.class).get(player.getUniqueId());
            messages.send(player, "&7------", messages.prefixGeneral);
            messages.send(player, "&6" + inputPrompt, messages.prefixGeneral);
            messages.send(player, "&6 or \"cancel\" to cancel the action", messages.prefixGeneral);
            messages.send(player, "&7------", messages.prefixGeneral);
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messages.send(player, "&CYou have cancelled this action", messages.prefixGeneral);
                    return;
                }
                pl.put("%input%", message);
                Bukkit.getScheduler().runTaskLater(StaffPlus.get(), () -> action.execute(player, pl), 1);
            });
        };
    }

}
