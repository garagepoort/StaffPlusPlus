package net.shortninja.staffplus.common.exceptions;

public class PlayerNotFoundException extends BusinessException {
    public PlayerNotFoundException(String name) {
        super("No player found with name ["+name+"]");
    }
}
