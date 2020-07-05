package com.smbaiwsy.beers.supplier.rest;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.OptionalDouble;

@Builder
@Data
public class BeerDto implements Serializable {

    private Long id;

    private String name;

    private String description;

    private OptionalDouble meanTemp;
}
