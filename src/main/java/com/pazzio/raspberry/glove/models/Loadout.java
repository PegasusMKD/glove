package com.pazzio.raspberry.glove.models;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Loadout {
    @Id
    @GeneratedValue(generator = "strategy-uuid2")
    @GenericGenerator(name="strategy-uuid2", strategy = "uuid2")
    private String id;

    private Boolean active;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderColumn(name = "RGB_ORDER")
    private List<RGBValue> rgbValues;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    private List<Double> pauseValues;
}
