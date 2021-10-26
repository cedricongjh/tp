package seedu.smartnus.model;

import static java.util.Objects.requireNonNull;
import static seedu.smartnus.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.smartnus.commons.core.GuiSettings;
import seedu.smartnus.commons.core.LogsCenter;
import seedu.smartnus.commons.core.theme.Theme;
import seedu.smartnus.model.note.Note;
import seedu.smartnus.model.question.Question;

/**
 * Represents the in-memory model of SmartNus data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final SmartNus smartNus;
    private final UserPrefs userPrefs;
    private final FilteredList<Question> filteredQuestions;
    private final FilteredList<Note> filteredNotes;
    private final FilteredList<Question> filteredQuizQuestions;


    /**
     * Initializes a ModelManager with the given smartNus and userPrefs.
     */
    public ModelManager(ReadOnlySmartNus smartNus, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(smartNus, userPrefs);

        logger.fine("Initializing with smartNus: " + smartNus + " and user prefs " + userPrefs);

        this.smartNus = new SmartNus(smartNus);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredQuestions = new FilteredList<>(this.smartNus.getQuestionList());
        filteredNotes = new FilteredList<>(this.smartNus.getNoteList());
        filteredQuizQuestions = new FilteredList<>(this.smartNus.getQuestionList());
    }

    public ModelManager() {
        this(new SmartNus(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getSmartNusFilePath() {
        return userPrefs.getSmartNusFilePath();
    }

    @Override
    public void setSmartNusFilePath(Path smartNusFilePath) {
        requireNonNull(smartNusFilePath);
        userPrefs.setSmartNusFilePath(smartNusFilePath);
    }

    @Override
    public Theme getTheme() {
        return userPrefs.getTheme();
    }

    @Override
    public void setTheme(Theme theme) {
        requireNonNull(theme);
        userPrefs.setTheme(theme);
    }

    //=========== SmartNus ================================================================================

    @Override
    public void setSmartNus(ReadOnlySmartNus smartNus) {
        this.smartNus.resetData(smartNus);
    }

    @Override
    public ReadOnlySmartNus getSmartNus() {
        return smartNus;
    }

    @Override
    public boolean hasQuestion(Question question) {
        requireNonNull(question);
        return smartNus.hasQuestion(question);
    }

    @Override
    public void deleteQuestion(Question target) {
        smartNus.removeQuestion(target);
    }

    @Override
    public void deleteNote(Note target) {
        smartNus.removeNote(target);
    }

    @Override
    public void addQuestion(Question question) {
        smartNus.addQuestion(question);
        updateFilteredQuestionList(PREDICATE_SHOW_ALL_QUESTIONS);
    }

    @Override
    public void addNote(Note note) {
        smartNus.addNote(note);
        updateFilteredNoteList(PREDICATE_SHOW_ALL_NOTES);
    }

    @Override
    public void setQuestion(Question target, Question editedQuestion) {
        requireAllNonNull(target, editedQuestion);

        smartNus.setQuestion(target, editedQuestion);
    }

    @Override
    public void setNote(Note target, Note editedNote) {
        requireAllNonNull(target, editedNote);

        smartNus.setNote(target, editedNote);
    }

    //=========== Filtered Question List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Question} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Question> getFilteredQuestionList() {
        return filteredQuestions;
    }

    @Override
    public ObservableList<Question> getFilteredQuizQuestionList() {
        return filteredQuizQuestions;
    }

    @Override
    public void updateFilteredQuestionList(Predicate<Question> predicate) {
        requireNonNull(predicate);
        filteredQuestions.setPredicate(predicate);
    }

    //=========== Filtered Note List Accessors =============================================================

    @Override
    public ObservableList<Note> getFilteredNoteList() {
        return filteredNotes;
    }

    @Override
    public void updateFilteredNoteList(Predicate<Note> predicate) {
        requireNonNull(predicate);
        filteredNotes.setPredicate(predicate);
    }

    @Override
    public void updateFilteredQuizQuestionList(Predicate<Question> predicate) {
        requireNonNull(predicate);
        filteredQuizQuestions.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return smartNus.equals(other.smartNus)
                && userPrefs.equals(other.userPrefs)
                && filteredQuestions.equals(other.filteredQuestions);
    }

}
