package com.acme.git.contributors.application.exception;

public class IncorrectValuesException extends Throwable {
    public IncorrectValuesException(String reasonPhrase) {
        super(reasonPhrase);
    }
}
