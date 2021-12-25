package Task.Management.System.commands.list;

import Task.Management.System.commands.BaseCommand;
import Task.Management.System.core.contracts.TaskManagementSystemRepository;
import Task.Management.System.exceptions.InvalidUserInput;
import Task.Management.System.models.tasks.contracts.Task;
import Task.Management.System.utils.FiltrationHelpers;
import Task.Management.System.utils.ValidationHelpers;

import java.util.List;
import java.util.stream.Collectors;

public class ListAllTasksFiltered extends BaseCommand {

    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;

    public ListAllTasksFiltered(TaskManagementSystemRepository repository) {
        super(repository);
    }

    @Override
    protected String executeCommand(List<String> parameters) {
        ValidationHelpers.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        String filter = parameters.get(0).split(":")[0].trim();
        String value = parameters.get(0).split(":")[1].trim();

        if (!filter.equalsIgnoreCase("Title")) {
            throw new InvalidUserInput(String.format(INVALID_FILTER, "All tasks", "title"));
        }

        List<Task> result = FiltrationHelpers.
                filterByTitle(value, getRepository().getTasks());

        if (result.isEmpty()) {
            return String.format(NO_ITEMS_TO_DISPLAY, "tasks");
        }

        return result
                .stream()
                .map(Task::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
