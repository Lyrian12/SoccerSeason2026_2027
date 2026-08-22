package com.example.SoccerSeason.teams;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamsRepository extends JpaRepository<Teams, Integer> {

    List<Teams> findByLeaguesId (int league_id);
}
