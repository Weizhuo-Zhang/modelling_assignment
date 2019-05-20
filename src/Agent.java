import java.util.ArrayList;
import java.util.Random;

public class Agent extends Person{
    public static final int NO_JAIL_TERM = 0;
    // K is a positive constant value to ensure reasonable value of
    // estimated arrested probability.
    private static final float K = 2.3f;
    // Threshold for rebellion (perceivedHardship - netRisk)
    private static final float THRESHOLD = 0.1f;
    private double riskAversion;
    private double perceivedHardship;
    private boolean active;
    private int jailTerm;

    public Agent(Environment environment) throws Exception{
        super(environment);
        Random random = new Random();
        riskAversion = random.nextDouble();
        perceivedHardship = random.nextDouble();
        active = false;
        jailTerm = 0;
    }

    public boolean isActive() {
        return active;
    }

    public int getJailTerm() {
        return jailTerm;
    }

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
            if ((grievance - netRisk) > THRESHOLD) {
                active = true;
            } else {
                active = false;
            }
        }

//        if (jailTerm > 0) {
//            // If this agent is in jail
//            decreaseJailTerm();
//        } else {
////            if (null == getPosition()) {
////                // If this agent just leave the jail, a new position will be
////                // allocated to him.
////                this.setPosition(
////                        getPersonEnvironment().acquireAvailablePosition(this));
////            }
//
//            // If movement switch is true, the agent can move to another
//            // position
//            if (this.getPersonEnvironment().isMovement()) {
//                move();
//            }
//
//            // Agent is not in jail
//            double arrestedProbability = getArrestedProbability();
//            double netRisk = arrestedProbability * riskAversion;
//            double grievance = getGrievance();
//            if ((grievance - netRisk) > THRESHOLD) {
//                active = true;
//            } else {
//                active = false;
//            }
//        }
    }

    public void beArrested(int jailTerm, Person cop) throws Exception {
        this.active = false;
        this.jailTerm = jailTerm;
        this.getPersonEnvironment().releasePosition(this, cop);
    }

    private double getGrievance() {
        double antiGovernment =
                1.0 - getPersonEnvironment().getGovernmentLegitimacy();
        return (perceivedHardship * antiGovernment);
    }

    private double getArrestedProbability() {
        ArrayList<Position> occupiedNeighbor =
                this.getPosition().getOccupiedNeighborhood();
        int copCount = 0;
        int agentCount = 1;
        for (Position neighbor : occupiedNeighbor) {
            String className =
                    neighbor.getOccupiedPerson().getClass().getName();
            if (className.equals(Person.AGENT)) {
                Agent agent = (Agent)neighbor.getOccupiedPerson();
                if (agent.isActive()) {
                    agentCount++;
                }
            } else if (className.equals(Person.COP)) {
                copCount++;
            }
        }

        double ratio = Math.floor(copCount / agentCount);
        double estimatedArrestedProbability =
                1 - Math.exp(-1 * K * ratio);
        return estimatedArrestedProbability;
    }

    public void decreaseJailTerm() throws Exception {
        if (jailTerm > NO_JAIL_TERM) {
            jailTerm--;
        } else {
            throw new Exception("Invalid decrease jailTerm! JailTerm is less " +
                    "than or equals 0.");
        }
    }
}
