package Task.Management.System.models.tasks;

import Task.Management.System.exceptions.InvalidUserInput;
import Task.Management.System.models.tasks.contracts.AssignableTask;
import Task.Management.System.models.tasks.contracts.TaskStatus;
import Task.Management.System.models.tasks.enums.Priority;
import Task.Management.System.models.tasks.enums.Tasks;

import static Task.Management.System.models.contracts.EventLogger.DUPLICATE;
import static Task.Management.System.models.contracts.EventLogger.TASK_CHANGE;

public abstract class AssignableTaskImpl extends TaskBase implements AssignableTask {

    public static final String UNASSIGNED = "Unassigned";
    private final Tasks taskType;
    private Priority priority;
    private String assignee;

    public AssignableTaskImpl(long id, Tasks tasksType, String title, String description,
                              Priority priority, TaskStatus status) {
        super(id, tasksType, title, description, status);
        taskType = tasksType;
        setPriority(priority);
        setAssignee(UNASSIGNED);
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public void setPriority(Priority priority) {
        if (this.priority == null) {
            this.priority = priority;
            return;
        }

        if (this.priority.equals(priority)) {
            String event = String.format(DUPLICATE, taskType, getID(), "Priority", this.priority);
            addChangeToHistory(event);
            throw new InvalidUserInput(event);
        }

        addChangeToHistory(String.format(TASK_CHANGE, taskType, getID(), "Priority", this.priority, priority));
        this.priority = priority;
    }

    @Override
    public String getAssignee() {
        return assignee;
    }

    @Override
    public void setAssignee(String assignee) {
        if (this.assignee == null) {
            this.assignee = assignee;
            return;
        }

        if (this.assignee.equals(assignee)) {
            String event = String.format(DUPLICATE, taskType, getID(), "Assignee", this.assignee);
            addChangeToHistory(event);
            throw new InvalidUserInput(event);
        }

        addChangeToHistory(String.format(TASK_CHANGE, taskType, getID(), "Assignee", this.assignee, assignee));
        this.assignee = assignee;
    }

    @Override
    public void unAssign() {
        setAssignee(UNASSIGNED);
    }
}