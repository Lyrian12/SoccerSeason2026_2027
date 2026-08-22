package com.example.SoccerSeason.teams;


import com.example.SoccerSeason.players.Player;
import com.example.SoccerSeason.players.PlayerRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TeamServices {

    private final TeamsRepository teamsRepository;
    private final PlayerRepository playerRepository; // Injection du repository des joueurs

    public TeamServices(TeamsRepository teamsRepository, PlayerRepository playerRepository) {
        this.teamsRepository = teamsRepository;
        this.playerRepository = playerRepository;
    }

    public List<Teams> getTeamsByLeagueId(int leagueId) {
        return teamsRepository.findByLeaguesId(leagueId);
    }

    public List<Player> getPlayersByTeamId(int teamId) {
        return playerRepository.findByTeamsId(teamId);
    }

    // Méthode utile pour récupérer l'équipe et éviter les erreurs dans le contrôleur
    public Teams getTeamById(int teamId) {
        return teamsRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Équipe introuvable avec l'ID : " + teamId));
    }
}
