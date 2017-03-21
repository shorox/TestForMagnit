package ru.test.magnit;

public class Main {

    public static void main(String[] args) {

        String url = args[0];
        String username = args[1];
        String password = args[2];
        int n = Integer.parseInt(args[3]);

        Controller controller = new Controller(url, username, password);

        controller.addFields(n);
        controller.entityToXml();
        controller.xmlToXslt();
        int sum = controller.sum();

        System.out.println(sum);
    }
}
