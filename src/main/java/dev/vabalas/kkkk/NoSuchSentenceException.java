package dev.vabalas.kkkk;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchSentenceException extends RuntimeException {
    public NoSuchSentenceException(String message) {
        super(message);
    }
}
