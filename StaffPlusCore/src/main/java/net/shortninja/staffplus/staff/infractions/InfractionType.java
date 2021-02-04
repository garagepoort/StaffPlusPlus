package net.shortninja.staffplus.staff.infractions;

public enum InfractionType {
    MUTE("Mutes"),
    BAN("Bans"),
    REPORTED("Reported"),
    WARNING("Warns"),
    KICK("Kicks");

    private final String guiTitle;

    InfractionType(String guiTitle) {
        this.guiTitle = guiTitle;
    }

    public String getGuiTitle() {
        return guiTitle;
    }
}
