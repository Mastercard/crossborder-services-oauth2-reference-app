package com.mastercard.oauth2.requesttoken.exception;

public class TokenGenerationException extends RuntimeException {

    public TokenGenerationException(String message){
        super(message);
    }
}
