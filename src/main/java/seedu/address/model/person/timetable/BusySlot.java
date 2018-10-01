package seedu.address.model.person.timetable;

public class BusySlot {
    private String module;
    private int startTime;
    private int endTime;

    public BusySlot(String module, int startTime, int endTime) {
        this.module = module;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

}