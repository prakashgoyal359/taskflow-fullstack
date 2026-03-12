package com.infy.authSystem.controller;

import com.infy.authSystem.entity.Team;
import com.infy.authSystem.service.TeamService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TeamController {

    public TeamController(TeamService teamService) {
		this.teamService = teamService;
	}

	private final TeamService teamService;

    // CREATE TEAM
    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody Team team, Authentication auth) {

        Team created = teamService.createTeam(team, auth.getName());

        return ResponseEntity.ok(created);
    }

    // GET TEAMS (ADMIN → all teams, MANAGER → own teams)
    @GetMapping
    public ResponseEntity<List<Team>> getTeams(Authentication auth) {

        List<Team> teams = teamService.getTeams(auth.getName());

        return ResponseEntity.ok(teams);
    }

    // GET SINGLE TEAM
    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeam(@PathVariable Long id) {

        return ResponseEntity.ok(teamService.getTeam(id));
    }

    // ADD MEMBER
    @PostMapping("/{teamId}/members/{userId}")
    public ResponseEntity<Team> addMember(
            @PathVariable Long teamId,
            @PathVariable Long userId) {

        Team team = teamService.addMember(teamId, userId);

        return ResponseEntity.ok(team);
    }

    // REMOVE MEMBER
    @DeleteMapping("/{teamId}/members/{userId}")
    public ResponseEntity<Team> removeMember(
            @PathVariable Long teamId,
            @PathVariable Long userId) {

        Team team = teamService.removeMember(teamId, userId);

        return ResponseEntity.ok(team);
    }

    // DELETE TEAM
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id) {

        teamService.deleteTeam(id);

        return ResponseEntity.ok().build();
    }
}