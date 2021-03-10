package de.hsd.medien.numerik.mvc.view.gauss;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Hilfsklasse Gauss: stellt verschiedene Formatierungen bereit
 *  - für verschiedene Matrix-Darstellungen
 *  - für Kennzeichnung geänderter Koeffizienten
 *  - Klammersetzung
 */
public class FormattingElements {

    /**
     * Methode intToSubscript()
     * wandelt die Zahlen (int) 0-9 um in tiefergestellte Strings
     * (Einsatz: Label für X-Variablen im Eingabeformular und für Ausgabe der Lösung)
     * @param _j int Zahl von 0-10 für Indices der Variablen
     * @return String tiefergestellte Zahl
     * @author Focken, Mareike
     */
    static String intToSubscript(int _j){
        String label = Integer.toString(_j);
        switch(label){
            case "0": label = "\u2080"; break;
            case "1": label = "\u2081"; break;
            case "2": label = "\u2082"; break;
            case "3": label = "\u2083"; break;
            case "4": label = "\u2084"; break;
            case "5": label = "\u2085"; break;
            case "6": label = "\u2086"; break;
            case "7": label = "\u2087"; break;
            case "8": label = "\u2088"; break;
            case "9": label = "\u2089"; break;
            default: break;
        }
        return label;
    }

    /**
     * Methode matrixToBracketHBox() überladen: mit Indices für farbige GUI-Elemente, falls vorhanden
     * erstellt aus Gesamtmatrix eine Ansicht mit mathematischen Zeichen (Klammern, Labeln, Operatoren)
     * @param _matrix übergebene Matrix
     * @param _activeLineIndex Index aktiver Zeile
     * @param _passiveLineIndex Index passiver Zeile
     * @param _newZeroLineIndex Zeilenindex von null
     * @param _newZeroColumnIndex Spaltenindex von null
     * @return HBox mit Klammern, Koeffizienten, Variablen, Determinanten, Operatoren
     * @author Focken, Mareike
     */
    static HBox matrixToBracketHBox(float[][] _matrix, Integer _activeLineIndex, Integer _passiveLineIndex, Integer _newZeroLineIndex, Integer _newZeroColumnIndex){

        // erstelle GUI-Elemente mit Werten aus Matrix (Koeffizienten, Variablen bereits mit Klammern, Determinanten)
        GridPane coefficients = createCoefficientMatrix(_matrix, _activeLineIndex, _passiveLineIndex, _newZeroLineIndex, _newZeroColumnIndex);
        HBox variablesWithBrackets = createVariableHBox(_matrix);
        VBox determinants = createDeterminantVBox(_matrix, _activeLineIndex, _passiveLineIndex);

        // erstelle GUI-Element für Klammern um Koeffizienten und Determinanten
        HBox coefficientsWithBrackets = setBrackets(coefficients, _matrix.length);
        HBox determinantsWithBrackets = setBrackets(determinants, _matrix.length);

        return new HBox(coefficientsWithBrackets,variablesWithBrackets,determinantsWithBrackets);
    }

    /**
     * Methode createCoefficientMatrix() überladen: mit Farben für GUI-Elemente, falls vorhanden
     * erstellt aus Gesamtmatrix ein GridPane mit den Werten der Koeffizienten
     * @param _matrix Gesamtmatrix, die Koeffizienten und Determinanten enthält
     * @param _activeLineIndex Index der Zeile, in der Änderungen vorhanden sind
     * @param _passiveLineIndex Index der Zeile, deren Werte zur Änderung der aktiven Zeile verwendet werden
     * @param _newZeroLineIndex Zeilenindex von neuer Null
     * @param _newZeroColumnIndex Spaltenindex von neuer Null
     * @return GridPane mit Koeffizienten der Matrix
     */
    private static GridPane createCoefficientMatrix(float[][] _matrix, Integer _activeLineIndex, Integer _passiveLineIndex, Integer _newZeroLineIndex, Integer _newZeroColumnIndex){
        // GridPane erstellen
        GridPane coefficients = new GridPane();

        // activeLineIndex nutzen, um entsprechende Zeile zu färben
        // laufe über Zeilen
        for (int m = 0; m < _matrix.length; m++) {
            //laufe über Spalten bis auf die letzte (weil wir die Lösung nicht wollen)
            for (int n = 0; n < _matrix[m].length-1; n++) {

                // lies Wert am Index und wandle Wert in String um
                String value = String.valueOf(_matrix[m][n]);

                //zeichne GridPane matrixPane mit Inhalt Pane box mit Inhalt Label label mit Inhalt String value (das ist unsere Zahl)
                Label label = new Label(value);
                Pane box = new Pane(label);

                // Stufenform sichtbar machen
                if(m >0 && n<_matrix[m].length-2 && n<m){
                    box.getStyleClass().add("toZero");
                }

                // GUI CSS: aktive Zeile färben
                if(_activeLineIndex!=null && m==_activeLineIndex) {
                    box.getStyleClass().add("active");
                }
                // GUI CSS: passive Zeile färben
                if(_activeLineIndex!=null && m==_passiveLineIndex) {
                    box.getStyleClass().add("passive");
                }

                // GUI CSS: neue Null färben, wenn Zeile und Spalte der neuen Null erreicht sind
                if( _newZeroLineIndex!=null && _newZeroColumnIndex!=null && m==_newZeroLineIndex && n==_newZeroColumnIndex) {
                    //label.getStyleClass().add("newZero");
                    box.getStyleClass().add("newZero");
                }

                //GUI CSS: falls value 0 ist und im Stufenform-Bereich, dann färben
                if(value.compareTo("0.0")==0 && m >0 && n<_matrix[m].length-2 && n<m){
                    //label.getStyleClass().add("zero");
                    box.getStyleClass().add("zero");
                }

                // Pane box mit im GridPane platzieren
                GridPane.setRowIndex(box, m);
                GridPane.setColumnIndex(box, n);

                // setzt den Container HBox als Unterobjekt für GridPane coefficients
                coefficients.getChildren().add(box);
            }
        }
        return coefficients;
    }

