package com.pazzio.raspberry.glove.models;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RGBValue {
    public Integer finger;
    public Double activeTime;
    @Id
    @GenericGenerator(name = "strategy-uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "strategy-uuid2")
    private String id;
    private Integer red;
    private Integer green;
    private Integer blue;
    private Double pauseTime;
}
