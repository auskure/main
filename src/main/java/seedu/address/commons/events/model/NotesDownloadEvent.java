package seedu.address.commons.events.model;

/** Indicates new notes have been downloaded*/
public class NotesDownloadEvent extends NotesEvent {

    private final String moduleName;

    public NotesDownloadEvent(String event, String moduleName){
        super(event);
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }
}
