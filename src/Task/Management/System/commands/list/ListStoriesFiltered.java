package Task.Management.System.commands.list;

import Task.Management.System.commands.BaseCommand;
import Task.Management.System.core.contracts.TaskManagementSystemRepository;
import Task.Management.System.exceptions.InvalidUserInput;
import Task.Management.System.models.tasks.contracts.Story;
import Task.Management.System.models.tasks.enums.StoryStatus;
import Task.Management.System.utils.ParsingHelpers;
import Task.Management.System.utils.ValidationHelpers;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ListStoriesFiltered extends BaseCommand {

    public static final int MIN_ARGUMENTS = 1;
    public static final int MAX_ARGUMENTS = 2;
    public static final String INVALID_FILTERS_COUNT =
            String.format("Filters must be no less than %d and no more than %d.", MIN_ARGUMENTS, MAX_ARGUMENTS);
    public static final String NO_STORIES_EXIST = "No stories to display.";
    public static final String INVALID_PARAMETER_FOR_SORTING = "Story can only be filtered by status or assignee.";

    public ListStoriesFiltered(TaskManagementSystemRepository repository) {
        super(repository);
    }

    @Override
    protected String executeCommand(List<String> parameters) {
        ValidationHelpers.validateIntRange(parameters.size(),
                MIN_ARGUMENTS, MAX_ARGUMENTS, INVALID_FILTERS_COUNT);

        List<Story> result = getFilteredList(parameters.get(0), getRepository().getStories());
        if (parameters.size() == MAX_ARGUMENTS) {
            result = getFilteredList(parameters.get(1), result);
        }

        if (result.isEmpty()) {
            return NO_STORIES_EXIST;
        }

        return result
                .stream()
                .map(Story::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private List<Story> getFilteredList(String criterion, List<Story> stories) {

        String filter = criterion.split(":")[0].trim();
        String value = criterion.split(":")[1];

        switch (filter.toUpperCase()) {
            case "STATUS":
                StoryStatus status = ParsingHelpers.tryParseEnum(value, StoryStatus.class);
                return stories
                        .stream()
                        .filter(e -> e.getStatus().equals(status.toString()))
                        .collect(Collectors.toList());
            case "ASSIGNEE":
                Pattern assignee = Pattern.compile(Pattern.quote(value), Pattern.CASE_INSENSITIVE);
                return stories
                        .stream()
                        .filter(e -> assignee.matcher(e.getAssignee()).find())
                        .collect(Collectors.toList());
            default:
                throw new InvalidUserInput(INVALID_PARAMETER_FOR_SORTING);
        }
    }
}
