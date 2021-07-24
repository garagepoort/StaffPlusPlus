package net.shortninja.staffplus.core.common.gui;


import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.common.Items;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PagedGuiBuilder {
    private static final int SIZE = 54;


    public static class Builder extends TubingGui.Builder {

        public Builder(String title) {
            super(title, SIZE);
        }

        public Builder addPagedItems(String action, List<ItemStack> items, int currentPage, int amount) {
            int index = 0;
            for (ItemStack item : items) {
                this.addItem(null, index, item);
                index++;
            }

            addNextPageItem(this, 53, action, currentPage, amount);
            addNextPageItem(this, 52, action, currentPage, amount);
            addNextPageItem(this, 51, action, currentPage, amount);

            if (currentPage != 0) {
                addPreviousPageItem(this, 45, action, currentPage, amount);
                addPreviousPageItem(this, 46, action, currentPage, amount);
                addPreviousPageItem(this, 47, action, currentPage, amount);
            }
            return this;
        }

        private void addNextPageItem(Builder builder, int slot, String action, int currentPage, int amount) {
            ItemStack item = Items.editor(Items.createGreenColoredGlass("Next Page", ""))
                .setAmount(1)
                .build();
            builder.addItem(action + "?page=" + (currentPage + 1) + "&amount=" + amount, slot, item);
        }

        private void addPreviousPageItem(Builder builder, int slot, String action, int currentPage, int amount) {
            ItemStack item = Items.editor(Items.createRedColoredGlass("Previous Page", ""))
                .setAmount(1)
                .build();
            builder.addItem(action + "?page=" + (currentPage - 1) + "&amount=" + amount, slot, item);
        }
    }


}