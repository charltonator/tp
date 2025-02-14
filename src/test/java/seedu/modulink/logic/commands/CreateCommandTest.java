package seedu.modulink.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.modulink.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.modulink.testutil.Assert.assertThrows;
import static seedu.modulink.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.modulink.commons.core.GuiSettings;
import seedu.modulink.logic.commands.exceptions.CommandException;
import seedu.modulink.logic.parser.exceptions.ParseException;
import seedu.modulink.model.AddressBook;
import seedu.modulink.model.Model;
import seedu.modulink.model.ModelManager;
import seedu.modulink.model.ReadOnlyAddressBook;
import seedu.modulink.model.ReadOnlyUserPrefs;
import seedu.modulink.model.UserPrefs;
import seedu.modulink.model.person.Email;
import seedu.modulink.model.person.GitHubUsername;
import seedu.modulink.model.person.Name;
import seedu.modulink.model.person.Person;
import seedu.modulink.model.person.Phone;
import seedu.modulink.model.person.StudentId;
import seedu.modulink.model.tag.Mod;
import seedu.modulink.testutil.PersonBuilder;

// import java.util.Arrays;

public class CreateCommandTest {

    private Model model;
    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CreateCommand(null));
    }

    @Test
    public void execute_createPerson_success() throws CommandException {
        Person validPerson = new PersonBuilder().build();
        CommandResult commandResult = new CreateCommand(validPerson).execute(model);
        assertEquals(String.format(CreateCommand.MESSAGE_SUCCESS, validPerson), commandResult.getFeedbackToUser());
    }

    @Test
    public void execute_duplicateIdPerson_throwsCommandException() throws ParseException {
        Person myProfile = new Person(new Name("alex"), new StudentId("A1234589R"), new Phone("12332110"),
                new Email("alex@example.com"), new GitHubUsername("alexyeoh"),
                null, false,
                Set.of(new Mod("CS2100 need member")), true);
        Person otherPerson = new Person(new Name("alexa"), new StudentId("A1234589R"), new Phone("1232110"),
                new Email("alexa@example.com"), new GitHubUsername("alexayeoh"),
                null, false,
                Set.of(new Mod("CS2100 need group")), false);
        model.addPerson(otherPerson);
        CreateCommand createCommand = new CreateCommand(myProfile);
        assertCommandFailure(createCommand, model, CreateCommand.MESSAGE_DUPLICATE_STUDENT_ID);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        CreateCommand addCommand = new CreateCommand(validPerson);
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        assertThrows(CommandException.class,
            CreateCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        CreateCommand createAliceCommand = new CreateCommand(alice);
        CreateCommand createBobCommand = new CreateCommand(bob);

        // same object -> returns true
        assertTrue(createAliceCommand.equals(createAliceCommand));

        // same values -> returns true
        CreateCommand createAliceCommandCopy = new CreateCommand(alice);
        assertTrue(createAliceCommand.equals(createAliceCommandCopy));

        // different types -> returns false
        assertFalse(createAliceCommand.equals(1));

        // null -> returns false
        assertFalse(createAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(createAliceCommand.equals(createBobCommand));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasStudentId(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasStudentIdNotProfile(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addProfile(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Person getProfile() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void refreshFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getPersonList() {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
