import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/*
    Usage: Main entrance of this program
    Author:
        Heming Li       804996      hemingl1@student.unimelb.edu.au
        An Luo          657605      aluo1@student.unimelb.edu.au
        Weizhuo Zhang   1018329     weizhuoz@student.unimelb.edu.au
*/

class Main{
    public static void main(String[] args) {
        try {
            Configurator configurator = Configurator.getConfigurator();
            Environment environment = Environment.getEnvironment(
                    configurator.getVision(),
                    configurator.MAP_WIDTH,
                    configurator.MAP_HEIGHT,
                    configurator.getMaxJailTerm(),
                    configurator.getGovernmentLegitimacy(),
                    configurator.isMovement());

            // Compute the size of the map and the count of agent and cop.
            int mapSize = configurator.MAP_HEIGHT * configurator.MAP_WIDTH;
            int copCount =
                    (int)Math.floor(configurator.getCopDensity() * mapSize);
            int agentCount =
                    (int)Math.floor(configurator.getAgentDensity() * mapSize);
            ArrayList<Person> personList = new ArrayList<>();
            ArrayList<Agent> agentList = new ArrayList<>();

            // Init cops
            for (int i = 0; i < copCount; i++) {
                Person person =
                        new Cop(environment);
                personList.add(person);
            }

            // Init agents
            for (int i = 0; i < agentCount; i++) {
                Person person = new Agent(environment);
                personList.add(person);
                agentList.add((Agent) person);
            }

            int iterationTimes = configurator.getIterationTimes();
            int totalCount = personList.size();
            ArrayList<Integer> quietList = new ArrayList<>();
            ArrayList<Integer> jailedList = new ArrayList<>();
            ArrayList<Integer> activeList = new ArrayList<>();
            ArrayList<Integer> waitingTimeList = new ArrayList<>();
            SimpleDateFormat df =
                    new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
            String date = df.format(new Date());
            File csvOut = new File("out_" + date + ".csv");
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(csvOut, false));
            bw.write("Export_plot data,,\n");
            bw.write("Rebellion model,,\n");
            bw.write(date + ",,\n");
            bw.write(",,\n");
            bw.write("Model Settings,,\n");
            bw.write("Agent Density," +
                    configurator.getAgentDensity() * 100 + "%,\n");
            bw.write("Cop Density," +
                    configurator.getCopDensity() * 100 + "%,\n");
            bw.write("Vision," + configurator.getVision() + ",\n");
            bw.write("Government Legitimacy," +
                    configurator.getGovernmentLegitimacy() + ",\n");
            bw.write("Max Jail Term," +
                    configurator.getMaxJailTerm() + ",\n");
            bw.write("Movement?," +
                    Boolean.toString(configurator.isMovement()) + ",\n");
            bw.write("Iteration times," +
                    configurator.getIterationTimes() + ",\n");
            bw.write(",,\n");
            bw.write("quiet, jailed, active\n");
            bw.write(agentCount + ", 0, 0\n");
            quietList.add(agentCount);
            jailedList.add(0);
            activeList.add(0);
            int waitingTime = 0;
            int activeTime = 0;
            int internal = 10;

            for (int i = 0; i < iterationTimes; i++) {
                int quietCount = 0;
                int jailedCount = 0;
                int activeCount = 0;

                Object[] values = generateRandomArray(totalCount);

                for (Object value : values) {
                    personList.get((int) value).action();
                }

                for (Agent agent : agentList) {
                    if (agent.getJailTerm() > Agent.NO_JAIL_TERM) {
                        // Agent is in jail
                        agent.decreaseJailTerm();
                        jailedCount++;
//                    } else if (null == agent.getPosition()) {
//                        // Agent will leave jail in next turn
//                        jailedCount++;
                    } else if (agent.isActive()) {
                        activeCount++;
                    } else {
                        quietCount++;
                    }
                }

                if (activeCount > 50) {
                    activeTime++;
                } else {
                    waitingTime++;
                }

                if (0 == (activeTime % internal) && 0 != activeTime) {
                    waitingTimeList.add(waitingTime);
                    waitingTime = 0;
                    internal += 10;
                }

                bw.write(quietCount + ", " +
                        jailedCount + ", " + activeCount +"\n");
                quietList.add(quietCount);
                jailedList.add(jailedCount);
                activeList.add(activeCount);
            }

            bw.flush();
            bw.close();
            ChartPrinter.main(
                    args, quietList, jailedList, activeList, waitingTimeList);
        } catch (Exception e) {
            System.err.println("[ERROR]: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Object[] generateRandomArray(int size) {
        Random random = new Random();
        ArrayList<Integer> rangeList = new ArrayList<>();
        Object[] values = new Object[size];
        for (int i = 0; i < size; i++) {
            rangeList.add(i);
        }

        for (int i = 0; i < size; i++) {
            int maxIndex = rangeList.size();
            int index = random.nextInt(maxIndex);
            values[i] = rangeList.get(index);
            rangeList.remove(index);
        }
        return values;
    }
}
