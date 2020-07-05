package com.smbaiwsy.beers.supplier.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class Beer {
    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "beer_id")
    @JsonIgnore
    private Long id;

    @Column(name = "name", unique= true)
    @NonNull
    private String name;

    @Column(name = "description", unique= true)
    @NonNull
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "beer_id")
    @EqualsAndHashCode.Exclude
    private List<MashTemp> mashTemp;

}
