package main.models.tasks;

import main.models.tasks.contracts.AssignableTask;
import main.models.tasks.contracts.TaskStatus;
import main.models.tasks.enums.Priority;
import main.models.tasks.enums.Tasks;

public abstract class AssignableTaskImpl extends TaskBase implements AssignableTask {

    public static final String UNASSIGNED = "Unassigned";
    public static final String PRIORITY_FIELD = "Priority";
    public static final String ASSIGNEE_FIELD = "Assignee";

    private Priority priority;
    private String assignee;

    public AssignableTaskImpl(
            long id, Tasks tasksType, String title, String description, Priority priority, TaskStatus status) {
        super(id, tasksType, title, description, status);
        setPriority(priority);
        setAssignee(UNASSIGNED);
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public void setPriority(Priority priority) {
        checkForDuplication(getPriority(), priority, PRIORITY_FIELD);
        this.priority = priority;
    }

    @Override
    public String getAssignee() {
        return assignee;
    }

    @Override
    public void setAssignee(String assignee) {
        checkForDuplication(getAssignee(), assignee, ASSIGNEE_FIELD);
        this.assignee = assignee;
    }

    @Override
    public void unAssign() {
        setAssignee(UNASSIGNED);
    }
}