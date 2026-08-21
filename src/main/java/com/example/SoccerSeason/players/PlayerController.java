package com.example.SoccerSeason.players;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PlayerController {

@GetMapping("/players")
    public String getSquad(){
    return "squad";
}

}



