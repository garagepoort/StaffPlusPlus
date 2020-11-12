package net.shortninja.staffplus.staff.examine;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.PassThroughClickAction;
import net.shortninja.staffplus.common.cmd.CommandUtil;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.staff.freeze.FreezeRequest;
import net.shortninja.staffplus.staff.reporting.Report;
import net.shortninja.staffplus.staff.reporting.ReportService;
import net.shortninja.staffplus.staff.warn.Warning;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ExamineGui extends AbstractGui {

    private static final int SIZE = 54;
    public static final int INVENTORY_START = 11;
    public static final int INVENTORY_END = INVENTORY_START + 36;
    public static final int ARMOR_START = INVENTORY_END + 1;
    public static final int ARMOR_END = ARMOR_START + 5;
    private static final PassThroughClickAction PASS_THROUGH_ACTION = new PassThroughClickAction();
    private final MessageCoordinator message = IocContainer.getMessage();
    private final Options options = IocContainer.getOptions();
    private final Messages messages = IocContainer.getMessages();
    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private final FreezeHandler freezeHandler = IocContainer.getFreezeHandler();
    private final PlayerManager playerManager = IocContainer.getPlayerManager();
    private final ReportService reportService = IocContainer.getReportService();
    private final PermissionHandler permissionHandler = IocContainer.getPermissionHandler();
    private final Player staff;
    private final Player targetPlayer;
    private String itemSelectedFrom;
    private int itemSelectedSlot;

    public ExamineGui(Player player, Player targetPlayer, String title) {
        super(SIZE, title);
        staff = player;
        this.targetPlayer = targetPlayer;

        update();
        player.closeInventory();
        player.openInventory(getInventory());
        sessionManager.get(player.getUniqueId()).setCurrentGui(this);
    }

    public void update() {
        if (permissionHandler.has(staff, options.permissionExamineViewInventory)) {
            setInventoryContents(targetPlayer);
        }

        if (options.modeExamineFood >= 0) {
            setItem(options.modeExamineFood - 1, foodItem(targetPlayer), null);
        }

        if (options.modeExamineIp >= 0) {
            setItem(options.modeExamineIp - 1, ipItem(targetPlayer), null);
        }

        if (options.modeExamineGamemode >= 0) {
            setItem(options.modeExamineGamemode - 1, gameModeItem(targetPlayer), null);
        }

        if (options.modeExamineInfractions >= 0) {
            setItem(options.modeExamineInfractions - 1, infractionsItem(sessionManager.get(targetPlayer.getUniqueId())), null);
        }

        setInteractiveItems(targetPlayer);
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }

    private void setInventoryContents(Player targetPlayer) {
        ItemStack[] items = targetPlayer.getInventory().getContents();
        ItemStack[] armor = targetPlayer.getInventory().getArmorContents();

        JavaUtils.reverse(armor);
        for (int i = 0; i < 36; i++) {
            setItem(INVENTORY_START + i, items[i], PASS_THROUGH_ACTION);
        }
        for (int i = 0; i < 5; i++) {
            setItem(ARMOR_START + i, items[36 + i], PASS_THROUGH_ACTION);
        }

        setItem(INVENTORY_START - 1, Items.createRedColoredGlass("Inventory Items", ""), null);
        setItem(ARMOR_START - 1, Items.createRedColoredGlass("Armor items", ""), null);
        setItem(SIZE - 1, Items.createRedColoredGlass("Inventory end", ""), null);
    }

    private void setInteractiveItems(final Player targetPlayer) {
        if (options.modeExamineLocation >= 0) {
            setItem(options.modeExamineLocation - 1, locationItem(targetPlayer), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    player.teleport(targetPlayer);
                }

                @Override
                public boolean shouldClose() {
                    return true;
                }
            });
        }

        if (options.modeExamineNotes >= 0) {
            setItem(options.modeExamineNotes - 1, notesItem(sessionManager.get(targetPlayer.getUniqueId())), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    PlayerSession playerSession = sessionManager.get(player.getUniqueId());

                    message.send(player, messages.typeInput, messages.prefixGeneral);

                    playerSession.setChatAction((player12, input) -> {
                        sessionManager.get(targetPlayer.getUniqueId()).addPlayerNote("&7" + input);
                        message.send(player12, messages.inputAccepted, messages.prefixGeneral);
                    });
                }

                @Override
                public boolean shouldClose() {
                    return true;
                }
            });
        }

        if (options.modeExamineFreeze >= 0) {
            setItem(options.modeExamineFreeze - 1, freezeItem(targetPlayer), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    CommandUtil.playerAction(player, () -> {
                        if (targetPlayer != null) {
                            freezeHandler.execute(new FreezeRequest(player, targetPlayer, !freezeHandler.isFrozen(targetPlayer.getUniqueId())));
                        }
                    });
                }

                @Override
                public boolean shouldClose() {
                    return true;
                }
            });
        }

        if (options.modeExamineWarn >= 0) {
            setItem(options.modeExamineWarn - 1, warnItem(), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    PlayerSession playerSession = sessionManager.get(player.getUniqueId());

                    message.send(player, messages.typeInput, messages.prefixGeneral);

                    playerSession.setChatAction((player1, input) -> {
                        Optional<SppPlayer> onOrOfflinePlayer = playerManager.getOnOrOfflinePlayer(targetPlayer.getUniqueId());
                        if (!onOrOfflinePlayer.isPresent()) {
                            message.send(player1, messages.playerOffline, messages.prefixGeneral);
                        } else {
                            IocContainer.getWarnService().sendWarning(player1, onOrOfflinePlayer.get(), input);
                            message.send(player1, messages.inputAccepted, messages.prefixGeneral);
                        }
                    });
                }

                @Override
                public boolean shouldClose() {
                    return true;
                }
            });
        }
    }

    private ItemStack foodItem(Player player) {
        int healthLevel = (int) player.getHealth();
        int foodLevel = player.getFoodLevel();
        List<String> lore = new ArrayList<String>();

        for (String string : messages.examineFood) {
            lore.add(string.replace("%health%", healthLevel + "/20").replace("%hunger%", foodLevel + "/20"));
        }

        ItemStack item = Items.builder()
            .setMaterial(Material.BREAD).setAmount(1)
            .setName("&bFood")
            .setLore(lore)
            .build();

        return item;
    }

    private ItemStack ipItem(Player player) {
        String ip = player.hasPermission(options.ipHidePerm) ? "127.0.0.1" : player.getAddress().getAddress().getHostAddress().replace("/", "");

        ItemStack item = Items.builder()
            .setMaterial(Material.COMPASS).setAmount(1)
            .setName("&bConnection")
            .addLore(messages.examineIp.replace("%ipaddress%", ip))
            .build();

        return item;
    }

    private ItemStack gameModeItem(Player player) {
        ItemStack item = Items.builder()
            .setMaterial(Material.GRASS).setAmount(1)
            .setName("&bGamemode")
            .addLore(messages.examineGamemode.replace("%gamemode%", player.getGameMode().toString()))
            .build();

        return item;
    }

    private ItemStack infractionsItem(PlayerSession playerSession) {
        List<Report> reports = reportService.getReports(playerSession.getUuid(), 0, 40);

        List<String> lore = new ArrayList<String>();
        IReport latestReport = reports.size() >= 1 ? reports.get(reports.size() - 1) : null;
        String latestReason = latestReport == null ? "null" : latestReport.getReason();

        for (String string : messages.infractionItem) {
            List<Warning> warnings = IocContainer.getWarnService().getWarnings(playerSession.getUuid());
            lore.add(string.replace("%warnings%", Integer.toString(warnings.size())).replace("%reports%", Integer.toString(reports.size())).replace("%reason%", latestReason));
        }

        ItemStack item = Items.builder()
            .setMaterial(Material.BOOK).setAmount(1)
            .setName("&bInfractions")
            .setLore(lore)
            .build();

        return item;
    }

    private ItemStack locationItem(Player player) {
        Location location = player.getLocation();

        ItemStack item = Items.builder()
            .setMaterial(Material.MAP).setAmount(1)
            .setName("&bLocation")
            .addLore(messages.examineLocation.replace("%location%", location.getWorld().getName() + " &8� &7" + JavaUtils.serializeLocation(location)))
            .build();

        return item;
    }

    private ItemStack notesItem(PlayerSession playerSession) {
        List<String> notes = playerSession.getPlayerNotes().isEmpty() ? Arrays.asList("&7No notes found") : playerSession.getPlayerNotes();

        ItemStack item = Items.builder()
            .setMaterial(Material.MAP).setAmount(1)
            .setName(messages.examineNotes)
            .setLore(notes)
            .build();

        return item;
    }

    private ItemStack freezeItem(Player player) {
        String frozenStatus = freezeHandler.isFrozen(player.getUniqueId()) ? "" : "not ";

        ItemStack item = Items.builder()
            .setMaterial(Material.BLAZE_ROD).setAmount(1)
            .setName("&bFreeze player")
            .setLore(Arrays.asList(messages.examineFreeze, "&7Currently " + frozenStatus + "frozen."))
            .build();

        return item;
    }

    private ItemStack warnItem() {
        ItemStack item = Items.builder()
            .setMaterial(Material.PAPER).setAmount(1)
            .setName("&bWarn player")
            .addLore(messages.examineWarn)
            .build();

        return item;
    }

    boolean isInventorySlot(int slot) {
        return slot >= INVENTORY_START && slot != (ARMOR_START - 1);
    }

    void setItemSelectedFrom(String itemSelectedFrom) {
        this.itemSelectedFrom = itemSelectedFrom;
    }

    public String getItemSelectedFrom() {
        return itemSelectedFrom;
    }

    void setItemSelectedSlot(int itemSelectedSlot) {
        if (itemSelectedSlot < ARMOR_START) {
            this.itemSelectedSlot = itemSelectedSlot - INVENTORY_START;
        } else {
            //deduct one to take into account the divider item
            this.itemSelectedSlot = itemSelectedSlot - INVENTORY_START - 1;
        }
    }

    int getItemSelectedSlot() {
        return itemSelectedSlot;
    }

}