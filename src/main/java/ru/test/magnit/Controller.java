package ru.test.magnit;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Класс <code>Controller</code> содержит необходимые методы для поэтапного выполнения задания
 * @author Sharov V.
 */
public class Controller {

    /**
     * Объект данных для работы с базой данных на высоком уровне
     */
    private Dao dao;

    /**
     * Проперти с необходимым набором параметров
     */
    private Properties properties;

    public Controller() {
        setProperties();
        dao = new Dao(properties);
    }

    private void setProperties() {
        try (FileInputStream inputStream = new FileInputStream("app.properties")) {
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Метод добавляет в таблицу test заданное количество записей
     */
    public void addFields() {
        int n = Integer.valueOf(properties.getProperty("app.n"));
        for (int i = 1; i <= n; i++) {
            Test test = new Test();
            test.setField(i);
            dao.save(test);
            if (i % 500 == 0) {
                dao.commit();
            }
            if (i % 50_000 == 0) {
                System.out.println("   " + String.valueOf(i) + " records input in database");
            }
        }
        dao.commit();
        System.out.println("Total records input in database: " + String.valueOf(n));
    }

    /**
     * Метод достает из базы данных список записей и помещает его в xml файл
     */
    public void entityToXml() {
        List<Test> list = dao.getAll();

        if (list != null && list.size() > 0) {
            try {
                File file = new File("1.xml");
                file.delete();

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                Document doc = factory.newDocumentBuilder().newDocument();

                Element rootElement = doc.createElement("entries");
                doc.appendChild(rootElement);

                for (Test test : list) {
                    Element entry = doc.createElement("entry");

                    Element field = doc.createElement("field");
                    field.appendChild(doc.createTextNode(String.valueOf(test.getField())));
                    entry.appendChild(field);

                    rootElement.appendChild(entry);
                }

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                transformer.transform(new DOMSource(doc), new StreamResult(file));
            } catch (ParserConfigurationException | TransformerException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Метод транспортирует данные из одного xml файла в другой с разными формататми данных
     */
    public void transformXml() {
        try {
            File inFile = new File("1.xml");
            File outFile = new File("2.xml");
            outFile.delete();

            Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(new File("transform.xsl")));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            transformer.transform(new StreamSource(inFile), new StreamResult(outFile));
        } catch (TransformerException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Метод считывает данные из xml файла, суммирует и выводит полученную сумму
     */
    public long summary() {
        long summary = 0L;
        try {
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse("2.xml");

            NodeList entries = document.getElementsByTagName("entry");


            for (int i = 0; i < entries.getLength(); i++) {
                Element element = (Element) entries.item(i);
                summary += Integer.parseInt(element.getAttribute("field"));
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println(e.getMessage());
        }
        return summary;
    }

    /**
     * Метод вызывает закрытие текущего коннекта к базе данных
     */
    public void closeDao() {
        if (dao != null) {
            dao.close();
        }
    }

    /**
     * Метод выводит проверку суммы заданного числа
     */
    public void checkup(){
        long summary = 0L;
        int n = Integer.parseInt(properties.getProperty("app.n"));
        for (int i = 1; i <= n; i++) {
            summary += i;
//            if(i % 10000 == 0) System.out.println(i + " " + summary);
        }
        System.out.println("Checksumm: "+ String.valueOf(summary));
    }
}
