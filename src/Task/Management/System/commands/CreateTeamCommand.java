package Task.Management.System.commands;

import Task.Management.System.core.contracts.TaskManagementSystemRepository;
import Task.Management.System.models.teams.TeamImpl;
import Task.Management.System.models.teams.contracts.Team;
import Task.Management.System.utils.ValidationHelpers;

import java.util.List;

public class CreateTeamCommand extends BaseCommand {

    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;
    public static final String TEAM_ADDED_SUCCESSFULLY = "Team %s created successfully.";

    public CreateTeamCommand(TaskManagementSystemRepository repository) {
        super(repository);
    }

    @Override
    protected String executeCommand(List<String> parameters) {
        ValidationHelpers.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
        getRepository().validateUniqueTeamName(parameters.get(0));
        Team team = new TeamImpl(parameters.get(0));
        getRepository().addTeam(team);
        return String.format(TEAM_ADDED_SUCCESSFULLY, team.getName());
    }
}