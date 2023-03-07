package cherhy.soloProject.application.controller;

import cherhy.soloProject.application.exception.SnsException;
import cherhy.soloProject.application.exception.dto.ExceptionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(SnsException.class)
    public ResponseEntity<ExceptionDto> blogException(SnsException e) {
        int statusCode = e.getStatusCode();

        ExceptionDto response = ExceptionDto.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity.status(statusCode).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> blogException(MethodArgumentNotValidException e) {
        Map<String,String> error = new HashMap<>();
        e.getAllErrors().forEach(c -> error.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return ResponseEntity.badRequest().body(error);
    }

}
