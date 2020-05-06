package com.pazzio.raspberry.glove.models;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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

    @OneToMany
    private List<RGBValue> rgbValues;

    @ElementCollection
    private List<Double> pauseValues;
}
