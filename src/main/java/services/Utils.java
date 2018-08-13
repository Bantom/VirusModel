package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Utils {

    public static List<Integer> vectorMinusVector(List<Integer> firstVector, List<Integer> secondVector) {
        if (firstVector.size() != secondVector.size()) throw new IllegalArgumentException("Different sizes of lists");
        List<Integer> result = new ArrayList<Integer>();

        for (int i = 0; i < firstVector.size(); i++) {
            result.add(firstVector.get(i) - secondVector.get(i));
        }
        return result;
    }

    public static List<Integer> vectorPlusVector(List<Integer> firstVector, List<Integer> secondVector) {
        if (firstVector.size() != secondVector.size()) throw new IllegalArgumentException("Different sizes of lists");
        List<Integer> result = new ArrayList<Integer>();

        for (int i = 0; i < firstVector.size(); i++) {
            result.add(firstVector.get(i) + secondVector.get(i));
        }
        return result;
    }

    public static List<Integer> cloneFromVector(List<Integer> vectorToClone) {
        return new ArrayList<>(vectorToClone);
    }

    public static List<Integer> negativeElementsToZero(List<Integer> vector) {
        return vector.stream()
                .map(v -> v > 0 ? 1 : 0)
                .collect(toList());
    }

    public static List<Integer> getVectorWithZeroValues(int size) {
        return IntStream
                .generate(() -> 0)
                .limit(size)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public static List<Integer> getVectorWithValuesOne(int size) {
        return IntStream
                .generate(() -> 1)
                .limit(size)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public static List<Integer> getVectorsWithRandomOneOrZeroValues(int size) {
        return new Random()
                .ints(size, 0, 2)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
