package com.ryel.springBootJpa.exception;

/**
 * Author: koabs
 * 9/19/16.
 */
public class UserException extends RuntimeException {

    public UserException(String message) {
        super(message);
    }
}
