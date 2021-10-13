package com.dk.security.exception;

public class ProfileIdFromBodyDifferenThanHeaderException extends RuntimeException{
    public ProfileIdFromBodyDifferenThanHeaderException() {
        super("ProfileID in Body is different than is header!");
    }
}
