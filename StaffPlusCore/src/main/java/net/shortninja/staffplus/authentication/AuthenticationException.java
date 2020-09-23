package net.shortninja.staffplus.authentication;

import net.shortninja.staffplus.common.BusinessException;

public class AuthenticationException extends BusinessException {

    public AuthenticationException() {
        super("You are not authorized to use this command. Please log in");
    }
}
