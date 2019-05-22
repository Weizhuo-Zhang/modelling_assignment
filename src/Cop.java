import java.util.ArrayList;
import java.util.Random;

/*
    Usage: Class for simulating the cop
    Author:
        Heming Li       804996      hemingl1@student.unimelb.edu.au
        An Luo          657605      aluo1@student.unimelb.edu.au
        Weizhuo Zhang   1018329     weizhuoz@student.unimelb.edu.au
*/

public class Cop extends Person{
    public Cop(Environment environment) throws Exception{
        super(environment);
    }

    // Inherited method from person
    public void action() throws Exception {
        // Cop move to another place
        move();

        // Search for agents nearby
        ArrayList<Position> occupiedNeighbor =
                this.getPosition().getOccupiedNeighborhood();
        ArrayList<Agent> arrestList = new ArrayList<>();
        for (Position neighbor : occupiedNeighbor) {
            Person person = neighbor.getOccupiedPerson();
            String className =
                    person.getClass().getName();
            // if this is a agent and is active
            if (className.equals(Person.AGENT)) {
                if (((Agent) person).isActive()) {
                    arrestList.add((Agent) person);
                }
            }
        }

        // If there at least one agent nearby
        if (0 != arrestList.size()) {
            // Randomly pick active agent to jail
            Random random = new Random();
            int maxIndex = arrestList.size();
            int randomIndex = random.nextInt(maxIndex);
            Agent arrestAgent = arrestList.get(randomIndex);
            int jailTerm = random.nextInt(
                    getPersonEnvironment().getMaxJailTerm());
            arrestAgent.beArrested(jailTerm, this);
        }
    }
}
