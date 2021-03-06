package main.commands.show;

import main.commands.contracts.Command;
import main.core.CommandFactoryImpl;
import main.core.TaskManagementSystemRepositoryImpl;
import main.core.contracts.CommandFactory;
import main.core.contracts.TaskManagementSystemRepository;
import main.exceptions.InvalidNumberOfArguments;
import main.models.tasks.contracts.Comment;
import main.models.tasks.contracts.Feedback;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static main.commands.BaseCommand.NO_ITEMS_TO_DISPLAY;
import static main.commands.show.ShowTaskComments.EXPECTED_NUMBER_OF_ARGUMENTS;
import static main.models.TestData.BoardImpl.VALID_BOARD_NAME;
import static main.models.TestData.FeedbackImpl.VALID_RATING;
import static main.models.TestData.TaskBase.VALID_DESCRIPTION;
import static main.models.TestData.TaskBase.VALID_TITLE;
import static main.models.TestData.TeamImpl.VALID_TEAM_NAME;
import static main.models.TestData.UserImpl.VALID_USER_NAME;
import static main.models.tasks.CommentImpl.CONTENT_LEN_MIN;

public class ShowAllTaskComments_Tests {

    private static CommandFactory commandFactory;
    private static TaskManagementSystemRepository repository;
    private static Command showTaskComments;

    @BeforeAll
    public static void setup() {
        commandFactory = new CommandFactoryImpl();
        repository = new TaskManagementSystemRepositoryImpl();
        showTaskComments = commandFactory.createCommand("ShowTaskComments", repository);

        Command createUser = commandFactory.createCommand("CreateUser", repository);
        createUser.execute(List.of(VALID_USER_NAME));

        Command createTeam = commandFactory.createCommand("CreateTeam", repository);
        createTeam.execute(List.of(VALID_TEAM_NAME));

        Command addUserToTeam = commandFactory.createCommand("AddUserToTeam", repository);
        addUserToTeam.execute(List.of(VALID_USER_NAME, VALID_TEAM_NAME));

        Command createBoard = commandFactory.createCommand("CreateBoard", repository);
        createBoard.execute(List.of(VALID_BOARD_NAME, VALID_TEAM_NAME));

        Command createFeedback = commandFactory.createCommand("CreateFeedback", repository);
        List<String> parameters = new ArrayList<>();
        parameters.add(VALID_USER_NAME);
        parameters.add(VALID_TEAM_NAME);
        parameters.add(VALID_BOARD_NAME);
        parameters.add(VALID_TITLE);
        parameters.add(VALID_DESCRIPTION);
        parameters.add(String.valueOf(VALID_RATING));
        createFeedback.execute(parameters);
    }


    @ParameterizedTest (name = "with length {0}")
    @ValueSource(ints = {EXPECTED_NUMBER_OF_ARGUMENTS-1, EXPECTED_NUMBER_OF_ARGUMENTS+1})
    public void showTaskComments_Should_throwException_WhenValidArguments(int parametersCount) {
        List<String> parameters = new ArrayList<>();

        for (int i = 1; i <= parametersCount; i++) {
            parameters.add("1");
        }

        Assertions.assertThrows(InvalidNumberOfArguments.class, () -> showTaskComments.execute(parameters));
    }

    @Test
    public void showTaskComments_Should_Indicate_When_NoCommentsToDisplay() {
        Assertions.assertEquals(String.format(NO_ITEMS_TO_DISPLAY, "comments"), showTaskComments.execute(List.of("1")));
    }

    @Test
    public void showTaskComments_Should_Execute_When_ValidInput() {
        Command addComment = commandFactory.createCommand("AddCommentToTask", repository);
        List<String> commentParameters = new ArrayList<>();
        commentParameters.add(VALID_USER_NAME);
        commentParameters.add("1");
        commentParameters.add("x".repeat(CONTENT_LEN_MIN));
        addComment.execute(commentParameters);

        Feedback task = repository.findFeedback(1);
        List<Comment> commentList = task.getComments();

        String output = String.format("Author: %s - Comment: %s", commentList.get(0).getAuthor(), commentList.get(0).getContent());

        Assertions.assertEquals(output, showTaskComments.execute(List.of("1")));
    }

}
