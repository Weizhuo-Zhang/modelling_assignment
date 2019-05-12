import java.util.ArrayList;

/*
    Usage: Main entrance of this program
    Author:
        Heming Li       804996      hemingl1@student.unimelb.edu.au
        An Luo          657605      aluo1@student.unimelb.edu.au
        Weizhuo Zhang   1018329     weizhuoz@student.unimelb.edu.au
*/

class Main {
    public static void main(String[] args) {
        try {
            Configurator configurator = Configurator.getConfigurator();
            Environment environment = Environment.getEnvironment(
                    configurator.getVision(),
                    configurator.MAP_WIDTH,
                    configurator.MAP_HEIGHT);
            int mapSize = configurator.MAP_HEIGHT * configurator.MAP_WIDTH;
            int copCount =
                    (int)Math.floor(configurator.getCopDensity() * mapSize);
            int agentCount =
                    (int)Math.floor(configurator.getAgentDensity() * mapSize);
            ArrayList<Cop> copList = new ArrayList<>();
            ArrayList<Agent> agentList = new ArrayList<>();
            for (int i = 0; i < copCount; i++) {
                Position position = environment.acquireAvailablePosition();
                Person person =
                        new Cop(position, configurator.getMaxJailTerm());
                copList.add((Cop) person);
            }
            for (int i = 0; i < agentCount; i++) {
                Position position = environment.acquireAvailablePosition();
                Person person =
                        new Agent(position, configurator.getMaxJailTerm());
                agentList.add((Agent) person);
            }
        } catch (Exception e) {
            System.err.println("[ERROR]: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
