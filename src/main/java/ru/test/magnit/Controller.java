package ru.test.magnit;

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
        if(list != null){

        }
    }

    public void xmlToXslt(){

    }

    public int sum(){
        return 0;
    }

    public void closeDao(){
        if(dao != null){
            dao.close();
        }
    }
}
