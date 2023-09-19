package org.example;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class SnakesAndLadders {

    public static Map<Integer, Integer> snakes = Map.of(
            17, 7,
            54, 34,
            62, 19,
            64, 60,
            87, 24,
            93, 73,
            95, 75,
            99, 78
    );
    public static Map<Integer, Integer> ladders = Map.of(
            4, 14,
            9, 31,
            20, 38,
            28, 84,
            40, 59,
            51, 67,
            63, 81,
            71, 91
    );

    public static void main(String[] args) {
        simulate();
    }

    public static void simulate() {
        Map<Integer, Integer> newSnakes = new HashMap<>();
        newSnakes.putAll(snakes);
        newSnakes.putAll(reverseMap(ladders));

        double[] boardA = new double[100]; // create a board to track the percentage of games completed
        double[] boardB = new double[100]; // create a board to hold the changes made during the step
        boardA[0] = 1; // initialise the board

        System.out.println("i\t\t% finished");

        double accumulated = 0;

        // loop 1000 rolls (steps)
        for (int i = 0; i < 1005; i++) {

            // loop through each cell
            for (int j = 0; j < 99; j++) {
                double currentVal = boardA[j];

                // loop through each possible output of the dice roll
                for (int k = 1; k <= 6; k++) {
                    int newPos = j + k;

                    // handle the case where the player bounces backwards when overshooting the end square
                    if (newPos > 99) {
                        newPos = 99 - (newPos - 99);
                    }

                    // split the percentage of the games at the current cell among the 6 possible results of the roll
                    boardB[newPos] += currentVal / 6;
                }
            }

            // handle the cases where the player landed on a snake
            for (Map.Entry<Integer, Integer> snake : newSnakes.entrySet()) {
                boardB[snake.getValue()-1] += boardB[snake.getKey()-1];
                boardB[snake.getKey()-1] = 0;
            }

            // maintain the accumulated ratio of game states that reach the end
            accumulated += boardB[99];

            // move the outputs to the input board variable
            boardA = boardB;
            boardB = new double[100];

            // log the step
            //System.out.println(Arrays.stream(boardA).sum() + Arrays.toString(boardA));
            System.out.println(i+"\t\t"+accumulated);
        }

    }

    public static <K,V> Map<V,K> reverseMap(Map<K,V> map) {
        return map.entrySet().stream().collect(toMap(Map.Entry::getValue, Map.Entry::getKey));
    }


}
