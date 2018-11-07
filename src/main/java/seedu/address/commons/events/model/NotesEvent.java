package seedu.address.commons.events.model;

import java.util.Iterator;
import java.util.Set;

import seedu.address.commons.events.BaseEvent;

/** Indicates the notes downloaded has changed*/
public class NotesEvent extends BaseEvent {

    private static final String NO_MODULE = "empty";

    private final String event;
    private final Set<String> moduleCodes;

    public NotesEvent(String event, Set<String> moduleCodes) {
        this.event = event;
        this.moduleCodes = moduleCodes;
    }

    public String getEvent() {
        return event;
    }

    public Set getModuleCodes() {
        return moduleCodes;
    }

    public String getSingleModuleCode() {
        if (moduleCodes.isEmpty()) {
            return NO_MODULE;
        }
        Iterator<String> iterator = moduleCodes.iterator();
        return iterator.next();
    }

    @Override
    public String toString() {
        return "notes manipulated: " + event;
    }
}
