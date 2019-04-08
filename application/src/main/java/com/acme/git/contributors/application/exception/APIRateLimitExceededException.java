package com.acme.git.contributors.application.exception;

public class APIRateLimitExceededException extends Throwable {
    public APIRateLimitExceededException(String reasonPhrase) {
        super(reasonPhrase);
    }
}
