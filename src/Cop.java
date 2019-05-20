import java.util.ArrayList;
import java.util.Random;

public class Cop extends Person{
    public Cop(Environment environment) throws Exception{
        super(environment);
    }

    public void action() throws Exception {
        move();
        ArrayList<Position> occupiedNeighbor =
                this.getPosition().getOccupiedNeighborhood();
        ArrayList<Agent> arrestList = new ArrayList<>();
        for (Position neighbor : occupiedNeighbor) {
            Person person = neighbor.getOccupiedPerson();
            String className =
                    person.getClass().getName();
            if (className.equals(Person.AGENT)) {
                if (((Agent) person).isActive()) {
                    arrestList.add((Agent) person);
                }
            }
        }

        if (0 != arrestList.size()) {
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
