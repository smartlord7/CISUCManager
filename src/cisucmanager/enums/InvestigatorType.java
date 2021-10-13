package cisucmanager.enums;

/**
 * Enum that specifies the allows investigator types
 */
public enum InvestigatorType {

    // region Enum Values
    EFFECTIVE("Effective investigator"), STUDENT("Student investigator");
    // endregion Enum Values

    // region Constants
    private final String value;
    // endregion Constants

    // region Constructors
    private InvestigatorType(String value) {
        this.value = value;
    }
    // endregion Constructors

    // region Public Methods
    // region Getters
    public String getValue() {
        return value;
    }
    // endregion Getters
    // endregion Public Methods

}
