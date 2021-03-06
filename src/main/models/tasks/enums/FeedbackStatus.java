package main.models.tasks.enums;

import main.models.tasks.contracts.TaskStatus;

public enum FeedbackStatus implements TaskStatus {
    NEW, UNSCHEDULED, SCHEDULED, DONE;

    @Override
    public String toString() {
        switch (this) {
            case NEW:
                return "New";
            case UNSCHEDULED:
                return "Unscheduled";
            case SCHEDULED:
                return "Scheduled";
            case DONE:
                return "Done";
            default:
                throw new IllegalArgumentException("Unreachable exception: Feedback Status Enum.");
        }
    }

}