    /**
     * Methode createDeterminantVBox() überladen: mit Farben für GUI-Elemente, falls vorhanden
     * erstellt aus der Gesamtmatrix eine VBox mit den Werten der Determinanten
     * @param _matrix Gesamtmatrix, die Koeffizienten und Determinanten enthält
     * @param _activeLineIndex Index der Zeile, in der Änderungen vorhanden sind
     * @param _passiveLineIndex Index der Zeile, deren Werte zur Änderung der aktiven Zeile verwendet werden
     * @return VBox mit Determinanten
     * @author Focken, Mareike
     */
    private static VBox createDeterminantVBox(float [][] _matrix, Integer _activeLineIndex, Integer _passiveLineIndex){
        VBox determinants = new VBox();
        // lies letzte Spalte jeder Zeile und mach VBox draus
        // lauf über Zeile
        for(int i = 0; i< _matrix.length; i++){
            // speichere letzten Wert der Zeile
            float val =  _matrix[i][_matrix.length];
            // lies Wert am Index und wandle Wert in String um
            String value = String.valueOf(val);
            // erstelle label mit Inhalt String (zahl)
            Label label = new Label(value);
            // erstelle Pane mit Inhalt Label
            Pane box = new Pane(label);

            // GUI CSS: aktive Zeile färben
            if(_activeLineIndex !=null && i==_activeLineIndex) {
                box.getStyleClass().add("active");
            }
            // GUI CSS: passive Zeile färben
            if(_passiveLineIndex !=null && i==_passiveLineIndex) {
                box.getStyleClass().add("passive");
            }

            //zeichne VBox determinants mit Inhalt Pane box
            determinants.getChildren().add(box);
        }
        return determinants;
    }

    /**
     * Methode createVariableVBox(2DArray float)
     * erstellt eine VBox mit den Variablen mit Klammern und Operatoren
     * @param _matrix 2D-Array mit Werten aus LGS (Koeffizienten und Lösungen)
     * @return HBox mit Variablen inklusive Labels, Klammern und Operatoren
     * @author Focken, Mareike
     */
    private static HBox createVariableHBox(float[][] _matrix){

        // VBox erstellen
        VBox variableVBox = new VBox();

        // über Zeilen laufen
        for (int i=0; i<_matrix.length; i++){
            // mach pro Zeile ein X-Label mit tiefergestelltem Index i+1
            // Beispiel:in Zeile 0 ist XLabel 1)
            //TODo: super. oder FormattingElements.
            String xLabel = FormattingElements.intToSubscript(i+1);
            String s = "X" + xLabel;

            // erstelle GUI-Element Label
            Label variable = new Label(s);
            // setzt Label als Unterobjekt für VBox variables
            variableVBox.getChildren().add(variable);
        }

        // Klammern um VBox
        HBox variableHBoxWithBrackets = setBrackets(variableVBox, _matrix.length);

        // Operatoren erstellen
        Label multiplication = new Label(" * ");
        Label equation = new Label (" = ");

        //HBox mit Operatoren, Klammern und Variablen
        return new HBox(multiplication, variableHBoxWithBrackets, equation);

    }


    /**
     * Methode setBrackets()
     * erstellt eine HBox, die vorn und hinten mit Klammern gefüllt ist und setzt das übergebene Pane dazwischen
     * @param _grid Pane mit enthaltender Matrix
     * @author Focken, Mareike
     */
    private static HBox setBrackets(Pane _grid, int _matrixSize){
        bracketSize = setBracketSize(_matrixSize);

        // Frontklammer
        Label bracketFront = new Label("(");
        bracketFront.setStyle("-fx-font-size: " + bracketSize);

        // EndKlammer
        Label bracketEnd = new Label(")");
        bracketEnd.setStyle("-fx-font-size: " + bracketSize );

        // HBox erstellen und zurückgeben: Klammer vorne, GridPane und Klammer hinten in HBox einfügen
        return new HBox(bracketFront, _grid, bracketEnd);
    }

    // Speicherplatz für Klammergröße
    static int bracketSize;
    /**
     * Methode setBracketSize()
     * setzt die Größe der Klammern entsprechend der Größe der Matrix
     * @return int Schriftgröße
     * @author Focken, Mareike
     */
    private static int setBracketSize(int _matrixSize){
        // Fälle 2-8
        switch(_matrixSize){
            case 2: bracketSize = 60; break;
            case 3: bracketSize = 85;break;
            case 4: bracketSize = 120;break;
            case 5: bracketSize = 140;break;
            case 6: bracketSize = 160;break;
            case 7: bracketSize = 180;break;
            case 8: bracketSize = 200;break;
        }
        return bracketSize;
    }


}
