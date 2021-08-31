package net.shortninja.staffplus.core.common.gui;

public class GuiItemConfig implements IGuiItemConfig{
    private final boolean enabled;
    private final String title;

    public GuiItemConfig(boolean enabled, String title) {
        this.enabled = enabled;
        this.title = title;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getTitle() {
        return title;
    }

}
