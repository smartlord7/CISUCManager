package cisucmanager;

import cisucmanager.datamodels.*;
import cisucmanager.enums.InvestigatorType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Sancho Amaral Simoes, 2019217590
 * Universidade de Coimbra, Licenciatura em Engenharia Informática
 * Programação Orientada a Objetos, 2º ano, 1º semestre, 2020/2021
 *
 * Class that handles file R/W as well as their validation.
 */
public class CISUCFileHandler {

    // region Constants
    private final CISUCManagerConfig conf;

    private final String FIELD_SEPARATOR = "|",
            FIELD_SEPARATOR_REGEX = "\\s*\\" + FIELD_SEPARATOR + "\\s*",
            COMMENT_SEPARATOR = "//",
            DATE_FORMAT = "dd/MM/yyyy",
            EFFECTIVE_INVESTIGATOR_TXT_TYPE = "0",
            STUDENT_INVESTIGATOR_TXT_TYPE = "1",
            CONFERENCE_ARTICLE_TXT_TYPE = "0",
            MAGAZINE_ARTICLE_TXT_TYPE = "1",
            BOOK_TXT_TYPE = "2",
            BOOK_CHAPTER_TXT_TYPE = "3",
            CONFERENCE_ARTICLE_BOOK_TXT_TYPE = "4";
    // endregion Constants

    // region Instance Variables
    private BufferedReader textFileReader;
    private ObjectInputStream objectFileReader;
    private ObjectOutputStream objectWriter;
    // endregion Instance Variables

    // region Constructors
    public CISUCFileHandler(CISUCManagerConfig conf) {
        this.conf = conf;
    }
    // endregion Constructors

