package net.shortninja.staffplus.core.authentication;

import net.shortninja.staffplus.core.common.exceptions.BusinessException;

public class AuthenticationException extends BusinessException {

    public AuthenticationException() {
        super("You are not authorized to use this command. Please log in");
    }
}
