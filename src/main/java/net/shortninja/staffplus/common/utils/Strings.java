package net.shortninja.staffplus.common.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author TigerHix, Shortninja
 */

public final class Strings {

    private Strings() {
    }

    public static String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String[] format(String[] strings) {
        return format(Arrays.asList(strings)).toArray(
            new String[strings.length]);
    }

    public static List<String> format(List<String> strings) {
        List<String> collection = new ArrayList<>();
        for (String string : strings) {
            collection.add(format(string));
        }
        return collection;
    }

    public static String prefix(String string, String prefix) {
        StringBuilder builder = new StringBuilder();

        for (String word : string.split(" ")) {
            builder.append(prefix + word + " ");
        }

        return builder.toString().trim();
    }

    public static String repeat(char c, int count) {
        if (count == 0) return "";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(c);
        }
        return builder.toString();
    }
}