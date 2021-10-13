package cisucmanager.datamodels;

import cisucmanager.enums.ImpactFactor;
import java.util.ArrayList;

/**
 * @author Sancho Amaral Simoes, 2019217590
 * Universidade de Coimbra, Licenciatura em Engenharia Informática
 * Programação Orientada a Objetos, 2º ano, 1º semestre, 2020/2021
 *
 * Class that holds the information about a conference article book.
 */
public class ConferenceArticleBook extends Book {

    // region Static Variables
    private static int globalConferenceArticleBookId = 0;
    // endregion Static Variables

    // region Constants
    private final int conferenceArticleBookId;
    // endregion Constants

    // region Instance Variables
    private String conferenceName;
    private int numberOfArticles;
    // endregion Instance Variables

    // region Constructors
    public ConferenceArticleBook() {
        conferenceArticleBookId = globalConferenceArticleBookId++;
    }

    /**
     * @param title the publication's title.
     * @param publicationYear the publications year.
     * @param audienceSize the publications audience size.
     * @param summary a small text that summarizes the publication content.
     * @param keywords some words which have a key role in the publication.
     * @param authors the publication's author's.
     * @param isbn the conference article book ISBN.
     * @param publishingCompany the conference article book publishing company.
     * @param conferenceName the name of the conference name from which the
     * conference articles integrating this book were written.
     * @param numberOfArticles the number of articles included in this
     * conference article book.
     */
    public ConferenceArticleBook(String title, int publicationYear, int audienceSize, String summary, ArrayList<String> keywords, ArrayList<Investigator> authors, String isbn, String publishingCompany, String conferenceName, int numberOfArticles) {
        super(title, publicationYear, audienceSize, summary, keywords, authors, isbn, publishingCompany);
        conferenceArticleBookId = globalConferenceArticleBookId++;
        this.conferenceName = conferenceName;
        this.numberOfArticles = numberOfArticles;
    }
    // endregion Constructors

    //region Public Methods
    //region Getters & Setters
    public String getConferenceName() {
        return conferenceName;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }

    public int getArticleNumber() {
        return numberOfArticles;
    }

    public void setArticleNumber(int articleNumber) {
        this.numberOfArticles = articleNumber;
    }

    //region Overridden Methods
    @Override
    public void setImpactFactor() {
        if (audienceSize >= 7500) {
            impactFactor = ImpactFactor.A;
        } else if (2500 <= audienceSize && audienceSize < 7500) {
            impactFactor = ImpactFactor.B;
        } else {
            impactFactor = ImpactFactor.C;
        }
    }
    //endregion Getters & Setters

    @Override
    public String toString() {
        return breakText("\nPublication no: " + publicationId + "\n"
                + "Publication type: " + publicationType.getValue() + "\n"
                + "Book publication no: " + bookId + "\n"
                + "Conference article book publication no: " + conferenceArticleBookId + FIELDSET_DIVIDER
                + "Publication year: " + publicationYear + "\n"
                + "Audience size: " + audienceSize + "\n"
                + "Impact factor: " + impactFactor + "\n"
                + "Authors: " + getFormattedAuthors() + FIELDSET_DIVIDER
                + "Book ISBN: " + isbn + "\n"
                + "Publishing company: " + publishingCompany + FIELDSET_DIVIDER
                + "Conference name: " + conferenceName + "\n"
                + "Number of conference articles: " + numberOfArticles + FIELDSET_DIVIDER
                + "Summary: " + summary + "\n"
                + "Keywords: " + getFormattedKeywords() + "\n\n", MAX_LINE_SIZE);

    }
    //endregion Overridden Methods
    //region Public Methods
}
