package com.example.SoccerSeason.league;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeagueRepository extends JpaRepository<Leagues, Integer> {
    Optional<Leagues> findBySlug(String slug);
}
