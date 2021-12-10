package net.shortninja.staffplus.core.domain.staff.playernotes.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.playernotes.PlayerNoteService;
import net.shortninja.staffplusplus.session.SppInteractor;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.BOTH;

@Command(
    command = "commands:note",
    permissions = "permissions:player-notes.create",
    description = "Create a player note",
    usage = "[player] [note]",
    playerRetrievalStrategy = BOTH
)
@IocBean
@IocMultiProvider(SppCommand.class)
public class NoteCmd extends AbstractCmd {

    @ConfigProperty("permissions:player-notes.create-private")
    private String privateNotePermission;
    @ConfigProperty("permissions:player-notes.create")
    private String createNotePermission;

    private final PlayerNoteService playerNoteService;
    private final BukkitUtils bukkitUtils;
    private final PlayerManager playerManager;

    public NoteCmd(PermissionHandler permissionHandler,
                   Messages messages,
                   CommandService commandService,
                   PlayerNoteService playerNoteService,
                   BukkitUtils bukkitUtils, PlayerManager playerManager) {
        super(messages, permissionHandler, commandService);
        this.playerNoteService = playerNoteService;
        this.bukkitUtils = bukkitUtils;
        this.playerManager = playerManager;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer, Map<String, String> optionalParameters) {
        bukkitUtils.runTaskAsync(sender, () -> {
            boolean isPrivateNote = optionalParameters.containsKey("-private");
            permissionHandler.validate(sender, isPrivateNote ? privateNotePermission : createNotePermission);

            UUID senderUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : Constants.CONSOLE_UUID;
            String senderName = sender instanceof Player ? sender.getName() : "Console";

            playerNoteService.createNote(new SppInteractor(senderUuid, senderName, sender), JavaUtils.compileWords(args, 1), targetPlayer, isPrivateNote);
        });
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 2;
    }

    @Override
    protected List<String> getOptionalParameters() {
        return Collections.singletonList("-private");
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.of(args[0]);
    }

    @Override
    protected List<String> autoComplete(CommandSender sender, String[] args, String[] sppArgs) throws IllegalArgumentException {
        String currentArg = args.length > 0 ? args[args.length - 1] : "";

        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
                .collect(Collectors.toList());
        }
        if (currentArg.startsWith("-") && sender.hasPermission(privateNotePermission)) {
            return Stream.of("-private")
                .filter(s -> s.contains(currentArg))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}