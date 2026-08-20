package com.example.SoccerSeason.league;

import com.example.SoccerSeason.teams.Teams;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;


@Entity
@Table(name = "leagues")
@Data
public class Leagues {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @NotNull
    String name;
    @Column(unique = true , nullable = false)
    String slug;

    @OneToMany(mappedBy = "leagues", cascade = CascadeType.ALL)
    List<Teams> teams;
}
