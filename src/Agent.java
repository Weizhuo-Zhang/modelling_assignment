public class Agent extends Person{
    private double riskAversion;
    private double perceivedHardship;
    private boolean active;
    private int jailTerm;

    public Agent(){
        super();
    }

    public double getRiskAversion() {
        return riskAversion;
    }

    public double getPerceivedHardship() {
        return perceivedHardship;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void decreaseJailTerm() {

    }

    public int getJailTerm() {
        return jailTerm;
    }
}
