package com.riffo.users.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Un partenaire existe déjà avec l'email: " + email);
    }
}