package com.smbaiwsy.beers.supplier.exception;

import lombok.Getter;

/**
 * Used to handle the not found exception.
 *
 * @author anamattuzzi-stojanovic
 *
 */
public class BeerNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6507934884227938446L;

    @Getter
    private String errorCode;

    /**
     * Instantiates a new not found exception.
     *
     * @param message the message
     */
    public BeerNotFoundException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}