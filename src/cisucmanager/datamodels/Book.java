package cisucmanager.datamodels;

import cisucmanager.enums.ImpactFactor;
import cisucmanager.enums.PublicationType;
import java.util.ArrayList;

/**
 * @author Sancho Amaral Simoes, 2019217590
 * Universidade de Coimbra, Licenciatura em Engenharia Informática
 * Programação Orientada a Objetos, 2º ano, 1º semestre, 2020/2021
 *
 * Class that holds the information about a publication of type Book.
 */
public class Book extends Publication {

    // region Static Variables
    private static int globalBookId = 0;
    // endregion Static Variables

    // region Constants
    protected final int bookId;
    // endregion Constants

    // region Instance Variables
    protected String isbn;
    protected String publishingCompany;
    // endregion Instance Variables

    // region Constructors
    public Book() {
        super();
        bookId = globalBookId++;
        publicationType = PublicationType.BOOK;
    }

    /**
     * @param title the publication title.
     * @param publicationYear the publication year.
     * @param audienceSize the publication audience size.
     * @param summary a small text that summarizes the publication content.
     * @param keywords some words which have a key role in the publication.
     * @param authors the publication authors.
     * @param isbn the conference article book ISBN.
     * @param publishingCompany the conference article book publishing company.
     */
    public Book(String title, int publicationYear, int audienceSize, String summary, ArrayList<String> keywords, ArrayList<Investigator> authors, String isbn, String publishingCompany) {
        super(title, publicationYear, audienceSize, summary, keywords, authors);
        bookId = globalBookId++;
        publicationType = PublicationType.BOOK;
        this.publishingCompany = publishingCompany;
        this.isbn = isbn;
    }
    // endregion Constructors

    // region Public Methods
    // region Getters & Setters
    public String getPublishingCompany() {
        return publishingCompany;
    }

    public void setPublishingCompany(String publishingCompany) {
        this.publishingCompany = publishingCompany;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    // region Overridden Methods
    @Override
    public void setImpactFactor() {
        if (audienceSize >= 10000) {
            impactFactor = ImpactFactor.A;
        } else if (5000 <= audienceSize && audienceSize < 10000) {
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
                + "Book publication no: " + bookId + FIELDSET_DIVIDER
                + "Publication year: " + publicationYear + "\n"
                + "Audience size: " + audienceSize + "\n"
                + "Impact factor: " + impactFactor + "\n"
                + "Authors:" + getFormattedAuthors() + FIELDSET_DIVIDER
                + "Book ISBN: " + isbn + "\n"
                + "Publishing company: " + publishingCompany + FIELDSET_DIVIDER
                + "Summary: " + summary + "\n"
                + "Keywords: " + getFormattedKeywords() + "\n\n", MAX_LINE_SIZE);
    }
    // endregion Overridden Methods
    // endregion Public Methods
}
