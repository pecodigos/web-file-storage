package com.pecodigos.web_file_storage.exceptions;

import java.util.NoSuchElementException;

public class InvalidUserIdException extends NoSuchElementException {
    public InvalidUserIdException() {
        super("Invalid user ID");
    }
}
