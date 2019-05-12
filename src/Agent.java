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

    public Agent(Position position, int maxJailTerm) throws Exception{
        super(position, maxJailTerm);
        Random random = new Random();
        riskAversion = random.nextDouble();
        perceivedHardship = random.nextDouble();
        active = false;
        jailTerm = 0;
    }

    public double getRiskAversion() {
        return riskAversion;
    }

    public double getPerceivedHardship() {
        return perceivedHardship;
    }

    // TODO: release Position
    // TODO: governmentLegitimacy how to use
    public void beArrested(int jailTerm) {
        this.active = false;
        this.jailTerm = jailTerm;
    }

    public boolean isActive() {
        return active;
    }

    public void decreaseJailTerm() {
        if (jailTerm > 0) {
            jailTerm--;
        }
    }

    public int getJailTerm() {
        return jailTerm;
    }

    public void action() {
        double netRisk = getArrestedProbability() * riskAversion;
        if ((perceivedHardship - netRisk) > THRESHOLD) {
            active = true;
        }
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
}
