package net.shortninja.staffplus.core.common.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static net.shortninja.staffplus.core.common.JavaUtils.formatLines;

public class LoreBuilder {

    private List<String> lore = new ArrayList<>();
    private String labelColor;
    private String valueColor;

    private LoreBuilder(String labelColor, String valueColor) {
        this.labelColor = labelColor;
        this.valueColor = valueColor;
    }

    public static LoreBuilder builder(String labelColor, String valueColor) {
        return new LoreBuilder(labelColor, valueColor);
    }

    public LoreBuilder addItem(String value) {
        addItem(value, true);
        return this;
    }

    public LoreBuilder addItem(String value, boolean shouldAdd) {
        if (shouldAdd) {
            lore.add(valueColor + value);
        }
        return this;
    }

    public LoreBuilder addItem(String label, String value) {
        addItem(label, value, true);
        return this;
    }

    public LoreBuilder addItem(String label, String value, boolean shouldAdd) {
        if (shouldAdd) {
            lore.add(labelColor + label + ": " + valueColor + value);
        }
        return this;
    }

    public LoreBuilder addIndented(String label, String value) {
        addIndented(label, value, true);
        return this;
    }

    public LoreBuilder addIndented(String label, Supplier<String> value, boolean shouldAdd) {
        if(shouldAdd) {
            addIndented(label, value.get(), true);
        }
        return this;
    }

    public LoreBuilder addIndented(String label, String value, boolean shouldAdd) {
        if (shouldAdd) {
            addMultiLineIntended(label, formatLines(value, 30));
        }
        return this;
    }


    public LoreBuilder addDuration(String label, String humanReadableDuration, boolean shouldAdd) {
        if (shouldAdd) {
            addMultiLineIntended(label, Arrays.asList(humanReadableDuration.split(", ")));
        }
        return this;
    }

    private void addMultiLineIntended(String label, List<String> lines) {
        lore.add(labelColor + label + ":");
        for (String line : lines) {
            lore.add("  " + valueColor + line);
        }
    }

    public List<String> build() {
        return lore;
    }
}