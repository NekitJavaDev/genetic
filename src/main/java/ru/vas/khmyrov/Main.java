package ru.vas.khmyrov;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.apache.commons.math3.random.RandomDataGenerator;
import ru.vas.khmyrov.utils.IntegerTextField;

import java.util.*;

import static java.util.stream.Collectors.joining;

public class Main extends Application {

    Label inputSizeLabel;
    Label inputSizeFitnessLabel;

    IntegerTextField inputSizeText;
    IntegerTextField inputSizeFitnessText;
    int inputSizeInt;
    int inputSizeFitnessInt;

    Button btn;
    Button btnStartAlgorithm;
    Button btnLastIteration;

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
    Label finalElementsSumOfArray;

    HashMap<Integer, Double> tmpRandValueHashMap;
    List<List<Double>> top10ResultsByMinSum;

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Genetic algorithm application");
        primaryStage.setWidth(800);
        primaryStage.setHeight(800);

        showStartWindow(primaryStage);
    }

    public void showStartWindow(Stage primaryStage) {
        initComponentsOnStartWindow(primaryStage);

//        Controller controller = new Controller();
//        controller.setAppFX(this);
        addlistenersToIntegerTextFields(btn, inputSizeText, inputSizeFitnessText);
        addListenersToStartAlgorithmButton(btnStartAlgorithm);
        primaryStage.show();
    }

    //    public void addListenersToStartAlgorithmButton(Button btnStartAlgorithm, TextArea outputFitnessArrayTextArea0, TextArea outputFitnessArrayTextArea1, TextArea outputFitnessArrayTextArea2) {
    public void addListenersToStartAlgorithmButton(Button btnStartAlgorithm) {
        btnStartAlgorithm.setOnAction(event -> {
            if (tmpRandValueHashMap == null || tmpRandValueHashMap.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Сначала сгенерируйте случайные значения!");
                alert.showAndWait();
                return;
            }


            System.out.println("--------FITNESS FUNCTION CALCULATING--------------");

            top10ResultsByMinSum = new ArrayList<>(10);

            for (int i = 0; i < 10; i++) {
                List<List<Double>> resultFitnessGeneratedLists = genericRandomArraysFromFitnessArray(inputSizeInt, inputSizeFitnessInt, 10_000);
                double minTmpSum = resultFitnessGeneratedLists.parallelStream().map(list -> list.get(list.size() - 1)).min(Double::compareTo).get();
                for (List<Double> doubleListWithLastValueAsSum : resultFitnessGeneratedLists) {
                    if (doubleListWithLastValueAsSum.get(inputSizeFitnessInt) == minTmpSum) {
                        top10ResultsByMinSum.add(doubleListWithLastValueAsSum);
                        break;
                    }
                }
            }

            fillOutputTextAreasAndFields();

        });

    }

    public void addlistenersToIntegerTextFields(Button btn, IntegerTextField inputSizeText, IntegerTextField inputSizeFitnessText) {
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
                String randomValueStr = String.format("%.4f", randomValue).replace(",", ".");
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

//            List<Double> tmpValuesFromHashMap = new ArrayList<>(inputSizeInt);
//            tmpValuesFromHashMap.addAll(tmpRandValueHashMap.values().parallelStream().sorted().collect(toList()));
//            List<Double> removedValues = new ArrayList<>(inputSizeInt - inputSizeFitnessInt);
//
//            removedValues.addAll(tmpValuesFromHashMap.subList(inputSizeFitnessInt, tmpValuesFromHashMap.size()));
//            System.out.println("DEETED size = " + removedValues.size());
//            for (int i = inputSizeFitnessInt - 1; i < inputSizeInt; i++) {
//                removedValues.add(tmpValuesFromHashMap.get(i));
//            }

//            Map<Integer, Double> fitnessFinalMapBeforeGenericVariants = new HashMap<>(inputSizeFitnessInt);
//            int iteratorCount = 0;
//            for (Map.Entry<Integer, Double> map : tmpRandValueHashMap.entrySet()) {
//                if (removedValues.contains(map.getValue())) {
////                    map.setValue(null);
//                    continue;
//                }
//                fitnessFinalMapBeforeGenericVariants.put(iteratorCount, map.getValue());
//                iteratorCount++;
//            }

            for (Map.Entry<Integer, Double> map : tmpRandValueHashMap.entrySet()) {
                System.out.print("key=" + map.getKey());
                System.out.println(" value=" + map.getValue());
            }


//            outputFitnessArrayTextArea.setText(tmpRandValueHashMap.values().stream().filter(Objects::nonNull).map(Objects::toString).collect(joining(";")));
//            System.out.println(tmpRandValueHashMap.values().stream().filter(Objects::nonNull).count());

//            outputFitnessArrayTextArea.setText(fitnessFinalMapBeforeGenericVariants.values().stream().map(Objects::toString).collect(joining(";")));
//            System.out.println((long) fitnessFinalMapBeforeGenericVariants.values().size());

//            outputFitnessArrayTextArea.setText(sortedRandValueList.toString());
//            for (int i = 0; i < inputSizeInt; i++) {
//                double random = ThreadLocalRandom.current().nextDouble(rangeMin, rangeMax);//2 var
//                String randomValueStr = String.format("%.4f", random);
//                System.out.print(randomValueStr + ";");
//            }
//            System.out.println();
//            System.out.println("END------------------------END");
//            System.out.println();

//            for (int i = 0; i < 80; i++) {
//                System.out.println("Random = " + ThreadLocalRandom.current().nextInt(0, 80));
//            }


//
//            // TODO: 08.12.2020 genetic classes
//            MyChromosome firstChromosome = new MyChromosome(top10ResultsByMinSum.get(0));
//            MyChromosome secondChromosome = new MyChromosome(top10ResultsByMinSum.get(1));
//            MyChromosome thirdChromosome = new MyChromosome(top10ResultsByMinSum.get(2));
//            MyChromosome fourthChromosome = new MyChromosome(top10ResultsByMinSum.get(3));
//            MyChromosome fiveChromosome = new MyChromosome(top10ResultsByMinSum.get(4));
//            MyChromosome sixChromosome = new MyChromosome(top10ResultsByMinSum.get(5));
//            MyChromosome sevenChromosome = new MyChromosome(top10ResultsByMinSum.get(6));
//            MyChromosome eightChromosome = new MyChromosome(top10ResultsByMinSum.get(7));
//            MyChromosome nineChromosome = new MyChromosome(top10ResultsByMinSum.get(8));
//            MyChromosome tenChromosome = new MyChromosome(top10ResultsByMinSum.get(9));
//
//          TournamentSelection tournamentSelection = new TournamentSelection(10);

////            List<ChromosomePair> chromosomeList = new ArrayList<>(10);
//            chromosomeList.add(firstChromosome);
//            List<Chromosome> chromosomes = Arrays.asList(firstChromosome, secondChromosome, thirdChromosome, fourthChromosome, fiveChromosome, sixChromosome, sevenChromosome, eightChromosome, nineChromosome, tenChromosome);
//
//
//            ListPopulation listPopulation = new ListPopulation(chromosomes, 1) {
//                @Override
//                public Population nextGeneration() {
//                    return null;
//                }
//            };
//
//
//            ElitisticListPopulation listPopulation = new ElitisticListPopulation(80, 0.9);

//            tournamentSelection.select(listPopulation);
//            System.out.println(tournamentSelection);
//            GeneticAlgorithm ga = new GeneticAlgorithm();

//            ChromosomePair pair = tournamentSelection.select();


        });
    }

    /*
    public List<List<Double>> genericRandomArraysFromFitnessArray(int sizeOfInputArray, int sizeOfOutputArray, int countOfOutputArrays) {
        List<List<Double>> outputList = new ArrayList<>(countOfOutputArrays);
        List<Double> tmpList;
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
//        Set<Integer> tmpUniqueIndexes;
//        int countIndex = 0;
        double sum;
        for (int i = 0; i < countOfOutputArrays; i++) {
            sum = 0.0;
//            countIndex = 0;
            tmpList = new ArrayList<>(sizeOfOutputArray + 1);
//            tmpUniqueIndexes = new HashSet<>(sizeOfOutputArray);
            for (int j = 0; j < sizeOfOutputArray; j++) {
                int randomInt = randomDataGenerator.nextInt(0, sizeOfInputArray - 1);
//                while(!tmpUniqueIndexes.contains(randomInt)){
//                    randomInt = randomDataGenerator.nextInt(0, sizeOfInputArray - 1);
//                }

//                if(!tmpUniqueIndexes.contains(randomInt)){
//                    countIndex++;
//                }
//                tmpUniqueIndexes.add(randomInt);
//                int randomInt = ThreadLocalRandom.current().nextInt(0, sizeOfInputArray);
//                System.out.println(randomInt);
                double currentValueByKey = tmpRandValueHashMap.get(randomInt);
                sum += currentValueByKey;
                tmpList.add(currentValueByKey);
            }
            tmpList.add(sizeOfOutputArray, sum);
            outputList.add(tmpList);
        }

        return outputList;
    }

     */

    public List<List<Double>> genericRandomArraysFromFitnessArray(int sizeOfInputArray, int sizeOfOutputArray, int countOfOutputArrays) {
        List<List<Double>> outputList = new ArrayList<>(countOfOutputArrays);
        List<Double> tmpList;
        Set<Integer> tmpUniqueIndexes;
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
        int countIndex = 0;
        double sum;
        for (int i = 0; i < countOfOutputArrays; i++) {
            sum = 0.0;
            countIndex = 0;
            tmpList = new ArrayList<>(sizeOfOutputArray + 1);
            tmpUniqueIndexes = new HashSet<>(sizeOfOutputArray);
//            for (int j = 0; j < sizeOfOutputArray; j++) {
            while (countIndex < sizeOfOutputArray) {
                int randomInt = randomDataGenerator.nextInt(0, sizeOfInputArray - 1);
//                while(!tmpUniqueIndexes.contains(randomInt)){
//                    randomInt = randomDataGenerator.nextInt(0, sizeOfInputArray - 1);
//                }

                if (!tmpUniqueIndexes.contains(randomInt)) {
                    countIndex++;
                } else {
                    continue;
                }
                tmpUniqueIndexes.add(randomInt);
//                int randomInt = ThreadLocalRandom.current().nextInt(0, sizeOfInputArray);
//                System.out.println(randomInt);
                double currentValueByKey = tmpRandValueHashMap.get(randomInt);
                sum += currentValueByKey;
                tmpList.add(currentValueByKey);
            }
            tmpList.add(sizeOfOutputArray, sum);
            outputList.add(tmpList);
        }

        return outputList;
    }

    private void initComponentsOnStartWindow(Stage primaryStage) {
        inputSizeLabel = new Label("Введите общее количество катушек > 10");
        inputSizeText = new IntegerTextField();
        inputSizeFitnessLabel = new Label("Введите необходимое количество катушек");
        inputSizeFitnessText = new IntegerTextField();
        btn = new Button();
        btn.setText("Сгенерировать слчайные числа");
        btnStartAlgorithm = new Button();
        btnStartAlgorithm.setText("Применить функцию");
        btnLastIteration = new Button();
        btnLastIteration.setText("Скрещивание");

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
        finalElementsSumOfArray = new Label();

        FlowPane root = new FlowPane(Orientation.VERTICAL, inputSizeLabel, inputSizeText, inputSizeFitnessLabel, inputSizeFitnessText,
                btn, outputAllArrayTextArea, btnStartAlgorithm,
                outputFitnessArrayTextArea0, outputFitnessLabel0, outputFitnessArrayTextArea1, outputFitnessLabel1,
                outputFitnessArrayTextArea2, outputFitnessLabel2, outputFitnessArrayTextArea3, outputFitnessLabel3,
                outputFitnessArrayTextArea4, outputFitnessLabel4, outputFitnessArrayTextArea5, outputFitnessLabel5,
                outputFitnessArrayTextArea6, outputFitnessLabel6, outputFitnessArrayTextArea7, outputFitnessLabel7,
                outputFitnessArrayTextArea8, outputFitnessLabel8, outputFitnessArrayTextArea9, outputFitnessLabel9,
                btnLastIteration, finalElementsSumOfArray);
        root.setVgap(10);
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
    }

    private void fillOutputTextAreasAndFields(){
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

        outputFitnessLabel0.setText("Сумма = " + top10ResultsByMinSum.get(0).get(80).toString());
        outputFitnessLabel1.setText("Сумма = " + top10ResultsByMinSum.get(1).get(80).toString());
        outputFitnessLabel2.setText("Сумма = " + top10ResultsByMinSum.get(2).get(80).toString());
        outputFitnessLabel3.setText("Сумма = " + top10ResultsByMinSum.get(3).get(80).toString());
        outputFitnessLabel4.setText("Сумма = " + top10ResultsByMinSum.get(4).get(80).toString());
        outputFitnessLabel5.setText("Сумма = " + top10ResultsByMinSum.get(5).get(80).toString());
        outputFitnessLabel6.setText("Сумма = " + top10ResultsByMinSum.get(6).get(80).toString());
        outputFitnessLabel7.setText("Сумма = " + top10ResultsByMinSum.get(7).get(80).toString());
        outputFitnessLabel8.setText("Сумма = " + top10ResultsByMinSum.get(8).get(80).toString());
        outputFitnessLabel9.setText("Сумма = " + top10ResultsByMinSum.get(9).get(80).toString());
    }

}
