package org.example.accesscontrolapi.exceptionhandler.customexceptions;

public class AttributesMismatchException extends RuntimeException {
    public AttributesMismatchException(String message) {
        super(message);
    }
}