    // region Public Methods
    /**
     * Function that reads and validates the .txt that holds information about
     * investigators.
     *
     * @return a HashMap that maps for each investigator id its correspondent
     * investigator object.
     */
    public HashMap< Integer, Investigator> readInvestigatorsTextFile() {
        HashMap< Integer, Investigator> investigators = new HashMap<>();
        HashSet<String> usedEmails = new HashSet<>();
        HashSet<String> usedPhoneNumbers = new HashSet<>();

        try {
            textFileReader = new BufferedReader(new FileReader(conf.getInvestigatorsTxtFilePath()));
        } catch (FileNotFoundException ex) {
            errorAndExit("ERROR! INVESTIGATORS TEXT FILE WITH PATH " + conf.getInvestigatorsTxtFilePath() + " NOT FOUND!");
        }

        String line;
        int lineCounter = 1;
        CISUCManagerValidator validator = new CISUCManagerValidator();

        try {
            while ((line = textFileReader.readLine()) != null) {

                if (line.trim().startsWith(COMMENT_SEPARATOR) || line.length() == 0) {
                    lineCounter++;
                    continue;
                }

                String[] splitLine = line.split(FIELD_SEPARATOR_REGEX);

                String investigatorType = splitLine[0];

                if (!STUDENT_INVESTIGATOR_TXT_TYPE.equals(investigatorType) && !EFFECTIVE_INVESTIGATOR_TXT_TYPE.equals(investigatorType)) {
                    errorAndExit("ERROR AT FIELD 1 AT LINE " + lineCounter + " IN INVESTIGATORS TEXT FILE: INVALID INVESTIGATOR TYPE " + investigatorType + "! IT MUST BE 0 OR 1!\n"
                            + "(0 - EFFECTIVE INVESTIGATOR\n"
                            + " 1 - STUDENT INVESTIGATOR)");
                }

                int id = -1;
                String name, email;

                try {
                    id = Integer.parseInt(splitLine[1]);
                } catch (NumberFormatException ex) {
                    errorAndExit("ERROR AT FIELD 2 AT LINE " + lineCounter + " IN INVESTIGATORS TEXT FILE: INVALID INVESTIGATOR ID " + splitLine[1] + "! IT MUST BE AN INTEGER!");
                }

                if (investigators.containsKey(id)) {
                    errorAndExit("ERROR AT FIELD 2 AT LINE " + lineCounter + " IN INVESTIGATORS TEXT FILE: INVESTIGATOR WITH ID " + id + " ALREADY EXISTS!");
                }

                name = splitLine[2];

                if (validator.isInvalid(name, CISUCManagerValidator.NAME_REGEX)) {
                    errorAndExit("ERROR AT FIELD 3 AT LINE " + lineCounter + " IN INVESTIGATORS TEXT FILE: INVALID INVESTIGATOR NAME " + name + "!");
                }

                if (name.split("\\s+").length < 2) {
                    errorAndExit("ERROR AT FIELD 3 AT LINE " + lineCounter + " IN INVESTIGATORS TEXT FILE: INVALID INVESTIGATOR NAME " + name + "! IT MUST CONTAIN AT LEAST TWO WORDS!");
                }

                email = splitLine[3];

                if (validator.isInvalid(email, CISUCManagerValidator.EMAIL_REGEX)) {
                    errorAndExit("ERROR AT FIELD 4 AT LINE  " + lineCounter + " IN INVESTIGATORS TEXT FILE: INVALID INVESTIGATOR EMAIL " + email + "!");
                }

                if (usedEmails.contains(email)) {
                    errorAndExit("ERROR AT FIELD 4 AT LINE " + lineCounter + " IN INVESTIGATORS TEXT FILE: EMAIL " + email + " ALREADY IN USE!");
                }

                usedEmails.add(email);

                Investigator investigator = null;

                switch (investigatorType) {
                    case EFFECTIVE_INVESTIGATOR_TXT_TYPE:

                        if (splitLine.length != 6) {
                            errorAndExit("ERROR AT LINE " + lineCounter + " IN INVESTIGATORS TEXT FILE: NUMBER OF " + FIELD_SEPARATOR + " SEPARATED VALUES PER LINE MUST BE 6 WHEN INVESTIGATOR IS OF TYPE EFFECTIVE!");
                        }

                        String officeId,
                         deiPhoneNumber;

                        officeId = splitLine[4];
                        deiPhoneNumber = splitLine[5];

                        if (validator.isInvalid(deiPhoneNumber, CISUCManagerValidator.PHONE_NUMBER_REGEX)) {
                            errorAndExit("ERROR AT FIELD 6 AT LINE " + lineCounter + " IN INVESTIGATORS TEXT FILE: INVALID EFFECTIVE INVESTIGATOR PHONE NUMBER " + deiPhoneNumber + "! IT MUST HAVE 9 DIGITS!");
                        }

                        if (usedPhoneNumbers.contains(deiPhoneNumber)) {
                            errorAndExit("ERROR AT FIELD 6 AT LINE " + lineCounter + " IN INVESTIGATORS TEXT FILE: DEI PHONE NUMBER " + deiPhoneNumber + " ALREADY IN USE!");
                        }

                        usedPhoneNumbers.add(deiPhoneNumber);

                        investigator = new EffectiveInvestigator(id, name, email, null, deiPhoneNumber, officeId);
                        break;
                    case STUDENT_INVESTIGATOR_TXT_TYPE: // Student Investigator

                        if (splitLine.length != 7) {
                            errorAndExit("ERROR AT LINE " + lineCounter + " IN INVESTIGATORS TEXT FILE: NUMBER OF " + FIELD_SEPARATOR + " SEPARATED VALUES PER LINE MUST BE 7 WHEN INVESTIGATOR IS OF TYPE STUDENT!");
                        }

                        String thesisTitle;
                        Date expectedPhdCompletionDate = null;

                        thesisTitle = splitLine[4];

                        try {
                            expectedPhdCompletionDate = new SimpleDateFormat(DATE_FORMAT).parse(splitLine[5]);
                        } catch (ParseException ex) {
                            errorAndExit("ERROR AT FIELD 6 AT LINE " + lineCounter + " IN INVESTIGATORS TEXT FILE: INVALID STUDENT INVESTIGATOR'S EXPECTED PHD COMPLETION DATE " + splitLine[6] + "! IT MUST HAVE THE FORMAT " + DATE_FORMAT + "!");
                        }

                        int advisorInvestigatorId = -1;

                        try {
                            advisorInvestigatorId = Integer.parseInt(splitLine[6]);
                        } catch (NumberFormatException ex) {
                            errorAndExit("ERROR AT FIELD 7 AT LINE " + lineCounter + " IN INVESTIGATORS TEXT FILE: INVALID ADVISOR INVESTIGATOR ID " + splitLine[7] + "! IT MUST BE AN INTEGER!");
                        }

                        Investigator tempAdvisorInvestigator = new Investigator();
                        tempAdvisorInvestigator.setInvestigatorId(advisorInvestigatorId);

                        investigator = new StudentInvestigator(id, name, email, null, thesisTitle, tempAdvisorInvestigator, expectedPhdCompletionDate);
                        break;
                }
                investigators.put(id, investigator);

                lineCounter++;
            }
        } catch (IOException ex) {
            errorAndExit("ERROR! COULDN'T READ INVESTIGATORS TEXT FILE!");
        }
        closeTextReader();
        log("FINISHED READING INVESTIGATORS TEXT FILE.\n---------------------------------------------------------------");
        return investigators;
    }

