package ru.vas.khmyrov;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.math3.random.RandomDataGenerator;
import ru.vas.khmyrov.utils.IntegerTextField;

import java.util.*;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Main extends Application {
    Label inputSizeLabel;
    Label inputSizeFitnessLabel;

    IntegerTextField inputSizeText;
    IntegerTextField inputSizeFitnessText;
    int inputSizeInt;
    int inputSizeFitnessInt;

    Button btn;
    Button btnStartGeneticAlgorithm;
    Button btnStartSwapAlgorithm;
    Button btnEvaluateBestSum;
    Button btnFinalSelection;

    TextArea outputAllArrayTextArea;

    TextArea outputFitnessArrayTextArea0;
    TextArea outputFitnessArrayTextArea1;
    TextArea outputFitnessArrayTextArea2;
    TextArea outputFitnessArrayTextArea3;
    TextArea outputFitnessArrayTextArea4;
    TextArea outputFitnessArrayTextArea5;
    TextArea outputFitnessArrayTextArea6;
    TextArea outputFitnessArrayTextArea7;
    TextArea outputFitnessArrayTextArea8;
    TextArea outputFitnessArrayTextArea9;
    TextArea outputFinalArrayTextArea;

    Label outputFitnessLabel0;
    Label outputFitnessLabel1;
    Label outputFitnessLabel2;
    Label outputFitnessLabel3;
    Label outputFitnessLabel4;
    Label outputFitnessLabel5;
    Label outputFitnessLabel6;
    Label outputFitnessLabel7;
    Label outputFitnessLabel8;
    Label outputFitnessLabel9;
    Label theBestSumOfTop10Arrays;
    Label sumAfterSelectionOfTop10Arrays;
    Label timeToEvaluateByGeneticAlgorithmLbl;
    Label timeToEvaluateBySwapAlgorithmLbl;

    AreaChart<Number, Number> areaChart;

    HashMap<Integer, Double> tmpRandValueHashMap;
    HashMap<Integer, Double> tmpRandValueHashMapCloneToSwap;
    HashMap<Integer, Double> resultFitnessGeneratedMap;
    List<List<Double>> top10ResultsByMinSum;
    List<Double> finalArrayAfterGeneticSelection;

    double theBestSum = 0.0;
    int theBestSumIndexOfArrays = 0;
    //    final int COUNT_OF_ARRAY_BEFORE_SELECTION = 10;
    //    final static int COUNT_OF_ARRAY_BEFORE_SELECTION = 10;
    long startGeneticFunctionTime = 0;
    long finishGeneticFunctionTime = 0;
    long differenceGeneticFunctionTime = 0;
    long startSwapFunctionTime = 0;
    long finishSwapFunctionTime = 0;
    long differenceSwapFunctionTime = 0;

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Genetic algorithm application");
        primaryStage.setWidth(800);
        primaryStage.setHeight(1000);
        showStartWindow(primaryStage);
    }

    public void showStartWindow(Stage primaryStage) {
        initComponentsOnStartWindow(primaryStage);

        addlistenersToIntegerTextFields(btn, inputSizeText, inputSizeFitnessText);
        addListenersToStartGeneticAlgorithmButton(btnStartGeneticAlgorithm);
        addListenersToStartSwapAlgorithmButton(btnStartSwapAlgorithm);
        addListenersToEvaluateBestSumButton(btnEvaluateBestSum);
        addListenersToFinalSelectionButton(btnFinalSelection);

        primaryStage.show();
    }

    private void addListenersToEvaluateBestSumButton(Button btnEvaluateBestSum) {
        btnEvaluateBestSum.setOnAction(event -> {
            if (tmpRandValueHashMap == null || tmpRandValueHashMap.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Сначала сгенерируйте случайные значения!");
                alert.showAndWait();
                return;
            }

            if (top10ResultsByMinSum == null || top10ResultsByMinSum.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Сначала примените функцию к сгенерированным значениям!");
                alert.showAndWait();
                return;
            }

            theBestSum = returnTheBestSumOfTop10Arrays(inputSizeFitnessInt);
            theBestSumIndexOfArrays = returnIndexOfTheBestSumOfTop10Arrays(inputSizeFitnessInt);
            theBestSumOfTop10Arrays.setText("Лучшая сумма до скрещивания = " + theBestSum);
        });
    }

    private double returnTheBestSumOfTop10Arrays(int lastIndexIsSum) {
        double result = Double.MAX_VALUE; //example max value of double
        for (int i = 0; i < 10; i++) {
            double tmp = top10ResultsByMinSum.get(i).get(lastIndexIsSum);
            if (tmp < result) {
                result = tmp;
            }
        }

        return result;
    }

    private int returnIndexOfTheBestSumOfTop10Arrays(int lastIndexIsSum) {
        double result = Double.MAX_VALUE; //example max value of double
        int index = 0;
        for (int i = 0; i < 10; i++) {
            double tmp = top10ResultsByMinSum.get(i).get(lastIndexIsSum);
            if (tmp < result) {
                result = tmp;
                index = i;
            }
        }

        return index;
    }

    private void addListenersToFinalSelectionButton(Button btnFinalSelection) {
        btnFinalSelection.setOnAction(event -> {
            if (tmpRandValueHashMap == null || tmpRandValueHashMap.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Сначала сгенерируйте случайные значения!");
                alert.showAndWait();
                return;
            }

            if (top10ResultsByMinSum == null || top10ResultsByMinSum.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Сначала примените функцию к сгенерированным значениям!");
                alert.showAndWait();
                return;
            }

            evaluateTheWorstAndBadEightValuesOfArray(theBestSumIndexOfArrays);

        });
    }

    private void evaluateTheWorstAndBadEightValuesOfArray(int indexOfTheBestSumArray) {
        finalArrayAfterGeneticSelection = new ArrayList<>(inputSizeFitnessInt);
        int limit = inputSizeFitnessInt / 10;

        List<Double> currentTmpBestArray = top10ResultsByMinSum.get(indexOfTheBestSumArray);
        finalArrayAfterGeneticSelection.addAll(currentTmpBestArray.stream().sorted().limit(limit).collect(toList()));

        System.out.println(finalArrayAfterGeneticSelection);

        int limitCount;
        for (int i = 0; i < 10; i++) {
            if (i == indexOfTheBestSumArray) {
                continue;
            }
            currentTmpBestArray = top10ResultsByMinSum.get(i).stream().sorted().collect(toList());
            limitCount = 0;
            for (int j = 0; limitCount < limit && j < inputSizeFitnessInt; j++) {
                double currentTmpElement = currentTmpBestArray.get(j);
                if (!finalArrayAfterGeneticSelection.contains(currentTmpElement)) {
                    finalArrayAfterGeneticSelection.add(currentTmpElement);
                    limitCount++;
                }
            }
        }

        System.out.println("Длина финального массива = " + finalArrayAfterGeneticSelection.size());
        System.out.println("Arrays.toString = " + Arrays.toString(finalArrayAfterGeneticSelection.toArray()));

        double finalSum = finalArrayAfterGeneticSelection.stream().mapToDouble(d -> d).sum();
        System.out.println(finalSum);
        sumAfterSelectionOfTop10Arrays.setText("Cумма элементов после скрещивания = " + finalSum);
        outputFinalArrayTextArea.setText(finalArrayAfterGeneticSelection.toString());
        System.out.println("сумма элементов после скрещивания = " + finalSum);
    }

    private void addListenersToStartGeneticAlgorithmButton(Button btnStartAlgorithm) {
        btnStartAlgorithm.setOnAction(event -> {
            if (tmpRandValueHashMap == null || tmpRandValueHashMap.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Сначала сгенерируйте случайные значения!");
                alert.showAndWait();
                return;
            }
            System.out.println("--------FITNESS GENETIC FUNCTION CALCULATING--------------");

            top10ResultsByMinSum = new ArrayList<>(10);

            startTimeOfEvaluatingGeneticFunction();
            List<List<Double>> resultFitnessGeneratedLists = genericRandomArraysFromFitnessArray(inputSizeInt, inputSizeFitnessInt, 50_000);
            System.out.println("все эле-ты до скрещивания");

            top10ResultsByMinSum.addAll(resultFitnessGeneratedLists.parallelStream().sorted((o1, o2) -> {
                if (o1.get(o1.size() - 1).equals(o2.get(o2.size() - 1))) {
                    return 0;
                } else if (o1.get(o1.size() - 1) > o2.get(o2.size() - 1)) {
                    return 1;
                } else {
                    return -1;
                }
            }).limit(10).collect(toList()));

            for (List<Double> doubles : top10ResultsByMinSum) {
                System.out.println(doubles);
            }

            finishTimeOfEvaluatingGeneticFunction();
            fillOutputTextAreasAndFields(inputSizeFitnessInt);
        });
    }

    // TODO: 22.12.2020 Доделать метод перестановки
    private void addListenersToStartSwapAlgorithmButton(Button btnStartSwapAlgorithm) {
        btnStartSwapAlgorithm.setOnAction(event -> {
            if (tmpRandValueHashMap == null || tmpRandValueHashMap.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Сначала сгенерируйте случайные значения!");
                alert.showAndWait();
                return;
            }
            System.out.println("--------FITNESS SWAP FUNCTION CALCULATING--------------");

            startTimeOfEvaluatingSwapFunction();

            resultFitnessGeneratedMap = genericRandomArrayWithInputLength(inputSizeInt, inputSizeFitnessInt);
            System.out.println("все эле-ты до обмена");
            final int ITERATOR_COUNT_TO_SWAP_ALGORITHM = 100_000;

            startSwapAlgorithm(resultFitnessGeneratedMap, ITERATOR_COUNT_TO_SWAP_ALGORITHM);

            fillOutputFinalArrayTextAreaBySwapAlgorithm();

            finishTimeOfEvaluatingSwapFunction();

        });
    }

    private void fillOutputFinalArrayTextAreaBySwapAlgorithm() {
        sumAfterSelectionOfTop10Arrays.setText("Сумма элементов = " + resultFitnessGeneratedMap.get(inputSizeInt));
        resultFitnessGeneratedMap.remove(inputSizeInt);
        outputFinalArrayTextArea.setText(resultFitnessGeneratedMap.toString() + "\n" + resultFitnessGeneratedMap.values());
    }

    // TODO: 23.12.2020

    private boolean swapTwoElementsFromMapsIfNewSumIsBest(int indexOfBigMap, int indexOfSmallResultMap) {
        tmpRandValueHashMapCloneToSwap.put(indexOfSmallResultMap, tmpRandValueHashMap.get(indexOfSmallResultMap));
        resultFitnessGeneratedMap.remove(indexOfSmallResultMap);
        resultFitnessGeneratedMap.put(indexOfBigMap, tmpRandValueHashMap.get(indexOfBigMap));
        double newSum = resultFitnessGeneratedMap.entrySet().stream().filter(pair -> pair.getKey() != inputSizeInt).mapToDouble(Map.Entry::getValue).sum();
        if (newSum < resultFitnessGeneratedMap.get(inputSizeInt)) {
            resultFitnessGeneratedMap.put(inputSizeInt, newSum);
            return true;
        } else {
            resultFitnessGeneratedMap.remove(indexOfBigMap);
            resultFitnessGeneratedMap.put(indexOfSmallResultMap, tmpRandValueHashMap.get(indexOfSmallResultMap));
            tmpRandValueHashMapCloneToSwap.put(indexOfSmallResultMap, null);
            return false;
        }

    }

    private void startSwapAlgorithm(HashMap<Integer, Double> inputArrayListToSwap, final int iterator_count) {
        RandomDataGenerator rdg = new RandomDataGenerator();
        tmpRandValueHashMapCloneToSwap = new HashMap<>(tmpRandValueHashMap);

        //затираем в null все ключи из 1-ой Map, которые сгенирировались в необходимой Map.
        Set<Integer> uniqueKeys = inputArrayListToSwap.keySet();
        for (Map.Entry<Integer, Double> map : tmpRandValueHashMapCloneToSwap.entrySet()) {
            if (uniqueKeys.contains(map.getKey())) {
                tmpRandValueHashMapCloneToSwap.put(map.getKey(), null);
            }
        }

        int randomInt;
        int tmpCurrentIndexOfBigArray;
        int tmpCurrentIndexOfResultSwapArray;

        for (int i = 0; i < iterator_count; i++) {
            tmpCurrentIndexOfBigArray = rdg.nextInt(0, inputSizeInt - 1);
            while (inputArrayListToSwap.containsKey(tmpCurrentIndexOfBigArray)) {
                tmpCurrentIndexOfBigArray = rdg.nextInt(0, inputSizeInt - 1);
            }

            randomInt = rdg.nextInt(0, uniqueKeys.size() - 1);
            while (!uniqueKeys.stream().filter(key -> key != inputSizeInt).skip(randomInt).findFirst().isPresent()) {
                randomInt = rdg.nextInt(0, uniqueKeys.size() - 1);
            }
            tmpCurrentIndexOfResultSwapArray = uniqueKeys.stream().filter(key -> key != inputSizeInt).skip(randomInt).findFirst().get();

            System.out.println("tmpCurrentIndexOfResultSwapArray = " + tmpCurrentIndexOfResultSwapArray);
            boolean isSwapped = swapTwoElementsFromMapsIfNewSumIsBest(tmpCurrentIndexOfBigArray, tmpCurrentIndexOfResultSwapArray);
        }
    }

    private void addlistenersToIntegerTextFields(Button btn, IntegerTextField inputSizeText, IntegerTextField inputSizeFitnessText) {
        btn.setOnAction(event -> {
            if ((inputSizeText.getText() != null && !inputSizeText.getText().isEmpty()) && Integer.parseInt(inputSizeText.getText()) > 10) {
                inputSizeInt = Integer.parseInt(inputSizeText.getText());
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Общее количество катушек должно быть больше 10!");
                alert.showAndWait();
                return;
            }

            if ((inputSizeFitnessText.getText() != null && !inputSizeFitnessText.getText().isEmpty()) && Integer.parseInt(inputSizeFitnessText.getText()) > 10) {
                inputSizeFitnessInt = Integer.parseInt(inputSizeFitnessText.getText());
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Необходимое количество катушек должно быть больше 10!");
                alert.showAndWait();
                return;
            }

            double rangeMin = 0.0;
            double rangeMax = 0.5;
            Random r = new Random();
            tmpRandValueHashMap = new HashMap<>();
            for (int i = 0; i < inputSizeInt; i++) {
                double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();//1 var
                String randomValueStr = String.format("%.5f", randomValue).replace(",", ".");
                tmpRandValueHashMap.put(i, Double.parseDouble(randomValueStr));

                System.out.print(randomValueStr + ";");
            }
            System.out.println();

            for (Map.Entry<Integer, Double> map : tmpRandValueHashMap.entrySet()) {
                System.out.print("key=" + map.getKey());
                System.out.println(" value" + map.getValue());
            }

            System.out.println();
            System.out.println("END------------------------END_STRING_ARRAY");
            System.out.println();
            outputAllArrayTextArea.setText(tmpRandValueHashMap.values().stream().map(Object::toString).collect(joining(";")));

        });
    }

    private List<List<Double>> genericRandomArraysFromFitnessArray(int sizeOfInputArray, int sizeOfOutputArray, int countOfOutputIterationArrays) {
        List<List<Double>> outputList = new ArrayList<>(countOfOutputIterationArrays);
        List<Double> tmpList;
        Set<Integer> tmpUniqueIndexes;
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
        int countIndex; // == 0
        int randomInt;
        double currentValueByKey;
        for (int i = 0; i < countOfOutputIterationArrays; i++) {
            countIndex = 0;
            tmpList = new ArrayList<>(sizeOfOutputArray + 1);
            tmpUniqueIndexes = new HashSet<>(sizeOfOutputArray);
            while (countIndex < sizeOfOutputArray) {
                randomInt = randomDataGenerator.nextInt(0, sizeOfInputArray - 1);
                if (!tmpUniqueIndexes.contains(randomInt)) {
                    countIndex++;
                } else {
                    continue;
                }
                tmpUniqueIndexes.add(randomInt);
                currentValueByKey = tmpRandValueHashMap.get(randomInt);
                tmpList.add(currentValueByKey);
            }

            tmpList.add(sizeOfOutputArray, Double.parseDouble(String.format("%.5f", tmpList.stream().mapToDouble(d -> d).sum())
                    .replace(",", ".")));
            outputList.add(tmpList);
        }


        return outputList;
    }

    private HashMap<Integer, Double> genericRandomArrayWithInputLength(int sizeOfInputArray, int sizeOfOutputArray) {
        HashMap<Integer, Double> result = new HashMap<>(sizeOfOutputArray + 1);
        Set<Integer> tmpUniqueIndexes = new HashSet<>(sizeOfOutputArray);
        RandomDataGenerator rdg = new RandomDataGenerator();
        int randomInt;
        double sum = 0;
        double currentValueByKey;
        int countIndex = 0;

        while (countIndex < sizeOfOutputArray) {
            randomInt = rdg.nextInt(0, sizeOfInputArray - 1);
            if (!tmpUniqueIndexes.contains(randomInt)) {
                countIndex++;
            } else {
                continue;
            }
            currentValueByKey = tmpRandValueHashMap.get(randomInt);
            sum += currentValueByKey;
            tmpUniqueIndexes.add(randomInt);
            result.putIfAbsent(randomInt, currentValueByKey);
        }

        result.putIfAbsent(sizeOfInputArray, Double.parseDouble(String.format("%.5f", sum).replace(",", ".")));

        return result;
    }

    private void initComponentsOnStartWindow(Stage primaryStage) {
        inputSizeLabel = new Label("Введите общее количество катушек > 10");
        inputSizeText = new IntegerTextField();
        inputSizeFitnessLabel = new Label("Введите необходимое количество катушек");
        inputSizeFitnessText = new IntegerTextField();

        btn = new Button();
        btn.setText("Сгенерировать случайные числа");
        btnStartGeneticAlgorithm = new Button();
        btnStartGeneticAlgorithm.setText("Применить генетический алгоритм");
        btnStartSwapAlgorithm = new Button();
        btnStartSwapAlgorithm.setText("Применить алгоритм обмена");
        btnFinalSelection = new Button();
        btnFinalSelection.setText("Скрещивание");
        btnEvaluateBestSum = new Button();
        btnEvaluateBestSum.setText("Лучшая сумма");

        outputAllArrayTextArea = new TextArea();
        outputAllArrayTextArea.setEditable(false);
        outputAllArrayTextArea.setWrapText(true);
        outputFitnessArrayTextArea0 = new TextArea();
        outputFitnessArrayTextArea1 = new TextArea();
        outputFitnessArrayTextArea2 = new TextArea();
        outputFitnessArrayTextArea3 = new TextArea();
        outputFitnessArrayTextArea4 = new TextArea();
        outputFitnessArrayTextArea5 = new TextArea();
        outputFitnessArrayTextArea6 = new TextArea();
        outputFitnessArrayTextArea7 = new TextArea();
        outputFitnessArrayTextArea8 = new TextArea();
        outputFitnessArrayTextArea9 = new TextArea();
        outputFinalArrayTextArea = new TextArea();
        outputFitnessArrayTextArea0.setMaxWidth(500);
        outputFitnessArrayTextArea1.setMaxWidth(500);
        outputFitnessArrayTextArea2.setMaxWidth(500);
        outputFitnessArrayTextArea3.setMaxWidth(500);
        outputFitnessArrayTextArea4.setMaxWidth(500);
        outputFitnessArrayTextArea5.setMaxWidth(500);
        outputFitnessArrayTextArea6.setMaxWidth(500);
        outputFitnessArrayTextArea7.setMaxWidth(500);
        outputFitnessArrayTextArea8.setMaxWidth(500);
        outputFitnessArrayTextArea9.setMaxWidth(500);
        outputFitnessArrayTextArea0.setMaxHeight(50);
        outputFitnessArrayTextArea1.setMaxHeight(50);
        outputFitnessArrayTextArea2.setMaxHeight(50);
        outputFitnessArrayTextArea3.setMaxHeight(50);
        outputFitnessArrayTextArea4.setMaxHeight(50);
        outputFitnessArrayTextArea5.setMaxHeight(50);
        outputFitnessArrayTextArea6.setMaxHeight(50);
        outputFitnessArrayTextArea7.setMaxHeight(50);
        outputFitnessArrayTextArea8.setMaxHeight(50);
        outputFitnessArrayTextArea9.setMaxHeight(50);
        outputFitnessArrayTextArea0.setEditable(false);
        outputFitnessArrayTextArea1.setEditable(false);
        outputFitnessArrayTextArea2.setEditable(false);
        outputFitnessArrayTextArea3.setEditable(false);
        outputFitnessArrayTextArea4.setEditable(false);
        outputFitnessArrayTextArea5.setEditable(false);
        outputFitnessArrayTextArea6.setEditable(false);
        outputFitnessArrayTextArea7.setEditable(false);
        outputFitnessArrayTextArea8.setEditable(false);
        outputFitnessArrayTextArea9.setEditable(false);
        outputFinalArrayTextArea.setEditable(false);
//        outputFitnessArrayTextArea.setWrapText(true);

        outputFitnessLabel0 = new Label();
        outputFitnessLabel1 = new Label();
        outputFitnessLabel2 = new Label();
        outputFitnessLabel3 = new Label();
        outputFitnessLabel4 = new Label();
        outputFitnessLabel5 = new Label();
        outputFitnessLabel6 = new Label();
        outputFitnessLabel7 = new Label();
        outputFitnessLabel8 = new Label();
        outputFitnessLabel9 = new Label();
        timeToEvaluateByGeneticAlgorithmLbl = new Label();
        timeToEvaluateBySwapAlgorithmLbl = new Label();
        theBestSumOfTop10Arrays = new Label();
        sumAfterSelectionOfTop10Arrays = new Label();

        final NumberAxis xAxis = new NumberAxis(1, 1000, 1);
        final NumberAxis yAxis = new NumberAxis();
        areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setTitle("Revenue");

        areaChart.setLegendSide(Side.BOTTOM);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        // теперь заполняем его данными
        // тут я строю правую ветку параболы
        for (int x = 0; x < 100; ++x) {
            // чтобы добавить к нашему массиву точек данные,
            // мы обращаемся к нему через series.getData()
            // и используем метод add
            //
            // методу add в качестве параметров передаем точку с координатами (x, x^2)
            // делаем это для x меняющегося от 0 до 99
            series.getData().add(new XYChart.Data<>(x, x * x));
        }

        // сформированный массив точек, передаем графику для отображения
        areaChart.getData().setAll(series);

        FlowPane evaluateSumPanel = new FlowPane(Orientation.HORIZONTAL, btnEvaluateBestSum, theBestSumOfTop10Arrays);
        evaluateSumPanel.setHgap(10);

        FlowPane evaluateFinalSumPanel = new FlowPane(Orientation.HORIZONTAL, btnFinalSelection, sumAfterSelectionOfTop10Arrays);
        evaluateSumPanel.setHgap(10);

        FlowPane algorithmButtonsPanel = new FlowPane(Orientation.HORIZONTAL, btnStartGeneticAlgorithm, btnStartSwapAlgorithm);
        algorithmButtonsPanel.setAlignment(Pos.CENTER);
        algorithmButtonsPanel.setHgap(10);

        Menu m = new Menu("Menu");
        MenuItem m1 = new MenuItem("menu item 1");
        MenuItem m2 = new MenuItem("menu item 2");
        MenuItem m3 = new MenuItem("menu item 3");
        m.getItems().add(m1);
        m.getItems().add(m2);
        m.getItems().add(m3);
        MenuBar mb = new MenuBar(m);
        VBox vb = new VBox(mb);

        FlowPane root = new FlowPane(Orientation.VERTICAL, vb, inputSizeLabel, inputSizeText, inputSizeFitnessLabel, inputSizeFitnessText,
                btn, outputAllArrayTextArea, algorithmButtonsPanel,
                outputFitnessArrayTextArea0, outputFitnessLabel0, outputFitnessArrayTextArea1, outputFitnessLabel1,
                outputFitnessArrayTextArea2, outputFitnessLabel2, outputFitnessArrayTextArea3, outputFitnessLabel3,
                outputFitnessArrayTextArea4, outputFitnessLabel4, outputFitnessArrayTextArea5, outputFitnessLabel5,
                outputFitnessArrayTextArea6, outputFitnessLabel6, outputFitnessArrayTextArea7, outputFitnessLabel7,
                outputFitnessArrayTextArea8, outputFitnessLabel8, outputFitnessArrayTextArea9, outputFitnessLabel9,
                evaluateSumPanel,
                evaluateFinalSumPanel,
                outputFinalArrayTextArea,
                timeToEvaluateByGeneticAlgorithmLbl, timeToEvaluateBySwapAlgorithmLbl
                , areaChart
        );
        root.setVgap(10);
        Scene scene = new Scene(root, 700, 700);
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
    }

    private void fillOutputTextAreasAndFields(int lastIndexIsSum) {
        outputFitnessArrayTextArea0.setText(String.valueOf(top10ResultsByMinSum.get(0).stream().filter(val -> val < 10).map(Object::toString).count()));
        outputFitnessArrayTextArea0.setText(top10ResultsByMinSum.get(0).stream().filter(val -> val < 10).map(Object::toString).collect(joining(";")));
        outputFitnessArrayTextArea1.setText(top10ResultsByMinSum.get(1).stream().filter(val -> val < 10).map(Object::toString).collect(joining(";")));
        outputFitnessArrayTextArea2.setText(top10ResultsByMinSum.get(2).stream().filter(val -> val < 10).map(Object::toString).collect(joining(";")));
        outputFitnessArrayTextArea3.setText(top10ResultsByMinSum.get(3).stream().filter(val -> val < 10).map(Object::toString).collect(joining(";")));
        outputFitnessArrayTextArea4.setText(top10ResultsByMinSum.get(4).stream().filter(val -> val < 10).map(Object::toString).collect(joining(";")));
        outputFitnessArrayTextArea5.setText(top10ResultsByMinSum.get(5).stream().filter(val -> val < 10).map(Object::toString).collect(joining(";")));
        outputFitnessArrayTextArea6.setText(top10ResultsByMinSum.get(6).stream().filter(val -> val < 10).map(Object::toString).collect(joining(";")));
        outputFitnessArrayTextArea7.setText(top10ResultsByMinSum.get(7).stream().filter(val -> val < 10).map(Object::toString).collect(joining(";")));
        outputFitnessArrayTextArea8.setText(top10ResultsByMinSum.get(8).stream().filter(val -> val < 10).map(Object::toString).collect(joining(";")));
        outputFitnessArrayTextArea9.setText(top10ResultsByMinSum.get(9).stream().filter(val -> val < 10).map(Object::toString).collect(joining(";")));

        outputFitnessLabel0.setText("Сумма = " + top10ResultsByMinSum.get(0).get(lastIndexIsSum).toString());
        outputFitnessLabel1.setText("Сумма = " + top10ResultsByMinSum.get(1).get(lastIndexIsSum).toString());
        outputFitnessLabel2.setText("Сумма = " + top10ResultsByMinSum.get(2).get(lastIndexIsSum).toString());
        outputFitnessLabel3.setText("Сумма = " + top10ResultsByMinSum.get(3).get(lastIndexIsSum).toString());
        outputFitnessLabel4.setText("Сумма = " + top10ResultsByMinSum.get(4).get(lastIndexIsSum).toString());
        outputFitnessLabel5.setText("Сумма = " + top10ResultsByMinSum.get(5).get(lastIndexIsSum).toString());
        outputFitnessLabel6.setText("Сумма = " + top10ResultsByMinSum.get(6).get(lastIndexIsSum).toString());
        outputFitnessLabel7.setText("Сумма = " + top10ResultsByMinSum.get(7).get(lastIndexIsSum).toString());
        outputFitnessLabel8.setText("Сумма = " + top10ResultsByMinSum.get(8).get(lastIndexIsSum).toString());
        outputFitnessLabel9.setText("Сумма = " + top10ResultsByMinSum.get(9).get(lastIndexIsSum).toString());
    }

    private void startTimeOfEvaluatingGeneticFunction() {
        startGeneticFunctionTime = System.currentTimeMillis();
    }

    private void finishTimeOfEvaluatingGeneticFunction() {
        finishGeneticFunctionTime = System.currentTimeMillis();
        if (finishGeneticFunctionTime > startGeneticFunctionTime) {
            differenceGeneticFunctionTime = (finishGeneticFunctionTime - startGeneticFunctionTime) / 1000; // ms -> seconds
            System.out.println("Время выполнения генетическим алгоритмом = " + differenceGeneticFunctionTime + " секунд");
            timeToEvaluateByGeneticAlgorithmLbl.setText("Время выполнения генетическим алгоритмом = " + differenceGeneticFunctionTime + " с");
        }
    }

    private void startTimeOfEvaluatingSwapFunction() {
        startSwapFunctionTime = System.currentTimeMillis();
    }

    private void finishTimeOfEvaluatingSwapFunction() {
        finishSwapFunctionTime = System.currentTimeMillis();
        if (finishSwapFunctionTime > startSwapFunctionTime) {
            differenceSwapFunctionTime = (finishSwapFunctionTime - startSwapFunctionTime) / 1000; // ms -> seconds
            System.out.println("Время выполнения алгоритма обмена = " + differenceSwapFunctionTime + " секунд");
            timeToEvaluateBySwapAlgorithmLbl.setText("Время выполнения алгоритмом обмена = " + differenceSwapFunctionTime + " с");
        }
    }
}
