package com.mastercard.oauth2.requesttoken.exception;

public class KeyRetrievalException extends RuntimeException {

    public KeyRetrievalException(String message) {
        super(message);
    }

    public KeyRetrievalException(String message, Throwable th){
        super(message, th);
    }

    public KeyRetrievalException(Throwable message) {
        super(message);
    }
}
