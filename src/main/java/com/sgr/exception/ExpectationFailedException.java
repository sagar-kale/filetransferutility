package com.sgr.exception;


public class ExpectationFailedException extends RuntimeException {

    public ExpectationFailedException(String message) {
        super("Expectation failed. " + message);
    }

}
