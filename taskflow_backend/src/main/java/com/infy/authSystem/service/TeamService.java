package com.infy.authSystem.service;

import com.infy.authSystem.entity.Role;
import com.infy.authSystem.entity.Team;
import com.infy.authSystem.entity.User;
import com.infy.authSystem.repository.TeamRepository;
import com.infy.authSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepo;
    public TeamService(TeamRepository teamRepo, UserRepository userRepo) {
		this.teamRepo = teamRepo;
		this.userRepo = userRepo;
	}

	private final UserRepository userRepo;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Team createTeam(Team team, String email){

        User manager = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        team.setManager(manager);

        return teamRepo.save(team);
    }

    public List<Team> getTeams(String email){

        User user = userRepo.findByEmail(email).orElseThrow();

        if(user.getRole() == Role.ADMIN){
            return teamRepo.findAll();
        }

        if(user.getRole() == Role.MANAGER){
            return teamRepo.findByManager(user);
        }

        return teamRepo.findTeamsForUser(email);
    }

    public Team getTeam(Long id){
        return teamRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Team addMember(Long teamId, Long userId){

        Team team = teamRepo.findById(teamId).orElseThrow();
        User user = userRepo.findById(userId).orElseThrow();

        team.getMembers().add(user);

        return teamRepo.save(team);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Team removeMember(Long teamId, Long userId){

        Team team = teamRepo.findById(teamId).orElseThrow();

        team.getMembers().removeIf(u -> u.getId().equals(userId));

        return teamRepo.save(team);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public void deleteTeam(Long id){

        teamRepo.deleteById(id);
    }
}
