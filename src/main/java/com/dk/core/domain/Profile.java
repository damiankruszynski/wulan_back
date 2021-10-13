package com.dk.core.domain;


import com.dk.security.login.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="PROFILE")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SequenceGenerator(name="profile_seq", initialValue=98752)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_seq")
    private Long id;

    @Column(name="PROFILE_NAME")
    private String profileName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(targetEntity = MovieTimeWatched.class,
            mappedBy = "profile",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<MovieTimeWatched> movieTimeWatchedList;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profile)) return false;

        Profile profile = (Profile) o;

        return Objects.equals(id, profile.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", profileName='" + profileName + '\'' +
                '}';
    }
}
