package cisucmanager;

import cisucmanager.datamodels.InvestigationGroup;
import cisucmanager.datamodels.Investigator;
import cisucmanager.datamodels.Publication;
import cisucmanager.enums.ImpactFactor;
import cisucmanager.enums.InvestigatorType;
import cisucmanager.enums.PublicationType;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sancho Amaral Simoes, 2019217590 Universidade de Coimbra,
 * Licenciatura em Engenharia Informática Programação Orientada a Objetos, 2º
 * ano, 1º semestre, 2020/2021
 *
 * Main class.
 */
public class CISUCManager {

    // region Constants
    private final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    private final String EMPTY_LIST_STRING = "<NONE>";
    private final int LAST_YEARS = 5;

    private final CISUCManagerConfig conf;
    private final CISUCFileHandler fileManager;
    private final String generalInfo;
    private final BufferedReader consoleReader;
    // endregion Constants

    // region Instance Variables
    private HashMap<Integer, Investigator> investigators;
    private HashMap<String, InvestigationGroup> investigationGroups;
    private HashMap<String, Publication> publications;
    // endregion Instance Variables

    // region Constructors
    /**
     * @param conf the configuration of CISUCManager.
     * @param fileManager the object that handles and validates CISUCManager
     * related files.
     * @throws IOException if something went wrong whilst reading the console
     * input.
     */
    public CISUCManager(CISUCManagerConfig conf, CISUCFileHandler fileManager) throws IOException {
        this.conf = conf;
        this.fileManager = fileManager;

        investigators = new HashMap<>();
        investigationGroups = new HashMap<>();
        publications = new HashMap<>();
        consoleReader = new BufferedReader(new InputStreamReader(System.in));

        readFilesToMemory();

        generalInfo = "\n---CISUC GENERAL INFORMATION---"
                + "\nTOTAL NUMBER OF INVESTIGATORS: " + investigators.size()
                + "\n---EFFECTIVE INVESTIGATORS: " + getInvestigatorsByType(investigators.values(), InvestigatorType.EFFECTIVE).size()
                + "\n---STUDENT INVESTIGATORS: " + getInvestigatorsByType(investigators.values(), InvestigatorType.STUDENT).size()
                + "\nTOTAL NUMBER OF INVESTIGATION GROUPS: " + investigationGroups.size()
                + "\nTOTAL NUMBER OF PUBLICATIONS: " + publications.size()
                + "\n---PUBLICATIONS FROM THE LAST " + LAST_YEARS + " YEARS: " + getPublicationFromLastYears(publications.values(), LAST_YEARS).size()
                + "\n---CONFERENCE ARTICLE PUBLICATIONS: " + getPublicationsByType(publications.values(), PublicationType.CONFERENCE_ARTICLE).size()
                + "\n---MAGAZINE ARTICLE PUBLICATIONS: " + getPublicationsByType(publications.values(), PublicationType.MAGAZINE_ARTICLE).size()
                + "\n---BOOK PUBLICATIONS: " + getPublicationsByType(publications.values(), PublicationType.BOOK).size();

        writeFilesFromMemory();
        execute();
    }
    // endregion Constructors

    /**
     * Function that displays to the user all the available operations and asks
     * him for an operation id.
     *
     * @throws IOException if something went wrong whilst reading the console
     * input.
     */
    private void execute() throws IOException {
        String operationId;

        while (true) {

            displayMenu();

            operationId = consoleReader.readLine();

            switch (operationId) {
                case "0":
                    println(generalInfo);
                    break;
                case "1":
                    displayInvestigationGroupPublications();
                    break;
                case "2":
                    displayInvestigationGroupMembers();
                    break;
                case "3":
                    displayInvestigatorPublications();
                    break;
                case "4":
                    displayInvestigatorList();
                    break;
                case "5":
                    displayInvestigationGroupList();
                    break;
                case "6":
                    displayPublicationList();
                    break;
                case "7":
                    exit();
                    break;
                default:
                    println("INVALID OPERATION ID: " + operationId + "!");
            }
        }
    }

