package com.example.SoccerSeason.teams;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/leagues")
public class TeamsController {

    private final TeamServices teamServices;

    public TeamsController( TeamServices teamServices){
        this.teamServices = teamServices;
    }

    @GetMapping("/{leaguesId}/teams")
    public String ShowTeamsByLigue(@PathVariable int leaguesId, Model model){

        List<Teams> teams = teamServices.getTeamsByLeagueId(leaguesId);
        model.addAttribute("teams",teams);
        String leagueName = switch (leaguesId){
          case 1 -> "premier League";
          case 2 -> "La Liga";
          case 3 -> "Serie A";
          case 4 -> "Bundesliga";
          case 5 -> "Ligue 1";
            default -> "European Football ligues";
        };
        model.addAttribute("leagueName", leagueName);
        return  "teams";
    }

}