    /**
     * Function that reads and validates the .txt which holds information about
     * investigation groups.
     *
     * @param investigators the previous parsed investigators list, as a needed
     * dependency.
     * @return a HashMap that maps for each investigator group name its
     * correspondent investigator group object.
     */
    public HashMap< String, InvestigationGroup> readInvestigationGroupsTextFile(HashMap< Integer, Investigator> investigators) {
        HashMap< String, InvestigationGroup> investigationGroups = new HashMap<>();

        try {
            textFileReader = new BufferedReader(new FileReader(conf.getInvestigationGroupsTxtFilePath()));
        } catch (FileNotFoundException ex) {
            errorAndExit("ERROR! INVESTIGATION GROUPS TEXT FILE WITH PATH " + conf.getInvestigationGroupsTxtFilePath() + " NOT FOUND!");
        }

        try {
            String line;
            int lineCounter = 1;
            HashSet<Integer> responsibleInvestigatorsIds = new HashSet<>();

            while ((line = textFileReader.readLine()) != null) {
                if (line.trim().startsWith(COMMENT_SEPARATOR) || line.length() == 0) {
                    lineCounter++;
                    continue;
                }
                String[] splitLine = line.split(FIELD_SEPARATOR_REGEX);

                if (splitLine.length != 3) {
                    errorAndExit("ERROR AT LINE " + lineCounter + " IN INVESTIGATION GROUPS TEXT FILE: NUMBER OF " + FIELD_SEPARATOR + " SEPARATED VALUES PER LINE MUST BE 3!");
                }

                int responsibleInvestigatorId = -1;
                String name, acronym;
                ArrayList< Investigator> groupMembers = new ArrayList<>();

                name = splitLine[0];

                if (investigationGroups.containsKey(splitLine[0])) {
                    errorAndExit("ERROR AT FIELD 1 AT LINE " + lineCounter + " IN INVESTIGATION GROUPS TEXT FILE: INVESTIGATION GROUP NAMED " + splitLine[0] + " ALREADY EXISTS!");
                }

                acronym = splitLine[1];

                try {
                    responsibleInvestigatorId = Integer.parseInt(splitLine[2]);
                } catch (NumberFormatException ex) {
                    errorAndExit("ERROR AT FIELD 3 AT LINE " + lineCounter + " IN INVESTIGATION GROUPS TEXT FILE: INVALID RESPONSIBLE INVESTIGATOR ID " + splitLine[2] + "! IT MUST BE AN INTEGER!");
                }
                if (!investigators.containsKey(responsibleInvestigatorId)) {
                    errorAndExit("ERROR AT FIELD 3 AT LINE " + lineCounter + " IN INVESTIGATION GROUPS TEXT FILE: RESPONSIBLE INVESTIGATOR WITH ID " + responsibleInvestigatorId + " DOESN'T EXIST!");
                }

                Investigator responsibleInvestigator = investigators.get(responsibleInvestigatorId);

                if (responsibleInvestigator.getInvestigatorType() != InvestigatorType.EFFECTIVE) {
                    errorAndExit("ERROR AT FIELD 3 AT LINE " + lineCounter + " IN INVESTIGATION GROUPS TEXT FILE: INVESTIGATOR WITH ID" + responsibleInvestigatorId + " CANNOT BE A RESPONSIBLE INVESTIGATOR SINCE IT'S NOT AN EFFECTIVE INVESTIGATOR!");
                }

                if (responsibleInvestigatorsIds.contains(responsibleInvestigator.getInvestigatorId())) {
                    errorAndExit("ERROR AT FIELD 3 AT LINE " + lineCounter + " IN INVESTIGATION GROUPS TEXT FILE: RESPONSIBLE INVESTIGATOR WITH ID " + responsibleInvestigatorId + " IS ALREADY IN CHARGE OF AN INVESTIGATION GROUP!");
                }

                responsibleInvestigatorsIds.add(responsibleInvestigatorId);

                lineCounter++;

                line = textFileReader.readLine();
                splitLine = line.split(FIELD_SEPARATOR_REGEX);

                if (splitLine.length == 0) {
                    errorAndExit("ERROR AT LINE " + lineCounter + " IN INVESTIGATION GROUPS TEXT FILE: NUMBER OF RESEARCH GROUP MEMBERS MUST BE AT LEAST 1!");
                }

                HashSet<Integer> members = new HashSet<>();

                InvestigationGroup invGroup = new InvestigationGroup();

                invGroup.setName(name);
                invGroup.setAcronym(acronym);
                invGroup.setResponsibleInvestigator(responsibleInvestigator);

                responsibleInvestigator.setInvestigationGroup(invGroup);

                for (int i = 0; i < splitLine.length; i++) {
                    int memberId = -1;

                    try {
                        memberId = Integer.parseInt(splitLine[i]);
                    } catch (NumberFormatException ex) {
                        errorAndExit("ERROR AT FIELD " + (i + 1) + " AT LINE " + lineCounter + " IN INVESTIGATION GROUPS TEXT FILE: INVALID MEMBER INVESTIGATOR ID " + splitLine[i] + "! EACH INVESTIGATION GROUP MEMBER ID MUST BE AN INTEGER!");
                    }

                    if (!investigators.containsKey(memberId)) {
                        errorAndExit("ERROR AT FIELD " + (i + 1) + " AT LINE " + lineCounter + " IN INVESTIGATION GROUPS TEXT FILE: MEMBER INVESTIGATOR WITH ID " + memberId + " DOESN'T EXIST!");
                    }

                    if (!members.contains(memberId)) {
                        groupMembers.add(investigators.get(memberId));
                        Investigator member = investigators.get(memberId);
                        if (member.getInvestigationGroup() != null) {
                            errorAndExit("ERROR AT FIELD " + (i + 1) + " AT LINE " + lineCounter + " IN INVESTIGATION GROUPS TEXT FILE: INVESTIGATOR WITH ID " + member.getInvestigatorId() + " IS ALREADY ASSOCIATED WITH THE INVESTIGATION GROUP " + member.getInvestigationGroup().getName());
                        }
                        member.setInvestigationGroup(invGroup);
                        members.add(memberId);
                    }
                }

                if (!members.contains(responsibleInvestigator.getInvestigatorId())) {
                    groupMembers.add(responsibleInvestigator);
                }

                investigationGroups.put(name, new InvestigationGroup(name, acronym, responsibleInvestigator, groupMembers));
                lineCounter++;
            }
        } catch (IOException ex) {
            errorAndExit("ERROR! COULDN'T READ INVESTIGATOR GROUPS TEXT FILE!");
        }
        closeTextReader();
        log("FINISHED READING INVESTIGATION GROUPS TEXT FILE.\n---------------------------------------------------------------");
        return investigationGroups;
    }

