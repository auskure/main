package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.model.tag.Tag;
/**
 * Tests that a {@code Person}'s {@code Tag} does not contain merged or self tag
 */
public class IsNotSelfOrMergedPredicate implements Predicate<Person> {

    public IsNotSelfOrMergedPredicate() {

    }

    @Override
    public boolean test(Person person) {
        for (Tag it : person.getTags()) {
            if (it.toString().equalsIgnoreCase("[merged]")
                    || it.toString().equalsIgnoreCase("[self]")) {
                return false;
            }
        }
        return true;
    }

}
