package com.example.SoccerSeason.teams;

import com.example.SoccerSeason.league.Leagues;
import com.example.SoccerSeason.players.Player;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;


@Entity
@Table(name = "teams")
@Data
public class Teams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;


    @NotNull
    String name;

    @ManyToOne
    @JoinColumn(name = "league_id")
    Leagues leagues;

    @OneToMany(mappedBy = "teams", cascade = CascadeType.ALL)
    private List<Player> players;

}
