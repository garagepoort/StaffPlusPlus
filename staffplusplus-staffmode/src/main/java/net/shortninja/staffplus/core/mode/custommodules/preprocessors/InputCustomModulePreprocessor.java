package net.shortninja.staffplus.core.mode.custommodules.preprocessors;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.mode.custommodules.CustomModuleConfiguration;
import net.shortninja.staffplus.core.mode.custommodules.CustomModuleExecutor;
import org.bukkit.Bukkit;

import java.util.Map;

@IocBean
@IocMultiProvider(CustomModulePreProcessor.class)
public class InputCustomModulePreprocessor implements CustomModulePreProcessor {

    private static final String CANCEL = "cancel";

    private final Messages messages;
    private final OnlineSessionsManager sessionsManager;

    public InputCustomModulePreprocessor(Messages messages, OnlineSessionsManager sessionsManager) {
        this.messages = messages;
        this.sessionsManager = sessionsManager;
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

            OnlinePlayerSession playerSession = sessionsManager.get(player);
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
                Bukkit.getScheduler().runTaskLater(TubingBukkitPlugin.getPlugin(), () -> action.execute(player, pl), 1);
            });
        };
    }

}
