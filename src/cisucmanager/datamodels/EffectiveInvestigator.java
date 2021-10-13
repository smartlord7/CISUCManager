package cisucmanager.datamodels;

import cisucmanager.enums.InvestigatorType;

/**
 * @author Sancho Amaral Simoes, 2019217590
 * Universidade de Coimbra, Licenciatura em Engenharia Informática
 * Programação Orientada a Objetos, 2º ano, 1º semestre, 2020/2021
 *
 * Class that encapsulates the information about an effective investigator.
 */
public class EffectiveInvestigator extends Investigator {

    // region Static Variables
    private static int globalEffectiveInvestigatorId = 0;
    // endregion Static Variables

    // region Constants
    private final int effectiveInvestigatorId;
    // endregion Constants

    // region Instance Variables
    private String officeId, deiPhoneNumber;
    // endregion Instance Variables

    // region Constructors
    public EffectiveInvestigator() {
        super();
        investigatorType = InvestigatorType.EFFECTIVE;
        effectiveInvestigatorId = globalEffectiveInvestigatorId++;
    }

    /**
     * @param investigatorId the investigator's id (primary key purpose).
     * @param name the student investigator name.
     * @param email the student investigator DEI email.
     * @param investigationGroup the investigation group in which the student
     * student investigator in question.
     * @param deiPhoneNumber the investigator's DEI phone number.
     * @param officeId investigator's DEI office.
     */
    public EffectiveInvestigator(int investigatorId, String name, String email, InvestigationGroup investigationGroup, String deiPhoneNumber, String officeId) {
        super(investigatorId, name, email, investigationGroup);
        investigatorType = InvestigatorType.EFFECTIVE;
        effectiveInvestigatorId = globalEffectiveInvestigatorId++;
        this.officeId = officeId;
        this.deiPhoneNumber = deiPhoneNumber;
    }
    // endregion Constructors

    // region Public Methods
    // region Getters & Setters
    public int getEffectiveInvestigatorId() {
        return effectiveInvestigatorId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getDeiPhoneNumber() {
        return deiPhoneNumber;
    }

    public void setDeiPhoneNumber(String deiPhoneNumber) {
        this.deiPhoneNumber = deiPhoneNumber;
    }
    // endregion Getters & Setters

    // region Overridden Methods
    @Override
    public String toString() {
        return "\nInvestigator no: " + investigatorId + "\n"
                + "Effective investigator no: " + effectiveInvestigatorId + "\n"
                + "Name: " + name + FIELDSET_DIVIDER
                + "DEI email: " + deiPhoneNumber + "\n"
                + "DEI phone number: " + deiPhoneNumber + "\n"
                + "Office id: " + officeId + "\n"
                + "Investigation group: " + investigationGroup.getName() + "\n";

    }
    // endregion Overridden Methods
    // endregion Public Methods

}
