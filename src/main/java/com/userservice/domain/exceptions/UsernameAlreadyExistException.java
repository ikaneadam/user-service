package com.userservice.domain.exceptions;

public class UsernameAlreadyExistException extends Exception {
    public UsernameAlreadyExistException() {
        super("Username already exist");
    }
}

