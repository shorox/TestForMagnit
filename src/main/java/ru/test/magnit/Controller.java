package ru.test.magnit;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Controller {

    private Dao dao;

    public Controller(String url, String username, String password) {
        dao = new Dao(url, username, password);
    }

    public void addFields(int could){
        for (int i = 1; i <= could; i++) {
            Test test = new Test();
            test.setField(i);
            dao.save(test);
        }
    }

    public void entityToXml(){
        List<Test> list = dao.getAll();
        if(list != null && list.size() > 0){
            try {
                File file = new File("1.xml");
                file.delete();

                Document doc = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder()
                        .newDocument();

                Element rootElement = doc.createElement("entries");
                doc.appendChild(rootElement);

                for (Test test : list) {
                    Element entry = doc.createElement("entry");

                    Element field = doc.createElement("field");
                    field.appendChild(doc.createTextNode(String.valueOf(test.getField())));
                    entry.appendChild(field);

                    rootElement.appendChild(entry);
                }

                TransformerFactory.newInstance()
                        .newTransformer()
                        .transform(new DOMSource(doc), new StreamResult(file));
            } catch (ParserConfigurationException | TransformerException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void transformXml(){
        try {
            File inFile = new File("1.xml");
            File outFile = new File("2.xml");
            outFile.delete();

            TransformerFactory.newInstance()
                    .newTransformer(new StreamSource(new File("transform.xsl")))
                    .transform(new StreamSource(inFile), new StreamResult(outFile));
        } catch (TransformerException e) {
            System.err.println(e.getMessage());
        }
    }

    public int summary(){
        int summary = 0;
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

    public void closeDao(){
        if(dao != null){
            dao.close();
        }
    }
}
