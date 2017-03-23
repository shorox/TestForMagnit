package ru.test.magnit;

/**
 *
 *
 * Main class
 */

public class Main {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        Controller controller = new Controller();

        controller.addFields();
        controller.entityToXml();
        controller.closeDao();

        controller.transformXml();
        long summary = controller.summary();

        long totalTime = System.currentTimeMillis() - start;

        System.out.println("Total summary: " + String.valueOf(summary));
        controller.checkup();
        timerOutput(totalTime);
    }

    public static void timerOutput(long time){
        System.out.println("Time: " + String.valueOf(time / 1000 / 60) + "m " + String.valueOf(time / 1000 % 60) + "s");
    }


}
