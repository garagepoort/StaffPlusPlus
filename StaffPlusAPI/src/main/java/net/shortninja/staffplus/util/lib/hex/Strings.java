package net.shortninja.staffplus.util.lib.hex;

import net.shortninja.staffplus.util.lib.Message;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author TigerHix, Shortninja
 */

public final class Strings {

    private Strings() {
    }

    public static String format(String string) {
        return Message.colorize(string);
    }

    public static String[] format(String[] strings) {
        return format(Arrays.asList(strings)).toArray(
                new String[strings.length]);
    }

    public static List<String> format(List<String> strings) {
        List<String> collection = new ArrayList<String>();
        for (String string : strings) {
            collection.add(format(string));
        }
        return collection;
    }

    public static String link(List<String> strings) {
        String newString = "";
        for (String string : strings) {
            newString += string + ", ";
        }
        newString = newString.substring(0, newString.length() - 2);
        return newString;
    }

    public static String prefix(String string, String prefix) {
        StringBuilder builder = new StringBuilder();

        for (String word : string.split(" ")) {
            builder.append(prefix + word + " ");
        }

        return builder.toString().trim();
    }

    public static String rainbowlize(String string) {
        int lastColor = 0;
        int currColor;
        String newString = "";
        String colors = "123456789abcde";
        for (int i = 0; i < string.length(); i++) {
            do {
                currColor = new Random().nextInt(colors.length() - 1) + 1;
            } while (currColor == lastColor);
            newString += ChatColor.RESET.toString()
                    + ChatColor.getByChar(colors.charAt(currColor)) + ""
                    + string.charAt(i);
        }
        return newString;
    }

    public static String repeat(char c, int count) {
        if (count == 0) return "";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(c);
        }
        return builder.toString();
    }

    public static String stripFormat(String string) {
        return ChatColor.stripColor(string);
    }

    public static String toMMSS(int seconds) {
        int rem = seconds % 3600;
        int mn = rem / 60;
        int sec = rem % 60;
        return (mn < 10 ? "0" : "") + mn + ":" + (sec < 10 ? "0" : "") + sec;
    }

    public static boolean isVowel(char c) {
        return "AEIOUaeiou".indexOf(c) != -1;
    }
}