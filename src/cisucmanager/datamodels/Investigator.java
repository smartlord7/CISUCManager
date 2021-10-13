package cisucmanager.datamodels;

import cisucmanager.enums.InvestigatorType;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Sancho Amaral Simoes, 2019217590
 * Universidade de Coimbra, Licenciatura em Engenharia Informática
 * Programação Orientada a Objetos, 2º ano, 1º semestre, 2020/2021
 *
 * Class that encapsulates the base information about an investigator.
 */
public class Investigator implements Serializable {

    // region Constants
    protected final String FIELDSET_DIVIDER = "\n====================================================\n";
    // endregion Constants

    // region Instance Variables
    protected InvestigatorType investigatorType;
    protected int investigatorId;
    protected String name, email;
    protected InvestigationGroup investigationGroup;
    // endregion Instance Variables

    // region Constructors
    public Investigator() {

    }

    /**
     * @param investigatorId the investigator's id (primary key purpose).
     * @param nome the investigator's name.
     * @param email the investigator's email.
     * @param investigationGroup the investigation group where the investigator
     * in question is engaged.
     */
    public Investigator(int investigatorId, String nome, String email, InvestigationGroup investigationGroup) {
        this.investigatorId = investigatorId;
        this.name = nome;
        this.email = email;
        this.investigationGroup = investigationGroup;
    }
    // endregion Constructors

    // region Public Methods
    // region Getters & Setters
    public InvestigatorType getInvestigatorType() {
        return investigatorType;
    }

    public void setInvestigatorId(int investigatorId) {
        this.investigatorId = investigatorId;
    }

    public int getInvestigatorId() {
        return investigatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public InvestigationGroup getInvestigationGroup() {
        return investigationGroup;
    }

    public void setInvestigationGroup(InvestigationGroup investigationGroup) {
        this.investigationGroup = investigationGroup;
    }
    // endregion Getters & Setters

    //region Overridden Methods
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.investigatorId;
        hash = 67 * hash + Objects.hashCode(getInvestigatorType());
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Objects.hashCode(this.email);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Investigator other = (Investigator) obj;
        if (this.investigatorId != other.investigatorId) {
            return false;
        }
        if (getInvestigatorType() != other.getInvestigatorType()) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.email, other.email);
    }

    @Override
    public String toString() {
        return "\nInvestigator no: " + investigatorId + "\n"
                + "Investigator type: " + getInvestigatorType().getValue() + FIELDSET_DIVIDER + "\n"
                + "Name: " + name + "\n"
                + "Email: " + email + "\n"
                + "Investigation group: " + investigationGroup.getName() + "\n";
    }
    // endregion Overridden Methods
    // endregion Public Methods
}
