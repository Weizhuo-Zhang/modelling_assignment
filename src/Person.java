/**
 * Usage: Class for simulating a person, no matter he/she is an agent or cop.
 *
 * @author Weizhuo Zhang (1018329) - weizhuoz@student.unimelb.edu.au
 * @author Heming Li (804996) - hemingl1@student.unimelb.edu.au
 * @author An Luo (657605) - aluo1@student.unimelb.edu.au
 */
public abstract class Person {
  /** Agent type. */
  static final String AGENT = "Agent";
  /** Cop type. */
  static final String COP = "Cop";

  /** Current position of this person. */
  private Position position;
  /** Current environment this person is in. */
  private Environment environment;

  Person(Environment environment) throws Exception {
    this.environment = environment;
    this.position = environment.acquireAvailablePosition(this);
  }

  /**
   * Get current position of the person.
   *
   * @return This person's current position.
   */
  Position getPosition() {
    return position;
  }

  /**
   * Move this person to the specified position.
   *
   * @param position Position this person is going to move to.
   */
  void setPosition(Position position) {
    this.position = position;
  }

  /**
   * Get the environment of current person.
   *
   * @return Current environment this person is in.
   */
  Environment getPersonEnvironment() {
    return environment;
  }

  /**
   * Move the person to another position.
   *
   * @throws Exception
   */
  void move() throws Exception {
    position = environment.moveThePerson(this);
  }

  /**
   * Method indicating the action of the person, which will vary depends on the type of the person
   * (agent or cop).
   *
   * @throws Exception
   */
  public abstract void action() throws Exception;
}
