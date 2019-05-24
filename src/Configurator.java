import java.util.ResourceBundle;

/*
   Usage: reading the parameters in CONFIG_FILE_PATH.properties file
   Author:
       Heming Li       804996      hemingl1@student.unimelb.edu.au
       An Luo          657605      aluo1@student.unimelb.edu.au
       Weizhuo Zhang   1018329     weizhuoz@student.unimelb.edu.au
*/

public class Configurator {
  public static final int MAP_WIDTH = 40;
  public final int MAP_HEIGHT = 40;
  private static final float DENSITY_MIN = 0.0f;
  private static final float DENSITY_MAX = 100.0f;
  private static final float VISION_MIN = 0.0f;
  private static final float VISION_MAX = 10.0f;
  private static final float GOVERNMENT_LEGITIMACY_MIN = 0.0f;
  private static final float GOVERNMENT_LEGITIMACY_MAX = 1.0f;
  private static final int MAX_JAIL_TERM_MIN = 0;
  private static final int MAX_JAIL_TERM_MAX = 50;
  private static String CONFIG_FILE_PATH = "config";

  private float copDensity;
  private float agentDensity;
  private float vision;
  private float governmentLegitimacy;
  private int maxJailTerm;
  private boolean movementSwitch;

  private int iterationTimes;

  public Configurator(String configFilePath) throws Exception {
    // Read resource file (configuration file)
    ResourceBundle resource = ResourceBundle.getBundle(configFilePath);
    copDensity = Float.parseFloat(resource.getString("COP_DENSITY"));
    agentDensity = Float.parseFloat(resource.getString("AGENT_DENSITY"));
    vision = Float.parseFloat(resource.getString("VISION"));
    governmentLegitimacy = Float.parseFloat(resource.getString("GOVERNMENT_LEGITIMACY"));
    maxJailTerm = Integer.parseInt(resource.getString("MAX_JAIL_TERM"));
    movementSwitch = Boolean.parseBoolean(resource.getString("MOVEMENT_SWITCH"));
    iterationTimes = Integer.parseInt(resource.getString("ITERATION_TIMES"));
    // Check the range of parameters
    checkParameters();
    copDensity = copDensity * 0.01f;
    agentDensity = agentDensity * 0.01f;
  }

  public Configurator() throws Exception {
    this(CONFIG_FILE_PATH);
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
              + "(COP_DENSITY + AGENT_DENSITY) is between ("
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
          "Invalid vision! Please ensure VISION is "
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
          "Invalid government legitimacy! Please ensure"
              + " GOVERNMENT_LEGITIMACY is between ["
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
              + "MAX_JAIL_TERM is between ["
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
