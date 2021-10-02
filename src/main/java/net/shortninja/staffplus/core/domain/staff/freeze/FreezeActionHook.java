package net.shortninja.staffplus.core.domain.staff.freeze;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.CreateStoredCommandRequest;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommandMapper;
import net.shortninja.staffplus.core.domain.staff.freeze.config.FreezeConfiguration;
import net.shortninja.staffplusplus.freeze.PlayerFrozenEvent;
import net.shortninja.staffplusplus.freeze.PlayerUnFrozenEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

@IocBean
@IocListener
public class FreezeActionHook implements Listener {

    private final FreezeConfiguration freezeConfiguration;
    private final ActionService actionService;
    private final ConfiguredCommandMapper configuredCommandMapper;
    private final BukkitUtils bukkitUtils;

    public FreezeActionHook(FreezeConfiguration freezeConfiguration,
                            ActionService actionService,
                            ConfiguredCommandMapper configuredCommandMapper, BukkitUtils bukkitUtils) {
        this.freezeConfiguration = freezeConfiguration;
        this.actionService = actionService;
        this.configuredCommandMapper = configuredCommandMapper;
        this.bukkitUtils = bukkitUtils;
    }

    @EventHandler
    public void onFrozen(PlayerFrozenEvent event) {
        executeActions(event.getIssuer(), event.getTarget(), freezeConfiguration.freezeCommandHooks);
    }

    @EventHandler
    public void onUnfreeze(PlayerUnFrozenEvent event) {
        executeActions(event.getIssuer(), event.getTarget(), freezeConfiguration.unfreezeCommandHooks);
    }

    private void executeActions(CommandSender issuer, Player target, List<ConfiguredCommand> commands) {
        bukkitUtils.runTaskAsync(() -> {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%issuer%", issuer.getName());
            placeholders.put("%target%", target.getName());

            List<CreateStoredCommandRequest> commancreateStoredCommandRequests = configuredCommandMapper.toCreateRequests(commands, placeholders, emptyMap(), emptyList());
            actionService.createCommands(commancreateStoredCommandRequests);
        });
    }


}
