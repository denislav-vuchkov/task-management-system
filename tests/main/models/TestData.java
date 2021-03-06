package main.models;

import main.models.tasks.enums.Priority;
import main.models.tasks.enums.Severity;
import main.models.tasks.enums.Size;

import java.util.ArrayList;
import java.util.List;

import static main.models.tasks.BugImpl.*;
import static main.models.tasks.FeedbackImpl.*;
import static main.models.teams.contracts.subcontracts.Nameable.*;

public class TestData {

    public static class TaskBase {
        public static final String VALID_TITLE = "x".repeat(TITLE_MIN);
        public static final String VALID_DESCRIPTION = "x".repeat(DESCRIPTION_MAX);
    }

    public static class AssignableTask {
        public static final Priority VALID_PRIORITY = Priority.HIGH;
        public static final String VALID_ASSIGNEE = "Peter";
    }

    public static class BugImpl {
        public static final List<String> STEPS_TO_REPRODUCE = new ArrayList<>();

        public BugImpl() {
            STEPS_TO_REPRODUCE.add("Open browser.");
            STEPS_TO_REPRODUCE.add("Open website.");
            STEPS_TO_REPRODUCE.add("Click \"About us\".");
        }

        public static final Severity VALID_SEVERITY = Severity.MAJOR;
    }

    public static class StoryImpl {
        public static final Size VALID_SIZE = Size.SMALL;
    }

    public static class FeedbackImpl {
        public static final int VALID_RATING = RATING_MIN;
    }

    public static class UserImpl {
        public static final String VALID_USER_NAME = "x".repeat(NAME_MIN_LEN);
    }

    public static class BoardImpl {
        public static final String VALID_BOARD_NAME = "x".repeat(NAME_MAX_LEN);
    }

    public static class TeamImpl {
        public static final String VALID_TEAM_NAME = "x".repeat(NAME_MAX_LEN);
    }

}
