package net.shortninja.staffplus.util.lib.json;

import net.shortninja.staffplus.util.lib.Message;
import org.json.simple.JSONObject;

/**
 * @author JustisR, Shortninja
 */

public class JsonStringBuilder {
    private final JsonMessage message;
    private final String string;
    private String hover = "", click = "";

    /**
     * Settings for the json message's text
     *
     * @param msg  the original message
     * @param text the text to be appended to the message.
     */
    public JsonStringBuilder(JsonMessage msg, String text) {
        message = msg;
        string = ",{\"text\":\"" + Message.colorize(text) + "\"";
    }

    private String esc(String s) {
        return JSONObject.escape(Message.colorize(s));
    }

    /**
     * Set the hover event's action as showing a tooltip with the given text
     *
     * @param lore the text to be displayed in the tooltip
     * @return the json string builder to which you are applying settings
     */
    public JsonStringBuilder setHoverAsTooltip(String... lore) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < lore.length; i++) {
            if (i + 1 == lore.length) {
                builder.append(Message.colorize(lore[i]));
            } else builder.append(Message.colorize(lore[i]) + "\n");
        }

        hover = ",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + esc(builder.toString()) + "\"}";

        return this;
    }

    /**
     * Set the hover event's action as showing an achievement
     *
     * @param ach the achievement to be displayed. for list of achievements,
     *            visit <a href="http://minecraft.gamepedia.com/Achievements"
     *            >here</a>
     * @return the json string builder to which you are applying settings
     */
    public JsonStringBuilder setHoverAsAchievement(String ach) {
        hover = ",\"hoverEvent\":{\"action\":\"show_achievement\",\"value\":\"achievement." + esc(ach) + "\"}";
        return this;
    }

    /**
     * Set the click event's action as redirecting to a URL
     *
     * @param link to redirect to
     * @return the json string builder to which you are applying settings.
     */
    public JsonStringBuilder setClickAsURL(String link) {
        click = ",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + esc(link) + "\"}";
        return this;
    }

    /**
     * Set the click event's action as suggesting a command
     *
     * @param cmd to suggest
     * @return the json string builder to which you are applying settings;
     */
    public JsonStringBuilder setClickAsSuggestCmd(String cmd) {
        click = ",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + esc(cmd) + "\"}";
        return this;
    }

    /**
     * Set the click event's action as executing a command
     *
     * @param cmd
     * @return
     */
    public JsonStringBuilder setClickAsExecuteCmd(String cmd) {
        click = ",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + esc(cmd) + "\"}";
        return this;
    }

    /**
     * Finalize the appending of the text, with settings.
     *
     * @return
     */
    public JsonMessage save() {
        message.msg += Message.colorize(string + hover + click + "}");
        return message;
    }
}