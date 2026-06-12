package com.example.gymdash.exceptions;

// Para errores de validación, como username ya en uso
public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }
}
