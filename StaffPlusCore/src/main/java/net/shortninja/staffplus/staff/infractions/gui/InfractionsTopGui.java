package net.shortninja.staffplus.staff.infractions.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.player.attribute.gui.PagedGui;
import net.shortninja.staffplus.unordered.IAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class InfractionsTopGui extends PagedGui {

    public InfractionsTopGui(Player player, String title, int page) {
        super(player, title, page);
    }

    @Override
    protected void getNextUi(Player player, SppPlayer target, String title, int page) {
        new InfractionsTopGui(player, title, page);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                UUID playerUuid = UUID.fromString(StaffPlus.get().versionProtocol.getNbtString(item));
                Optional<SppPlayer> offender = IocContainer.getPlayerManager().getOnOrOfflinePlayer(playerUuid);
                offender.ifPresent(sppPlayer -> new InfractionsGui(player, sppPlayer, "Infractions " + sppPlayer.getUsername(), 0, () -> new InfractionsTopGui(player, getTitle(), getCurrentPage())));
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };
    }

    @Override
    public List<ItemStack> getItems(Player player, SppPlayer target, int offset, int amount) {
        return IocContainer.getInfractionsService()
            .getTopInfractions(getCurrentPage(), amount)
            .stream()
            .map(InfractionOverviewGuiProvider::build)
            .collect(Collectors.toList());
    }
}