    /**
     * Function that reads the correspondent entities object files in case of
     * their existence. Otherwise, the correspondent text files will be
     * processed.
     */
    private void readFilesToMemory() {
        log("-----CISUC MANAGER STARTUP LOG-----\n"
                + "ATTEMPTING TO READ INVESTIGATION GROUPS, INVESTIGATORS AND PUBLICATIONS OBJECT FILES...\n---------------------------------------------------------------");

        File investigationGroupsObjFile, investigatorsObjFile, publicationsObjFile;

        investigatorsObjFile = new File(conf.getInvestigatorsObjFilePath());
        investigationGroupsObjFile = new File(conf.getInvestigationGroupsObjFilePath());
        publicationsObjFile = new File(conf.getPublicationsObjFilePath());
        boolean readInvestigatorsFromTxt = false;

        if (!investigatorsObjFile.exists()) {
            log("INVESTIGATORS OBJECT FILE WITH PATH " + conf.getInvestigatorsObjFilePath() + " NOT FOUND!\n"
                    + "READING INVESTIGATORS TEXT FILE INSTEAD...");

            investigators = fileManager.readInvestigatorsTextFile();
            readInvestigatorsFromTxt = true;

        } else {
            log("READING INVESTIGATORS OBJECT FILE...");

            investigators = fileManager.readEntityObjectFile(investigatorsObjFile);

            log("FINISHED READING INVESTIGATORS OBJECT FILE...\n---------------------------------------------------------------");
        }

        if (!investigationGroupsObjFile.exists()) {
            log("INVESTIGATION GROUPS OBJECT FILE WITH PATH " + conf.getInvestigationGroupsObjFilePath() + " NOT FOUND!\n"
                    + "READING INVESTIGATION GROUPS TEXT FILE INSTEAD...");

            investigationGroups = fileManager.readInvestigationGroupsTextFile(investigators);
        } else {
            log("READING INVESTIGATION GROUPS OBJECT FILE...");

            investigationGroups = fileManager.readEntityObjectFile(investigationGroupsObjFile);

            log("FINISHED READING INVESTIGATION GROUPS OBJECT FILE.\n---------------------------------------------------------------");
        }

        if (readInvestigatorsFromTxt) {
            investigators = fileManager.resolveInvestigators(investigators);
        }

        if (!publicationsObjFile.exists()) {
            log("PUBLICATIONS OBJECT FILE WITH PATH " + conf.getPublicationsObjFilePath() + " NOT FOUND!\n"
                    + "READING PUBLICATIONS TEXT FILE INSTEAD...");

            publications = fileManager.readPublicationsTextFile(investigators);
        } else {
            log("READING PUBLICATIONS OBJECT FILE...");

            publications = fileManager.readEntityObjectFile(publicationsObjFile);

            log("FINISHED READING PUBLICATIONS OBJECT FILE...\n---------------------------------------------------------------");
        }
    }

    /**
     * Function that writes all entities correspondent HashMap to the object
     * file with path specified in the configuration.
     */
    private void writeFilesFromMemory() {
        log("ATTEMPTING TO WRITE TEXT FILES INFORMATION TO MISSING OBJECT FILES...");
        int counter = 0;

        counter += fileManager.writeEntityObjectFile(investigators, new File(conf.getInvestigatorsObjFilePath()));
        counter += fileManager.writeEntityObjectFile(investigationGroups, new File(conf.getInvestigationGroupsObjFilePath()));
        counter += fileManager.writeEntityObjectFile(publications, new File(conf.getPublicationsObjFilePath()));

        if (counter == 0) {
            log("NO MISSING OBJECT FILES.\n---------------------------------------------------------------");
        } else {
            log("SUCCESSFULLY WRITTEN TEXT FILES INFORMATION INTO " + counter + " OBJECT FILES.\n---------------------------------------------------------------");
        }
    }

