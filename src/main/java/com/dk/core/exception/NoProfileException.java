package com.dk.core.exception;

public class NoProfileException extends RuntimeException{
    public NoProfileException(){
        super("That profileId not exists.");
    }
}
