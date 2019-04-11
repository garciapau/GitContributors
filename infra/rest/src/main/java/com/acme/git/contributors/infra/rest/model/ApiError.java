package com.acme.git.contributors.infra.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    private String message;

    private ApiError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static class ApiErrorBuilder {
        private String message;

        public ApiErrorBuilder() {
        }

        public ApiErrorBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ApiError build() {
            return new ApiError(message);
        }
    }
}
