import java.util.ArrayList;
import java.util.Random;

public class Agent extends Person{
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
        if (jailTerm > 0) {
            // If this agent is in jail
            decreaseJailTerm();
        } else {
            if (null == getPosition()) {
                // If this agent just leave the jail, a new position will be
                // allocated to him.
                this.setPosition(
                        getPersonEnvironment().acquireAvailablePosition(this));
            }

            // If movement switch is true, the agent can move to another
            // position
            if (this.getPersonEnvironment().isMovement()) {
                move();
            }

            // Agent is not in jail
            double netRisk = getArrestedProbability() * riskAversion;
            if ((getGrievance() - netRisk) > THRESHOLD) {
                active = true;
            } else {
                active = false;
            }
        }
    }

    public void beArrested(int jailTerm) throws Exception {
        this.active = false;
        this.jailTerm = jailTerm;
        this.getPersonEnvironment().releasePosition(this);
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
            if (className.equals("Agent")) {
                agentCount++;
            } else if (className.equals("Cop")) {
                copCount++;
            }
        }

        double estimatedArrestedProbability =
                1 - Math.exp((-1) * K * (double)(copCount / agentCount));
        return estimatedArrestedProbability;
    }

    private void decreaseJailTerm() throws Exception {
        if (jailTerm > 0) {
            jailTerm--;
        } else {
            throw new Exception("Invalid decrease jailTerm! JailTerm is less " +
                    "than or equals 0.");
        }
    }
}
