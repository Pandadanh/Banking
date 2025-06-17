package com.panda.mdmService.exception;

public class UserAlreadyExistsException extends RuntimeException {
    
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UserAlreadyExistsException(String userId) {
        super("User already exists with ID: " + userId);
    }
}