    /**
     * Function that reads and validates the .txt which holds information about
     * publications.
     *
     * @param investigators the previous parsed investigators list, as a needed
     * dependency.
     * @return a HashMap that maps for each publication title its correspondent
     * publication object.
     */
    public HashMap< String, Publication> readPublicationsTextFile(HashMap< Integer, Investigator> investigators) {
        HashMap< String, Publication> publications = new HashMap<>();

        try {
            textFileReader = new BufferedReader(new FileReader(conf.getPublicationsTxtFilePath()));
        } catch (FileNotFoundException ex) {
            errorAndExit("ERROR! PUBLICATIONS TEXT FILE WITH PATH " + conf.getPublicationsTxtFilePath() + " NOT FOUND!");
        }

        try {
            String line;
            int lineCounter = 1;
            CISUCManagerValidator validator = new CISUCManagerValidator();

            while ((line = textFileReader.readLine()) != null) {
                if (line.trim().startsWith(COMMENT_SEPARATOR) || line.length() == 0) {
                    lineCounter++;
                    continue;
                }

                String[] splitLine = line.split(FIELD_SEPARATOR_REGEX);
                String publicationType = splitLine[0];

                if (!publicationType.equals(CONFERENCE_ARTICLE_TXT_TYPE) && !publicationType.equals(MAGAZINE_ARTICLE_TXT_TYPE)
                        && !publicationType.equals(BOOK_TXT_TYPE) && !publicationType.equals(BOOK_CHAPTER_TXT_TYPE)
                        && !publicationType.equals(CONFERENCE_ARTICLE_BOOK_TXT_TYPE)) {
                    errorAndExit("ERROR AT FIELD 1 AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: INVALID PUBLICATION TYPE " + publicationType + "! IT MUST RANGE BETWEEN 0 AND 4, BOTH INCLUDED!\n"
                            + "(0 - CONFERENCE ARTICLE\n"
                            + " 1 - MAGAZINE ARTICLE\n"
                            + " 2 - BOOK\n"
                            + " 3 - BOOK CHAPTER\n"
                            + " 4 - CONFERENCE ARTICLE)");
                }

                String title, summary;
                int publicationYear = -1, audienceSize = -1;

                title = splitLine[1];

                if (publications.containsKey(title)) {
                    errorAndExit("ERROR AT FIELD 2 AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: PUBLICATION WITH TITLE " + title + " ALREADY EXISTS!");
                }

                try {
                    publicationYear = Integer.parseInt(splitLine[2]);
                } catch (NumberFormatException ex) {
                    errorAndExit("ERROR AT FIELD 3 AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: INVALID PUBLICATION YEAR " + splitLine[2] + "! IT MUST BE AN INTEGER!");
                }

                if (validator.isInvalid(CISUCManagerValidator.VALID_YEAR, publicationYear)) {
                    errorAndExit("ERROR AT FIELD 3 AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE! INVALID PUBLICATION YEAR " + splitLine[2] + "! IT MUST RANGE BETWEEN " + CISUCManagerValidator.MIN_YEAR + " AND " + CISUCManagerValidator.MAX_YEAR + "!");
                }

                try {
                    audienceSize = Integer.parseInt(splitLine[3]);
                } catch (NumberFormatException ex) {
                    errorAndExit("ERROR AT FIELD 4 AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: INVALID AUDIENCE SIZE " + splitLine[3] + "! IT MUST BE AN INTEGER!");
                }

                if (validator.isInvalid(CISUCManagerValidator.POSITIVE_NUMBER, audienceSize)) {
                    errorAndExit("ERROR AT FIELD5 4 AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE! INVALID AUDIENCE SIZE " + splitLine[3] + "! IT MUST BE A NON-NEGATIVE INTEGER!");
                }

                Publication publication = null;

                switch (publicationType) {
                    case CONFERENCE_ARTICLE_TXT_TYPE:
                        if (splitLine.length != 7) {
                            errorAndExit("ERROR AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: NUMBER OF " + FIELD_SEPARATOR + " SEPARATED VALUES PER LINE MUST BE 7 WHEN THE PUBLICATION OF TYPE CONFERENCE ARTICLE!");
                        }

                        String conferenceName,
                         conferencePlace;
                        Date conferenceDate = null;

                        conferenceName = splitLine[4];
                        conferencePlace = splitLine[5];

                        try {
                            conferenceDate = new SimpleDateFormat(DATE_FORMAT).parse(splitLine[6]);
                        } catch (ParseException ex) {
                            errorAndExit("ERROR AT FIELD 7 AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: INVALID CONFERENCE DATE " + splitLine[6] + "! IT MUST HAVE THE FORMAT" + DATE_FORMAT + "!");
                        }

                        ConferenceArticle conferenceArticle = new ConferenceArticle();

                        conferenceArticle.setConferenceName(conferenceName);
                        conferenceArticle.setConferencePlace(conferencePlace);
                        conferenceArticle.setConferenceDate(conferenceDate);

                        publication = conferenceArticle;
                        break;
                    case MAGAZINE_ARTICLE_TXT_TYPE:
                        if (splitLine.length != 7) {
                            errorAndExit("ERROR AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: NUMBER OF " + FIELD_SEPARATOR + " SEPARATED VALUES PER LINE MUST BE 7 WHEN THE PUBLICATION OF TYPE MAGAZINE ARTICLE!");
                        }

                        String magazineName,
                         magazineId;
                        Date magazineDate = null;

                        magazineName = splitLine[4];
                        magazineId = splitLine[5];

                        try {
                            magazineDate = new SimpleDateFormat(DATE_FORMAT).parse(splitLine[6]);
                        } catch (ParseException ex) {
                            errorAndExit("ERROR AT FIELD 7 AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: INVALID MAGAZINE DATE " + splitLine[6] + "! IT MUST HAVE THE FORMAT " + DATE_FORMAT + "!");
                        }

                        MagazineArticle magazineArticle = new MagazineArticle();

                        magazineArticle.setMagazineName(magazineName);
                        magazineArticle.setMagazineId(magazineId);
                        magazineArticle.setMagazineDate(magazineDate);

                        publication = magazineArticle;
                        break;
                    case BOOK_TXT_TYPE:
                    case BOOK_CHAPTER_TXT_TYPE:
                    case CONFERENCE_ARTICLE_BOOK_TXT_TYPE:
                        if (publicationType.equals(BOOK_TXT_TYPE)) {
                            if (splitLine.length != 6) {
                                errorAndExit("ERROR AT  LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: NUMBER OF " + FIELD_SEPARATOR + " SEPARATED VALUES PER LINE MUST BE 6 WHEN THE PUBLICATION OF TYPE BOOK!");
                            }
                        }

                        String publishingCompany,
                         isbn;

                        isbn = splitLine[4];

                        if (!(validator.isInvalidISBN(isbn))) {
                            errorAndExit("ERROR AT FIELD 5 AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: INVALID BOOK ISBN " + isbn + "!");
                        }

                        publishingCompany = splitLine[5];

                        Book book = new Book();

                        if (publicationType.equals(BOOK_CHAPTER_TXT_TYPE)) {
                            if (splitLine.length != 9) {
                                errorAndExit("ERROR AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: NUMBER OF COMMA SEPARATED VALUES PER LINE MUST BE 9 WHEN THE PUBLICATION OF TYPE BOOK CHAPTER!");
                            }
                            String chapterName;
                            int beginPage = -1, endPage = -1;

                            chapterName = splitLine[6];

                            try {
                                beginPage = Integer.parseInt(splitLine[7]);
                            } catch (NumberFormatException ex) {
                                errorAndExit("ERROR AT FIELD 8 AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: INVALID BEGIN PAGE " + splitLine[7] + "! IT MUST BE A POSITIVE INTEGER!");
                            }

                            if (validator.isInvalid(CISUCManagerValidator.POSITIVE_NUMBER, beginPage)) {
                                errorAndExit("ERROR AT FIELD 8 AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: INVALID BEGIN PAGE " + splitLine[7] + "! IT MUST BE A POSITIVE INTEGER!");
                            }

                            try {
                                endPage = Integer.parseInt(splitLine[8]);
                            } catch (NumberFormatException ex) {
                                errorAndExit("ERROR AT FIELD 9 AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: INVALID END PAGE " + splitLine[8] + "! IT MUST BE AN INTEGER!");
                            }

                            if (validator.isInvalid(CISUCManagerValidator.POSITIVE_NUMBER, beginPage)) {
                                errorAndExit("ERROR AT FIELD 9 AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: INVALID END PAGE " + splitLine[8] + "! IT MUST BE A POSITIVE INTEGER!");
                            }

                            BookChapter chapter = new BookChapter();

                            chapter.setChapterName(chapterName);
                            chapter.setBeginPage(beginPage);
                            chapter.setEndPage(endPage);

                            book = chapter;
                        } else if (publicationType.equals(CONFERENCE_ARTICLE_BOOK_TXT_TYPE)) {
                            if (splitLine.length != 8) {
                                errorAndExit("ERROR AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: NUMBER OF " + FIELD_SEPARATOR + " SEPARATED VALUES PER LINE MUST BE 8 WHEN THE PUBLICATION IS OF TYPE CONFERENCE ARTICLE BOOK!");
                            }

                            int numberOfArticles = -1;
                            conferenceName = splitLine[6];

                            try {
                                numberOfArticles = Integer.parseInt(splitLine[7]);
                            } catch (NumberFormatException ex) {
                                errorAndExit("ERROR AT FIELD 8 AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: INVALID NUMBER OF ARTICLES " + splitLine[7] + "! IT MUST BE A POSITIVE INTEGER");
                            }

                            if (validator.isInvalid(CISUCManagerValidator.POSITIVE_NUMBER, numberOfArticles)) {
                                errorAndExit("ERROR AT FIELD 8 AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: INVALID NUMBER OF ARTICLES " + splitLine[7] + "! IT MUST BE A POSITIVE INTEGER!");
                            }

                            ConferenceArticleBook confArticleBook = new ConferenceArticleBook();
                            confArticleBook.setConferenceName(conferenceName);
                            confArticleBook.setArticleNumber(numberOfArticles);

                            book = confArticleBook;
                        }
                        book.setPublishingCompany(publishingCompany);
                        book.setIsbn(isbn);

                        publication = book;
                        break;
                }
                publication.setTitle(title);
                publication.setPublicationYear(publicationYear);
                publication.setAudienceSize(audienceSize);
                publication.setImpactFactor();

                lineCounter++;

                summary = textFileReader.readLine();
                publication.setSummary(summary);

                lineCounter++;

                line = textFileReader.readLine();
                splitLine = line.split(FIELD_SEPARATOR_REGEX);

                ArrayList< String> keywords = new ArrayList<>();

                Collections.addAll(keywords, splitLine);
                lineCounter++;

                line = textFileReader.readLine();
                splitLine = line.split(FIELD_SEPARATOR_REGEX);

                if (splitLine.length == 0) {
                    errorAndExit("ERROR AT LINE " + lineCounter + " IN PUBLICATIONS TEXT FILE: EACH PUBLICATION MUST HAVE AT LEAST ONE AUTHOR!");
                }

                ArrayList< Investigator> authors = new ArrayList<>();
                HashSet<Integer> authorsIds = new HashSet<>();

                for (int i = 0; i < splitLine.length; i++) {
                    Investigator author;
                    int authorId = -1;
                    try {
                        authorId = Integer.parseInt(splitLine[i]);
                    } catch (NumberFormatException ex) {
                        errorAndExit("ERROR AT FIELD " + (i + 1) + " AT LINE " + lineCounter + ": INVALID AUTHOR ID " + splitLine[i] + "! EACH AUTHOR ID MUST BE AN INTEGER!");
                    }
                    if (!investigators.containsKey(authorId)) {
                        errorAndExit("ERROR AT FIELD " + (i + 1) + " AT LINE " + lineCounter + ": AUTHOR WITH ID " + authorId + " DOESN'T EXIST!");
                    }
                    if (!authorsIds.contains(authorId)) {
                        authorsIds.add(authorId);
                        author = investigators.get(authorId);
                        authors.add(author);
                    }
                }

                lineCounter++;

                publication.setAuthors(authors);
                publication.setKeywords(keywords);
                publications.put(title, publication);

            }

        } catch (IOException ex) {
            errorAndExit("ERROR! COULDN'T READ PUBLICATIONS FILE!");
        }
        closeTextReader();
        log("FINISHED READING PUBLICATIONS TEXT FILE.\n---------------------------------------------------------------");
        return publications;
    }

