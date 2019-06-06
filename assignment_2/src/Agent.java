import java.util.ArrayList;
import java.util.Random;

/**
 * Usage: Class for simulating the Agent.
 *
 * @author Weizhuo Zhang (1018329) - weizhuoz@student.unimelb.edu.au
 * @author Heming Li (804996) - hemingl1@student.unimelb.edu.au
 * @author An Luo (657605) - aluo1@student.unimelb.edu.au
 */
public class Agent extends Person {
  static final int NO_JAIL_TERM = 0;
  /** A positive constant value to ensure reasonable value of estimated arrested probability. */
  private static final float K = 2.3f;
  /** Threshold for rebellion (perceivedHardship - netRisk) */
  private static final float THRESHOLD = 0.1f;

  private double riskAversion;
  private double perceivedHardship;
  private boolean active;
  private int jailTerm;

  public Agent(Environment environment) throws Exception {
    super(environment);
    Random random = new Random();
    riskAversion = random.nextDouble();
    perceivedHardship = random.nextDouble();
    active = false;
    jailTerm = 0;
  }

  /**
   * Get the flag indicating if current agent is active.
   *
   * @return true if the agent is active, otherwise false.
   */
  public boolean isActive() {
    return active;
  }

  // Action method is implemented from its superclass

  /**
   * This method simulates what the agent might do in a new turn (tick).
   *
   * @throws Exception
   */
  public void action() throws Exception {
    if (NO_JAIL_TERM == jailTerm) {
      // If movement switch is true, the agent can move to another
      // position
      if (this.getPersonEnvironment().isMovement()) {
        move();
      } else {
        Position position = this.getPosition();
        if (null == position.getOccupiedPerson()) {
          position.occupy(this);
        }
      }

      // Agent is not in jail
      double arrestedProbability = getArrestedProbability();
      double netRisk = arrestedProbability * riskAversion;
      double grievance = getGrievance();
      active = (grievance - netRisk) > THRESHOLD;
    }
  }

  /**
   * Get the remaining jail term of current agent.
   *
   * @return number of jail term remained for current agent.
   */
  int getJailTerm() {
    return jailTerm;
  }

  /**
   * Method being called when agent is arrested. jail term, cop, and position of current agent will
   * be updated.
   *
   * @param jailTerm Jail term of the agent.
   * @param cop Cop who arrested this agent.
   * @throws Exception
   */
  void beArrested(int jailTerm, Person cop) throws Exception {
    this.active = false;
    this.jailTerm = jailTerm;
    this.getPersonEnvironment().releasePosition(this, cop);
  }

  /**
   * Decrease the jail term for the agent by one.
   *
   * @throws Exception Exception is thrown if the agent is not in jail.
   */
  void decreaseJailTerm() throws Exception {
    if (jailTerm > NO_JAIL_TERM) {
      jailTerm--;
    } else {
      throw new Exception("Invalid decrease jailTerm! JailTerm is less than or equals to 0.");
    }
  }

  /**
   * Get current grievance of current agent.
   *
   * @return current grievance of the agent.
   */
  private double getGrievance() {
    double antiGovernment = 1.0 - getPersonEnvironment().getGovernmentLegitimacy();
    return (perceivedHardship * antiGovernment);
  }

  /**
   * Get the probability that current agent is going to be arrested.
   *
   * <p>Acknowledgement: Formula is provided by NetLogo.
   *
   * @return Probability that current agent is going to be arrested.
   */
  private double getArrestedProbability() {
    ArrayList<Position> occupiedNeighbor = this.getPosition().getOccupiedNeighborhood();
    int copCount = 0;
    int agentCount = 1;
    for (Position neighbor : occupiedNeighbor) {
      String className = neighbor.getOccupiedPerson().getClass().getName();
      if (className.equals(Person.AGENT)) {
        Agent agent = (Agent) neighbor.getOccupiedPerson();
        if (agent.isActive()) {
          agentCount++;
        }
      } else if (className.equals(Person.COP)) {
        copCount++;
      }
    }

    double ratio = Math.floor(copCount / agentCount);
    return 1 - Math.exp(-1 * K * ratio);
  }
}
