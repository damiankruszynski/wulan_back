package com.dk.security.exception;

public class ProfileIdIsNotBelowsToThisUserIdException extends RuntimeException{
    public ProfileIdIsNotBelowsToThisUserIdException(){
        super("That profileId not exists or don't below to this userId");
    }
}

