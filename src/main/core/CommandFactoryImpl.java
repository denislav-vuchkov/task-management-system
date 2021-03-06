package main.core;

import main.commands.activity.ShowBoardActivity;
import main.commands.activity.ShowTaskActivity;
import main.commands.activity.ShowTeamActivity;
import main.commands.activity.ShowUserActivity;
import main.commands.contracts.Command;
import main.commands.enums.CommandType;
import main.commands.filter.*;
import main.commands.show.*;
import main.commands.sort.*;
import main.commands.task.*;
import main.commands.team.*;
import main.core.contracts.CommandFactory;
import main.core.contracts.TaskManagementSystemRepository;
import main.exceptions.InvalidUserInput;
import main.utils.ParsingHelpers;

public class CommandFactoryImpl implements CommandFactory {

    @Override
    public Command createCommand(String commandTypeAsString, TaskManagementSystemRepository taskManagementSystemRepository) {

        CommandType commandType = ParsingHelpers.tryParseCommand(commandTypeAsString, CommandType.class);

        switch (commandType) {
            case SHOW_ALL_TEAMS:
                return new ShowAllTeams(taskManagementSystemRepository);
            case SHOW_ALL_USERS:
                return new ShowAllUsers(taskManagementSystemRepository);
            case SHOW_ALL_TASKS:
                return new ShowAllTasks(taskManagementSystemRepository);
            case SHOW_TEAM_USERS:
                return new ShowTeamUsers(taskManagementSystemRepository);
            case SHOW_TEAM_BOARDS:
                return new ShowTeamBoards(taskManagementSystemRepository);
            case SHOW_TASK_COMMENTS:
                return new ShowTaskComments(taskManagementSystemRepository);
            case SHOW_TASK_DETAILS:
                return new ShowTaskDetails(taskManagementSystemRepository);
            case SHOW_TEAM_ACTIVITY:
                return new ShowTeamActivity(taskManagementSystemRepository);
            case SHOW_BOARD_ACTIVITY:
                return new ShowBoardActivity(taskManagementSystemRepository);
            case SHOW_USER_ACTIVITY:
                return new ShowUserActivity(taskManagementSystemRepository);
            case SHOW_TASK_ACTIVITY:
                return new ShowTaskActivity(taskManagementSystemRepository);
            case CREATE_TEAM:
                return new CreateTeam(taskManagementSystemRepository);
            case CREATE_BOARD:
                return new CreateBoard(taskManagementSystemRepository);
            case CREATE_USER:
                return new CreateUser(taskManagementSystemRepository);
            case ADD_USER_TO_TEAM:
                return new AddUserToTeam(taskManagementSystemRepository);
            case ADD_COMMENT_TO_TASK:
                return new AddCommentToTask(taskManagementSystemRepository);
            case ASSIGN_TASK:
                return new AssignTask(taskManagementSystemRepository);
            case CREATE_BUG:
                return new CreateBug(taskManagementSystemRepository);
            case CREATE_STORY:
                return new CreateStory(taskManagementSystemRepository);
            case CREATE_FEEDBACK:
                return new CreateFeedback(taskManagementSystemRepository);
            case CHANGE_BUG:
                return new ChangeBug(taskManagementSystemRepository);
            case CHANGE_STORY:
                return new ChangeStory(taskManagementSystemRepository);
            case CHANGE_FEEDBACK:
                return new ChangeFeedback(taskManagementSystemRepository);
            case FILTER_ALL_TASKS:
                return new FilterAllTasks(taskManagementSystemRepository);
            case SORT_ALL_TASKS:
                return new SortAllTasks(taskManagementSystemRepository);
            case FILTER_BUGS:
                return new FilterBugs(taskManagementSystemRepository);
            case SORT_BUGS:
                return new SortBugs(taskManagementSystemRepository);
            case FILTER_STORIES:
                return new FilterStories(taskManagementSystemRepository);
            case SORT_STORIES:
                return new SortStories(taskManagementSystemRepository);
            case FILTER_FEEDBACKS:
                return new FilterFeedbacks(taskManagementSystemRepository);
            case SORT_FEEDBACKS:
                return new SortFeedbacks(taskManagementSystemRepository);
            case FILTER_ASSIGNABLE_TASKS:
                return new FilterAssignableTasks(taskManagementSystemRepository);
            case SORT_ASSIGNABLE_TASKS:
                return new SortAssignableTasks(taskManagementSystemRepository);
            default:
                throw new InvalidUserInput("Invalid command provided.");
        }
    }
}
