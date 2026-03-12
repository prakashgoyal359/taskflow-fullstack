package com.infy.authSystem.controller;

import com.infy.authSystem.entity.Team;
import com.infy.authSystem.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
		this.teamService = teamService;
	}

	@PostMapping
    public Team createTeam(@RequestBody Team team, Authentication auth){

        return teamService.createTeam(team, auth.getName());
    }

    @GetMapping
    public List<Team> getTeams(Authentication auth){

        return teamService.getTeams(auth.getName());
    }

    @GetMapping("/{id}")
    public Team getTeam(@PathVariable Long id){

        return teamService.getTeam(id);
    }

    @PostMapping("/{id}/members")
    public Team addMember(@PathVariable Long id,
                          @RequestParam Long userId){

        return teamService.addMember(id, userId);
    }

    @DeleteMapping("/{id}/members/{userId}")
    public Team removeMember(@PathVariable Long id,
                             @PathVariable Long userId){

        return teamService.removeMember(id, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable Long id){

        teamService.deleteTeam(id);
    }
}