    /**
     * Function that resolves and validates some unfilled investigator fields,
     * such as investigationGroup, after reading the text files.
     *
     * @param investigators the investigators mapping to be resolved.
     * @return the resolved investigators mapping.
     */
    public HashMap<Integer, Investigator> resolveInvestigators(HashMap<Integer, Investigator> investigators) {
        log("RESOLVING INVESTIGATORS...");
        for (Integer investigatorId : investigators.keySet()) {
            Investigator investigator = investigators.get(investigatorId);
            if (investigator.getInvestigationGroup() == null) {
                errorAndExit("ERROR RESOLVING INVESTIGATORS: INVESTIGATOR WITH ID " + investigator.getInvestigatorId() + " IS NOT ASSOCIATED WITH ANY INVESTIGATION GROUP!");
            }
            if (investigator.getInvestigatorType() == InvestigatorType.STUDENT) {
                StudentInvestigator studentMember = (StudentInvestigator) investigator;
                Investigator advisor = studentMember.getAdvisorInvestigator();
                if (!investigators.containsKey(advisor.getInvestigatorId())) {
                    errorAndExit("ERROR RESOLVING INVESTIGATORS: ADVISOR INVESTIGATOR WITH ID " + advisor.getInvestigatorId() + " DOESN'T EXIST!");
                }
                advisor = investigators.get(advisor.getInvestigatorId());
                studentMember.setAdvisorInvestigator(investigators.get(advisor.getInvestigatorId()));
                if (!investigator.getInvestigationGroup().getName().equals(advisor.getInvestigationGroup().getName())) {
                    errorAndExit("ERROR RESOLVING INVESTIGATORS: STUDENT INVESTIGATOR WITH ID " + investigator.getInvestigatorId() + " HAS AN ADVISOR INVESTIGATOR WITH ID " + advisor.getInvestigatorId() + " THAT DOESN'T BELONG TO THE SAME INVESTIGATION GROUP!");
                }
            }
        }
        log("INVESTIGATORS SUCCESSFULLY RESOLVED.\n---------------------------------------------------------------");
        return investigators;
    }

