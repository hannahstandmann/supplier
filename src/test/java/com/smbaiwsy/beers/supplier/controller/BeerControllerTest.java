package com.smbaiwsy.beers.supplier.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smbaiwsy.beers.supplier.exception.BeerNotFoundException;
import com.smbaiwsy.beers.supplier.repository.model.Beer;
import com.smbaiwsy.beers.supplier.repository.model.MashTemp;
import com.smbaiwsy.beers.supplier.rest.BeerDto;
import com.smbaiwsy.beers.supplier.rest.StatusDto;
import com.smbaiwsy.beers.supplier.service.BeerService;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(BeerControllerTest.Config.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@WebMvcTest
public class BeerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BeerService beerService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldReturnAllBeers() throws Exception {
        List<BeerDto> lager = List.of(
                BeerDto.builder()
                        .id(1L)
                        .name("Scotch ale")
                        .description("Peat-smoked malt flavoured beer")
                        .meanTemp(OptionalDouble.of(34))
                        .build(),
                BeerDto.builder()
                        .id(1L)
                        .name("Feldschlösschen")
                        .description("Swiss traditional lager")
                        .meanTemp(OptionalDouble.of(23))
                        .build()
        );
        when(beerService.getBeers())
                .thenReturn(lager);

        mvc.perform(MockMvcRequestBuilders
                .get("/v1/beers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").isNotEmpty())
                .andExpect(jsonPath("$.[*].name").isArray())
                .andExpect(jsonPath("$.[*].name", hasSize(2)))
                .andExpect(jsonPath("$.[0].name").value("Scotch ale"))
                .andExpect(jsonPath("$.[1].name").value("Feldschlösschen"));
    }

    @Test
    public void shouldReturnOneBeer() throws Exception {
        when(beerService.getSpecificBeer(1L))
                .thenReturn(BeerDto.builder()
                        .id(1L)
                        .name("Scotch ale")
                        .description("Peat-smoked malt flavoured beer")
                        .meanTemp(OptionalDouble.of(34))
                        .build());

        mvc.perform(MockMvcRequestBuilders
                .get("/v1/beers/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Scotch ale"))
                .andExpect(jsonPath("$.description").value("Peat-smoked malt flavoured beer"))
                .andExpect(jsonPath("$.meanTemp").value("34.0"));
    }

    @Test
    public void shouldReturnNoBeerFound() throws Exception {
        when(beerService.getSpecificBeer(16L))
                .thenThrow(new BeerNotFoundException("NOT_FOUND","People were thirsty last night"));

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/v1/beers/16"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("People were thirsty last night"))
                .andReturn();
        Optional<BeerNotFoundException> nfException = Optional.ofNullable((BeerNotFoundException) result.getResolvedException());
        Assert.assertEquals("NOT_FOUND", nfException.get().getErrorCode());
        Assert.assertEquals("People were thirsty last night", nfException.get().getMessage());
    }

    @Test
    public void shouldDeleteOneBeer() throws Exception {
        doNothing().when(beerService).removeSpecificBeer(1L);

        mvc.perform(MockMvcRequestBuilders
                .delete("/v1/beers/1"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void shouldStore3of5Beers() throws Exception {
        Beer beer1 = new Beer("Pale Ale", "Irish Pale Ale");
        beer1.setMashTemp(List.of(new MashTemp(20L), new MashTemp(30L)));
        Beer beer2 = new Beer("Kozel", "Czech dark beer");
        Beer beer3 = new Beer("Feldschlösschen", "Swiss traditional lager");
        Beer beer4 = new Beer("Kozel", "Czech dark beer");
        Beer beer5 = new Beer("Feldschlösschen", "Swiss traditional lager");
        when(beerService.storeUpToNRandomDifferentBeers(
                List.of(beer1,beer2,beer3,beer4,beer5),
                5L))
                .thenReturn(StatusDto.builder()
                        .received(5)
                        .stored(3)
                        .build());

  
                mvc.perform(MockMvcRequestBuilders
                .post("/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString((List.of(beer1,beer2,beer3,beer4,beer5))) )
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.received").value(5))
                .andExpect(jsonPath("$.stored").value(3));

    }

    @TestConfiguration
    protected static class Config{
        public Long storageCapacity = 5L;

        @Bean
        public BeerService beerService() {
            return Mockito.mock(BeerService.class);
        }
    }
}
