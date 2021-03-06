package main.commands.show;

import main.commands.contracts.Command;
import main.core.CommandFactoryImpl;
import main.core.TaskManagementSystemRepositoryImpl;
import main.core.contracts.CommandFactory;
import main.core.contracts.TaskManagementSystemRepository;
import main.exceptions.InvalidNumberOfArguments;
import main.models.tasks.contracts.Feedback;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static main.commands.BaseCommand.NO_ITEMS_TO_DISPLAY;
import static main.commands.show.ShowTaskDetails.EXPECTED_NUMBER_OF_ARGUMENTS;
import static main.models.TestData.BoardImpl.VALID_BOARD_NAME;
import static main.models.TestData.FeedbackImpl.VALID_RATING;
import static main.models.TestData.TaskBase.VALID_DESCRIPTION;
import static main.models.TestData.TaskBase.VALID_TITLE;
import static main.models.TestData.TeamImpl.VALID_TEAM_NAME;
import static main.models.TestData.UserImpl.VALID_USER_NAME;

public class ShowAllTaskDetails_Tests {

    private static CommandFactory commandFactory;
    private static TaskManagementSystemRepository repository;
    private static Command showTasKDetails;

    @BeforeAll
    public static void setup() {
        commandFactory = new CommandFactoryImpl();
        repository = new TaskManagementSystemRepositoryImpl();
        showTasKDetails = commandFactory.createCommand("ShowTaskDetails", repository);

        Command createUser = commandFactory.createCommand("CreateUser", repository);
        createUser.execute(List.of(VALID_USER_NAME));

        Command createTeam = commandFactory.createCommand("CreateTeam", repository);
        createTeam.execute(List.of(VALID_TEAM_NAME));

        Command addUserToTeam = commandFactory.createCommand("AddUserToTeam", repository);
        addUserToTeam.execute(List.of(VALID_USER_NAME, VALID_TEAM_NAME));

        Command createBoard = commandFactory.createCommand("CreateBoard", repository);
        createBoard.execute(List.of(VALID_BOARD_NAME, VALID_TEAM_NAME));
    }

    @ParameterizedTest(name = "with length {0}")
    @ValueSource(ints = {EXPECTED_NUMBER_OF_ARGUMENTS-1, EXPECTED_NUMBER_OF_ARGUMENTS+1})
    public void showTaskDetails_Should_throwException_WhenInvalidArgumentsCounts(int parametersCount) {
        List<String> parameters = new ArrayList<>();

        for (int i = 1; i <= parametersCount; i++) {
            parameters.add("1");
        }

        Assertions.assertThrows(InvalidNumberOfArguments.class, () -> showTasKDetails.execute(parameters));
    }

    @Test
    public void showAllTaskDetails_Should_Indicate_When_NoTasksToDisplay() {
        Command showAllTasks = commandFactory.createCommand("ShowTaskDetails",
                new TaskManagementSystemRepositoryImpl());

        Assertions.assertEquals(String.format(NO_ITEMS_TO_DISPLAY, "tasks"), showAllTasks.execute(List.of("1")));
    }

    @Test
    public void showAllTaskDetails_Should_Execute_When_ValidInput() {
        Command createFeedback = commandFactory.createCommand("CreateFeedback", repository);
        List<String> parameters = new ArrayList<>();
        parameters.add(VALID_USER_NAME);
        parameters.add(VALID_TEAM_NAME);
        parameters.add(VALID_BOARD_NAME);
        parameters.add(VALID_TITLE);
        parameters.add(VALID_DESCRIPTION);
        parameters.add(String.valueOf(VALID_RATING));
        createFeedback.execute(parameters);

        Feedback task = repository.findFeedback(1);

        Assertions.assertDoesNotThrow(() -> showTasKDetails.execute(List.of("1")));
    }

}
