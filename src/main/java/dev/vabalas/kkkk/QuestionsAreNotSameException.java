package dev.vabalas.kkkk;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class QuestionsAreNotSameException extends RuntimeException {
    public QuestionsAreNotSameException(String message) {
        super(message);
    }
}
