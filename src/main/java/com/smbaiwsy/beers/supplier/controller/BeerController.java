package com.smbaiwsy.beers.supplier.controller;

import com.smbaiwsy.beers.supplier.repository.model.Beer;
import com.smbaiwsy.beers.supplier.rest.BeerDto;
import com.smbaiwsy.beers.supplier.rest.StatusDto;
import com.smbaiwsy.beers.supplier.service.BeerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.http.MediaType;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/beers")
public class BeerController {

    @NonNull
    private final BeerService beerService;

    @Value("${storage-capacity:5L}")
    private Long storageCapacity;

    @GetMapping
    public Collection<BeerDto> findBeers() {
        return beerService.getBeers();
    }

    @GetMapping("/{id}")
    public BeerDto findById(@PathVariable @NotNull long id) {
            return beerService.getSpecificBeer(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable @NotNull long id) {
        beerService.removeSpecificBeer(id);
    }

    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces =  MediaType.APPLICATION_JSON_VALUE)
    public StatusDto storeUpToNRandomDifferentBeer(@RequestBody final Beer[] beers) {
        return beerService.storeUpToNRandomDifferentBeers(Arrays.asList(beers), storageCapacity);
    }
}