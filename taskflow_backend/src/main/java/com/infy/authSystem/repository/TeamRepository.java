package com.infy.authSystem.repository;

import com.infy.authSystem.entity.Team;
import com.infy.authSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByManager(User manager);

    @Query("""
       SELECT t FROM Team t
       JOIN t.members m
       WHERE m.email = :email
    """)
    List<Team> findTeamsForUser(String email);
}
