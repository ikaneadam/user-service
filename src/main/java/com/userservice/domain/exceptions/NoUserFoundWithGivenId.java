package com.userservice.domain.exceptions;

public class NoUserFoundWithGivenId extends Exception {
    public NoUserFoundWithGivenId() {
        super("No user found with given id");
    }
}
