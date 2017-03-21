package ru.test.magnit;

public class Main {

    public static void main(String[] args) {
        Controller controller = new Controller();

        controller.addFields(Integer.parseInt(args[3]));
        controller.entityToXml();
        controller.xmlToXslt();

        System.out.println(controller.sum());
    }
}