    // region Aliases Methods
    /**
     * Alias function of System.out.println()
     *
     * @param message the message to print
     */
    private void println(Object message) {
        System.out.println(message);
    }

    /**
     * Alias function that ends the program execution.
     */
    private void exit() {
        println("EXITING PROGRAM...");
        System.exit(1);
    }

    /**
     * Alias function that logs a certain message if verbose mode is activated
     * in the conf object.
     *
     * @param message the message to be logged.
     */
    private void log(String message) {
        if (conf.isVerbose()) {
            println(message);
        }
    }
    // endregion Aliases Methods

    // region Utility Methods
    /**
     * Function that gets the existent publications of a certain type.
     *
     * @param publicationType the publications type.
     * @return the publications of the specified type.
     */
    private ArrayList<Publication> getPublicationsByType(Collection<Publication> publications, PublicationType publicationType) {
        ArrayList<Publication> publicationsByType = new ArrayList<>();

        for (Publication publication : publications) {
            if (publication.getPublicationType() == publicationType) {
                publicationsByType.add(publication);
            }
        }
        return publicationsByType;
    }

    /**
     * Function that filters, from a collection of publications, another list of
     * the publications from the last <lastYears> years.
     *
     * @param publications the target collection of publications.
     * @param lastYears the number of years to go back to retrieve publications.
     * @return the list of the publications from the last <lastYears> years.
     */
    private ArrayList<Publication> getPublicationFromLastYears(Collection<Publication> publications, int lastYears) {
        ArrayList<Publication> publicationsFromLastYears = new ArrayList<>();
        for (Publication publication : publications) {
            if (CURRENT_YEAR - publication.getPublicationYear() <= lastYears) {
                publicationsFromLastYears.add(publication);
            }
        }
        return publicationsFromLastYears;
    }

    /**
     * Function that gets the existent investigators of a certain type.
     *
     * @param investigatorType the type of the investigators.
     * @return the investigators of the specified type.
     */
    private ArrayList<Investigator> getInvestigatorsByType(Collection<Investigator> investigators, InvestigatorType investigatorType) {
        ArrayList<Investigator> investigatorsByType = new ArrayList<>();

        for (Investigator investigator : investigators) {
            if (investigator.getInvestigatorType() == investigatorType) {
                investigatorsByType.add(investigator);
            }
        }
        return investigatorsByType;
    }

    /**
     * Function that retrieves all the publications in the last <lastYears>
     * years which have, at least, one member investigator of a given
     * investigation group.
     *
     * @param investigationGroupName the name of the investigation group.
     * publications of the research group in question.
     * @return the investigation group publications.
     */
    private ArrayList<Publication> getInvestigationGroupPublications(String investigationGroupName) {
        if (!investigationGroups.containsKey(investigationGroupName)) {
            return null;
        }
        HashSet<Investigator> groupMembers = new HashSet(investigationGroups.get(investigationGroupName).getMemberList());
        HashSet<Publication> investigationGroupPublications = new HashSet<>();

        for (String publicationTitle : publications.keySet()) {
            Publication current = publications.get(publicationTitle);
            for (Investigator author : current.getAuthors()) {
                if (groupMembers.contains(author)) {
                    investigationGroupPublications.add(current);
                }
            }
        }
        return new ArrayList<>(investigationGroupPublications);
    }

    /**
     * Function that retrieves all the publications in the last <lastYears>
     * years authored by a certain investigator.
     *
     * @param investigatorId the author's id. publications of the research group
     * in question.
     * @return the investigator's publications.
     */
    private ArrayList<Publication> getInvestigatorPublications(int investigatorId) {
        if (!investigators.containsKey(investigatorId)) {
            return null;
        }
        ArrayList<Publication> investigatorPublications = new ArrayList<>();
        for (String publicationTitle : publications.keySet()) {
            Publication current = publications.get(publicationTitle);
            for (Investigator author : current.getAuthors()) {
                if (author.getInvestigatorId() == investigatorId) {
                    investigatorPublications.add(current);
                    break;
                }
            }
        }
        return investigatorPublications;
    }

