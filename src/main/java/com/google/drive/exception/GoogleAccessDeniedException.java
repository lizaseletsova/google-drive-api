package com.google.drive.exception;

public class GoogleAccessDeniedException extends RuntimeException {
    public GoogleAccessDeniedException(String message) {
        super(message);
    }
}