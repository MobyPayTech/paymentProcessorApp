package com.mobpay.Payment.Helper;

import com.mobpay.Payment.dao.ResultDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@RestControllerAdvice
public class PaymentExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        ResultDto resultDto = ResultDto.builder()
                .responseCode("02")
                .responseMessage("Bad Request")
                .responseDescription("Required parameter "+ex.getParameterName()+" missing")
                .build();

        return ResponseEntity.badRequest().body(resultDto);
    }

    @ExceptionHandler({ ValidationException.class })
    public ResponseEntity<Object> handleValidationException(
            Exception ex, WebRequest request) {
        ResultDto resultDto = ResultDto.builder()
                .responseCode("02")
                .responseMessage("Bad Request")
                .responseDescription(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().body(resultDto);
    }



    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        ResultDto resultDto = ResultDto.builder()
                .responseCode("02")
                .responseMessage("Bad Request")
                .responseDescription("Mandatory field missing")
                .build();

        return ResponseEntity.badRequest().body(resultDto);
    }

}
