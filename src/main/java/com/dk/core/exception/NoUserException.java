package com.dk.core.exception;

public class NoUserException extends RuntimeException{
    public NoUserException(){
        super("That user not exists.");
    }
}
