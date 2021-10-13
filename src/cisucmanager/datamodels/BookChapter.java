package cisucmanager.datamodels;

import java.util.ArrayList;

/**
 * @author Sancho Amaral Simoes, 2019217590
 * Universidade de Coimbra, Licenciatura em Engenharia Informática
 * Programação Orientada a Objetos, 2º ano, 1º semestre, 2020/2021
 *
 * Class that encapsulates the information about a book chapter.
 */
public class BookChapter extends Book {

    // region Static Variables
    private static int globalBookChapterId = 0;
    // endregion Static Variables

    // region Constants
    private final int bookChapterId;
    // endregion Constants

    // region Instance Variables
    private String chapterName;
    private int beginPage, endPage;
    // endregion Instance Variables

    // region Constructors
    public BookChapter() {
        super();
        bookChapterId = globalBookChapterId++;
    }

    /**
     *
     * @param title the publication title.
     * @param publicationYear the publication year.
     * @param audienceSize the publication audience size.
     * @param summary a small text that summarizes the publication content.
     * @param keywords some words which have a key role in the publication.
     * @param authors the publication authors.
     * @param isbn the book's ISBN.
     * @param publishingCompany the book's publishing company.
     * @param chapterName the book chapter name.
     * @param beginPage the begin page of the chapter.
     * @param endPage the end page of the chapter.
     */
    public BookChapter(String title, int publicationYear, int audienceSize, String summary, ArrayList<String> keywords, ArrayList<Investigator> authors, String isbn, String publishingCompany, String chapterName, int beginPage, int endPage) {
        super(title, publicationYear, audienceSize, summary, keywords, authors, isbn, publishingCompany);
        bookChapterId = globalBookChapterId++;
        this.chapterName = chapterName;
        this.beginPage = beginPage;
        this.endPage = endPage;
    }
    // endregion Constructors

    // region Public Methods
    // region Getters & Setters
    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public int getBeginPage() {
        return beginPage;
    }

    public void setBeginPage(int beginPage) {
        this.beginPage = beginPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }
    // region Getters & Setters

    // endregion Overridden Methods
    @Override
    public String toString() {
        return breakText("\nPublication no: " + publicationId + "\n"
                + "Publication type: " + publicationType.getValue() + "\n"
                + "Book publication no: " + bookId + "\n"
                + "Book chapter no: " + bookChapterId + FIELDSET_DIVIDER
                + "Publication year: " + publicationYear + "\n"
                + "Audience size: " + audienceSize + "\n"
                + "Impact factor: " + impactFactor + "\n"
                + "Authors: " + getFormattedAuthors() + FIELDSET_DIVIDER
                + "Book ISBN: " + isbn + "\n"
                + "Publishing company: " + publishingCompany + FIELDSET_DIVIDER
                + "Book chapter name: " + chapterName + "\n"
                + "Chapter begin page: " + beginPage + "\n"
                + "Chapter end page: " + endPage + FIELDSET_DIVIDER
                + "Summary: " + summary + "\n"
                + "Keywords: " + getFormattedKeywords() + "\n\n", MAX_LINE_SIZE);
    }
    // endregion Overridden Methods
    // endregion Public Methods

}
