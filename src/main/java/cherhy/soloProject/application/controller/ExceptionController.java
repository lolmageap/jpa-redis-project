package cherhy.soloProject.application.controller;

import cherhy.soloProject.application.exception.SnsException;
import cherhy.soloProject.application.exception.dto.ExceptionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {

    @ResponseBody
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

}
