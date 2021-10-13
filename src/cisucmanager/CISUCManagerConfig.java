package cisucmanager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Sancho Amaral Simoes, 2019217590
 * Universidade de Coimbra, Licenciatura em Engenharia Informática
 * Programação Orientada a Objetos, 2º ano, 1º semestre, 2020/2021
 *
 * Class designed to handle the flag configuration as well as the file paths
 * before CISUCManager gets in action.
 */
public class CISUCManagerConfig {

    // region Constants
    private final String WORKING_DIRECTORY = System.getProperty("user.dir") + "\\src\\cisucmanager\\",
            DEFAULT_INVESTIGATION_GROUPS_TXT_FILE_PATH = WORKING_DIRECTORY + "investigationGroups.txt",
            DEFAULT_INVESTIGATORS_TXT_FILE_PATH = WORKING_DIRECTORY + "investigators.txt",
            DEFAULT_PUBLICATIONS_TXT_FILE_PATH = WORKING_DIRECTORY + "publications.txt",
            DEFAULT_INVESTIGATION_GROUPS_OBJ_FILE_PATH = WORKING_DIRECTORY + "investigationGroups.obj",
            DEFAULT_INVESTIGATORS_OBJ_FILE_PATH = WORKING_DIRECTORY + "investigators.obj",
            DEFAULT_PUBLICATIONS_OBJ_FILE_PATH = WORKING_DIRECTORY + "publications.obj",
            DEFAULT_VERBOSE_VALUE = "false",
            TEXT_INPUT_FLAG = "-txt",
            OBJECT_INPUT_FLAG = "-obj",
            VERBOSE_FLAG = "-v",
            HELP_FLAG = "-help";

    private final HashMap<String, ArrayList<String>> config;
    // endregion Constants

    // region Instance Variables
    private boolean verbose;
    private String investigationGroupsTxtFilePath,
            investigationGroupsObjFilePath,
            investigatorsTxtFilePath,
            investigatorsObjFilePath,
            publicationsTxtFilePath,
            publicationsObjFilePath;
    // endregion Instance Variables

    // region Constructors
    public CISUCManagerConfig() {
        this.config = new HashMap<>();
        setDefaultConfig();
    }
    // endregion Constructors

    // region Public Methods
    // region Getters
    public boolean isVerbose() {
        return verbose;
    }

    public String getInvestigationGroupsTxtFilePath() {
        return investigationGroupsTxtFilePath;
    }

    public String getInvestigationGroupsObjFilePath() {
        return investigationGroupsObjFilePath;
    }

    public String getInvestigatorsTxtFilePath() {
        return investigatorsTxtFilePath;
    }

    public String getInvestigatorsObjFilePath() {
        return investigatorsObjFilePath;
    }

    public String getPublicationsTxtFilePath() {
        return publicationsTxtFilePath;
    }

    public String getPublicationsObjFilePath() {
        return publicationsObjFilePath;
    }

    public HashMap<String, ArrayList<String>> getConfig() {
        return config;
    }
    // endregion Getters

