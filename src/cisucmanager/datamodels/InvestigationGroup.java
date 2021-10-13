package cisucmanager.datamodels;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Sancho Amaral Simoes, 2019217590
 * Universidade de Coimbra, Licenciatura em Engenharia Informática
 * Programação Orientada a Objetos, 2º ano, 1º semestre, 2020/2021
 *
 * Class that encapsulates the base information about an investigation group.
 */
public class InvestigationGroup implements Serializable {

    // region Static Variables
    private static int globalInvestigationGroupId = 0;
    // endregion Static Variables

    // region Constants
    private final int investigationGroupId;
    // endregion Constants

    // region Instance Variables
    private String name, acronym;
    private Investigator responsibleInvestigator;
    private ArrayList<Investigator> memberList;
    // endregion Instance Variables

    // region Constructors
    public InvestigationGroup() {
        investigationGroupId = globalInvestigationGroupId++;
        memberList = new ArrayList<>();
    }

    /**
     * @param name the investigation group name.
     * @param acronym the investigation group acronym.
     * @param responsibleInvestigator the investigation who is in charge of the
     * investigation group in question.
     * @param memberList the investigators that belong to this investigation
     * group.
     */
    public InvestigationGroup(String name, String acronym, Investigator responsibleInvestigator, ArrayList<Investigator> memberList) {
        investigationGroupId = globalInvestigationGroupId++;
        this.name = name;
        this.acronym = acronym;
        this.responsibleInvestigator = responsibleInvestigator;
        this.memberList = memberList;
    }
    // endregion Constructors

    // region Public Methods
    public int getInvestigationGroupId() {
        return investigationGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public Investigator getResponsibleInvestigator() {
        return responsibleInvestigator;
    }

    public void setResponsibleInvestigator(Investigator responsibleInvestigator) {
        this.responsibleInvestigator = responsibleInvestigator;
    }

    public ArrayList<Investigator> getMemberList() {
        return memberList;
    }

    public void setMemberList(ArrayList<Investigator> memberList) {
        this.memberList = memberList;
    }

    // region Overridden Methods
    @Override
    public String toString() {
        return "\nInvestigation group no: " + investigationGroupId + "\n"
                + "Name: " + name + "\n"
                + "Acronym: " + acronym + "\n"
                + "Responsible investigator: " + responsibleInvestigator.getName() + "\n"
                + "Investigation members: " + getFormattedMembers() + "\n";
    }
    // endregion Overridden Methods
    // endregion Public Methods

    // region Private Methods
    protected String getFormattedMembers() {
        String out = "";
        for (int i = 0; i < memberList.size(); i++) {
            out += memberList.get(i).getName();
            if (i != memberList.size() - 1) {
                out += ", ";
            }
        }
        return out + "\n";
    }

    // endregion Private Methods
}
