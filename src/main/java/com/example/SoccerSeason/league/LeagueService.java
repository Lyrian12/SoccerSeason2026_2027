package com.example.SoccerSeason.league;

import org.springframework.stereotype.Service;

@Service
public class LeagueService {
    public final LeagueRepository leagueRepository;

    public LeagueService(LeagueRepository leagueRepository){
        this.leagueRepository = leagueRepository;
    }

}
