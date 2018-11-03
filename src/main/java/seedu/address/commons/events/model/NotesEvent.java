package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;

/** Indicates the notes downloaded has changed*/
public class NotesEvent extends BaseEvent {

    protected final String event;

    public NotesEvent(String event) {
        this.event = event;
    }

    public String getEvent(){
        return event;
    }

    @Override
    public String toString() {
        return "notes manipulated: " + event;
    }
}
