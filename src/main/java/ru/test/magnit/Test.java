package ru.test.magnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Test")
public class Test {

    @Column(name = "field")
    private int field;

    public Test() {
    }

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }
}
