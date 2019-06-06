import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

/**
 * Usage: Main entrance of this program
 *
 * @author Weizhuo Zhang (1018329) - weizhuoz@student.unimelb.edu.au
 * @author Heming Li (804996) - hemingl1@student.unimelb.edu.au
 * @author An Luo (657605) - aluo1@student.unimelb.edu.au
 */
class Main {
  public static void main(String[] args) {
    try {
      boolean printChart = false;

      // Initialize the configuration, read value from configuration file into the instance.
      Configurator configurator;
      if (args.length != 0) {
        String commandSpecified = args[0];

        if (commandSpecified.equals("PrintChart")) {
          printChart = true;
          configurator = new Configurator();
        } else {
          configurator = new Configurator(commandSpecified);
        }
      } else {
        configurator = new Configurator();
      }

      Environment environment =
          Environment.getEnvironment(
              configurator.getVision(),
              configurator.MAP_WIDTH,
              configurator.MAP_HEIGHT,
              configurator.getMaxJailTerm(),
              configurator.getGovernmentLegitimacy(),
              configurator.isMovement());

      // Compute the size of the map and the count of agent and cop.
      int mapSize = configurator.MAP_HEIGHT * configurator.MAP_WIDTH;
      int copCount = (int) Math.floor(configurator.getCopDensity() * mapSize);
      int agentCount = (int) Math.floor(configurator.getAgentDensity() * mapSize);
      ArrayList<Person> personList = new ArrayList<>();
      ArrayList<Agent> agentList = new ArrayList<>();

      // Init cops
      for (int i = 0; i < copCount; i++) {
        Person person = new Cop(environment);
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
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
      String date = df.format(new Date());

      String outputFileName = configurator.getOutputFileName();
      if (outputFileName == null || outputFileName.isEmpty()) {
        outputFileName = "out_" + date;
      }
      File csvOut = new File(outputFileName + ".csv");

      BufferedWriter bw = new BufferedWriter(new FileWriter(csvOut, false));
      bw.write("Export_plot data,,\n");
      bw.write("Rebellion model,,\n");
      bw.write(date + ",,\n");
      bw.write(",,\n");
      bw.write("Model Settings,,\n");
      bw.write("Agent Density," + configurator.getAgentDensity() * 100 + "%,\n");
      bw.write("Cop Density," + configurator.getCopDensity() * 100 + "%,\n");
      bw.write("Vision," + configurator.getVision() + ",\n");
      bw.write("Government Legitimacy," + configurator.getGovernmentLegitimacy() + ",\n");
      bw.write("Max Jail Term," + configurator.getMaxJailTerm() + ",\n");
      bw.write("Movement?," + Boolean.toString(configurator.isMovement()) + ",\n");
      bw.write("Iteration times," + configurator.getIterationTimes() + ",\n");
      bw.write(",,\n");
      bw.write("quiet, jailed, active\n");
      bw.write(agentCount + ", 0, 0\n");
      quietList.add(agentCount);
      jailedList.add(0);
      activeList.add(0);
      int waitingTime = 0;

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
          } else if (agent.isActive()) {
            activeCount++;
          } else {
            quietCount++;
          }
        }

        // Used for compute the frequency of the waiting times
        if (activeCount > 50) {
          if (0 != waitingTime) {
            waitingTimeList.add(waitingTime);
            waitingTime = 0;
          }
        } else {
          waitingTime++;
        }

        bw.write(quietCount + ", " + jailedCount + ", " + activeCount + "\n");
        quietList.add(quietCount);
        jailedList.add(jailedCount);
        activeList.add(activeCount);
      }

      bw.flush();
      bw.close();

      // Compute the frequency of this model
      Collections.sort(waitingTimeList);
      ArrayList<Integer> waitingFrequencyList = computeFrequency(waitingTimeList);

      if (printChart) {
        // Print chart
        ChartPrinter.main(args, quietList, jailedList, activeList, waitingFrequencyList);
      }
    } catch (Exception e) {
      System.err.println("[ERROR]: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Generate random array for given size
   *
   * @param size Size of the array.
   * @return An array of objects whose value is randomly generated, and size of the array is the
   *     provided parameter.
   */
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

  /**
   * Compute the frequency from [0,10], (10,11],...[10n,11n] where n is natural number.
   *
   * @param sortedList Sorted list.
   * @return An ArrayList of integer, indicating the frequency.
   */
  private static ArrayList<Integer> computeFrequency(ArrayList<Integer> sortedList) {
    int length = sortedList.size();
    if (0 == length) {
      return null;
    }

    int maxNum = (int) Math.ceil((float) sortedList.get(length - 1) * 0.1);
    int cursor = 0;
    ArrayList<Integer> frequencyList = new ArrayList<>();
    for (int i = 0; i < maxNum; i++) {
      int count = 0;
      for (; cursor < length; cursor++) {
        int tempValue = sortedList.get(cursor);
        if (tempValue >= i * 10 && tempValue <= (i + 1) * 10) {
          count++;
        } else {
          frequencyList.add(count);
          cursor++;
          break;
        }
      }
    }
    return frequencyList;
  }
}
