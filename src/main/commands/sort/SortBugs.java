package main.commands.sort;

import main.commands.BaseCommand;
import main.core.contracts.TaskManagementSystemRepository;
import main.exceptions.InvalidUserInput;
import main.models.tasks.contracts.Bug;
import main.utils.ListHelpers;
import main.utils.ValidationHelpers;

import java.util.Comparator;
import java.util.List;

public class SortBugs extends BaseCommand {

    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;

    public SortBugs(TaskManagementSystemRepository repository) {
        super(repository);
    }

    @Override
    protected String executeCommand(List<String> parameters) {
        ValidationHelpers.validateCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
        List<Bug> bugs = getRepository().getBugs();
        if (bugs.isEmpty()) {
            return String.format(NO_ITEMS_TO_DISPLAY, "bugs");
        }
        String criterion = parameters.get(0);
        switch (criterion.toUpperCase()) {
            case "TITLE":
                return ListHelpers.sortTasks(Comparator.comparing(Bug::getTitle), bugs);
            case "PRIORITY":
                return ListHelpers.sortTasks(Comparator.comparing(Bug::getPriority), bugs);
            case "SEVERITY":
                return ListHelpers.sortTasks(Comparator.comparing(Bug::getSeverity), bugs);
            default:
                throw new InvalidUserInput(String.format(INVALID_SORT_PARAMETER, "Bugs", "title, priority or severity"));
        }
    }
}
