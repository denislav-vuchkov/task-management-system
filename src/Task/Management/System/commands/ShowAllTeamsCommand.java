package Task.Management.System.commands;

import Task.Management.System.core.contracts.TaskManagementSystemRepository;
import Task.Management.System.models.teams.contracts.Team;
import Task.Management.System.utils.ValidationHelpers;

import java.util.List;

public class ShowAllTeamsCommand extends BaseCommand {

    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 0;

    public ShowAllTeamsCommand(TaskManagementSystemRepository repository) {
        super(repository);
    }

    @Override
    protected String executeCommand(List<String> parameters) {
        ValidationHelpers.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
        List<Team> teams = getRepository().getTeams();
        if (teams.isEmpty()) {
            return String.format(NO_ITEMS_TO_DISPLAY, "teams");
        }
        StringBuilder output = new StringBuilder();
        teams.forEach(output::append);
        return output.toString().trim();
    }
}
