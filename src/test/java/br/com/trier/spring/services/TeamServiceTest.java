package br.com.trier.spring.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import br.com.trier.spring.BaseTests;
import br.com.trier.spring.models.Team;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/equipe.sql")
class TeamServiceTest extends BaseTests {

    @Autowired
    TeamService teamService;

    @Test
    @DisplayName("Test find team by ID")
    void findByIdTest() {
        var team = teamService.findById(1);
        assertNotNull(team);
        assertEquals(1, team.getId());
        assertEquals("Hotwheels", team.getTeamName());
    }

    @Test
    @DisplayName("Test find team by non-existent ID")
    void findByIdNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> teamService.findById(10));
        assertEquals("Team 10 does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("Test insert team")
    void insertTeamTest() {
        Team team = new Team(1, "Hotwheels");
        teamService.insert(team);
        team = teamService.findById(1);
        assertEquals(1, team.getId());
        assertEquals("Hotwheels", team.getTeamName());
    }

    @Test
    @DisplayName("Test insert existing team")
    void insertExistingTeamTest() {
        Team team = new Team(1, "Hotwheels");
        Team team2 = new Team(5, "Hotwheels");
        teamService.insert(team);
        assertEquals(1, team.getId());
        assertEquals("Hotwheels", team.getTeamName());
        var exception = assertThrows(IntegrityViolation.class, () -> teamService.insert(team2));
        assertEquals("Team already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Test remove team")
    void removeTeamTest() {
        teamService.delete(1);
        List<Team> list = teamService.listAll();
        assertEquals(2, list.size());
        assertEquals(2, list.get(0).getId());
        assertEquals(3, list.get(1).getId());
    }

    @Test
    @DisplayName("Test remove non-existent team")
    void removeNonExistingTeamTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> teamService.delete(10));
        assertEquals("Team 10 does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("Test list all teams with records")
    void listAllTeamTest() {
        List<Team> list = teamService.listAll();
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("Test list all teams with no records")
    void listAllNoTeamTest() {
        List<Team> list = teamService.listAll();
        assertEquals(3, list.size());
        teamService.delete(1);
        teamService.delete(2);
        teamService.delete(3);
        var exception = assertThrows(ObjectNotFound.class, () -> teamService.listAll());
        assertEquals("No teams registered", exception.getMessage());
    }

    @Test
    @DisplayName("Test update team")
    void updateTeamTest() {
        var team = teamService.findById(1);
        assertEquals("Hotwheels", team.getTeamName());
        var updatedTeam = new Team(1, "alter");
        teamService.update(updatedTeam);
        team = teamService.findById(1);
        assertEquals("alter", team.getTeamName());
    }

    @Test
    @DisplayName("Test find team by name starting with")
    void findByTeamStartsWithTest() {
        List<Team> list = teamService.findByTeamNameStartingWithIgnoreCase("b");
        assertEquals(1, list.size());
        list = teamService.findByTeamNameStartingWithIgnoreCase("ferrari");
        assertEquals(1, list.size());
        var exception = assertThrows(ObjectNotFound.class, () -> teamService.findByTeamNameStartingWithIgnoreCase("z"));
        assertEquals("No team names start with z", exception.getMessage());
    }
}
