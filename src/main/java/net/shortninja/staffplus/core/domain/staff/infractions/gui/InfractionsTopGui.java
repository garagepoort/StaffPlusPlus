package net.shortninja.staffplus.core.domain.staff.infractions.gui;

import net.shortninja.staffplus.core.StaffPlus;
import be.garagepoort.mcioc.IocContainer;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.gui.PagedGui;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionType;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionsService;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class InfractionsTopGui extends PagedGui {

    private List<InfractionType> infractionFilters = new ArrayList<>();

    public InfractionsTopGui(Player player, String title, int page) {
        super(player, title, page);
    }

    public InfractionsTopGui(Player player, String title, int page, List<InfractionType> infractionFilters) {
        super(player, title, page);

        this.infractionFilters = infractionFilters;
    }

    @Override
    protected InfractionsTopGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new InfractionsTopGui(player, title, page, infractionFilters);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                UUID playerUuid = UUID.fromString(StaffPlus.get().versionProtocol.getNbtString(item));
                Optional<SppPlayer> offender = IocContainer.get(PlayerManager.class).getOnOrOfflinePlayer(playerUuid);
                offender.ifPresent(sppPlayer -> showInfractionsUI(player, sppPlayer));
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };
    }

    private void showInfractionsUI(Player player, SppPlayer sppPlayer) {
        new InfractionsGui(player, sppPlayer, "Infractions " + sppPlayer.getUsername(), 0, () -> new InfractionsTopGui(player, getTitle(), getCurrentPage())).show(player);
    }

    @Override
    public List<ItemStack> getItems(Player player, SppPlayer target, int offset, int amount) {
        return IocContainer.get(InfractionsService.class)
            .getTopInfractions(getCurrentPage(), amount, infractionFilters)
            .stream()
            .map(InfractionOverviewGuiProvider::build)
            .collect(Collectors.toList());
    }
}