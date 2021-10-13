package cisucmanager.enums;

/**
 * @author Sancho Amaral Simoes, 2019217590
 * Universidade de Coimbra, Licenciatura em Engenharia Informática
 * Programação Orientada a Objetos, 2º ano, 1º semestre, 2020/2021
 * 
 * Enum that specifies the allowed investigator types
 */
public enum ImpactFactor {

    // region Enum Values
    A("A"), B("B"), C("C");
    // endregion Enum Values

    // region Constants
    private final String value;
    // endregion Constants
    
    // region Constructors
    private ImpactFactor(String value) {
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
