package com.pazzio.raspberry.glove.models;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(generator = "strategy-uuid2")
    @GenericGenerator(name="strategy-uuid2", strategy = "uuid2")
    private String id;

    private String username;
    private String uid;
    private Boolean edited;
    private String serialNumber;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Loadout> loadoutList;


}
