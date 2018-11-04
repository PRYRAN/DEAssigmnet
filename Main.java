package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Locale;

public class Main extends Application {


    private static double f(double x, double y) {
        // Returns my F(x,y) = y`
        return 1 + (y * (2 * x - 1)) / (x * x);
    }

    private static double h = 0.5;
    private static double initX = 1;
    private static double initY = 1;
    private static double limitX = 4;
    private static double N = 10;

    private static LineChart<Number, Number> graph;

    private static double[] runge(double input[], double x[]) {
        double k[] = new double[4];
        int i = 0;
        double tempX = initX;
        double tempY = initY;
        for (int j = 0; j < input.length; j++) {
            k[0] = f(tempX, tempY);
            k[1] = f(tempX + h / 2, tempY + h / 2 * k[0]);
            k[2] = f(tempX + h / 2, tempY + h / 2 * k[1]);
            k[3] = f(tempX + h, tempY + h * k[2]);
            tempY = tempY + h / 6 * (k[0] + 2 * k[1] + 2 * k[2] + k[3]);
            tempX = x[(j+1)%(input.length)];
            input[i++] = tempY;
        }


        return input;

    }

    private static double[] raw(double input[], double x[]) {
        // Exact function
        double tempX = initX + h;
        double tempY;
        int i = 0;
        for (int j = 0; j < input.length; j++) {
            tempY = tempX * tempX;
            input[i++] = tempY;
            tempX = x[(j+1)%(input.length)];
        }
        return input;
    }

    private static double[] euler(double input[], double x[]) {
        double tempX = initX;
        double tempY = initY;
        int i = 0;
        for (int j = 0; j < input.length; j++) {
            tempY = tempY + h * f(tempX, tempY);

            input[i++] = tempY;
            tempX = x[(j+1)%(input.length)];
        }
        return input;

    }

    private static double[] improvedEuler(double input[], double x[]) {
        double tempX = initX;
        double tempY = initY;
        double temp;
        int i = 0;
        for (int j = 0; j < input.length; j++) {
            temp = tempY + h * f(tempX, tempY);
            tempY = tempY + h / 2 * (f(tempX, tempY) + f(tempX + h, temp));

            input[i++] = tempY;
            tempX = x[(j+1)%(input.length)];
        }
        return input;

    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        launch();

    }

