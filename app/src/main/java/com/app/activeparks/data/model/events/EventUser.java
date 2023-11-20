package com.app.activeparks.data.model.events;

public class EventUser {
    private boolean isCoordinator;
    private boolean isLeading;
    private boolean isApproved;

    public boolean isCoordinator() {
        return isCoordinator;
    }

    public void setCoordinator(boolean coordinator) {
        isCoordinator = coordinator;
    }

    public boolean isLeading() {
        return isLeading;
    }

    public void setLeading(boolean leading) {
        isLeading = leading;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}