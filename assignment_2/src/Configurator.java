import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/**
 * Usage: reading the parameters from configuration file.
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

  Configurator(String configFilePath) throws Exception {
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

  Configurator() throws Exception {
    this(DEFAULT_CONFIG_FILE_PATH);
  }

  /**
   * Get the vision set up for person (both agent and cop).
   *
   * @return Vision of person, which is defined in the config file.
   */
  public float getVision() {
    return vision;
  }

  /**
   * Get the movement status.
   *
   * @return movement status, which is defined in the config file.
   */
  public boolean isMovement() {
    return movementSwitch;
  }

  /**
   * Get cop density.
   *
   * @return Cop's density, which is defined in the config file.
   */
  float getCopDensity() {
    return copDensity;
  }

  /**
   * Get agent density.
   *
   * @return Agents' density, which is defined in the config file.
   */
  float getAgentDensity() {
    return agentDensity;
  }

  /**
   * Get the legitimacy of government.
   *
   * @return Government's legitimacy, which is defined in the config file.
   */
  float getGovernmentLegitimacy() {
    return governmentLegitimacy;
  }

  /**
   * Get the max jail term.
   *
   * @return Max jail term, which is defined in the config file.
   */
  int getMaxJailTerm() {
    return maxJailTerm;
  }

  /**
   * Get the iteration time.
   *
   * @return Iteration time, which is defined in the config file.
   */
  int getIterationTimes() {
    return iterationTimes;
  }

  /**
   * Get the output file name.
   *
   * @return Output file name, which is defined in the config file.
   */
  String getOutputFileName() {
    return this.outputFileName;
  }

  /**
   * Check the range of the parameters
   *
   * @throws Exception
   */
  private void checkParameters() throws Exception {
    checkDensity();
    checkVision();
    checkGovernmentLegitimacy();
    checkMaxJailTerm();
  }

  /**
   * Check the density value.
   *
   * @throws Exception Exception is thrown when total density is less than the minimum value or
   *     greater than the maximum value.
   */
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

  /**
   * Check the vision value.
   *
   * @throws Exception Exception is thrown when vision is smaller than the minimum value or greater
   *     than the maximum value.
   */
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

  /**
   * Check government legitimacy value.
   *
   * @throws Exception Exception is thrown when government legitimacy is less than the minimum value
   *     or greater than the maximum value.
   */
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

  /**
   * Check the max jailed term.
   *
   * @throws Exception Exception is thrown if max jail term is less than the minimum value or
   *     greater than the maximum value.
   */
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
}