    /**
     * Function that writes an HashMap of entities into a given object file.
     *
     * @param entities the HashMap to be serialized.
     * @param objectFile the target object file.
     * @return 1 - if the file was written. 0 - otherwise.
     */
    public int writeEntityObjectFile(HashMap entities, File objectFile) {
        if (!objectFile.exists()) {
            try {
                objectFile.createNewFile();
            } catch (IOException ex) {
                errorAndExit("ERROR: COULDN'T CREATE FILE WITH PATH " + objectFile.getPath());
            }
            try {
                objectWriter = new ObjectOutputStream(new FileOutputStream(objectFile));
            } catch (FileNotFoundException ex) {
                errorAndExit("ERROR: OBJECT FILE WITH PATH " + objectFile.getPath() + " NOT FOUND!");
            } catch (IOException ex) {
                errorAndExit("ERROR: COULDN'T OPEN OBJECT FILE WITH PATH " + objectFile.getPath() + "!");
            }

            try {
                objectWriter.writeObject(entities);
            } catch (IOException ex) {
                errorAndExit("ERROR: COULDN'T WRITE IN OBJECT FILE WITH PATH " + objectFile.getPath() + "!");
            }
            try {
                objectWriter.close();
            } catch (IOException ex) {
                errorAndExit("ERROR: COULDN'T CLOSE OBJECT FILE READER!");
            }
            closeObjectFileWriter();
            return 1;
        }
        return 0;
    }

