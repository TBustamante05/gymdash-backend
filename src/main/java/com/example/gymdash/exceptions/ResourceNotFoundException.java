package com.example.gymdash.exceptions;

// Extiende RuntimeException -> significa que es una excepción "no chequeada"
public class ResourceNotFoundException extends RuntimeException {
    // El constructor recibe el mensaje que se quiere mostrar
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
