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
 * Class that encapsulates the information about a magazine article.
 */
public class MagazineArticle extends Publication {

    // region Static Variables
    private static int globalMagazineArticleId = 0;
    // endregion Static Variables

    // region Constants
    private final int magazineArticleId;
    // endregion Constants

    // region Instance Variables
    private String magazineName, magazineId;
    private Date magazineDate;
    // endregion Instance Variables

    // region Constructors
    public MagazineArticle() {
        super();
        magazineArticleId = globalMagazineArticleId++;
        publicationType = PublicationType.MAGAZINE_ARTICLE;
    }

    /**
     *
     * @param title the publication's title.
     * @param publicationYear the publication's year.
     * @param audienceSize the publication's audience size.
     * @param summary a small text that summarizes the publication content.
     * @param keywords some words which have a key role in the publication.
     * @param authors the publication's author's.
     * @param magazineId the number of the magazine where the current magazine
     * article is contained.
     * @param magazineName the name of the magazine where the current magazine
     * article is contained.
     * @param magazineDate the date of the magazine where the current magazine
     * article is contained.
     */
    public MagazineArticle(String title, int publicationYear, int audienceSize, String summary, ArrayList<String> keywords, ArrayList<Investigator> authors, String magazineId, String magazineName, Date magazineDate) {
        super(title, publicationYear, audienceSize, summary, keywords, authors);
        magazineArticleId = globalMagazineArticleId++;
        publicationType = PublicationType.MAGAZINE_ARTICLE;
        this.magazineDate = magazineDate;
        this.magazineName = magazineName;
        this.magazineId = magazineId;
    }
    // endregion Constructors

    // region Public Methods
    // region Getters & Setters
    public Date getMagazineDate() {
        return magazineDate;
    }

    public void setMagazineDate(Date magazineDate) {
        this.magazineDate = magazineDate;
    }

    public String getMagazineName() {
        return magazineName;
    }

    public void setMagazineName(String magazineName) {
        this.magazineName = magazineName;
    }

    public String getMagazineId() {
        return magazineId;
    }

    public void setMagazineId(String magazineId) {
        this.magazineId = magazineId;
    }

    //region Overridden Methods
    @Override
    public void setImpactFactor() {
        if (audienceSize >= 1000) {
            impactFactor = ImpactFactor.A;
        } else if (500 <= audienceSize && audienceSize < 1000) {
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
                + "Magazine article publication no: " + magazineArticleId + FIELDSET_DIVIDER
                + "Publication year: " + publicationYear + "\n"
                + "Audience size: " + audienceSize + "\n"
                + "Impact factor: " + impactFactor + "\n"
                + "Authors: " + getFormattedAuthors() + FIELDSET_DIVIDER
                + "Magazine ID: " + magazineId + "\n"
                + "Magazine name: " + magazineName + "\n"
                + "Magazine date: " + new SimpleDateFormat("dd/MM/yyyy").format(magazineDate) + FIELDSET_DIVIDER
                + "Summary: " + summary + "\n"
                + "Keywords: " + getFormattedKeywords() + "\n\n", MAX_LINE_SIZE);
    }
    //endregion Overridden Methods
    // endregion Public Methods
}
