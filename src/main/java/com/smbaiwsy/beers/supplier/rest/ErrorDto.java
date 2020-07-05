package com.smbaiwsy.beers.supplier.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Error response object returned in case of controller exceptions
 * Class contains error code and message(description).
 *
 * @author anamattuzzi-stojanovic
 */
@Builder
@Data
public class ErrorDto {

    private String code;

    private String message;
}