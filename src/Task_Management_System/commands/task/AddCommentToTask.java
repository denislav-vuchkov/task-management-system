package Task_Management_System.commands.task;

import Task_Management_System.commands.BaseCommand;
import Task_Management_System.core.contracts.TaskManagementSystemRepository;
import Task_Management_System.models.tasks.CommentImpl;
import Task_Management_System.models.tasks.contracts.Comment;
import Task_Management_System.models.tasks.contracts.Task;
import Task_Management_System.models.teams.contracts.User;
import Task_Management_System.utils.FormatHelpers;
import Task_Management_System.utils.ParsingHelpers;
import Task_Management_System.utils.ValidationHelpers;

import java.util.List;

public class AddCommentToTask extends BaseCommand {

    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 3;

    public static final String COMMENT_EVENT = "User %s: Added a comment to %s with ID %d.";

    public AddCommentToTask(TaskManagementSystemRepository repository) {
        super(repository);
    }

    @Override
    protected String executeCommand(List<String> parameters) {

        ValidationHelpers.validateCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        User author = getRepository().findByName(getRepository().getUsers(), parameters.get(0), USER);
        long ID = ParsingHelpers.tryParseLong(parameters.get(1), INVALID_ID);
        Task task = getRepository().findTask(ID);
        String content = parameters.get(2);
        getRepository().validateUserAndTaskFromSameTeam(author.getName(), task.getID());
        Comment comment = new CommentImpl(content, author.getName());

        String event = String.format(COMMENT_EVENT, author.getName(), FormatHelpers.getType(task), task.getID());
        author.log(event);
        task.addComment(comment);
        return event;
    }
}