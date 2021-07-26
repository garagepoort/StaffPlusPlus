package net.shortninja.staffplus.core.common.gui;


import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.common.Items;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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

            addFooter(action, currentPage, amount);
            return this;
        }

        public <T> Builder addPagedItems(String action, Collection<T> items, Function<T, ItemStack> itemStackProvider, Function<T, String> actionProvider, int currentPage, int pageSize) {
            int index = 0;
            for (T item : items) {
                this.addItem(actionProvider.apply(item), index, itemStackProvider.apply(item));
            }

            addFooter(action, currentPage, pageSize);
            return this;
        }

        public Builder backAction(String backAction) {
            if (backAction != null) {
                addItem(backAction, 49, Items.createDoor("Back", "Go back"));
            }
            return this;
        }

        public Builder addPagedItems(String action, Consumer<Builder> itemProvider, int currentPage, int amount) {
            itemProvider.accept(this);
            addFooter(action, currentPage, amount);
            return this;
        }

        private void addFooter(String action, int currentPage, int pageSize) {
            addNextPageItem(this, 53, action, currentPage, pageSize);
            addNextPageItem(this, 52, action, currentPage, pageSize);
            addNextPageItem(this, 51, action, currentPage, pageSize);

            if (currentPage != 0) {
                addPreviousPageItem(this, 45, action, currentPage, pageSize);
                addPreviousPageItem(this, 46, action, currentPage, pageSize);
                addPreviousPageItem(this, 47, action, currentPage, pageSize);
            }
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