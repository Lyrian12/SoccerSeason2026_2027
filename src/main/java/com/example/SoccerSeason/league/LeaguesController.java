package com.example.SoccerSeason.league;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class LeaguesController  {




    @RequestMapping("/leagues")
    public String getLeagues() {
        return "league";
    }
}
