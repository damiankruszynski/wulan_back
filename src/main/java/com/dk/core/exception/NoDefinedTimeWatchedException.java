package com.dk.core.exception;

public class NoDefinedTimeWatchedException extends RuntimeException{
    public NoDefinedTimeWatchedException() {
        super("timeWatched is required !");
    }
}
