package net.shortninja.staffplus.player.attribute.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.CommandUtil;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.PlayerSession;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.player.attribute.infraction.Warning;
import net.shortninja.staffplus.reporting.Report;
import net.shortninja.staffplus.reporting.ReportService;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.staff.freeze.FreezeRequest;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.unordered.IPlayerSession;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.util.MessageCoordinator;
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
    private final MessageCoordinator message = IocContainer.getMessage();
    private final Options options = IocContainer.getOptions();
    private final Messages messages = IocContainer.getMessages();
    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private final FreezeHandler freezeHandler = IocContainer.getFreezeHandler();
    private final PlayerManager playerManager = IocContainer.getPlayerManager();
    private final ReportService reportService = IocContainer.getReportService();

    public ExamineGui(Player player, Player targetPlayer, String title) {
        super(SIZE, title);

        setInventoryContents(targetPlayer);

        if (options.modeExamineFood >= 0) {
            setItem(options.modeExamineFood, foodItem(targetPlayer), null);
        }

        if (options.modeExamineIp >= 0) {
            setItem(options.modeExamineIp, ipItem(targetPlayer), null);
        }


//        if (options.modeExaminePing >= 0) {
//            setItem(options.modeExaminePing, pingItem(targetPlayer), null);
//        }

        if (options.modeExamineGamemode >= 0) {
            setItem(options.modeExamineGamemode, gameModeItem(targetPlayer), null);
        }

        if (options.modeExamineInfractions >= 0) {
            setItem(options.modeExamineInfractions, infractionsItem(sessionManager.get(targetPlayer.getUniqueId())), null);
        }

        setInteractiveItems(targetPlayer);
        player.openInventory(getInventory());
        sessionManager.get(player.getUniqueId()).setCurrentGui(this);
    }

    private void setInventoryContents(Player targetPlayer) {
        ItemStack[] items = targetPlayer.getInventory().getContents();
        ItemStack[] armor = targetPlayer.getInventory().getArmorContents();

        JavaUtils.reverse(armor);
        for (int i = 0; i < items.length; i++) {
            setItem(i, items[i], null);
        }
        for (int i = 0; i <= armor.length - 1; i++) {
            if (i == 3) {
                setItem(39 + i, targetPlayer.getItemInHand(), null);
            }
            setItem(38 + i, armor[i], null);
        }
    }

    private void setInteractiveItems(final Player targetPlayer) {
        if (options.modeExamineLocation >= 0) {
            setItem(options.modeExamineLocation, locationItem(targetPlayer), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    player.teleport(targetPlayer);
                }

                @Override
                public boolean shouldClose() {
                    return true;
                }

                @Override
                public void execute(Player player, String input) {
                }
            });
        }

        if (options.modeExamineNotes >= 0) {
            setItem(options.modeExamineNotes, notesItem(sessionManager.get(targetPlayer.getUniqueId())), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    IPlayerSession playerSession = sessionManager.get(player.getUniqueId());

                    message.send(player, messages.typeInput, messages.prefixGeneral);

                    playerSession.setQueuedAction(new IAction() {
                        @Override
                        public void execute(Player player, String input) {
                            sessionManager.get(targetPlayer.getUniqueId()).addPlayerNote("&7" + input);
                            message.send(player, messages.inputAccepted, messages.prefixGeneral);
                        }

                        @Override
                        public boolean shouldClose() {
                            return true;
                        }

                        @Override
                        public void click(Player player, ItemStack item, int slot) {
                        }
                    });
                }

                @Override
                public boolean shouldClose() {
                    return true;
                }

                @Override
                public void execute(Player player, String input) {
                }
            });
        }

        if (options.modeExamineFreeze >= 0) {
            setItem(options.modeExamineFreeze, freezeItem(targetPlayer), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    CommandUtil.playerAction(player, () -> {
                        if (targetPlayer != null) {
                            freezeHandler.execute(new FreezeRequest(player, targetPlayer, freezeHandler.isFrozen(targetPlayer.getUniqueId())));
                        }
                    });
                }

                @Override
                public boolean shouldClose() {
                    return true;
                }

                @Override
                public void execute(Player player, String input) {
                }
            });
        }

        if (options.modeExamineWarn >= 0) {
            setItem(options.modeExamineWarn, warnItem(), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot) {
                    IPlayerSession playerSession = sessionManager.get(player.getUniqueId());

                    message.send(player, messages.typeInput, messages.prefixGeneral);

                    playerSession.setQueuedAction(new IAction() {
                        @Override
                        public void execute(Player player, String input) {
                            Optional<SppPlayer> onOrOfflinePlayer = playerManager.getOnOrOfflinePlayer(targetPlayer.getUniqueId());
                            if (!onOrOfflinePlayer.isPresent()) {
                                message.send(player, messages.playerOffline, messages.prefixGeneral);
                            } else {
                                IocContainer.getWarnService().sendWarning(player, onOrOfflinePlayer.get(), input);
                                message.send(player, messages.inputAccepted, messages.prefixGeneral);
                            }
                        }

                        @Override
                        public boolean shouldClose() {
                            return true;
                        }

                        @Override
                        public void click(Player player, ItemStack item, int slot) {
                        }
                    });
                }

                @Override
                public boolean shouldClose() {
                    return true;
                }

                @Override
                public void execute(Player player, String input) {
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

    private ItemStack pingItem(Player player) {
        ItemStack item = Items.builder()
            .setMaterial(Material.PAPER).setAmount(1)
            .setName("&bPing")
            .addLore(messages.examineIp.replace("%ping%", String.valueOf(PlayerSession.getPing(player))))
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

    private ItemStack infractionsItem(IPlayerSession playerSession) {
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

    private ItemStack notesItem(IPlayerSession playerSession) {
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
}