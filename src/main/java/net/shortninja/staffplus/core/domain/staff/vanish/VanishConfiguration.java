package net.shortninja.staffplus.core.domain.staff.vanish;

public class VanishConfiguration {

    private final  boolean vanishEnabled;
    private final  boolean vanishTabList;
    private final  boolean vanishShowAway;
    private final  boolean vanishChatEnabled;
    private final  boolean vanishMessageEnabled;

    private final String permissionVanishCommand;
    private final String permissionSeeVanished;
    private final String permissionVanishTotal;
    private final String permissionVanishList;
    private final String permissionVanishPlayer;

    private final String commandVanish;

    public VanishConfiguration(boolean vanishEnabled, boolean vanishTabList, boolean vanishShowAway, boolean vanishChatEnabled, boolean vanishMessageEnabled, String permissionVanishCommand, String permissionSeeVanished, String permissionVanishTotal, String permissionVanishList, String permissionVanishPlayer, String commandVanish) {
        this.vanishEnabled = vanishEnabled;
        this.vanishTabList = vanishTabList;
        this.vanishShowAway = vanishShowAway;
        this.vanishChatEnabled = vanishChatEnabled;
        this.vanishMessageEnabled = vanishMessageEnabled;
        this.permissionVanishCommand = permissionVanishCommand;
        this.permissionSeeVanished = permissionSeeVanished;
        this.permissionVanishTotal = permissionVanishTotal;
        this.permissionVanishList = permissionVanishList;
        this.permissionVanishPlayer = permissionVanishPlayer;
        this.commandVanish = commandVanish;
    }

    public boolean isVanishEnabled() {
        return vanishEnabled;
    }

    public boolean isVanishTabList() {
        return vanishTabList;
    }

    public boolean isVanishShowAway() {
        return vanishShowAway;
    }

    public boolean isVanishChatEnabled() {
        return vanishChatEnabled;
    }

    public boolean isVanishMessageEnabled() {
        return vanishMessageEnabled;
    }

    public String getPermissionVanishCommand() {
        return permissionVanishCommand;
    }

    public String getPermissionSeeVanished() {
        return permissionSeeVanished;
    }

    public String getPermissionVanishTotal() {
        return permissionVanishTotal;
    }

    public String getPermissionVanishList() {
        return permissionVanishList;
    }

    public String getPermissionVanishPlayer() {
        return permissionVanishPlayer;
    }

    public String getCommandVanish() {
        return commandVanish;
    }
}
