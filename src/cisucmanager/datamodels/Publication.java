package cisucmanager.datamodels;

import cisucmanager.enums.ImpactFactor;
import cisucmanager.enums.InvestigatorType;
import cisucmanager.enums.PublicationType;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Sancho Amaral Simoes, 2019217590
 * Universidade de Coimbra, Licenciatura em Engenharia Informática
 * Programação Orientada a Objetos, 2º ano, 1º semestre, 2020/2021
 *
 * Abstract class that encapsulates the base information about an investigation
 * publication.
 */
public abstract class Publication implements Serializable {

    // region Static Variables
    private static int globalPublicationId = 0;
    // endregion Static Variables

    // region Constants
    protected final int publicationId;
    protected final int MAX_LINE_SIZE = 50;
    protected final String FIELDSET_DIVIDER = "\n" + new String(new char[MAX_LINE_SIZE]).replace("\0", "=") + "\n";
    // endregion Constants

    // region Instance Variables
    protected PublicationType publicationType;
    protected String title;
    protected int publicationYear;
    protected int audienceSize;
    protected String summary;
    protected ImpactFactor impactFactor;
    protected ArrayList<String> keywords;
    protected ArrayList<Investigator> authors;
    // endregion Instance Variables

    // region Constructors
    public Publication() {
        publicationId = globalPublicationId++;
        authors = new ArrayList<>();
    }

    /**
     * @param title the publication's title.
     * @param publicationYear the publications year.
     * @param audienceSize the publications audience size.
     * @param summary a small text that summarizes the publication content.
     * @param keywords some words which have a key role in the publication.
     * @param authors the publication's author's.
     */
    public Publication(String title, int publicationYear, int audienceSize, String summary, ArrayList<String> keywords, ArrayList<Investigator> authors) {
        publicationId = globalPublicationId++;
        setImpactFactor();
        this.authors = authors;
        this.title = title;
        this.keywords = keywords;
        this.publicationYear = publicationYear;
        this.audienceSize = audienceSize;
        this.summary = summary;
    }
    // endregion Constructors

    // region Public Methods
    // region Getters & Setters
    public PublicationType getPublicationType() {
        return publicationType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getAudienceSize() {
        return audienceSize;
    }

    public void setAudienceSize(int audienceSize) {
        this.audienceSize = audienceSize;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ImpactFactor getImpactFactor() {
        return impactFactor;
    }

    public abstract void setImpactFactor();

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public ArrayList<Investigator> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<Investigator> authors) {
        this.authors = authors;
    }

    // endregion Getters & Setters
    // region Overridden Methods
    @Override
    public String toString() {
        return breakText("\nPublication no: " + publicationId + "\n"
                + "Publication type: " + publicationType.getValue() + FIELDSET_DIVIDER
                + "Publication year: " + publicationYear + "\n"
                + "Audience size: " + audienceSize + "\n"
                + "Impact factor: " + impactFactor + "\n"
                + "Authors: " + getFormattedAuthors() + "\n"
                + "Summary: " + breakText(summary, MAX_LINE_SIZE) + "\n"
                + "Keywords:" + getFormattedKeywords() + "\n\n", MAX_LINE_SIZE);
    }

    // endregion Overridden Methods
    // endregion Public Methods
    // region Protected Methods
    /**
     * Method that returns a csv formatted representation of the publication
     * authors.
     *
     * @return the formatted list.
     */
    protected String getFormattedAuthors() {
        String out = "";
        for (int i = 0; i < authors.size(); i++) {
            out += getFormattedAuthorName(authors.get(i));
            if (i != authors.size() - 1) {
                out += ", ";
            }
        }
        return out;
    }

    /**
     * Method that returns a csv formatted representation of the publication
     * keywords.
     *
     * @return the formatted list.
     */
    protected String getFormattedKeywords() {
        String out = "";
        for (int i = 0; i < keywords.size(); i++) {
            out += keywords.get(i);
            if (i != keywords.size() - 1) {
                out += ", ";
            }
        }
        return out;
    }

    /**
     * Rudimentary function that breaks text after a certain amount of
     * characters.
     *
     * @param toBreak the text to break.
     * @param maxLineSize the max number of chars per line.
     * @return the formatted text.
     */
    protected String breakText(String toBreak, int maxLineSize) {
        String out = "";
        int counter = 0;
        for (int i = 0; i < toBreak.length() - 1; i++) {
            char current = toBreak.charAt(i), next = toBreak.charAt(i + 1);
            out += Character.toString(current);
            if (counter % maxLineSize == 0 && counter != 0 && current != '\n') {
                if (!(next == '-' || next == ',' || next == ';' || next == ' ' || next == '.')) {
                    out += "_";
                }
                counter = 0;
                out += "\n";
            }
            counter++;
            if (current == '\n') {
                counter = 0;
            }
        }
        return out;
    }
    // endregion Protected Methods

    // region Private Methods
    /**
     * Util function that returns a formatted name of an author according to its
     * type.
     *
     * @param author the publication's author.
     * @return the formatted author's name.
     */
    private String getFormattedAuthorName(Investigator author) {
        String splitName[] = author.getName().split(" ");
        int nameLength = splitName.length;
        if (author.getInvestigatorType() == InvestigatorType.EFFECTIVE) {
            return "Professor " + splitName[0] + " " + splitName[nameLength - 1];
        } else {
            return Character.toString(splitName[0].charAt(0)) + ". " + splitName[nameLength - 1];
        }
    }
    // endregion Private Methods
}
