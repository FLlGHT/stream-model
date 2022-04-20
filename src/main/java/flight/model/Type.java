package flight.model;

/**
 * @author FLIGHT
 * @creationDate 20.04.2022
 */
public enum Type {

    COLLECTION("Звено сбора данных"), ANALYSIS("Звено анализа данных"), DATASTORE("Звено хранения данных");

    private String text;

    Type(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
