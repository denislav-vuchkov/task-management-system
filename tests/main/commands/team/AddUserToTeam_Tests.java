package main.commands.team;

import main.commands.contracts.Command;
import main.core.CommandFactoryImpl;
import main.core.TaskManagementSystemRepositoryImpl;
import main.core.contracts.CommandFactory;
import main.core.contracts.TaskManagementSystemRepository;
import main.exceptions.InvalidNumberOfArguments;
import main.exceptions.InvalidUserInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static main.commands.team.AddUserToTeam.EXPECTED_NUMBER_OF_ARGUMENTS;
import static main.commands.team.AddUserToTeam.USER_ADDED_TO_TEAM;
import static main.core.TaskManagementSystemRepositoryImpl.NOT_EXIST;
import static main.models.TestData.TeamImpl.VALID_TEAM_NAME;
import static main.models.TestData.UserImpl.VALID_USER_NAME;
import static main.models.teams.contracts.subcontracts.Nameable.NAME_MIN_LEN;

public class AddUserToTeam_Tests {

    CommandFactory commandFactory;
    TaskManagementSystemRepository repository;
    Command addUserToTeam;

    @BeforeEach
    public void setup() {
        commandFactory = new CommandFactoryImpl();
        repository = new TaskManagementSystemRepositoryImpl();
        addUserToTeam = commandFactory.createCommand("AddUserToTeam", repository);

        Command createTeam = commandFactory.createCommand("CreateTeam", repository);
        createTeam.execute(List.of(VALID_TEAM_NAME));

        Command createUser = commandFactory.createCommand("CreateUser", repository);
        createUser.execute(List.of(VALID_USER_NAME));
    }

    @ParameterizedTest(name = "with length {0}")
    @ValueSource(ints = {EXPECTED_NUMBER_OF_ARGUMENTS-1, EXPECTED_NUMBER_OF_ARGUMENTS+1})
    public void addUserToTeam_Should_throwException_WhenValidArgumentsCounts(int parametersCount) {
        List<String> parameters = new ArrayList<>();

        for (int i = 1; i <= parametersCount; i++) {
            parameters.add("1");
        }

        Assertions.assertThrows(InvalidNumberOfArguments.class, () -> addUserToTeam.execute(parameters));
        Assertions.assertEquals(1, repository.getUsers().size());
    }

    @Test
    public void addUserToTeam_Should_Execute_WhenValidInput() {
        List<String> parameters = List.of(VALID_USER_NAME, VALID_TEAM_NAME);

        Assertions.assertDoesNotThrow(() -> addUserToTeam.execute(parameters));
        Assertions.assertEquals(1, getUsersInTeams());
    }

    @Test
    public void addUserToTeam_Should_ThrowException_WhenUserAlreadyOnTeam() {
        List<String> parameters = List.of(VALID_USER_NAME, VALID_TEAM_NAME);
        addUserToTeam.execute(parameters);

        String errMessage = String.format(USER_ADDED_TO_TEAM, VALID_USER_NAME, VALID_TEAM_NAME);

        Assertions.assertThrows(InvalidUserInput.class, () -> addUserToTeam.execute(parameters), errMessage);
        Assertions.assertEquals(1, getUsersInTeams());
    }

    @Test
    public void addUserToTeam_Should_ThrowException_WhenUserDoesNotExist() {
        String invalidUserName = "x".repeat(NAME_MIN_LEN +1);
        List<String> parameters = List.of(invalidUserName, VALID_TEAM_NAME);

        String errMessage = String.format(NOT_EXIST, "user", "user");

        Assertions.assertThrows(InvalidUserInput.class, () -> addUserToTeam.execute(parameters), errMessage);
        Assertions.assertEquals(0, getUsersInTeams());
    }

    @Test
    public void addUserToTeam_Should_ThrowException_WhenTeamDoesNotExist() {
        String invalidTeamName = "T".repeat(NAME_MIN_LEN +1);
        List<String> parameters = List.of(VALID_USER_NAME, invalidTeamName);

        String errMessage = String.format(NOT_EXIST, "team", "team");

        Assertions.assertThrows(InvalidUserInput.class, () -> addUserToTeam.execute(parameters), errMessage);
        Assertions.assertEquals(0, getUsersInTeams());
    }


    public int getUsersInTeams() {
         return (int) repository.
                getTeams()
                .stream()
                .flatMap(team -> team.getUsers().stream())
                .count();
    }




}
