package com.pecodigos.web_file_storage.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class InvalidUsernameOrPasswordException extends BadCredentialsException {
    public InvalidUsernameOrPasswordException() {
        super("Invalid username and/or password.");
    }
}
