import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ChartPrinter extends Application {
    private static ArrayList<Integer> quietList;
    private static ArrayList<Integer> jailedList;
    private static ArrayList<Integer> activeList;

    public static void main(
            String[] args,
            ArrayList<Integer> quietListParam,
            ArrayList<Integer> jailedListParam,
            ArrayList<Integer> activeListParam) {
        quietList = quietListParam;
        jailedList = jailedListParam;
        activeList = activeListParam;
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

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setRGBColor(
            XYChart.Series series,
            int red, int green, int blue) {
        //Node fill = series.getNode().lookup()
        Node line = series.getNode().lookup(".chart-series-line");
        String rgb = String.format("%d, %d, %d", red, green, blue);
        line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
    }
}
