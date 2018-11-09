package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ENROLLED_MODULE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.*;

import javafx.collections.ObservableList;
import seedu.address.commons.core.Messages;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.enrolledmodule.EnrolledModule;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.IsNotSelfOrMergedPredicate;
import seedu.address.model.person.IsSelfPredicate;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.TimeSlots;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]..." + "[" + PREFIX_ENROLLED_MODULE + "MODULE]..." + "\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_EDIT_SELF_SUCCESS = "You have edited your own contact card.";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final String index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index                of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(String index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        Person personToEdit;
        if (index.equalsIgnoreCase("self")) {
            lastShownList = ((ObservableList<Person>) lastShownList).filtered(new IsSelfPredicate());
            personToEdit = lastShownList.get(0);
        } else {
            lastShownList = ((ObservableList<Person>) lastShownList).filtered(new IsNotSelfOrMergedPredicate());

            if (Integer.parseInt(index) > lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            personToEdit = lastShownList.get(Integer.parseInt(index) - 1);
        }
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.updatePerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitAddressBook();
        if (index.equalsIgnoreCase("self")) {
            return new CommandResult(MESSAGE_EDIT_SELF_SUCCESS);
        } else {
            return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedPerson));
        }
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Set<Tag> updatedTags = personToEdit.getTags();
        Map<String, EnrolledModule> updatedEnrolledModules = editPersonDescriptor.getEnrolledModules()
                .orElse(personToEdit.getEnrolledModules());
        Map<String, List<TimeSlots>> updatedTimeSlots = personToEdit.getTimeSlots();


        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags,
                updatedEnrolledModules, updatedTimeSlots);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Map<String, EnrolledModule> enrolledModules;
        private Map<String, List<TimeSlots>> timeslots;


        public EditPersonDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setEnrolledModules(toCopy.enrolledModules);
            setTimeSlots(toCopy.timeslots);

        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, enrolledModules);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code enrolledModules} to this object's {@code enrolledModules}.
         * A defensive copy of {@code enrolledModules} is used internally.
         */

        public void setEnrolledModules(Map<String, EnrolledModule> enrolledModules) {
            if (enrolledModules != null) {
                this.enrolledModules = enrolledModules;
            } else {
                this.enrolledModules = new TreeMap<>();
            }
        }

        /**
         * Returns an unmodifiable map of enrolled modules, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code enrolledModules} is null.
         */

        public Optional<Map<String, EnrolledModule>> getEnrolledModules() {
            if (enrolledModules != null) {
                return Optional.of(Collections.unmodifiableMap(enrolledModules));
            } else {
                return Optional.empty();
            }
        }

        public void setTimeSlots(Map<String, List<TimeSlots>> timeslots) {
            if (timeslots != null) {
                this.timeslots = timeslots;
            } else {
                this.timeslots = TimeSlots.initTimeSlots();
            }
        }

        public Map<String, List<TimeSlots>> getTimeSlots() {
            return timeslots;
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            // state check
            EditPersonDescriptor e = (EditPersonDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getEnrolledModules().equals(e.getEnrolledModules());
        }
    }
}
