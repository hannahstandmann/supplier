package com.smbaiwsy.beers.supplier.controller;


import com.smbaiwsy.beers.supplier.rest.ErrorDto;
import com.smbaiwsy.beers.supplier.exception.BadRequestException;
import com.smbaiwsy.beers.supplier.exception.BeerNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller for handling Rest exceptions.
 *
 * @author anamattuzzi-stojanovic
 */
@ControllerAdvice
@Slf4j
public class BeerExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle not found exception.
     *
     * @param e
     *            the exception
     * @return the error response specifying the error type and the message
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDto handleNotFoundException(BeerNotFoundException e) {
        log.info("Not found exception. Error code: {} message: {}", e.getErrorCode(), e.getMessage());
        return ErrorDto.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .build();
    }

    /**
     * Handle not found exception.
     *
     * @param e
     *            the exception
     * @return the error response specifying the error type and the message
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDto handleBadRequestException(BadRequestException e) {
        log.info("Not found exception. Error code: {} message: {}", e.getErrorCode(), e.getMessage());
        return ErrorDto.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .build();
    }


    /**
     * Handles any uncaught exception as INTERNAL_SERVER_ERROR.
     *
     * @param e
     *            the exception
     * @param request
     *            the request
     * @return the error response specifying the error type and the message
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDto handleApiException(Exception e, WebRequest request) {
        log.error("Runtime exception. Message: {}", e.getMessage());
        log.error("RuntimeException. StackTrace: ", e);
        return ErrorDto.builder()
                .code("SERVER_ERROR")
                .message(e.getMessage())
                .build();
    }

}