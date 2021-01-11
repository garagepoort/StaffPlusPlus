package net.shortninja.staffplus.staff.examine;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.PassThroughClickAction;
import net.shortninja.staffplus.common.UpdatableGui;
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
import net.shortninja.staffplus.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplus.staff.reporting.Report;
import net.shortninja.staffplus.staff.reporting.ReportService;
import net.shortninja.staffplus.staff.warn.Warning;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.factory.InventoryFactory;
import net.shortninja.staffplus.util.lib.JavaUtils;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ExamineGui extends AbstractGui implements UpdatableGui {

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
    private final SppPlayer targetPlayer;
    private final Inventory targetInventory;
    private String itemSelectedFrom;
    private int itemSelectedSlot;
    private ExamineModeConfiguration examineModeConfiguration;

    public ExamineGui(Player player, SppPlayer targetPlayer, String title) {
        super(SIZE, title);
        examineModeConfiguration = options.modeConfiguration.getExamineModeConfiguration();

        staff = player;
        this.targetPlayer = targetPlayer;
        targetInventory = targetPlayer.isOnline() ? targetPlayer.getPlayer().getInventory() : InventoryFactory.loadInventoryOffline(player, targetPlayer);

        update();
        player.closeInventory();
        player.openInventory(getInventory());
        sessionManager.get(player.getUniqueId()).setCurrentGui(this);
    }

    @Override
    public void update() {
        if ("player".equalsIgnoreCase(getItemSelectedFrom())) {
            //Stop updating if a staffmember picked up an item.
            //This prevents item duplication
            return;
        }
        if (permissionHandler.has(staff, options.examineConfiguration.getPermissionExamineViewInventory()) && targetPlayer.isOnline()) {
            setInventoryContents();
        }
        if (permissionHandler.has(staff, options.examineConfiguration.getPermissionExamineViewInventoryOffline()) && !targetPlayer.isOnline()) {
            setInventoryContents();
        }

        if (examineModeConfiguration.getModeExamineFood() >= 0 && targetPlayer.isOnline()) {
            setItem(examineModeConfiguration.getModeExamineFood() - 1, foodItem(targetPlayer.getPlayer()), null);
        }

        if (examineModeConfiguration.getModeExamineIp() >= 0 && targetPlayer.isOnline()) {
            setItem(examineModeConfiguration.getModeExamineIp() - 1, ipItem(targetPlayer.getPlayer()), null);
        }

        if (examineModeConfiguration.getModeExamineGamemode() >= 0 && targetPlayer.isOnline()) {
            setItem(examineModeConfiguration.getModeExamineGamemode() - 1, gameModeItem(targetPlayer.getPlayer()), null);
        }

        if (examineModeConfiguration.getModeExamineInfractions() >= 0) {
            setItem(examineModeConfiguration.getModeExamineInfractions() - 1, infractionsItem(targetPlayer), null);
        }

        setInteractiveItems(targetPlayer);
    }


    public SppPlayer getTargetPlayer() {
        return targetPlayer;
    }

    public Inventory getTargetInventory() {
        return targetInventory;
    }

    private void setInventoryContents() {
        ItemStack[] items = targetInventory.getContents();

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

    private void setInteractiveItems(final SppPlayer targetPlayer) {
        if (examineModeConfiguration.getModeExamineLocation() >= 0 && targetPlayer.isOnline()) {
            setItem(examineModeConfiguration.getModeExamineLocation() - 1, locationItem(targetPlayer.getPlayer()), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    player.teleport(targetPlayer.getPlayer());
                }

                @Override
                public boolean shouldClose(Player player) {
                    return true;
                }
            });
        }

        if (examineModeConfiguration.getModeExamineNotes() >= 0 && targetPlayer.isOnline()) {
            setItem(examineModeConfiguration.getModeExamineNotes() - 1, notesItem(sessionManager.get(targetPlayer.getId())), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    PlayerSession playerSession = sessionManager.get(player.getUniqueId());

                    message.send(player, messages.typeInput, messages.prefixGeneral);

                    playerSession.setChatAction((player12, input) -> {
                        sessionManager.get(targetPlayer.getId()).addPlayerNote("&7" + input);
                        message.send(player12, messages.inputAccepted, messages.prefixGeneral);
                    });
                }

                @Override
                public boolean shouldClose(Player player) {
                    return true;
                }
            });
        }

        if (examineModeConfiguration.getModeExamineFreeze() >= 0 && targetPlayer.isOnline()) {
            setItem(examineModeConfiguration.getModeExamineFreeze() - 1, freezeItem(targetPlayer.getPlayer()), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    CommandUtil.playerAction(player, () -> {
                        if (targetPlayer.getPlayer() != null) {
                            freezeHandler.execute(new FreezeRequest(player, targetPlayer.getPlayer(), !freezeHandler.isFrozen(targetPlayer.getId())));
                        }
                    });
                }

                @Override
                public boolean shouldClose(Player player) {
                    return true;
                }
            });
        }

        if (examineModeConfiguration.getModeExamineWarn() >= 0) {
            IAction severityAction = new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    new SeverityLevelSelectGui(player, "Select severity level", targetPlayer, () -> new ExamineGui(player, targetPlayer, getTitle()));
                }

                @Override
                public boolean shouldClose(Player player) {
                    return false;
                }
            };
            IAction warnAction = new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    PlayerSession playerSession = sessionManager.get(player.getUniqueId());

                    message.send(player, messages.typeInput, messages.prefixGeneral);

                    playerSession.setChatAction((player1, input) -> {
                        Optional<SppPlayer> onOrOfflinePlayer = playerManager.getOnOrOfflinePlayer(targetPlayer.getId());
                        if (!onOrOfflinePlayer.isPresent()) {
                            message.send(player1, messages.playerOffline, messages.prefixGeneral);
                        } else {
                            IocContainer.getWarnService().sendWarning(player1, onOrOfflinePlayer.get(), input);
                            message.send(player1, messages.inputAccepted, messages.prefixGeneral);
                        }
                    });
                }

                @Override
                public boolean shouldClose(Player player) {
                    return true;
                }
            };

            setItem(examineModeConfiguration.getModeExamineWarn() - 1, warnItem(), IocContainer.getOptions().warningConfiguration.getSeverityLevels().isEmpty() ? warnAction : severityAction);
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

    private ItemStack infractionsItem(SppPlayer player) {
        List<Report> reports = reportService.getReports(player.getId(), 0, 40);

        List<String> lore = new ArrayList<String>();
        IReport latestReport = reports.size() >= 1 ? reports.get(reports.size() - 1) : null;
        String latestReason = latestReport == null ? "null" : latestReport.getReason();

        for (String string : messages.infractionItem) {
            List<Warning> warnings = IocContainer.getWarnService().getWarnings(player.getId());
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