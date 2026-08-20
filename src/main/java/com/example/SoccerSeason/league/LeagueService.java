package com.example.SoccerSeason.league;

import org.springframework.stereotype.Service;

@Service
public class LeagueService {
    public final LeagueRepository leagueRepository;

    public LeagueService(LeagueRepository leagueRepository){
        this.leagueRepository = leagueRepository;
    }


    public Leagues FindBySlug(String slug){
        return leagueRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("league not found"));
    }
}
