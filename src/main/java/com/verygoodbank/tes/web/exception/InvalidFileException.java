package com.verygoodbank.tes.web.exception;

import java.io.Serial;

public class InvalidFileException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1940040416483104475L;

    public InvalidFileException(String message, Throwable cause) {
        super(message, cause);
        initCause(cause);
    }
}
