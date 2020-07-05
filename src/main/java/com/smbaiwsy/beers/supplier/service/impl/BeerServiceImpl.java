package com.smbaiwsy.beers.supplier.service.impl;

import com.smbaiwsy.beers.supplier.exception.BeerNotFoundException;
import com.smbaiwsy.beers.supplier.repository.BeerRepository;
import com.smbaiwsy.beers.supplier.repository.model.Beer;
import com.smbaiwsy.beers.supplier.rest.BeerDto;
import com.smbaiwsy.beers.supplier.rest.StatusDto;
import com.smbaiwsy.beers.supplier.service.BeerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {
    private BeerRepository repository;


    public List<Beer> findBeers() {
        return repository.findAll();
    }

    @Override
    public List<BeerDto> getBeers() {
        return findBeers()
                .stream()
                .map(BeerService::fromBeer).
                collect(Collectors.toList());
    }


    @Override
    public BeerDto getSpecificBeer(Long id) throws BeerNotFoundException {
       // System.out.println("I should be executed 1");
        return repository.findById(id)
                .map(BeerService::fromBeer)
               /* .map(beer -> {
                    System.out.println("I should be executed 2");
                    System.out.println(beer);
                    return beer;
                })*/
                .orElseThrow(() -> new BeerNotFoundException("NOT_FOUND","Beer not found"));
    }

    @Override
    public void removeSpecificBeer(Long id) {
        repository.deleteById(id);
    }

    @Override
    public StatusDto storeUpToNRandomDifferentBeers(List<Beer> beers, Long upperLimit) {
        Set<Beer> stored = new HashSet<Beer>(findBeers());
        Set<Beer> toStore = new HashSet<Beer>(beers);

        toStore.removeAll(stored);

        toStore.stream().limit(upperLimit).iterator().forEachRemaining(beer -> repository.save(beer));
        return StatusDto.builder()
                .received(beers.size())
                .stored(toStore.size())
                .build();
    }

}
