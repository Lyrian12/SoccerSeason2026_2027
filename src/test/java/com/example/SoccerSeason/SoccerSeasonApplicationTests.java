package com.example.SoccerSeason;

import com.example.SoccerSeason.league.Leagues;
import com.example.SoccerSeason.players.Player;
import com.example.SoccerSeason.players.PlayerController;
import com.example.SoccerSeason.players.PlayerPosition;
import com.example.SoccerSeason.teams.TeamServices;
import com.example.SoccerSeason.teams.Teams;
import com.example.SoccerSeason.teams.TeamsController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SoccerSeasonApplicationTests {

    private MockMvc teamsMvc;
    private MockMvc squadMvc;

    @BeforeEach
    void setUp() {
        TeamServices teamServices = mock(TeamServices.class);

        Leagues league = new Leagues();
        league.setId(1);
        league.setName("Premier League");

        Teams team = new Teams();
        team.setId(23);
        team.setName("Manchester City");
        team.setLeagues(league);

        Player player = new Player();
        player.setName("Kevin");
        player.setSurname("De Bruyne");
        player.setPlayerposition(PlayerPosition.CENTRAL_MIDFIELDER);

        when(teamServices.getTeamsByLeagueId(1)).thenReturn(List.of(team));
        when(teamServices.getTeamById(23)).thenReturn(team);
        when(teamServices.getPlayersByTeamId(23)).thenReturn(List.of(player));

        ViewResolver noOpViewResolver = new ViewResolver() {
            @Override
            public View resolveViewName(String viewName, Locale locale) {
                return new View() {
                    @Override
                    public String getContentType() {
                        return "text/html";
                    }

                    @Override
                    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                };
            }
        };

        teamsMvc = MockMvcBuilders.standaloneSetup(new TeamsController(teamServices))
                .setViewResolvers(noOpViewResolver)
                .build();

        PlayerController playerController = new PlayerController();
        ReflectionTestUtils.setField(playerController, "teamServices", teamServices);
        squadMvc = MockMvcBuilders.standaloneSetup(playerController)
                .setViewResolvers(noOpViewResolver)
                .build();
    }

    @Test
    void teamsQueryRouteRendersTeamsPage() throws Exception {
        teamsMvc.perform(get("/teams").param("leagueId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void squadRouteRendersSquadPage() throws Exception {
        squadMvc.perform(get("/teams/23/squad"))
                .andExpect(status().isOk());
    }
}
