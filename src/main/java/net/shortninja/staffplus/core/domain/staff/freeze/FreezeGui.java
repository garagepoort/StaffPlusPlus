package net.shortninja.staffplus.core.domain.staff.freeze;

import be.garagepoort.mcioc.IocContainer;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FreezeGui extends AbstractGui {
    private static final int SIZE = 9;
    private Messages messages = IocContainer.get(Messages.class);


    public FreezeGui(String title) {
        super(SIZE, title);
    }

    @Override
    public void buildGui() {
        setItem(4, freezeItem(), null);
    }

    private ItemStack freezeItem() {
        List<String> freezeMessage = new ArrayList<>(messages.freeze);
        String name = getTitle();
        List<String> lore = Arrays.asList("&7You are currently frozen!");

        if (freezeMessage.size() >= 1) {
            name = freezeMessage.get(0);
            freezeMessage.remove(0);
            lore = freezeMessage;
        }

        ItemStack item = Items.builder()
                .setMaterial(Material.PAPER).setAmount(1)
                .setName(name)
                .setLore(lore)
                .build();

        return item;
    }

}