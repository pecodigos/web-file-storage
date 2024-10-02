package com.pecodigos.web_file_manager.exceptions;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String message) {
        super(message);
    }
}
