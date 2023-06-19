package br.com.trier.spring.services.exceptions;

public class ObjectNotFound extends RuntimeException {

    public ObjectNotFound (String message) {
        super(message);
    }
}
