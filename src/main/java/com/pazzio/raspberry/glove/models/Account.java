package com.pazzio.raspberry.glove.models;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    @GenericGenerator(name = "strategy-uuid2", strategy = "uuid2")
    private String id;

    private String username;
    private String password;
    private String token;
    private Boolean edited;
    private String serialNumber;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Loadout> loadoutList;

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }


}
