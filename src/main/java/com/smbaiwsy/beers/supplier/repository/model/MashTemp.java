package com.smbaiwsy.beers.supplier.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@Data
public class MashTemp {
    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @NonNull
    private Long temperature;

    @Enumerated(EnumType.STRING)
    private TemperatureUnit unit = TemperatureUnit.CELSIUS;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long duration;
}
