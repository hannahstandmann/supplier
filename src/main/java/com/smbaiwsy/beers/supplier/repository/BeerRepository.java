package com.smbaiwsy.beers.supplier.repository;

import com.smbaiwsy.beers.supplier.repository.model.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeerRepository extends JpaRepository<Beer, Long> {
}
