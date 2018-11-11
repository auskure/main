package seedu.address.logic.commands;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.enrolledmodule.EnrolledModule;
import seedu.address.model.person.*;
import seedu.address.model.tag.Tag;
import seedu.address.ui.testutil.EventsCollectorRule;


import java.util.*;


import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalNotesDownloaded.getTypicalNotesDownloaded;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

/**
 * Contains integration tests (interaction with the Model) for {@code ExportCommand}.
 */
public class ImportCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory;

    //valid person string
    private String theString = "rO0ABXNyACFzZWVkdS5hZGRyZXNzLm1vZGVsLnBlcnNvbi5QZXJzb26cphFHBP9jYQIAB0wAB2FkZHJlc3N0ACRMc2VlZHUvYWRkcmVzcy9tb2RlbC9wZXJzb24vQWRkcmVzcztMAAVlbWFpbHQAIkxzZWVkdS9hZGRyZXNzL21vZGVsL3BlcnNvbi9FbWFpbDtMAA9lbnJvbGxlZE1vZHVsZXN0AA9MamF2YS91dGlsL01hcDtMAARuYW1ldAAhTHNlZWR1L2FkZHJlc3MvbW9kZWwvcGVyc29uL05hbWU7TAAFcGhvbmV0ACJMc2VlZHUvYWRkcmVzcy9tb2RlbC9wZXJzb24vUGhvbmU7TAAEdGFnc3QAD0xqYXZhL3V0aWwvU2V0O0wACXRpbWVzbG90c3EAfgADeHBzcgAic2VlZHUuYWRkcmVzcy5tb2RlbC5wZXJzb24uQWRkcmVzc+68QhHGjRwVAgABTAAFdmFsdWV0ABJMamF2YS9sYW5nL1N0cmluZzt4cHQAHzEyMywgSnVyb25nIFdlc3QgQXZlIDYsICMwOC0xMTFzcgAgc2VlZHUuYWRkcmVzcy5tb2RlbC5wZXJzb24uRW1haWw8Kz3e59rPIwIAAUwABXZhbHVlcQB+AAl4cHQAEWFsaWNlQGV4YW1wbGUuY29tc3IAEWphdmEudXRpbC5UcmVlTWFwDMH2Pi0lauYDAAFMAApjb21wYXJhdG9ydAAWTGphdmEvdXRpbC9Db21wYXJhdG9yO3hwcHcEAAAAAHhzcgAfc2VlZHUuYWRkcmVzcy5tb2RlbC5wZXJzb24uTmFtZeeoo+q1nfMLAgABTAAIZnVsbE5hbWVxAH4ACXhwdAANQWxpY2UgUGF1bGluZXNyACBzZWVkdS5hZGRyZXNzLm1vZGVsLnBlcnNvbi5QaG9uZU1oyTjSxPD8AgABTAAFdmFsdWVxAH4ACXhwdAAIOTQzNTEyNTNzcgARamF2YS51dGlsLkhhc2hTZXS6RIWVlri3NAMAAHhwdwwAAAAQP0AAAAAAAAFzcgAbc2VlZHUuYWRkcmVzcy5tb2RlbC50YWcuVGFndFW2GrgU+qsCAAFMAAd0YWdOYW1lcQB+AAl4cHQAB2ZyaWVuZHN4c3IAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAAAHcIAAAAEAAAAAB4";

    private String nonDuplicate = "rO0ABXNyACFzZWVkdS5hZGRyZXNzLm1vZGVsLnBlcnNvbi5QZXJzb26cphFHBP9jYQIAB0wAB2FkZHJlc3N0ACRMc2VlZHUvYWRkcmVzcy9tb2RlbC9wZXJzb24vQWRkcmVzcztMAAVlbWFpbHQAIkxzZWVkdS9hZGRyZXNzL21vZGVsL3BlcnNvbi9FbWFpbDtMAA9lbnJvbGxlZE1vZHVsZXN0AA9MamF2YS91dGlsL01hcDtMAARuYW1ldAAhTHNlZWR1L2FkZHJlc3MvbW9kZWwvcGVyc29uL05hbWU7TAAFcGhvbmV0ACJMc2VlZHUvYWRkcmVzcy9tb2RlbC9wZXJzb24vUGhvbmU7TAAEdGFnc3QAD0xqYXZhL3V0aWwvU2V0O0wACXRpbWVzbG90c3EAfgADeHBzcgAic2VlZHUuYWRkcmVzcy5tb2RlbC5wZXJzb24uQWRkcmVzc+68QhHGjRwVAgABTAAFdmFsdWV0ABJMamF2YS9sYW5nL1N0cmluZzt4cHQAGzMxMSwgQ2xlbWVudGkgQXZlIDIsICMwMi0yNXNyACBzZWVkdS5hZGRyZXNzLm1vZGVsLnBlcnNvbi5FbWFpbDwrPd7n2s8jAgABTAAFdmFsdWVxAH4ACXhwdAARam9obmRAZXhhbXBsZS5jb21zcgARamF2YS51dGlsLlRyZWVNYXAMwfY+LSVq5gMAAUwACmNvbXBhcmF0b3J0ABZMamF2YS91dGlsL0NvbXBhcmF0b3I7eHBwdwQAAAACdAAGQ1MyMTAxc3IAMXNlZWR1LmFkZHJlc3MubW9kZWwuZW5yb2xsZWRNb2R1bGUuRW5yb2xsZWRNb2R1bGXkkZAWN3IvQAIAAkwAEmVucm9sbGVkTW9kdWxlTmFtZXEAfgAJTAAQbm90ZXNTdG9yYWdlUGF0aHEAfgAJeHBxAH4AEnQAC2hvbWUvQ1MyMTAxdAAHQ1MyMTEzVHNxAH4AE3EAfgAWdAAMaG9tZS9DUzIxMTNUeHNyAB9zZWVkdS5hZGRyZXNzLm1vZGVsLnBlcnNvbi5OYW1l56ij6rWd8wsCAAFMAAhmdWxsTmFtZXEAfgAJeHB0AAhKb2huIERvZXNyACBzZWVkdS5hZGRyZXNzLm1vZGVsLnBlcnNvbi5QaG9uZU1oyTjSxPD8AgABTAAFdmFsdWVxAH4ACXhwdAAIOTg3NjU0MzJzcgARamF2YS51dGlsLkhhc2hTZXS6RIWVlri3NAMAAHhwdwwAAAAQP0AAAAAAAAJzcgAbc2VlZHUuYWRkcmVzcy5tb2RlbC50YWcuVGFndFW2GrgU+qsCAAFMAAd0YWdOYW1lcQB+AAl4cHQACW93ZXNNb25leXNxAH4AIXQAB2ZyaWVuZHN4c3IAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAABncIAAAACAAAAAV0AAN0aHVzcgATamF2YS51dGlsLkFycmF5TGlzdHiB0h2Zx2GdAwABSQAEc2l6ZXhwAAAAD3cEAAAAD3NyACRzZWVkdS5hZGRyZXNzLm1vZGVsLnBlcnNvbi5UaW1lU2xvdHOjFz5OTTj39QIAAUwACHRpbWVzbG90cQB+AAl4cHQABGZyZWVzcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAteHQAA2ZyaXNxAH4AKQAAAA93BAAAAA9zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC14dAADdHVlc3EAfgApAAAAD3cEAAAAD3NxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXh0AANtb25zcQB+ACkAAAAPdwQAAAAPc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAteHQAA3dlZHNxAH4AKQAAAA93BAAAAA9zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC1zcQB+ACtxAH4ALXNxAH4AK3EAfgAtc3EAfgArcQB+AC14eA==";

    @Before
    public void setUp() throws CommandException {
        theString = "rO0ABXNyACFzZWVkdS5hZGRyZXNzLm1vZGVsLnBlcnNvbi5QZXJzb26cphFHBP9jYQIAB0wAB2FkZHJlc3N0ACRMc2VlZHUvYWRkcmVzcy9tb2RlbC9wZXJzb24vQWRkcmVzcztMAAVlbWFpbHQAIkxzZWVkdS9hZGRyZXNzL21vZGVsL3BlcnNvbi9FbWFpbDtMAA9lbnJvbGxlZE1vZHVsZXN0AA9MamF2YS91dGlsL01hcDtMAARuYW1ldAAhTHNlZWR1L2FkZHJlc3MvbW9kZWwvcGVyc29uL05hbWU7TAAFcGhvbmV0ACJMc2VlZHUvYWRkcmVzcy9tb2RlbC9wZXJzb24vUGhvbmU7TAAEdGFnc3QAD0xqYXZhL3V0aWwvU2V0O0wACXRpbWVzbG90c3EAfgADeHBzcgAic2VlZHUuYWRkcmVzcy5tb2RlbC5wZXJzb24uQWRkcmVzc+68QhHGjRwVAgABTAAFdmFsdWV0ABJMamF2YS9sYW5nL1N0cmluZzt4cHQAHzEyMywgSnVyb25nIFdlc3QgQXZlIDYsICMwOC0xMTFzcgAgc2VlZHUuYWRkcmVzcy5tb2RlbC5wZXJzb24uRW1haWw8Kz3e59rPIwIAAUwABXZhbHVlcQB+AAl4cHQAEWFsaWNlQGV4YW1wbGUuY29tc3IAEWphdmEudXRpbC5UcmVlTWFwDMH2Pi0lauYDAAFMAApjb21wYXJhdG9ydAAWTGphdmEvdXRpbC9Db21wYXJhdG9yO3hwcHcEAAAAAHhzcgAfc2VlZHUuYWRkcmVzcy5tb2RlbC5wZXJzb24uTmFtZeeoo+q1nfMLAgABTAAIZnVsbE5hbWVxAH4ACXhwdAANQWxpY2UgUGF1bGluZXNyACBzZWVkdS5hZGRyZXNzLm1vZGVsLnBlcnNvbi5QaG9uZU1oyTjSxPD8AgABTAAFdmFsdWVxAH4ACXhwdAAIOTQzNTEyNTNzcgARamF2YS51dGlsLkhhc2hTZXS6RIWVlri3NAMAAHhwdwwAAAAQP0AAAAAAAAFzcgAbc2VlZHUuYWRkcmVzcy5tb2RlbC50YWcuVGFndFW2GrgU+qsCAAFMAAd0YWdOYW1lcQB+AAl4cHQAB2ZyaWVuZHN4c3IAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAAAHcIAAAAEAAAAAB4";
        model = new ModelManager(getTypicalAddressBook(), getTypicalNotesDownloaded(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), getTypicalNotesDownloaded(), new UserPrefs());
        commandHistory = new CommandHistory();
    }

    @Test
    public void execute_importStringIsInvalid() {

        //corrupting the string
        theString += "a";
        ImportCommand ic = new ImportCommand(theString);
        CommandTestUtil.assertCommandFailure(ic, model, commandHistory, ImportCommand.MESSAGE_FAILED);

    }

    @Test
    public void execute_importNotDuplicate() {


        ImportCommand ic = new ImportCommand(nonDuplicate);
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(new Tag("friends"));
        tagSet.add(new Tag("owesMoney"));
        Map<String, EnrolledModule> enrolledMap = new TreeMap<>();
        enrolledMap.put("CS2101", new EnrolledModule("CS2101"));
        enrolledMap.put("CS2113T", new EnrolledModule("CS2113T"));

        Person p =  new Person(new Name("John Doe"), new Phone("98765432"), new Email("johnd@example.com"), new Address("311, Clementi Ave 2, #02-25"),tagSet, enrolledMap, TimeSlots.initTimeSlots()) ;
        expectedModel.addPerson(p);
        expectedModel.commitAddressBook();

        CommandTestUtil.assertCommandSuccess(ic, model, commandHistory, ImportCommand.MESSAGE_SUCCESS, expectedModel);

    }

    @Test
    public void execute_importDuplicate() {

        ImportCommand ic = new ImportCommand(theString);
        Person p =  expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        System.out.println(p);
        expectedModel.deletePerson(p);
        expectedModel.addPerson(p);
        expectedModel.commitAddressBook();

        CommandTestUtil.assertCommandSuccess(ic, model, commandHistory, ImportCommand.MESSAGE_SUCCESS_OVERWRITE, expectedModel);

    }


}
