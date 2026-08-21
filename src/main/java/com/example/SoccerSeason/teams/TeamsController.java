package com.example.SoccerSeason.teams;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class TeamsController {


    @GetMapping("/team")
    public String getTeams() {
        return "teams";
    }
}