    /**
     * Function that groups a given ArrayList of publications according to their
     * publication year, publication type and impact factor.
     *
     * @param publications the publications list to be grouped.
     * @param lastYears the number of years to go back to retrieve the
     * publications.
     * @return the grouped publications.
     */
    private Map<Integer, Map<PublicationType, Map<ImpactFactor, List<Publication>>>> groupPublications(Collection<Publication> publications, int lastYears) {
        return publications
                .stream()
                .filter(p -> CURRENT_YEAR - p.getPublicationYear() <= lastYears)
                .collect(Collectors.groupingBy(Publication::getPublicationYear,
                                Collectors.groupingBy(Publication::getPublicationType,
                                        Collectors.groupingBy(Publication::getImpactFactor))));
    }

    /**
     * Function that formats the publication grouping previously mentioned into
     * a string.
     *
     * @param grouped the grouped publications.
     * @param count flag that toggles the display of publication counters
     * instead of a list.
     * @return the formatted publication grouping string.
     */
    private String getGroupedPublicationsAsString(Map<Integer, Map<PublicationType, Map<ImpactFactor, List<Publication>>>> grouped, boolean count) {
        int totalCounter = 0;
        String outputString = "";
        for (Integer year : grouped.keySet()) {

            String yearStr = "";
            int yearCounter = 0;

            for (PublicationType pubType : grouped.get(year).keySet()) {

                String typeStr = "";
                int typeCounter = 0;

                for (ImpactFactor impactFactor : grouped.get(year).get(pubType).keySet()) {

                    String impFctStr = "";
                    int impFctCounter = 0;

                    for (Publication publication : grouped.get(year).get(pubType).get(impactFactor)) {
                        impFctStr += publication.toString().replace("\n", "\n\t") + "\n";
                        totalCounter++;
                        impFctCounter++;
                        typeCounter++;
                        yearCounter++;
                    }
                    if (!count) {
                        typeStr += "------IMPACT FACTOR: " + impactFactor.getValue() + "\n" + impFctStr;
                    } else {
                        typeStr += "      IMPACT FACTOR: " + impactFactor.getValue() + " - " + impFctCounter + " PUBLICATION(S)\n";
                    }
                }
                if (!count) {
                    yearStr += "---TYPE: " + pubType.getValue() + "\n" + typeStr;
                } else {
                    yearStr += "   TYPE: " + pubType.getValue() + " - " + typeCounter + " PUBLICATION(S)\n" + typeStr;
                }
            }
            if (!count) {
                outputString += "YEAR: " + year + "\n" + yearStr;
            } else {
                outputString += "YEAR: " + year + " - " + yearCounter + " PUBLICATION(S)\n" + yearStr;
            }
        }

        if (outputString.length()
                == 0) {
            return EMPTY_LIST_STRING;
        }
        return "TOTAL NUMBER OF PUBLICATIONS, FROM LAST " + LAST_YEARS + " YEARS: " + totalCounter + " PUBLICATION(S)\n"
                + (count ? "NUMBER OF " : "") + "PUBLICATIONS, FROM LAST " + LAST_YEARS + " YEARS, GROUPED BY YEAR, TYPE AND IMPACT FACTOR: \n\n" + outputString;

    }
    // endregion Utility Methods

// region Presentation Methods
    /**
     * Function that displays to the user the available operations and their
     * correspondent ids.
     */
    private void displayMenu() {
        println("\n-------CISUC MANAGER-------\n"
                + "AVAILABLE OPERATIONS: \n"
                + "0 - DISPLAY GENERAL INFORMATION\n"
                + "1 - LIST INVESTIGATION GROUP PUBLICATIONS\n"
                + "2 - LIST INVESTIGATION GROUP MEMBERS\n"
                + "3 - LIST INVESTIGATOR PUBLICATIONS\n"
                + "4 - LIST INVESTIGATORS\n"
                + "5 - LIST INVESTIGATION GROUPS\n"
                + "6 - LIST PUBLICATIONS\n"
                + "7 - EXIT\n"
                + "INSERT THE ID OF THE WISHED OPERATION: ");
    }

