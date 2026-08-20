package com.example.SoccerSeason.league;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LeaguesController {


    @GetMapping("/leagues")
    public String getLeagues() {
        return "league";
    }
}
