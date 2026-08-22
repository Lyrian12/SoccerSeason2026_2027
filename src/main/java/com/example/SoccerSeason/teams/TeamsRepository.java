package com.example.SoccerSeason.teams;

import com.example.SoccerSeason.players.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamsRepository extends JpaRepository<Teams, Integer> {

    List<Teams> findByLeaguesId (int league_id);
}
