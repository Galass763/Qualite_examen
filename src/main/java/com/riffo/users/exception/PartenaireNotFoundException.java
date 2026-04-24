package com.riffo.users.exception;

public class PartenaireNotFoundException extends RuntimeException {
    public PartenaireNotFoundException(String message) {
        super(message);
    }
    
    public PartenaireNotFoundException(Long id) {
        super("Partenaire non trouvé avec l'ID: " + id);
    }
}