    /**
     * Function that reads an HashMap of entities from a given object file.
     *
     * @param objectFile the object file to be read.
     * @return the entities HashMap that was read.
     */
    public HashMap readEntityObjectFile(File objectFile) {
        try {
            objectFileReader = new ObjectInputStream(new FileInputStream(objectFile));
        } catch (IOException ex) {
            errorAndExit("ERROR: COULDN'T OPEN OBJECT FILE WITH PATH" + objectFile.getPath());
        }

        try {
            return (HashMap) objectFileReader.readObject();
        } catch (IOException ex) {
            errorAndExit("ERROR: COULDN'T READ FILE WITH PATH " + objectFile.getPath());
        } catch (ClassNotFoundException ex) {
            errorAndExit("ERROR: CLASS NOT FOUND!");
        }
        closeObjectReader();
        return null;
    }
    // endregion Public Methods

    // region Private Methods
    // region Aliases Methods
    /**
     * Alias function that closes the BufferedReader of this class.
     */
    private void closeTextReader() {
        try {
            textFileReader.close();
        } catch (IOException ex) {
            errorAndExit("ERROR: COULDN'T CLOSE TEXT FILE READER!");
        }
    }

    /**
     * Alias function that closes the ObjectInputStream of this class.
     */
    private void closeObjectReader() {
        try {
            objectFileReader.close();
        } catch (IOException ex) {
            errorAndExit("ERROR: COULDN'T CLOSE OBJECT FILE READER!");
        }
    }

    /**
     * Alias function that closes the ObjectWriter of this class.
     */
    private void closeObjectFileWriter() {
        try {
            objectWriter.close();
        } catch (IOException ex) {
            errorAndExit("ERROR: COULDN'T CLOSE OBJECT FILE WRITER!");
        }
    }

    /**
     * Alias function that logs a certain message if verbose mode is activated
     * in the conf object.
     *
     * @param message the message to be logged.
     */
    private void log(String message) {
        if (conf.isVerbose()) {
            System.out.println(message);
        }
    }

    /**
     * Alias function that writes the error message and immediately ends the
     * execution of the program.
     *
     * @param errorMessage the error message to be displayed
     */
    private void errorAndExit(String errorMessage) {
        System.out.println(errorMessage);
        System.out.println("EXITING PROGRAM...");
        System.exit(1);
    }
    // endregion Aliases Methods
    // endregion Private Methods
}
