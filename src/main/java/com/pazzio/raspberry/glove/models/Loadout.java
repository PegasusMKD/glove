package com.pazzio.raspberry.glove.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @Id
    @GeneratedValue(generator = "strategy-uuid2")
    @GenericGenerator(name="strategy-uuid2", strategy = "uuid2")
    private String id;

    private Boolean active;

    @OneToMany(fetch = FetchType.EAGER)
    private List<RGBValue> rgbValues;

    @ElementCollection
    private List<Double> pauseValues;
}
