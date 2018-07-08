package services;

import java.util.ArrayList;
import java.util.List;

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

    public static List cloneFromVector(List<Integer> vectorToClone) {
        List<Integer> result = new ArrayList<Integer>();

        result.addAll(vectorToClone);
        return result;
    }

    public static List<Integer> negativeElementsToZero(List<Integer> vector) {
        List<Integer> result = new ArrayList<Integer>();

        for (Integer aVector : vector) {
            result.add(aVector > 0 ? 1 : 0);
        }
        return result;
    }

    public static List<Integer> getVectorWithZeroValues(int size) {
        List<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            result.add(0);
        }
        return result;
    }
}
