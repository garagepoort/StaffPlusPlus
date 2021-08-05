package net.shortninja.staffplus.core.domain.staff.joinmessages;

public class JoinMessageGroup {

    private final String permission;
    private final String message;
    private final int weight;

    public JoinMessageGroup(String permission, String message, int weight) {
        this.permission = permission;
        this.message = message;
        this.weight = weight;
    }

    public String getPermission() {
        return permission;
    }

    public String getMessage() {
        return message;
    }

    public int getWeight() {
        return weight;
    }
}
