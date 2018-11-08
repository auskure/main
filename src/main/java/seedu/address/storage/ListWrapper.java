package seedu.address.storage;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * Wraps a list so it can be properly saved even when in a map.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ListWrapper {

    @XmlElementWrapper
    private ArrayList<XmlAdaptedTimeSlots> list;

    public void setList(ArrayList<XmlAdaptedTimeSlots> list) {
        this.list = list;
    }

    public ArrayList<XmlAdaptedTimeSlots> getList() {
        return list;
    }
}