    // region Endpoints
    /**
     * Function that displays a certain investigation group's list of members,
     * grouping them according to their type.
     *
     * @throws IOException if something went wrong whilst reading the console
     * input.
     */
    private void displayInvestigationGroupMembers() throws IOException {
        String groupName;

        println("\n-------INVESTIGATION GROUP MEMBERS-------");
        println("INVESTIGATION GROUP NAME: ");

        groupName = consoleReader.readLine();

        if (!investigationGroups.containsKey(groupName)) {
            println("ERROR: INVESTIGATION NAMED " + groupName + " DOESN'T EXIST!");
            return;
        }

        ArrayList<Investigator> members = investigationGroups.get(groupName).getMemberList();

        println("\n----" + groupName + " INVESTIGATION GROUP MEMBERS----\n"
                + "---EFFECTIVE INVESTIGATORS---");

        ArrayList<Investigator> investigatorsByType = getInvestigatorsByType(members, InvestigatorType.EFFECTIVE);

        if (investigatorsByType.isEmpty()) {
            println(EMPTY_LIST_STRING);
        } else {
            for (Investigator effectiveInvestigator : investigatorsByType) {
                println(effectiveInvestigator);
            }
        }

        println("---STUDENT INVESTIGATORS---");

        investigatorsByType = getInvestigatorsByType(members, InvestigatorType.STUDENT);

        if (investigatorsByType.isEmpty()) {
            println(EMPTY_LIST_STRING);
        } else {
            for (Investigator studentInvestigator : investigatorsByType) {
                println(studentInvestigator);
            }
        }
    }

    /**
     * Function that displays a certain investigation group's grouping of
     * publications, according to their year, type and impact factor.
     *
     * @throws IOException if something went wrong whilst reading the console
     * input.
     */
    private void displayInvestigationGroupPublications() throws IOException {
        String groupName;
        int lastYears;

        println("-------INVESTIGATION GROUP PUBLICATIONS-------\n"
                + "(GROUPED BY YEAR, TYPE AND IMPACT FACTOR)\n"
                + "INVESTIGATION GROUP NAME: ");

        groupName = consoleReader.readLine();

        if (!investigationGroups.containsKey(groupName)) {
            println("ERROR: INVESTIGATION GROUP NAMED " + groupName + " DOESN'T EXIST!");
            return;
        }

        println("LAST YEARS: ");

        lastYears = Integer.parseInt(consoleReader.readLine());

        println("\n----" + groupName + " INVESTIGATION GROUP PUBLICATIONS FROM " + lastYears + " LAST YEARS----\n");
        println(getGroupedPublicationsAsString(groupPublications(getInvestigationGroupPublications(groupName), lastYears), false));
    }

    /**
     * Function that displays a certain investigator's grouping of publications,
     * according to their year, type and impact factor.
     *
     * @throws IOException if something went wrong whilst reading the console
     * input.
     */
    private void displayInvestigatorPublications() throws IOException {
        int investigatorId, lastYears;

        println("-------INVESTIGATOR PUBLICATIONS-------\n"
                + "(GROUPED BY YEAR, TYPE AND IMPACT FACTOR)\n"
                + "INVESTIGATOR ID: ");

        try {
            investigatorId = Integer.parseInt(consoleReader.readLine());
        } catch (NumberFormatException ex) {
            println("ERROR: INVESTIGATOR ID MUST BE AN INTEGER!");
            return;
        }

        if (!investigators.containsKey(investigatorId)) {
            println("ERROR: INVESTIGATOR NAMED " + investigatorId + " DOESN'T EXIST!");
            return;
        }

        println("LAST YEARS: ");

        lastYears = Integer.parseInt(consoleReader.readLine());
        Investigator investigator = investigators.get(investigatorId);

        println("INVESTIGATOR: " + investigator + "\n"
                + "\n----INVESTIGATOR" + investigator.getName() + " PUBLICATIONS FROM " + lastYears + " LAST YEARS----\n"
                + getGroupedPublicationsAsString(groupPublications(getInvestigatorPublications(investigatorId), lastYears), false));
    }

