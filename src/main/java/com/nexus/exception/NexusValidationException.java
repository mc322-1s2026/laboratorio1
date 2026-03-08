package com.nexus.exception;

public class NexusValidationException extends RuntimeException {
    public NexusValidationException(String message) {
        super(message);
        // Incrementar contador global de erros aqui
        // Ou melhor deixar para a Task gerenciar?
    }
}