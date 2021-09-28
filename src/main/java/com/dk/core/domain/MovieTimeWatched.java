package com.dk.core.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(	name = "MOVIES_TIME_WATCHED")
public class MovieTimeWatched {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID")
    private Long Id;


    @Column(name="PATH", unique = true)
    private String pathFile;

    @Column(name="TIME_WATCHED")
    private Long timeWatched;


    @Override
    public String toString() {
        return "MovieTimeWatched{" +
                "pathFile='" + pathFile + '\'' +
                ", timeWatched=" + timeWatched +
                '}';
    }
}
