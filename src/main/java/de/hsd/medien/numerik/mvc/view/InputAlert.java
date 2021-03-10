package de.hsd.medien.numerik.mvc.view;

import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.HashMap;
import java.util.Map;

/**
 * Alert-Klasse für User-Input Fehlermeldungen
 * @author Focken, Mareike
 */
public class InputAlert {

    /**AlertType-Enum */
    public enum AlertType{
        LGS_INVALIDSIZE,
        LGS_NOINPUT,
        MATRIX_WRONGINPUT,
        WRONGINPUT,
        NOFLOATINPUT
    }

    /** Instanzvariablen */
    String title;
    String header;
    String content;
    String[] alert;

    /**
     * Konstruktor InputAlert
     * @param _type AlertType
     */
    public InputAlert(AlertType _type){
        this.alert = this.alertMap.get(_type);
        this.title = alert[0];
        this.header = alert[1];
        this.content = alert[2];
    }

    //Strings Allgemein
    private static final String DEFAULT_TITLE = "Fehler";

    //Strings für Gauss
    private static final String GAUSS_LGS_INSTRUCTION = "Bitte geben Sie nur ganze Zahlen zwischen 2 und 8 ein. \nVersuchen Sie es nochmal!";
    private static final String GAUSS_MATRIX_INSTRUCTION = "Bitte geben Sie ganze Zahlen oder Fließkommazahlen (mit Punkt, nicht mit Komma) ein. \nLeere Felder werden durch die Zahl Null ersetzt.";
    private static final String GAUSS_MATRIX_NULL = "Damit ein LGS vorliegt, muss jede Spalte und jede Zeile mindestens einen Wert enthalten, der ungleich Null ist.";

    /** Map mit allen Alerts
     * Index 0: String für title
     * Index 1: String für Header
     * Index 2: String für Content
     */
    Map<AlertType, String[]> alertMap = new HashMap<>(){
        {
            put(AlertType.LGS_INVALIDSIZE, new String[]{DEFAULT_TITLE, "Größe des LGS zu groß oder zu klein", GAUSS_LGS_INSTRUCTION});
            put(AlertType.LGS_NOINPUT, new String[]{DEFAULT_TITLE, "Keine Eingabe", GAUSS_LGS_INSTRUCTION});
            put(AlertType.WRONGINPUT, new String []{DEFAULT_TITLE, "Ungültige Eingabe", GAUSS_LGS_INSTRUCTION});
            put(AlertType.NOFLOATINPUT, new String[] {DEFAULT_TITLE, "Ungültige Eingabe", GAUSS_MATRIX_INSTRUCTION});
            put(AlertType.MATRIX_WRONGINPUT, new String[] {DEFAULT_TITLE, "Kein Lineares Gleichungssystem", GAUSS_MATRIX_NULL});
        }
    };


    /**
     * Methode showAlert() mit vordefinierter Fehlermeldungs-Ansicht
     * @author Focken, Mareike
     */
    public void showAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(this.title);
        alert.setHeaderText(this.header);
        alert.getDialogPane().setContent( new TextFlow(new Text(this.content)));
        alert.showAndWait();
    }


}
