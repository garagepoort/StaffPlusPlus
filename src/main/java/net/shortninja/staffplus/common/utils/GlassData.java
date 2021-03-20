package net.shortninja.staffplus.common.utils;

public enum GlassData {
    WHITE((short) 0, "WHITE"),
    ORANGE((short) 1, "ORANGE"),
    MAGENTA((short) 2, "MAGENTA"),
    LIGHTBLUE((short) 3, "LIGHT_BLUE"),
    YELLOW((short) 4, "YELLOW"),
    LIME((short) 5, "LIME"),
    PINK((short) 6, "PINK"),
    GRAY((short) 7, "GRAY"),
    LIGHTGRAY((short) 8, "LIGHT_GRAY"),
    CYAN((short) 9, "CYAN"),
    PURPLE((short) 10, "PURPLE"),
    BLUE((short) 11, "BLUE"),
    BROWN((short) 12, "BROWN"),
    GREEN((short) 13, "GREEN"),
    RED((short) 14, "RED"),
    BLACK((short) 15, "Black");

    private final short data;
    private final String color;

    GlassData(short data, String color) {
        this.data = data;
        this.color = color;
    }

    public static String getName(short data) {
        for (GlassData glass : GlassData.values()) {
            if (glass.data == data)
                return glass.color + "_STAINED_GLASS_PANE";
        }
        return "WHITE_STAINED_GLASS_PANE";
    }
}
