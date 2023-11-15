package com.plexus.test2023.infrastructure.exception;

public class TariffNotFoundException extends Exception{
    public TariffNotFoundException(String message) {
        super(message);
    }
}
