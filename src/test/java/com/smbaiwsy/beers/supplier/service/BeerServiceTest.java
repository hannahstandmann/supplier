package com.smbaiwsy.beers.supplier.service;

import java.util.*;

import com.smbaiwsy.beers.supplier.repository.BeerRepository;
import com.smbaiwsy.beers.supplier.repository.model.Beer;
import com.smbaiwsy.beers.supplier.repository.model.MashTemp;
import com.smbaiwsy.beers.supplier.rest.BeerDto;
import com.smbaiwsy.beers.supplier.exception.BeerNotFoundException;
import jdk.jfr.StackTrace;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackages = {"org.smbiwsy.repository"})
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class BeerServiceTest {

    @MockBean
    private BeerRepository beerRepository;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    BeerService beerService;

    @Test
    public void getBeers() {

        Beer beer1 = new Beer("Pale Ale", "Irish Pale Ale");
        Beer beer2 = new Beer("Kozel", "Czech dark beer");
        Beer beer3 = new Beer("Feldschlösschen", "Swiss traditional lager");

        List<Beer> beers = List.of(beer1, beer2, beer3);
        Mockito.when(beerRepository.findAll()).thenReturn(beers);
        List<BeerDto> response = beerService.getBeers();
        Assert.assertEquals("Number of beers as expected", 3, response.size());
        Assert.assertEquals("First beer", "Pale Ale", response.get(0).getName());
        Assert.assertEquals("Second beer", "Kozel", response.get(1).getName());
        Assert.assertEquals("Third beer", "Feldschlösschen", response.get(2).getName());
    }

    @Test
    public void getSpecificBeerSuccess() throws BeerNotFoundException {

        MashTemp temp1 = new MashTemp(20L);
        MashTemp temp2 = new MashTemp(30L);
        Beer beer1 = new Beer("Pale Ale", "Irish Pale Ale");

        beer1.setMashTemp(List.of(temp1, temp2));

        Mockito.when(beerRepository.findById(1L)).thenReturn(Optional.of(beer1));
        BeerDto response = null;
        try {
            response = beerService.getSpecificBeer(1L);
        } catch (BeerNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("Found Pale Ale", beer1.getId(), response.getId());
        Assert.assertEquals("Found Pale Ale", "Pale Ale", response.getName());
        Assert.assertEquals("Found Pale Ale", OptionalDouble.of(25.0), response.getMeanTemp());
    }

    @Test
    public void getSpecificBeerFailed() {
        try {
            beerService.getSpecificBeer(1L);
            Assert.fail("Expected exception to be thrown");
        } catch (BeerNotFoundException e) {
            e.printStackTrace();
        }
    }


}
