package seedu.address.commons.events.model;

/** Indicates new notes have been downloaded*/
public class NotesDownloadEvent extends NotesEvent {

    private final String moduleCode;

    public NotesDownloadEvent(String event, String moduleCode) {
        super(event);
        this.moduleCode = moduleCode;
    }

    public String getModuleCode() {
        return moduleCode;
    }
}
