import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

/*
    Usage: Class used for printing the charts
        It is convenient to check the correctness of our model and design.
    Author:
        Heming Li       804996      hemingl1@student.unimelb.edu.au
        An Luo          657605      aluo1@student.unimelb.edu.au
        Weizhuo Zhang   1018329     weizhuoz@student.unimelb.edu.au
 */

public class ChartPrinter extends Application {
    private static ArrayList<Integer> quietList;
    private static ArrayList<Integer> jailedList;
    private static ArrayList<Integer> activeList;
    private static ArrayList<Integer> waitingTimeList;

    public static void main(
            String[] args,
            ArrayList<Integer> quietListParam,
            ArrayList<Integer> jailedListParam,
            ArrayList<Integer> activeListParam,
            ArrayList<Integer> waitingTimeListParam) {
        quietList = quietListParam;
        jailedList = jailedListParam;
        activeList = activeListParam;
        waitingTimeList = waitingTimeListParam;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Agents");
        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setTitle("All agent types");
        lineChart.setCreateSymbols(false);

        XYChart.Series<Number, Number> quietSeries =
                new XYChart.Series<Number, Number>();
        quietSeries.setName("Quiet");

        XYChart.Series<Number, Number> jailedSeries =
                new XYChart.Series<Number, Number>();
        jailedSeries.setName("Jailed");

        XYChart.Series<Number, Number> activeSeries =
                new XYChart.Series<Number, Number>();
        activeSeries.setName("Active");

        int iterationTimes = quietList.size();
        for (int i = 0; i < iterationTimes; i++) {
            quietSeries.getData().add(
                    new XYChart.Data<>(i, quietList.get(i)));
            jailedSeries.getData().add(
                    new XYChart.Data<>(i, jailedList.get(i)));
            activeSeries.getData().add(
                    new XYChart.Data<>(i, activeList.get(i)));
        }

        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(activeSeries);
        lineChart.getData().add(jailedSeries);
        lineChart.getData().add(quietSeries);

        if (null != waitingTimeList) {
            extensionStage();
        }
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    // Print the waiting times frequency chart
    private void extensionStage() {
        Stage secondStage = new Stage();
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Active Times");
        yAxis.setLabel("Waiting times frequency");

        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setTitle("Waiting time Distribution");
        lineChart.setCreateSymbols(false);

        XYChart.Series<Number, Number> waitingTimeSeries =
                new XYChart.Series<Number, Number>();
        waitingTimeSeries.setName("WaitingTime");

        int iterationTimes = waitingTimeList.size();
        for (int i = 0; i < iterationTimes; i++) {
            waitingTimeSeries.getData().add(
                    new XYChart.Data<>((i + 1) * 10, waitingTimeList.get(i)));
        }

        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(waitingTimeSeries);

        secondStage.setScene(scene);
        secondStage.show();
    }

    // Set color for each line
    private void setRGBColor(
            XYChart.Series series,
            int red, int green, int blue) {
        //Node fill = series.getNode().lookup()
        Node line = series.getNode().lookup(".chart-series-line");
        String rgb = String.format("%d, %d, %d", red, green, blue);
        line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
    }
}
