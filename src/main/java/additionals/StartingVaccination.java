package additionals;

import java.util.Random;

public class StartingVaccination {

    public static void main(String[] args) {
        for (int i = 1; i < 12; i++) {
            Random random = new Random();
            double percent1 = random.nextInt(5)-2;
            double percent = (12 - i) + random.nextDouble() + percent1;
            System.out.println("On week " + i + " needs to vaccinate " + Math.abs(percent) + "% of people for starting extinction.");
        }
    }
}
