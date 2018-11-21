package application.model;

import application.util.DAO.DemoPointDAO;

public class DemoPoint {

    int id;
    String name;
    int value;
    String strValue;
    int category;
    Boolean modifiable;

    String info;

    public DemoPoint(String name, int value, int category, boolean modifiable) {
        this.name = name;
        this.value = value;
        this.strValue = Integer.toString(value);
        this.category = category;
        this.modifiable = modifiable;
    }

    public DemoPoint() {

    }

    public void updateName(String name) {
        if (!modifiable)
            return;

        this.name = name;

        DemoPointDAO demoPointDAO = new DemoPointDAO();
        demoPointDAO.update(this, this.id);
    }

    public void updateValue(String value) {
        if (!modifiable)
            return;

        this.value = Integer.parseInt(value);
        this.strValue = Integer.toString(this.value);

        DemoPointDAO demoPointDAO = new DemoPointDAO();
        demoPointDAO.update(this, this.id);
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        strValue = String.valueOf(value);
    }

    public Boolean isModifiable() {
        return modifiable;
    }

    public void setModifiable(Boolean modifiable) {
        this.modifiable = modifiable;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
