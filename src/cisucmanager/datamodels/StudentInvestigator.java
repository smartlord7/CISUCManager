package cisucmanager.datamodels;

import cisucmanager.enums.InvestigatorType;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Sancho Amaral Simoes, 2019217590
 * Universidade de Coimbra, Licenciatura em Engenharia Informática
 * Programação Orientada a Objetos, 2º ano, 1º semestre, 2020/2021
 * 
 * Class that encapsulates the information about a student investigator.
 */
public class StudentInvestigator extends Investigator {

    // region Static Variables
    private static int globalStudentInvestigatorId = 0;
    // endregion Static Variables

    // region Constants
    private final int studentInvestigatorId;
    // endregion Constants

    // region Instance Variables
    private String thesisTitle;
    private Date expectedPhdCompletionDate;
    private Investigator advisorInvestigator;
    // endregion Instance Variables

    // region Constructors
    public StudentInvestigator() {
        super();
        investigatorType = InvestigatorType.STUDENT;
        studentInvestigatorId = globalStudentInvestigatorId++;
    }

    /**
     *
     * @param investigatorId the investigator's id (primary key purpose).
     * @param name the student investigator name.
     * @param email the student investigator DEI email.
     * @param investigationGroup the investigation group in which the student
     * student investigator in question.
     * @param thesisTitle the student investigator thesis title.
     * @param advisorInvestigator the investigator that gives orientation to the
     * investigator is engaged.
     * @param expectedPhdCompletionDate the expected Phd completion date of the
     * student investigator.
     */
    public StudentInvestigator(int investigatorId, String name, String email, InvestigationGroup investigationGroup, String thesisTitle, Investigator advisorInvestigator, Date expectedPhdCompletionDate) {
        super(investigatorId, name, email, investigationGroup);
        investigatorType = InvestigatorType.STUDENT;
        studentInvestigatorId = globalStudentInvestigatorId++;
        this.thesisTitle = thesisTitle;
        this.expectedPhdCompletionDate = expectedPhdCompletionDate;
        this.advisorInvestigator = advisorInvestigator;
    }
    // endregion Constructors

    // region Public Methods
    // region Getters & Setters
    public int getStudentInvestigatorId() {
        return studentInvestigatorId;
    }

    public String getThesisTitle() {
        return thesisTitle;
    }

    public Date getExpectedPhdCompletionDate() {
        return expectedPhdCompletionDate;
    }

    public void setExpectedPhdCompletionDate(Date expectedPhdCompletionDate) {
        this.expectedPhdCompletionDate = expectedPhdCompletionDate;
    }

    public void setThesisTitle(String thesisTitle) {
        this.thesisTitle = thesisTitle;
    }

    public Investigator getAdvisorInvestigator() {
        return advisorInvestigator;
    }

    public void setAdvisorInvestigator(Investigator advisorInvestigator) {
        this.advisorInvestigator = advisorInvestigator;
    }

    // endregion Getters & Setters
    // region Overridden Methods
    @Override
    public String toString() {
        return "\nInvestigator no: " + investigatorId + "\n"
                + "Investigator type: " + getInvestigatorType().getValue() + "\n"
                + "Student investigator no: " + studentInvestigatorId + FIELDSET_DIVIDER
                + "Name: " + name + "\n"
                + "DEI email: " + email + "\n"
                + "Investigation group: " + email + FIELDSET_DIVIDER
                + "Advisor investigator: " + advisorInvestigator.getName() + "\n"
                + "Thesis title: " + thesisTitle + "\n"
                + "Expected PhD completion date: " + new SimpleDateFormat("dd/MM/yyyy").format(expectedPhdCompletionDate) + "\n";
    }
    // endregion Overridden Methods
    // endregion Public Methods

}
