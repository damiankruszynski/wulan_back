package com.dk.core.exception;

public class UserWithThisNameExist extends RuntimeException{
    public  UserWithThisNameExist(){
        super("User with that name is already exist.");
    }
}
