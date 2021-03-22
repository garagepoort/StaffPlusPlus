package net.shortninja.staffplus.core.domain.staff.infractions.gui;

import be.garagepoort.mcioc.IocContainer;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.gui.PagedGui;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.infractions.Infraction;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionsService;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class InfractionsGui extends PagedGui {

    public InfractionsGui(Player player, SppPlayer offender, String title, int page) {
        super(player, offender, title, page);
    }

    public InfractionsGui(Player player, SppPlayer offender, String title, int page, Supplier<AbstractGui> backGuiSupplier) {
        super(player, offender, title, page, backGuiSupplier);
    }

    @Override
    protected InfractionsGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new InfractionsGui(player, getTarget(), title, page, getPreviousGuiSupplier());
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                //Do nothing
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };
    }

    @Override
    public List<ItemStack> getItems(Player player, SppPlayer target, int offset, int amount) {
        return IocContainer.get(InfractionsService.class)
            .getAllInfractions(player, getTarget().getId(), getCurrentPage(), amount)
            .stream()
            .map(i -> {
                InfractionGuiProvider infractionGuiProvider = getInfractionGuiProvider(i);
                return infractionGuiProvider.getMenuItem(i);
            })
            .collect(Collectors.toList());
    }

    private InfractionGuiProvider getInfractionGuiProvider(Infraction i) {
        return IocContainer.getList(InfractionGuiProvider.class).stream()
            .filter(guiProvider -> guiProvider.getType().equals(i.getInfractionType())).findFirst()
            .orElseThrow(() -> new RuntimeException("No gui provider for infraction type: [" + i.getInfractionType() + "]"));
    }
}