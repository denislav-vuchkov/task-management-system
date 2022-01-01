package Task_Management_System.models.teams.contracts;

import Task_Management_System.models.logger.contracts.Loggable;
import Task_Management_System.models.tasks.contracts.Task;
import Task_Management_System.models.teams.contracts.subcontracts.Nameable;
import Task_Management_System.models.teams.contracts.subcontracts.TaskHandler;

public interface Board extends Loggable, Nameable, TaskHandler<Task> {

}