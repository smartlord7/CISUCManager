package cisucmanager.enums;

/**
 * Enum that specifies the allowed publication types.
 */
public enum PublicationType {

    // region Enum Values
    CONFERENCE_ARTICLE("Conference Article"), MAGAZINE_ARTICLE("Magazine Article"), BOOK("Book");
    // endregion Enum Values

    // region Constants
    private final String value;
    // endregion Constants

    // region Constructors
    private PublicationType(String value) {
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
