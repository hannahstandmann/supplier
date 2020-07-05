package com.smbaiwsy.beers.supplier.rest;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class StatusDto implements Serializable {
    private Integer received;
    private Integer stored;
}
