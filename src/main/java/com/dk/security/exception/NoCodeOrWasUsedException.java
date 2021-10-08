package com.dk.security.exception;

public class NoCodeOrWasUsedException extends RuntimeException{
    public NoCodeOrWasUsedException() {
        super("Bad code or code used up!");
    }
}