    @Override
    public void start(Stage primaryStage) {
        // Initialization
        // data is a 2 dimension array where i store all data that is required to plot a graph
        // data[0] - exact, data[1] - euler, data[2] - improved euler, data[3] - runge, data[4] - array of Xs
        double[][] data = initData();
        Group root = new Group();
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        graph = new LineChart<>(xAxis, yAxis);
        graph.setCreateSymbols(false);

        // Plotting charts
        graph.getData().add(getChart(data[4], data[0], "Exact"));
        graph.getData().add(getChart(data[4], data[1], "Euler"));
        graph.getData().add(getChart(data[4], data[2], "Improved Euler"));
        graph.getData().add(getChart(data[4], data[3], "Runge"));

        // each box contain label and input textfield
        // each one for each global variable.
        BorderPane box1 = new BorderPane();
        TextField text1 = new TextField(String.format("%.2f", initX));
        Label label1 = new Label("Init X");
        box1.setLeft(label1);
        box1.setRight(text1);

        BorderPane box2 = new BorderPane();
        TextField text2 = new TextField(String.format("%.2f", initY));
        Label label2 = new Label("Init Y");
        box2.setLeft(label2);
        box2.setRight(text2);

        BorderPane box3 = new BorderPane();
        TextField text3 = new TextField(String.format("%.2f", limitX));
        Label label3 = new Label("Limit X");
        box3.setLeft(label3);
        box3.setRight(text3);

        BorderPane box4 = new BorderPane();
        TextField text4 = new TextField(String.format("%.2f", h));
        Label label4 = new Label("H");
        box4.setLeft(label4);
        box4.setRight(text4);

        BorderPane box5 = new BorderPane();
        TextField text5 = new TextField(String.format("%.2f", N));
        Label label5 = new Label("N");
        box5.setLeft(label5);
        box5.setRight(text5);

        // toggle button(and radio) is providing ability to choice what type of grap you are looking for
        ToggleButton typeBut = new ToggleButton("Error");
        ToggleGroup group = new ToggleGroup();

        RadioButton localError = new RadioButton("Local");
        RadioButton globalError = new RadioButton("Global");

        localError.setSelected(true);

        localError.setToggleGroup(group);
        globalError.setToggleGroup(group);

        typeBut.setSelected(false);

        // button that you have to click in order to update the charts.
        Button but = new Button();
        but.setText("Update");

        // mouse event listener
        but.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // updating all variables
                initX = Double.parseDouble(text1.getText());
                initY = Double.parseDouble(text2.getText());
                limitX = Double.parseDouble(text3.getText());
                h = Double.parseDouble(text4.getText());
                N = Double.parseDouble(text5.getText());

                // computation of points (xi,yi,)
                double[][] data = initData();
                // creal the graph after previous configuration.
                graph.getData().clear();
                if (typeBut.isSelected()) {
                    if (localError.isSelected()) {
                        data = initDataForError(data);
                        graph.getData().add(getChart(data[4], data[1], "Euler Error"));
                        graph.getData().add(getChart(data[4], data[2], "Improved Euler Error"));
                        graph.getData().add(getChart(data[4], data[3], "Runge Error"));
                    } else {
                        // on each step of global error i find max local error, then creating point (step;max_local_error)
                        // i use that certain formula to compute step because that guarantee that step will not greater that interval [Xo,X]
                        double step = ((limitX - initX) / N);
                        // temp array that contains points for global error graph.
                        double temp[][] = new double[2][(int) (N / step)];
                        // on each iteration i compute charts of methods via certain step, then find max error.
                        for (int i = 1; i <= temp[0].length - 1; i++) {
                            h = i * step;
                            // init data for methods
                            data = initData();
                            // init data for errors of methods
                            data = initDataForError(data);
                            temp[0][i - 1] = h;
                            double temp1 = findMax(data);
                            temp[1][i - 1] = temp1;
                        }

                        XYChart.Series tempSeries =getChart(temp[0], temp[1], "Global error") ;
                        tempSeries.getData().remove(0);
                        graph.getData().addAll(tempSeries);
                        graph.getData();


                    }

                } else {
                    graph.getData().add(getChart(data[4], data[0], "Exact"));
                    graph.getData().add(getChart(data[4], data[1], "Euler"));
                    graph.getData().add(getChart(data[4], data[2], "Improved Euler"));
                    graph.getData().add(getChart(data[4], data[3], "Runge"));
                }
            }
        });
        // all nodes( UI control elements) are structured via VBox layout.
        VBox dataVBox = new VBox();
        dataVBox.getChildren().addAll(box1, box2, box3, box4, box5);

        VBox typeVBox = new VBox();
        typeVBox.getChildren().addAll(but, typeBut, localError, globalError);

        // another box that contains two Vboxes.
        HBox topBox = new HBox();

        topBox.setSpacing(5);
        topBox.getChildren().addAll(typeVBox, dataVBox);
        topBox.setPadding(new Insets(0, 10, 10, 10));

        BorderPane bord = new BorderPane();
        bord.setTop(topBox);

        bord.setCenter(graph);


        root.getChildren().add(bord);
        primaryStage.setScene(new Scene(root, 505, 540));

        primaryStage.show();

    }

    /**
     *
     * @param data is 2dimension array of points (x,y)
     * @return max value from all subarrays
     */
    private double findMax(double[][] data) {
        double max = 0;
        int temp = 0;
        for (int i = 1; i <= 3; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] > max) {
                    max = data[i][j];
                    temp = i;
                }
            }
        }

        return max;
    }

    /**
     *
     * @param x array of Xs
     * @param y array of Ys
     * @param name is name of the chart
     * @return object of type Series feeded with points
     */
    private XYChart.Series getChart(double[] x, double[] y, String name) {
        XYChart.Series serie = new XYChart.Series();
        serie.setName(name);
        // putting very first values of charts. I am doing that because my methods are computing values
        // that was based on initial values, thus we still need points of IV to start chart from right place.
        serie.getData().add(new XYChart.Data(initX, initY));
        for (int j = 0; j < x.length; j++) {
            serie.getData().add(new XYChart.Data(x[j], y[j]));
        }
        return serie;
    }

    /**
     * Computing points of each method.
     * @return 2dimesion array that contain points of each method.
     * data[0] - exact, data[1] - euler, data[2] - improved euler, data[3] - runge, data[4] - array of Xs
     */
    private double[][] initData() {

        int size = (int) ((limitX - initX) / h);
        double[][] data;
        if(initX + size*h < limitX)
        {
            data = new double[5][size+1];
            for (int i = 0; i < data[4].length-1;i++) {
                data[4][i] = initX +(i+1)*h;
            }
            data[4][data[4].length-1] = limitX;
        }
        else
        {
            data = new double[5][size];
            for (int i = 0; i < data[4].length;i++) {
                data[4][i] = initX +(i+1)*h;
            }
        }


        data[0] = raw(data[0],data[4]);
        data[1] = euler(data[1],data[4]);
        data[2] = improvedEuler(data[2],data[4]);
        data[3] = runge(data[3],data[4]);


        return data;
    }

    /**
     * based on computed points of methods, i am computing array of local erorrs of form (Local Error = Exact - Soluting)
     * @param data 2dimension array of points of solutions
     * @return 2dimension array of pointt of local errors.
     */
    private double[][] initDataForError(double[][] data) {
        for (int i = 1; i <= 3; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = Math.abs(data[0][j] - data[i][j]);            }

        }
        return data;

    }


}
