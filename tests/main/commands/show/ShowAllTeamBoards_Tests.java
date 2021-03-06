package main.commands.show;

import main.commands.contracts.Command;
import main.core.CommandFactoryImpl;
import main.core.TaskManagementSystemRepositoryImpl;
import main.core.contracts.CommandFactory;
import main.core.contracts.TaskManagementSystemRepository;
import main.exceptions.InvalidNumberOfArguments;
import main.models.teams.contracts.Board;
import main.models.teams.contracts.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static main.commands.BaseCommand.*;
import static main.commands.show.ShowTeamBoards.EXPECTED_NUMBER_OF_ARGUMENTS;
import static main.models.TestData.BoardImpl.VALID_BOARD_NAME;
import static main.models.TestData.TeamImpl.VALID_TEAM_NAME;

public class ShowAllTeamBoards_Tests {

    CommandFactory commandFactory;
    TaskManagementSystemRepository repository;
    Command command;

    @BeforeEach
    public void setup() {
        commandFactory = new CommandFactoryImpl();
        repository = new TaskManagementSystemRepositoryImpl();
        command = commandFactory.createCommand("ShowTeamBoards", repository);

    }

    @ParameterizedTest(name = "with length {0}")
    @ValueSource(ints = {EXPECTED_NUMBER_OF_ARGUMENTS-1, EXPECTED_NUMBER_OF_ARGUMENTS+1})
    public void showTeamBoards_Should_throwException_WhenValidArguments(int parametersCount) {
        List<String> parameters = new ArrayList<>();

        for (int i = 1; i <= parametersCount; i++) {
            parameters.add("1");
        }

        Assertions.assertThrows(InvalidNumberOfArguments.class, () -> command.execute(parameters));
    }

    @Test
    public void showTeamBoards_Should_Indicate_When_NoCommentsToDisplay() {
        Command createTeam = commandFactory.createCommand("CreateTeam", repository);
        createTeam.execute(List.of(VALID_TEAM_NAME));

        Assertions.assertEquals(String.format(NO_ITEMS_TO_DISPLAY, "boards"), command.execute(List.of(VALID_TEAM_NAME)));
    }

    @Test
    public void showTeamBoards_Should_Execute_When_ValidInput() {
        Command createTeam = commandFactory.createCommand("CreateTeam", repository);
        createTeam.execute(List.of(VALID_TEAM_NAME));

        Command createBoard = commandFactory.createCommand("CreateBoard", repository);
        createBoard.execute(List.of(VALID_BOARD_NAME, VALID_TEAM_NAME));

        Team team = repository.findTeam(VALID_TEAM_NAME);
        Board board = repository.findBoard(VALID_BOARD_NAME, VALID_TEAM_NAME);

        String output = String.format("Board name: %s - Board items: %d", board.getName(), board.getTasks().size());

        Assertions.assertEquals(output, command.execute(List.of(VALID_BOARD_NAME)));
    }

}
