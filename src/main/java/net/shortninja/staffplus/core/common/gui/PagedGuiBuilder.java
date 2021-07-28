package net.shortninja.staffplus.core.common.gui;


import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.common.Items;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static be.garagepoort.mcioc.gui.TubingGuiActions.NOOP;

public class PagedGuiBuilder {
    private static final int SIZE = 54;


    public static class Builder extends TubingGui.Builder {

        public Builder(String title) {
            super(title, SIZE);
        }

        public Builder addPagedItems(String action, List<ItemStack> items, int currentPage) {
            int index = 0;
            for (ItemStack item : items) {
                this.addItem(NOOP, index, item);
                index++;
            }

            addFooter(action, currentPage);
            return this;
        }

        public <T> Builder addPagedItems(String action, Collection<T> items, Function<T, ItemStack> itemStackProvider, Function<T, String> actionProvider, int currentPage) {
            int index = 0;
            for (T item : items) {
                this.addItem(actionProvider.apply(item), index, itemStackProvider.apply(item));
                index++;
            }

            addFooter(action, currentPage);
            return this;
        }

        public <T> Builder addPagedItems(String action, Collection<T> items,
                                         Function<T, ItemStack> itemStackProvider,
                                         Function<T, String> leftActionProvider,
                                         Function<T, String> rightActionProvider, int currentPage) {
            int index = 0;
            for (T item : items) {
                this.addItem(leftActionProvider.apply(item), rightActionProvider.apply(item), index, itemStackProvider.apply(item));
                index++;
            }

            addFooter(action, currentPage);
            return this;
        }

        public Builder backAction(String backAction) {
            if (backAction != null) {
                addItem(backAction, 49, Items.createDoor("Back", "Go back"));
            }
            return this;
        }

        public Builder addPagedItems(String action, Consumer<Builder> itemProvider, int currentPage) {
            itemProvider.accept(this);
            addFooter(action, currentPage);
            return this;
        }

        private void addFooter(String action, int currentPage) {
            addNextPageItem(this, 53, action, currentPage);
            addNextPageItem(this, 52, action, currentPage);
            addNextPageItem(this, 51, action, currentPage);

            if (currentPage != 0) {
                addPreviousPageItem(this, 45, action, currentPage);
                addPreviousPageItem(this, 46, action, currentPage);
                addPreviousPageItem(this, 47, action, currentPage);
            }
        }

        private void addNextPageItem(Builder builder, int slot, String action, int currentPage) {
            ItemStack item = Items.editor(Items.createGreenColoredGlass("Next Page", ""))
                .setAmount(1)
                .build();
            String actionQuery = GuiActionBuilder.fromAction(action)
                .param("page", String.valueOf((currentPage + 1)))
                .build();
            builder.addItem(actionQuery, slot, item);
        }

        private void addPreviousPageItem(Builder builder, int slot, String action, int currentPage) {
            ItemStack item = Items.editor(Items.createRedColoredGlass("Previous Page", ""))
                .setAmount(1)
                .build();
            String actionQuery = GuiActionBuilder.fromAction(action)
                .param("page", String.valueOf((currentPage - 1)))
                .build();
            builder.addItem(actionQuery, slot, item);
        }

    }


}