package com.example.SoccerSeason.players;

import com.example.SoccerSeason.players.Player;
import com.example.SoccerSeason.players.PlayerPosition;
import com.example.SoccerSeason.teams.TeamServices;
import com.example.SoccerSeason.teams.Teams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class PlayerController {

    @Autowired
    private TeamServices teamServices;

    @GetMapping("/teams/{teamId}/squad")
    public String showTeamSquad(@PathVariable int teamId, Model model) {

        // 1. Récupère l'équipe
        Teams team = teamServices.getTeamById(teamId);
        if (team == null) {
            return "error"; // Redirige vers une page d'erreur si l'équipe n'existe pas
        }

        model.addAttribute("teamName", team.getName());

        // 2. Gestion sécurisée de la liaison League (évite le NullPointerException si mal mappé)
        if (team.getLeagues() != null) {
            model.addAttribute("leagueId", team.getLeagues().getId());
            model.addAttribute("leagueName", team.getLeagues().getName());
        }

        // 3. Récupère tous les joueurs de cette équipe
        List<Player> players = teamServices.getPlayersByTeamId(teamId);

        // 4. Groupe les joueurs par position
        Map<PlayerPosition, List<Player>> squadByPosition = players.stream()
                .collect(Collectors.groupingBy(Player::getPlayerposition));

        model.addAttribute("squadByPosition", squadByPosition);

        return "squad"; // Cherche le fichier squad.html dans src/main/resources/templates/
    }
}