package com.example.SoccerSeason.teams;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServices {

    private final  TeamsRepository teamsRepository;

    public TeamServices(TeamsRepository teamsRepository){
        this.teamsRepository = teamsRepository;
    }

    public List<Teams> getTeamsByLeagueId(int leagueId){
        return  teamsRepository.findByLeaguesId(leagueId);
    }
}
