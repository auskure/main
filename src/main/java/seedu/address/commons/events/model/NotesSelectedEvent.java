package seedu.address.commons.events.model;

import java.util.Set;

/** Indicates some notes have been selected, for modification*/
public class NotesSelectedEvent extends NotesEvent {
    private final Set<String> moduleCodes;

    public NotesSelectedEvent(String event, Set<String> moduleCodes) {
        super(event);
        this.moduleCodes = moduleCodes;
    }

    public Set getModuleCodes() {
        return moduleCodes;
    }

}
