package com.smbaiwsy.beers.supplier.repository;
import com.smbaiwsy.beers.supplier.repository.model.Beer;
import com.smbaiwsy.beers.supplier.repository.model.MashTemp;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackages = {"org.smbiwsy.repository" })
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class BeerRepositoryTest {


        @Autowired
        private BeerRepository lager;
        
        private Beer beer;
        
        @Before
        public void init(){
            beer = new Beer("Guinness® Hop House 13 Lager", "Light and hoppy with floral and citrus notes");
            MashTemp temp1 = new MashTemp(20L);
            MashTemp temp2 = new MashTemp(30L);
            beer.setMashTemp(List.of(temp1, temp2));
            lager.save(beer);
        }


        @Test
        public void testFindBeerById() {
          
            Beer findById = lager.findById(1L).get();

            assertThat(findById).extracting(Beer::getDescription).isEqualTo(beer.getDescription());
            assertThat(findById).extracting(Beer::getName).isEqualTo(beer.getName());
        }
        
       /* @Test
        public void testDeleteBeerById() {
          
            lager.deleteById(1L);
            Optional<Beer> findById = lager.findById(1L);

            assertThat(findById).isEmpty();
        }*/


}
