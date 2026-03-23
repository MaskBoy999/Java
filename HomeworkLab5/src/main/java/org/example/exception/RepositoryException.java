package org.example.exception;

public class RepositoryException extends Exception {
    public RepositoryException(String message) {
        super(message);
    }
    public RepositoryException(Throwable cause) {
        super(cause);
    }
}