    /**
     * Function that displays the list of investigation groups.
     */
    private void displayInvestigationGroupList() {
        println("-------INVESTIGATION GROUP LIST-------\n");
        if (investigationGroups.isEmpty()) {
            println(EMPTY_LIST_STRING);
        } else {
            for (String investigationGroupName : investigationGroups.keySet()) {
                InvestigationGroup current = investigationGroups.get(investigationGroupName);
                ArrayList<Publication> groupPublications = getInvestigationGroupPublications(investigationGroupName);
                ArrayList<Investigator> members = current.getMemberList();
                println("INVESTIGATION GROUP: " + investigationGroupName + "\n\n"
                        + current + "-----------------------------\n"
                        + "NUMBER OF MEMBERS: " + current.getMemberList().size() + "\n"
                        + "NUMBER OF EFFECTIVE MEMBERS: " + getInvestigatorsByType(members, InvestigatorType.EFFECTIVE).size() + "\n"
                        + "NUMBER OF STUDENT MEMBERS: " + getInvestigatorsByType(members, InvestigatorType.STUDENT).size() + "\n"
                        + getGroupedPublicationsAsString(groupPublications(groupPublications, LAST_YEARS), true));
            }
        }
    }

    /**
     * Function that displays the list of investigators.
     */
    private void displayInvestigatorList() {
        println("-------INVESTIGATOR LIST-------\n");
        if (investigators.isEmpty()) {
            println(EMPTY_LIST_STRING);
        } else {
            ArrayList<Investigator> effectives = getInvestigatorsByType(investigators.values(), InvestigatorType.EFFECTIVE),
                    students = getInvestigatorsByType(investigators.values(), InvestigatorType.STUDENT);
            println("---EFFECTIVE INVESTIGATORS");
            if (effectives.isEmpty()) {
                println(EMPTY_LIST_STRING);
            } else {
                for (Investigator effective : effectives) {
                    println(effective);
                }
            }
            if (students.isEmpty()) {
                println(EMPTY_LIST_STRING);
            } else {
                for (Investigator student : students) {
                    println(student);
                }
            }
        }
    }

    /**
     * Function that displays the list of publications.
     *
     * @throws IOException if something went wrong whilst reading the console
     * input.
     */
    private void displayPublicationList() throws IOException {
        println("-------PUBLICATION LIST-------\n"
                + "COUNT? (Y/N)");
        String answer, str;

        answer = consoleReader.readLine();

        if (answer.equalsIgnoreCase("Y")) {
            str = getGroupedPublicationsAsString(groupPublications(publications.values(), Integer.MAX_VALUE), true);
        } else if (answer.equalsIgnoreCase("N")) {
            str = getGroupedPublicationsAsString(groupPublications(publications.values(), Integer.MAX_VALUE), false);
        } else {
            println("ERROR: INVALID ANSWER!");
            return;
        }
        if ("".equals(str)) {
            println(EMPTY_LIST_STRING);
        } else {
            println(str);
        }
    }
    // endregion Endpoints
    // region Presentation Methods

    /**
     * @param args the command line arguments
     * @throws java.io.IOException if something went wrong whilst reading the
     * console input.
     */
    public static void main(String[] args) throws IOException {
        CISUCManagerConfig conf = new CISUCManagerConfig();
        conf.setConfigFromConsole(args);
        conf.applyConfig();

        CISUCFileHandler fileManager = new CISUCFileHandler(conf);

        CISUCManager mng = new CISUCManager(conf, fileManager);
    }

}
