package net.shortninja.staffplus.core.domain.player;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.cmd.CommandUtil;
import net.shortninja.staffplus.core.domain.chat.ChatAction;
import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class ChatActionChatInterceptor implements ChatInterceptor {
    private final OnlineSessionsManager sessionManager;
    private final CommandUtil commandUtil;

    public ChatActionChatInterceptor(OnlineSessionsManager sessionManager, CommandUtil commandUtil) {
        this.sessionManager = sessionManager;
        this.commandUtil = commandUtil;
    }


    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        OnlinePlayerSession session = sessionManager.get(event.getPlayer());
        ChatAction chatAction = session.getChatAction();

        if (chatAction != null) {
            commandUtil.playerAction(event.getPlayer(), () -> {
                chatAction.execute(event.getPlayer(), event.getMessage());
                session.setChatAction(null);
            });
            return true;
        }
        return false;
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
