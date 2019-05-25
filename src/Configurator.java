import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/**
 * Usage: reading the parameters in DEFAULT_CONFIG_FILE_PATH.properties file
 *
 * @author Weizhuo Zhang (1018329) - weizhuoz@student.unimelb.edu.au
 * @author Heming Li (804996) - hemingl1@student.unimelb.edu.au
 * @author An Luo (657605) - aluo1@student.unimelb.edu.au
 */
public class Configurator {
  public final int MAP_WIDTH = 40;
  public final int MAP_HEIGHT = 40;

  private final String COP_DENSITY_LABEL = "COP_DENSITY";
  private final String AGENT_DENSITY_LABEL = "AGENT_DENSITY";
  private final String VISION_LABEL = "VISION";
  private final String GOVERNMENT_LEGITIMACY_LABEL = "GOVERNMENT_LEGITIMACY";
  private final String MAX_JAIL_TERM_LABEL = "MAX_JAIL_TERM";
  private final String MOVEMENT_SWITCH_LABEL = "MOVEMENT_SWITCH";
  private final String ITERATION_TIMES_LABEL = "ITERATION_TIMES";
  private final String OUTPUT_FILE_NAME_LABEL = "OUTPUT_FILE_NAME";

  private final float DENSITY_MIN = 0.0f;
  private final float DENSITY_MAX = 100.0f;
  private final float VISION_MIN = 0.0f;
  private final float VISION_MAX = 10.0f;
  private final float GOVERNMENT_LEGITIMACY_MIN = 0.0f;
  private final float GOVERNMENT_LEGITIMACY_MAX = 1.0f;
  private final int MAX_JAIL_TERM_MIN = 0;
  private final int MAX_JAIL_TERM_MAX = 50;

  private static String DEFAULT_CONFIG_FILE_PATH = "config.properties";

  private float copDensity;
  private float agentDensity;
  private float vision;
  private float governmentLegitimacy;
  private int maxJailTerm;
  private boolean movementSwitch;
  private int iterationTimes;
  private String outputFileName;

  public Configurator(String configFilePath) throws Exception {
    // Read resource file (configuration file)
    BufferedReader bufferedReader = new BufferedReader(new FileReader(configFilePath));
    HashMap<String, String> configValues = new HashMap<>();
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      System.out.println(line);
      String[] configLineComponent = line.split("=");
      if (configLineComponent.length == 2) {
        configValues.put(configLineComponent[0], configLineComponent[1]);
      }
    }

    // Load the data from config file into the instance variables.
    copDensity = Float.parseFloat(configValues.get(COP_DENSITY_LABEL));
    agentDensity = Float.parseFloat(configValues.get(AGENT_DENSITY_LABEL));
    vision = Float.parseFloat(configValues.get(VISION_LABEL));
    governmentLegitimacy = Float.parseFloat(configValues.get(GOVERNMENT_LEGITIMACY_LABEL));
    maxJailTerm = Integer.parseInt(configValues.get(MAX_JAIL_TERM_LABEL));
    movementSwitch = Boolean.parseBoolean(configValues.get(MOVEMENT_SWITCH_LABEL));
    iterationTimes = Integer.parseInt(configValues.get(ITERATION_TIMES_LABEL));

    try {
      outputFileName = configValues.get(OUTPUT_FILE_NAME_LABEL);
    } catch (Exception e) {
      outputFileName = "";
    }

    // Check the range of parameters
    checkParameters();
    copDensity = copDensity * 0.01f;
    agentDensity = agentDensity * 0.01f;
  }

  public Configurator() throws Exception {
    this(DEFAULT_CONFIG_FILE_PATH);
  }

  public float getCopDensity() {
    return copDensity;
  }

  public float getAgentDensity() {
    return agentDensity;
  }

  public float getVision() {
    return vision;
  }

  public float getGovernmentLegitimacy() {
    return governmentLegitimacy;
  }

  public int getMaxJailTerm() {
    return maxJailTerm;
  }

  public boolean isMovement() {
    return movementSwitch;
  }

  public int getIterationTimes() {
    return iterationTimes;
  }

  public String getOutputFileName() {
    return this.outputFileName;
  }

  // Check the range of the parameters
  private void checkParameters() throws Exception {
    checkDensity();
    checkVision();
    checkGovernmentLegitimacy();
    checkMaxJailTerm();
  }

  // Check the range of density
  private void checkDensity() throws Exception {
    float totalDensity = copDensity + agentDensity;
    if (totalDensity <= DENSITY_MIN || totalDensity >= DENSITY_MAX) {
      throw new Exception(
          "Invalid density! Please ensure "
              + "("
              + COP_DENSITY_LABEL
              + "+ "
              + AGENT_DENSITY_LABEL
              + ") is between ("
              + DENSITY_MIN
              + ", "
              + DENSITY_MAX
              + ").");
    }
  }

  // Check the range of the vision
  private void checkVision() throws Exception {
    if (vision < VISION_MIN || vision > VISION_MAX) {
      throw new Exception(
          "Invalid vision! Please ensure "
              + VISION_LABEL
              + " is "
              + "between ["
              + VISION_MIN
              + ", "
              + VISION_MAX
              + "].");
    }
  }

  // Check the range of government legitimacy
  private void checkGovernmentLegitimacy() throws Exception {
    if (governmentLegitimacy < GOVERNMENT_LEGITIMACY_MIN
        || governmentLegitimacy > GOVERNMENT_LEGITIMACY_MAX) {
      throw new Exception(
          "Invalid government legitimacy! Please ensure "
              + GOVERNMENT_LEGITIMACY_LABEL
              + " is between ["
              + GOVERNMENT_LEGITIMACY_MIN
              + ", "
              + GOVERNMENT_LEGITIMACY_MAX
              + "].");
    }
  }

  // Check the range of max jail term
  private void checkMaxJailTerm() throws Exception {
    if (maxJailTerm < MAX_JAIL_TERM_MIN || maxJailTerm > MAX_JAIL_TERM_MAX) {
      throw new Exception(
          "Invalid max jail term! Please ensure "
              + MAX_JAIL_TERM_LABEL
              + " is between ["
              + MAX_JAIL_TERM_MIN
              + ", "
              + MAX_JAIL_TERM_MAX
              + "].");
    }
  }

  private void testParameters() {
    System.out.println(copDensity);
    System.out.println(agentDensity);
    System.out.println(vision);
    System.out.println(governmentLegitimacy);
    System.out.println(maxJailTerm);
    System.out.println(movementSwitch);
  }
}
