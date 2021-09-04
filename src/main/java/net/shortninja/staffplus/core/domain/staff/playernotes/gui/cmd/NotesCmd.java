package net.shortninja.staffplus.core.domain.staff.playernotes.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.GuiActionService;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.playernotes.PlayerNote;
import net.shortninja.staffplus.core.domain.staff.playernotes.PlayerNoteService;
import net.shortninja.staffplusplus.playernotes.PlayerNoteFilters;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.OPTIONAL_BOTH;

@Command(
    command = "commands:notes",
    permissions = "permissions:player-notes.view",
    description = "View a player's notes",
    usage = "[player]",
    playerRetrievalStrategy = OPTIONAL_BOTH
)
@IocBean
@IocMultiProvider(SppCommand.class)
public class NotesCmd extends AbstractCmd {

    private static final String LIST_PARAM = "-l";

    private final PlayerNoteService playerNoteService;
    private final BukkitUtils bukkitUtils;
    private final GuiActionService guiActionService;
    private final PlayerManager playerManager;
    private final PlayerNoteFiltersMapper playerNoteFiltersMapper;

    public NotesCmd(PermissionHandler permissionHandler,
                    Messages messages,
                    CommandService commandService,
                    PlayerNoteService playerNoteService,
                    BukkitUtils bukkitUtils, GuiActionService guiActionService, PlayerManager playerManager, PlayerNoteFiltersMapper playerNoteFiltersMapper) {
        super(messages, permissionHandler, commandService);
        this.playerNoteService = playerNoteService;
        this.bukkitUtils = bukkitUtils;
        this.guiActionService = guiActionService;
        this.playerManager = playerManager;
        this.playerNoteFiltersMapper = playerNoteFiltersMapper;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer, Map<String, String> optionalParameters) {
        if (!(sender instanceof Player) || optionalParameters.containsKey(LIST_PARAM)) {
            PlayerNoteFilters.PlayerNoteFiltersBuilder playerNoteFiltersBuilder = new PlayerNoteFilters.PlayerNoteFiltersBuilder();
            optionalParameters.forEach((k, v) -> playerNoteFiltersMapper.map(k.substring(1), v, playerNoteFiltersBuilder));
            if (targetPlayer != null) {
                playerNoteFiltersBuilder.target(targetPlayer);
            }

            bukkitUtils.runTaskAsync(sender, () -> listNotes(sender, playerNoteService.findPlayerNotes(sender, playerNoteFiltersBuilder.build(), 0, 100)));
        } else {
            GuiActionBuilder guiActionBuilder = GuiActionBuilder.builder().action("player-notes/view/overview");
            optionalParameters.forEach((key, value) -> guiActionBuilder.param(key.substring(1), value));
            if (targetPlayer != null) {
                guiActionBuilder.param("target", targetPlayer.getUsername());
            }

            guiActionService.executeAction((Player) sender, guiActionBuilder.build());
        }
        return true;
    }

    @Override
    protected List<String> getOptionalParameters() {
        List<String> filterKeys = playerNoteFiltersMapper.getFilterKeys().stream().map(k -> "-" + k).collect(Collectors.toList());
        filterKeys.add(LIST_PARAM);
        return filterKeys;
    }

    private void listNotes(CommandSender sender, List<PlayerNote> notes) {

        for (String message : messages.noteListStart) {
            this.messages.send(sender, message.replace("%longline%", this.messages.LONG_LINE), message.contains("%longline%") ? "" : messages.prefixGeneral);
        }

        for (int i = 0, notesSize = notes.size(); i < notesSize; i++) {
            if (notes.get(i).isPrivateNote()) {
                messages.send(sender, messages.noteListEntryPrivate.replace("%count%", Integer.toString(i + 1)).replace("%note%", notes.get(i).getNote()), messages.prefixGeneral);
            } else {
                messages.send(sender, messages.noteListEntry.replace("%count%", Integer.toString(i + 1)).replace("%note%", notes.get(i).getNote()), messages.prefixGeneral);
            }
        }

        for (String message : messages.noteListEnd) {
            this.messages.send(sender, message.replace("%longline%", this.messages.LONG_LINE), message.contains("%longline%") ? "" : messages.prefixGeneral);
        }
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 0;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Arrays.stream(args).filter(a -> !a.startsWith("-")).findFirst();
    }

    @Override
    protected List<String> autoComplete(CommandSender sender, String[] args, String[] sppArgs) throws IllegalArgumentException {
        String currentArg = args.length > 0 ? args[args.length - 1] : "";

        if (currentArg.startsWith("-")) {
            return playerNoteFiltersMapper.getFilterKeys().stream()
                .map(k -> "-" + k + "=")
                .collect(Collectors.toList());
        }

        return playerManager.getAllPlayerNames().stream()
            .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
            .collect(Collectors.toList());
    }
}