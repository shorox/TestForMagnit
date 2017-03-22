package ru.test.magnit;

public class Main {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        String url = args[0];
        String username = args[1];
        String password = args[2];
        int n = Integer.parseInt(args[3]);

        Controller controller = new Controller(url, username, password);

        controller.addFields(n);
//        controller.entityToXml();
        controller.closeDao();

//        long timeDB = System.currentTimeMillis();
//        timerOutput(timeDB - start, "Time working with DB: ");

//        controller.transformXml();
//        int summary = controller.summary();

        long end = System.currentTimeMillis();

//        System.out.println(summary);
//        checkup(n);
        timerOutput(end - start, null);
    }

    public static void timerOutput(long time, String message){
//        System.out.println(time / 1000);
        System.out.println((message != null ? message : "Time: ") +
                String.valueOf(time / 1000 / 60) + "m " +
                String.valueOf(time / 1000 % 60) + "s");
    }

    public static void checkup(int count){
        int summary = 0;
        for (int i = 1; i <= count; i++) {
            summary += i;
        }
        System.out.println("Checkup: "+ summary);
    }
}
