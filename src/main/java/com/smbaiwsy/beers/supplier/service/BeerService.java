package com.smbaiwsy.beers.supplier.service;

import com.smbaiwsy.beers.supplier.exception.BeerNotFoundException;
import com.smbaiwsy.beers.supplier.repository.model.Beer;
import com.smbaiwsy.beers.supplier.rest.BeerDto;
import com.smbaiwsy.beers.supplier.rest.StatusDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface BeerService {

    List<BeerDto> getBeers();

    BeerDto getSpecificBeer(Long id) throws BeerNotFoundException;

    void removeSpecificBeer(Long id);

    StatusDto storeUpToNRandomDifferentBeers(final List<Beer> beers, Long upperLimit);

    static BeerDto fromBeer(Beer beer) {
        return BeerDto
                .builder()
                .id(beer.getId())
                .name(beer.getName())
                .description(beer.getDescription())
                .meanTemp(collectionToStream(beer.getMashTemp())
                        .mapToLong(mash -> mash.getTemperature())
                        .average())
                .build();
    }


    static public <T> Stream<T> collectionToStream(Collection<T> collection) {
        return Optional.ofNullable(collection)
                .map(Collection::stream)
                .orElseGet(Stream::empty);
    }
}
