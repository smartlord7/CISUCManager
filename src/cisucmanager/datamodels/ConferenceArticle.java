package cisucmanager.datamodels;

import cisucmanager.enums.ImpactFactor;
import cisucmanager.enums.PublicationType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Sancho Amaral Simoes, 2019217590
 * Universidade de Coimbra, Licenciatura em Engenharia Informática
 * Programação Orientada a Objetos, 2º ano, 1º semestre, 2020/2021
 *
 * Class that holds the information about a conference article.
 */
public class ConferenceArticle extends Publication {

    // region Static Variables
    private static int globalConferenceArticleId = 0;
    // endregion Static Variables

    // region Constants
    private final int conferenceArticleId;
    // endregion Constants

    // region Instance Variables
    private String conferenceName, conferencePlace;
    private Date conferenceDate;
    // endregion Instance Variables

    // region Constructors
    public ConferenceArticle() {
        super();
        publicationType = PublicationType.CONFERENCE_ARTICLE;
        conferenceArticleId = globalConferenceArticleId++;
    }

    /**
     * @param title the publication title.
     * @param publicationYear the publication year.
     * @param audienceSize the publication audience size.
     * @param summary a small text that summarizes the publication content.
     * @param keywords some words which have a key role in the publication.
     * @param authors the publication authors.
     * @param conferenceName the name of the conference name from which the
     * conference article in question was written.
     * @param conferencePlace the place of the conference name from which the
     * conference article in question was written.
     * @param conferenceDate the date of the conference name from which the
     * conference article in question was written.
     */
    public ConferenceArticle(String title, int publicationYear, int audienceSize, String summary, ArrayList<String> keywords, ArrayList<Investigator> authors, String conferenceName, String conferencePlace, Date conferenceDate) {
        super(title, publicationYear, audienceSize, summary, keywords, authors);
        conferenceArticleId = globalConferenceArticleId++;
        publicationType = PublicationType.CONFERENCE_ARTICLE;
        this.conferenceName = conferenceName;
        this.conferencePlace = conferencePlace;
        this.conferenceDate = conferenceDate;
    }
    // endregion Constructors

    // region Public Methods
    // region Getters & Setters
    public String getConferenceName() {
        return conferenceName;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }

    public String getConferencePlace() {
        return conferencePlace;
    }

    public void setConferencePlace(String conferencePlace) {
        this.conferencePlace = conferencePlace;
    }

    public Date getConferenceDate() {
        return conferenceDate;
    }

    public void setConferenceDate(Date conferenceDate) {
        this.conferenceDate = conferenceDate;
    }

    // region Overridden Methods
    @Override
    public void setImpactFactor() {
        if (audienceSize >= 500) {
            impactFactor = ImpactFactor.A;
        } else if (200 <= audienceSize && audienceSize < 500) {
            impactFactor = ImpactFactor.B;
        } else {
            impactFactor = ImpactFactor.C;
        }
    }
    // endregion Getters & Setters

    @Override
    public String toString() {
        return breakText("\nPublication no: " + publicationId + "\n"
                + "Publication type: " + publicationType.getValue() + "\n"
                + "Conference article publication no: " + conferenceArticleId + FIELDSET_DIVIDER
                + "Publication year: " + publicationYear + "\n"
                + "Audience size: " + audienceSize + "\n"
                + "Impact factor: " + impactFactor + "\n"
                + "Authors: " + getFormattedAuthors() + FIELDSET_DIVIDER
                + "Conference name: " + conferenceName + "\n"
                + "Conference place: " + conferencePlace + "\n"
                + "Conference date: " + new SimpleDateFormat("dd/MM/yyyy").format(conferenceDate) + FIELDSET_DIVIDER
                + "Summary: " + summary + "\n"
                + "Keywords: " + getFormattedKeywords() + "\n\n", MAX_LINE_SIZE);
    }
    // endregion Overridden Methods
    // endregion Public Methods
}
