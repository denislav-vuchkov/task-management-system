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

import static main.commands.team.CreateBoard.EXPECTED_NUMBER_OF_ARGUMENTS;
import static main.models.teams.BoardImpl.*;
import static main.models.TestData.BoardImpl.VALID_BOARD_NAME;
import static main.models.TestData.TeamImpl.VALID_TEAM_NAME;

public class CreateBoard_Tests {

    CommandFactory commandFactory;
    TaskManagementSystemRepository repository;
    Command createBoard;

    @BeforeEach
    public void setup() {
        commandFactory = new CommandFactoryImpl();
        repository = new TaskManagementSystemRepositoryImpl();
        createBoard = commandFactory.createCommand("CreateBoard", repository);
    }

    @ParameterizedTest(name = "with length {0}")
    @ValueSource(ints = {EXPECTED_NUMBER_OF_ARGUMENTS-1, EXPECTED_NUMBER_OF_ARGUMENTS+1})
    public void createBoard_Should_throwException_WhenValidArgumentsCounts(int parametersCount) {
        List<String> parameters = new ArrayList<>();

        for (int i = 1; i <= parametersCount; i++) {
            parameters.add("1");
        }

        Assertions.assertThrows(InvalidNumberOfArguments.class, () -> createBoard.execute(parameters));
        Assertions.assertEquals(0, getBoardsInRepositoryNum());
    }

    @Test
    public void createBoard_Should_throwException_whenInvalidTeamName() {
        String invalidTeamName = "Team Rocket"; //This team doesn't exist in the repo as it has not been created

        List<String> parameters = new ArrayList<>();
        parameters.add(VALID_BOARD_NAME);
        parameters.add(invalidTeamName);

        Assertions.assertThrows(InvalidUserInput.class, () -> createBoard.execute(parameters));
        Assertions.assertEquals(0, getBoardsInRepositoryNum());
    }

    @Test
    public void createBoard_Should_Execute_WhenValidInput() {
        Command createTeam = commandFactory.createCommand("CreateTeam", repository);
        List<String> teamParameters = List.of(VALID_TEAM_NAME);
        createTeam.execute(teamParameters);

        List<String> boardParameters = List.of(VALID_TEAM_NAME, VALID_BOARD_NAME);

        Assertions.assertDoesNotThrow(() -> createBoard.execute(boardParameters));
        Assertions.assertEquals(1, getBoardsInRepositoryNum());
    }

    @Test
    public void createBoard_Should_ThrowException_When_BoardNameExists() {
        Command createTeam = commandFactory.createCommand("CreateTeam", repository);
        List<String> teamParameters = List.of(VALID_TEAM_NAME);
        createTeam.execute(teamParameters);

        List<String> boardParameters = List.of(VALID_TEAM_NAME, VALID_BOARD_NAME);
        createBoard.execute(boardParameters);

        Assertions.assertThrows(InvalidUserInput.class, () -> createBoard.execute(boardParameters));
        Assertions.assertEquals(1, getBoardsInRepositoryNum());
    }

    @Test
    public void createBoard_Should_Execute_When_BoardNameIsUniqueForTeam() {
        Command createTeam = commandFactory.createCommand("CreateTeam", repository);
        List<String> teamOneParameters = List.of(VALID_TEAM_NAME);
        createTeam.execute(teamOneParameters);

        List<String> boardOneParameters = List.of(VALID_TEAM_NAME, VALID_BOARD_NAME);
        createBoard.execute(boardOneParameters);

        String anotherValidTeamName = "x".repeat(NAME_MAX_LEN -1);
        List<String> teamTwoParameters = List.of(anotherValidTeamName);
        createTeam.execute(teamTwoParameters);

        List<String> boardTwoParameters = List.of(anotherValidTeamName, VALID_BOARD_NAME);

        Assertions.assertDoesNotThrow(() -> createBoard.execute(boardTwoParameters));
        Assertions.assertEquals(2, getBoardsInRepositoryNum());
    }

    @ParameterizedTest(name = "with length {0}")
    @ValueSource(ints = {NAME_MIN_LEN -1, NAME_MAX_LEN +1})
    public void createBoard_Should_throwException_whenInvalidBoardName(int nameLength) {
        Command createTeam = commandFactory.createCommand("CreateTeam", repository);
        List<String> teamParameters = List.of(VALID_TEAM_NAME);
        createTeam.execute(teamParameters);

        List<String> boardParameters = List.of(VALID_TEAM_NAME, "x".repeat(nameLength));
        Assertions.assertThrows(InvalidUserInput.class, () -> createBoard.execute(boardParameters));
        Assertions.assertEquals(0, getBoardsInRepositoryNum());
    }

    public int getBoardsInRepositoryNum() {
        return (int) repository.getTeams()
                .stream()
                .flatMap(team -> team.getBoards().stream())
                .count();
    }


}
