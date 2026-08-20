package com.example.SoccerSeason.players;

import com.example.SoccerSeason.teams.Teams;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "players")
@Data
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotNull
    int Age;
    @NotNull
    String Name;
    @NotNull
    String Surname;
    @Enumerated(EnumType.STRING)
    PlayerPosition playerposition;
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    Teams teams;
    @NotNull
    int height;
    @NotNull
    int weight;
    @Enumerated(EnumType.STRING)
    Country country;

}