    /**
     * Function that parses the flags provided by the user in the console and
     * adds them and their values to the configuration.
     *
     * @param args the console arguments.
     */
    public void setConfigFromConsole(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            ArrayList<String> values = new ArrayList<>();
            if (arg.equals(TEXT_INPUT_FLAG)) {
                if (args.length - i < 3) {
                    System.out.println("ERROR: NUMBER OF FLAG PARAMS MUST BE 3!");
                    System.exit(0);
                }
                for (int j = 1; j < 4; j++) {
                    if (!args[i + j].endsWith(".txt")) {
                        System.out.println("ERROR: INVALID EXTENSION " + args[i + j] + "! IT MUST BE .txt ");
                        System.exit(0);
                    }
                    values.add(args[i + j]);
                }
                config.put(TEXT_INPUT_FLAG, values);
                i += 4;
            } else if (arg.equals(OBJECT_INPUT_FLAG)) {
                if (args.length - i < 3) {
                    System.out.println("ERROR: NUMBER OF FLAG PARAMS MUST BE 3!");
                    System.exit(0);
                }
                for (int j = 1; j < 4; j++) {
                    if (!args[i + j].endsWith(".obj")) {
                        System.out.println("ERROR: INVALID EXTENSION " + args[i + j] + "! IT MUST BE .obj ");
                        System.exit(0);
                    }
                    values.add(args[i + j]);
                }
                config.put(OBJECT_INPUT_FLAG, values);
                i += 4;
            } else if (arg.equals(VERBOSE_FLAG) || arg.equals("-verbose") || arg.equals("-verb")) {
                values.add("true");
                config.put(VERBOSE_FLAG, values);
            } else if (arg.equals(HELP_FLAG) || arg.equals("--help")) {
                System.out.println(getHelpString());
                System.exit(0);
            } else {
                System.out.println("ERROR: UNKNOWN FLAG " + arg + "!");
                System.exit(0);
            }
        }
    }

    /**
     * Function that applies the configuration that was set.
     */
    public void applyConfig() {
        for (String key : config.keySet()) {
            switch (key) {
                case VERBOSE_FLAG:
                    if (config.get(key).get(0).equals("true")) {
                        verbose = true;
                    }
                    break;
                case TEXT_INPUT_FLAG:
                    investigatorsTxtFilePath = config.get(key).get(0);
                    investigationGroupsTxtFilePath = config.get(key).get(1);
                    publicationsTxtFilePath = config.get(key).get(2);
                    break;
                case OBJECT_INPUT_FLAG:
                    investigatorsObjFilePath = config.get(key).get(0);
                    investigationGroupsObjFilePath = config.get(key).get(1);
                    publicationsObjFilePath = config.get(key).get(2);

            }
        }
    }

    // region Overridden Methods
    @Override
    public String toString() {
        return "CISUCManagerConfigurator{" + "config=" + config + '}';
    }
    // endregion Overridden Methods
    //endregion Public Methods

    // region Private Methods
    /**
     * Function that returns the -help flag message
     *
     * @return the -help flag message
     */
    private String getHelpString() {
        return "Available flags:\n"
                + "-help    displays this message\n"
                + "-v       activates CISUCManager startup logging\n"
                + "-txt     $investigatorsTxtFile $investigationTxtGroupFile $publicationsTxtFile    sets the path for the correspondent input text files\n"
                + "-obj     $investigatorsObjFile $investigationGroupObjFile $publicationsObjFile   sets the path for the correspondent input/output object files\n";
    }

    /**
     * Function that sets the default config. It may be overridden after.
     */
    private void setDefaultConfig() {
        ArrayList<String> txtFilePaths = new ArrayList<>();
        ArrayList<String> objFilePaths = new ArrayList<>();
        ArrayList<String> verboseValue = new ArrayList<>();
        txtFilePaths.add(DEFAULT_INVESTIGATORS_TXT_FILE_PATH);
        txtFilePaths.add(DEFAULT_INVESTIGATION_GROUPS_TXT_FILE_PATH);
        txtFilePaths.add(DEFAULT_PUBLICATIONS_TXT_FILE_PATH);
        objFilePaths.add(DEFAULT_INVESTIGATORS_OBJ_FILE_PATH);
        objFilePaths.add(DEFAULT_INVESTIGATION_GROUPS_OBJ_FILE_PATH);
        objFilePaths.add(DEFAULT_PUBLICATIONS_OBJ_FILE_PATH);
        verboseValue.add(DEFAULT_VERBOSE_VALUE);
        config.put(TEXT_INPUT_FLAG, txtFilePaths);
        config.put(OBJECT_INPUT_FLAG, objFilePaths);
        config.put(VERBOSE_FLAG, verboseValue);
    }
    // endregion Private Methods
}
