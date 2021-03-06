package main.utils;

import main.exceptions.InvalidUserInput;
import main.models.tasks.contracts.AssignableTask;
import main.models.tasks.contracts.Task;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static main.commands.BaseCommand.INVALID_ID;

public class ListHelpers {

    public static final String INVALID_FILTER = "%s can only be filtered by %s.";

    public static <T extends AssignableTask, E extends Enum<E>> List<T> filterTasks
            (String criterion, List<T> tasks, Class<E> type) {

        String filter = criterion.split(":")[0].trim();
        String value = criterion.split(":")[1].trim();

        switch (filter.toUpperCase()) {
            case "STATUS":
                return filterByStatus(value, tasks, type);
            case "ASSIGNEE":
                return filterByAssignee(value, tasks);
            default:
                throw new InvalidUserInput(String.format(INVALID_FILTER, "Bugs and Stories", "status or assignee"));
        }
    }

    public static <T extends AssignableTask> List<T> filterTasks
            (String criterion, List<T> tasks) {

        String filter = criterion.split(":")[0].trim();
        String value = criterion.split(":")[1].trim();

        switch (filter.toUpperCase()) {
            case "TITLE":
                return filterByTitle(value, tasks);
            case "ASSIGNEE":
                return filterByAssignee(value, tasks);
            default:
                throw new InvalidUserInput(String.format(INVALID_FILTER, "Assignable tasks", "title or assignee"));
        }
    }

    public static <T extends Task> List<T> filterByTitle
            (String filter, List<T> tasks) {

        Pattern title = Pattern.compile(Pattern.quote(filter), Pattern.CASE_INSENSITIVE);
        return tasks
                .stream()
                .filter(e -> title.matcher(e.getTitle()).find())
                .collect(Collectors.toList());
    }

    public static <T extends Task, E extends Enum<E>> List<T> filterByStatus
            (String filter, List<T> tasks, Class<E> type) {

        E status = ParsingHelpers.tryParseEnum(filter, type);
        return tasks
                .stream()
                .filter(e -> e.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public static <T extends AssignableTask> List<T> filterByAssignee
            (String criterion, List<T> tasks) {

        Pattern assignee = Pattern.compile(Pattern.quote(criterion), Pattern.CASE_INSENSITIVE);
        return tasks
                .stream()
                .filter(e -> assignee.matcher(e.getAssignee()).find())
                .collect(Collectors.toList());
    }

    public static <T extends Task> String sortTasks(Comparator<T> criterion, List<T> tasks) {
        return tasks
                .stream()
                .sorted(criterion)
                .map(Task::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public static <T extends Task> T findTask(long taskID, List<T> tasks) {
        return tasks.stream()
                .filter(task -> task.getID() == taskID)
                .findAny()
                .orElseThrow(() -> new InvalidUserInput(INVALID_ID));
    }

}
