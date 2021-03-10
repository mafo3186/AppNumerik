package de.hsd.medien.numerik.mvc.view.gauss;

import de.hsd.medien.numerik.AppNumerikMvc;
import de.hsd.medien.numerik.mvc.model.gauss.GaussAlgorithm;
import de.hsd.medien.numerik.mvc.model.gauss.GaussStufenformSteps;
import de.hsd.medien.numerik.mvc.model.gauss.GaussSubstitutionSteps;
import de.hsd.medien.numerik.mvc.view.AbstractAlgorithmView;
import de.hsd.medien.numerik.mvc.view.IDs;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * GaussView
 * - Auslesen des Inputs des Users
 *      - Anzahl Gleichungen (Größe der Matrix)
 *      - Koeffizienten/Determinanten (Matrix-Werte)
 * - Anzeigen der Lösungsschritte
 *      - GaussStufenform & Lösbarkeit
 *      - Substitution
 *      - Lösung
 * @author Focken, Mareike
 */
public class GaussView extends AbstractAlgorithmView {

    /**
     * GaussView Konstruktor, setzt IDs und erzeugt erste User-Eingabe-GUI
     * @param _gauss GaussAlgorithm
     * @param _stage Stage
     */
    public GaussView(GaussAlgorithm _gauss, Stage _stage){
        super(_gauss, _stage);
        setElementIds();
        System.out.println("GaussView-Objekt wurde erstellt");
        updateView();
    }


    /** GUI-Elemente für Gauss-Algorithmus: Anzahl der Gleichungen für Bestimmung der Matrix-Größe */
    VBox sizeInput = new VBox();
    VBox userinputSizeVBox = new VBox();
    TextField numberOfVariables = new TextField();

    /** @return User-Eingabe für Größe der Matrix als String */
    public String getNumberOfVariables() {
        return numberOfVariables.getText();
    }


    /** Buttons (Bereitstellung für Actionsetter im Controller */
    Button generateInputFieldButton = new Button("Eingabefeld für Matrix erstellen");
    Button solveMatrixButton = new Button("Zeichne Matrix");
    Button showSolutionButton = new Button("Gehe zur Lösungs-Ansicht");


    /** GUI-Elemente für User-Eingabe der Koeffizienten (Matrix) */
    GridPane inputForm = new GridPane();

    Text inputInstructionText = new Text("\n" + "Geben Sie die Koeffizienten und das Ergebnis des Linearen Gleichungssystems (LGS) ein. \nBitte beachten Sie, dass für jede Variable in mindestens einer Gleichung ein Koeffizient eingegeben werden muss, damit es sich um ein LGS handelt.");
    VBox formAndButtonVBox = new VBox();
    TextField[][] userInputTextFieldMatrix;

    VBox matrixSolutionVBox = new VBox();

    Text explanationText = new Text("\n" + "Aus Ihrer Eingabe ergibt sich folgende Matrix:" + "\n");

    /** GUI-Elemente für Lösungsanzeige */
    Text explanationEchelonFormText = new Text("\n" + "Um das Lineare Gleichungssystem zu lösen, wird es auf Stufenform gebracht." + "\n" +  "Die grau hinterlegten Koeffizienten sollen durch Umformung null werden." + "\n" +  "Ziel: pro Zeile soll mindestens eine Variable weniger auftreten." + "\n");
    Text legendText = new Text("\n" + "Legende: " + "\n" +  "ROT: Zeile, in der eine Umformung vorgenommen wird." + "\n" + "BLAU: Zeile, die für die Umformung der rot gefärbten Zeile verwendet wird."  + "\n" + "GRAU: Koeffizienten, die zur Erreichung der Stufenform Null werden sollen." + "\n"+ "GRÜN: Erreichte Null als Ergebnis der Umformung.");



    /**
     * setElementIds versieht IVs mit IDs
     * Zweck: späteren Zugriff ermöglichen für z.B. Actionsetter
     */
    private void setElementIds(){
        generateInputFieldButton.setId(String.valueOf(IDs.Gauss.generateInputFieldButton));
        solveMatrixButton.setId(String.valueOf(IDs.Gauss.solveMatrixButton));
        showSolutionButton.setId(String.valueOf(IDs.Gauss.showSolutionButton));
    }

    /**
     * updateUserinputSection
     * - erzeugt erstes User-Eingabe-Feld
     * - aktiviert CSS
     */
    @Override
    protected void updateUserinputSection() {
        generateSizeInputQuery();
        //CSS mit sichtbaren Rändern aller JavaFX-Elemente für Testzwecke
        //stage.getScene().getStylesheets().add(AppNumerikMvc.class.getResource("style_gauss_helpers.css").toExternalForm());
        algorithmStage.getScene().getStylesheets().add(AppNumerikMvc.class.getResource("style_gauss.css").toExternalForm());
        System.out.println("updateUserinputsection wurde aufgerufen");
    }


    /**
     * generateSizeInputQuery setzt in der userInputSection die Gauss-GUI-Elemente
     * - Eingabemöglichkeit für die Größe der Matrix bzw. des LGS
     */
    private void generateSizeInputQuery(){

        TextFlow comment = new TextFlow();
        Text inputCommentSize = new Text("Wie viele Gleichungen bzw. Variablen enthält das Lineare Gleichungssystem, das Sie lösen möchten? (Die Anzahl der Gleichungen und Variablen sollte identisch sein.) Bitte geben Sie eine ganze Zahl zwischen 2 und 8 an.");

        userinputSizeVBox.setPrefWidth(600);
        userinputSizeVBox.setMaxWidth(1000);
        numberOfVariables.setPrefWidth(80);
        numberOfVariables.setMaxWidth(80);

        comment.getChildren().add(inputCommentSize);
        userinputSizeVBox.getChildren().addAll(comment, numberOfVariables, generateInputFieldButton);
        sizeInput.getChildren().addAll(userinputSizeVBox);
        userInputSection.getChildren().addAll(sizeInput);
    }

    /**
     * Methode, die durch Controller per ButtonClick als Action ausgelöst wird
     * @param _matrixSize int Größe der Matrix
     */
    public void generateMatrixUserInputFields(int _matrixSize) {

        //VBox userinputSizeVBox aus VBox sizeInput durch klicken entfernen
        sizeInput.getChildren().remove(userinputSizeVBox);
        userInputSection.getChildren().remove(sizeInput);

        //2D-Array-Größe für Textfelder für Usereingabe wird entsprechend der Usereingabe int Size gesetzt
        userInputTextFieldMatrix = new TextField[_matrixSize][_matrixSize+1];
        System.out.println("KoeffizientenMatrix-Zeilen-Anzahl ist: " + userInputTextFieldMatrix.length);
        System.out.println("KoeffizientenMatrix-Spalten-Anzahl ist: " + userInputTextFieldMatrix[0].length);

        // Index zur Vergabe der Variablen-Labels: column für Spalte
        int column = 0;

        // Erstellung TextField als Eingabefeld für die Matrix des LGS ...
        // ... iterativ für jede Zeile r
        for (int r = 0; r < _matrixSize; r++) {
            System.out.print("\n");
            // ... iterativ für jede Spalte c (size + 1 um die Ergebnisspalte im LGS zu berücksichtigen)
            for (int c = 0; c < _matrixSize +1 ; c++) {
                // Nach jedem Durchlauf Label-Index hochzählen
                column++;
                //Label in String verwandeln und tieferstellen
                String xLabel = FormattingElements.intToSubscript(column);

                // Eingabefeld für jeden Koeffizienten des LGS
                TextField field = new TextField();
                userInputTextFieldMatrix[r][c] = field;
                // Label zur Bezeichnung jeder Variable (zum Koeffizienten-Eingabefeld) erstellen
                Label labelVor = new Label();
                Label labelNach = new Label();
                // Labeltext erstellen je nach Matrix-Index (Variable mit tiefgestellten Zahlen oder Ergebnis)
                // Alle Spalten außer erster und letzter: +, Textfeld, Variable mit Indices
                if (c>0 && c<_matrixSize ){
                    labelVor.setText(" + ");
                    labelNach.setText(" X" + xLabel + " ");
                }
                // erste Koeffizient soll kein Plus im Label davor haben
                else if (c==(0)){
                    labelVor.setText(" ");
                    labelNach.setText(" X" + xLabel + " ");
                }
                // sonst (bleibt nur noch n == size)
                else {
                    labelVor.setText("\t" + "= ");
                }
                // HBox als Container für Eingabefeld und Label zur korrekten Platzierung
                HBox cell = new HBox(labelVor, field, labelNach);

                // CSS zuordnen
                field.getStyleClass().add("inputField");

                // setzt den GridPane-Index für die Zellen des Eingabefeldes
                GridPane.setRowIndex(cell, r);
                GridPane.setColumnIndex(cell, c);

                // setzt den Container als Unterobjekt für GridPane inputForm
                inputForm.getChildren().add(cell);
            }
            // Spaltenindex wieder auf Null setzen für nächsten Durchlauf
            column = 0;
        }

        // neue VBox mit dem Matrix-Eingabefeld und dem Button zur Lösung des Gleichungssystems füllen
        formAndButtonVBox.getChildren().addAll(inputInstructionText, inputForm, solveMatrixButton);
        // zum StepPane root das Eingabefeld in sichtbar hinzufügen (unsichtbar macht hier keinen Sinn)
        userInputSection.addStepVisible(formAndButtonVBox);

        System.out.println("GaussView.generateCoefficienInputFields wurde aufgerufen mit Zahl " + _matrixSize);
    }


    /**
     * leert Matrix-Userinputfeld am Index Zeile und Spalte
     * @param _row int Zeile
     * @param _column int Spalte
     */
    public void clearUserInputTextFieldMatrixAt(int _row, int _column){
        userInputTextFieldMatrix[_row][_column].clear();
    }

    /**leert LGS-Userinputfeld */
    public void clearSizeInput(){  numberOfVariables.clear(); }

    /**
     * setzt Matrix-Userinput-Feld an Zeile und Spalte auf den Wert "0".
     * @param _row int Zeile
     * @param _column int Spalte
     */
    public void updateUserInputTextFieldMatrixAt(int _row, int _column) {
        userInputTextFieldMatrix[_row][_column].setText("0");
    }


    /**
     * Methode getCoefficientAndDeterminantValuesAsString(),
     * die die Inputwerte aus Eingabefeld in 2DArray speichert und zurückgibt
     * @return String [][] matrix 2DArray
     * @author Focken, Mareike; AlAli, Salar
     */
    public String [][] getCoefficientAndDeterminantValuesAsString() {
        int size = userInputTextFieldMatrix.length;
        String[][] matrix = new String[size][size + 1];
        // Schleife über 2D-Array mit gespeicherten Textfeldern
        for (int m = 0; m < size; m++) {
            for (int n = 0; n < size + 1; n++) {
                //lies Feld als String aus und speichere ihn.
                matrix[m][n] = userInputTextFieldMatrix[m][n].getText();
            }
        }
        return matrix;
    }


    /**
     * Methode, die durch Controller per Button-Click als Action ausgelöst wird
     * zeigt Werte des UserInputs als matrix an
     * Precondition: _matrix enthält nur float-Werte und keine Nullzeilen
     * @param _matrix float[][] Matrix
     */
    public void drawUserInputAsMatrix(float[][] _matrix)  {
        // veränderte Lösungsmatrix einfügen
        HBox matrixHBox = FormattingElements.matrixToBracketHBox(_matrix, null, null, null, null);
        matrixSolutionVBox.getChildren().addAll(explanationText, matrixHBox, showSolutionButton);
        // Eingabefeld entfernen
        userInputSection.getChildren().remove(formAndButtonVBox);
        // zum StepPane userInputSection das Eingabefeld sichtbar hinzufügen
        userInputSection.addStepVisible(matrixSolutionVBox);

        System.out.println("GaussView.drawUserInputAsMatrix(float[][] _matrix) aufgerufen");
    }


    /**
     * Methode drawGaussSolution()
     * (ohne Button-Action-Handler)
     * - zeichnet die Matrix der Usereingabe (drawMatrixValues)
     * - erstellt GaussStufenform-Objekt mit User-Eingabe-Werten (matrix)
     * - zeichnet Lösung in unsichtbaren Schritten (StepPane) hinzu:
     * -- Stufenform in allen Schritten (drawGaussStufenform)
     * -- Lösbarkeit (drawSolvabilityType)
     * -- Substitution (drawSubstitution)
     * -- Lösung (drawSolution)
     * @author Focken,Mareike
     */
    public void drawGaussSolution(ArrayList<GaussStufenformSteps> _calculationStepList, String _solvabilityType, ArrayList<GaussSubstitutionSteps> _substitutionStepList) {

        matrixSolutionVBox.getChildren().removeAll(showSolutionButton);
        userInputSection.getChildren().remove(matrixSolutionVBox);
        sections.getChildren().remove(userInputSection);

        // zeichne Erklärung, Werte der übergebenen Matrix (in diesem Fall Usereingabe) und Legende
        matrixSolutionVBox.getChildren().addAll(explanationEchelonFormText, legendText);

        //zum StepPane gaussSolution das Eingabefeld in sichtbar hinzufügen mit Anzeige des"gesamte Lösung anzeigen" Buttons
        solutionSection.addShowAllButton(matrixSolutionVBox);

        GaussViewStufenform stufenform = new GaussViewStufenform(solutionSection);
        // zeige GaussStufenformSteps versteckt an
        stufenform.drawGaussStufenform(_calculationStepList);
        // zeige Lösbarkeit versteckt an
        stufenform.drawSolvabilityType(_solvabilityType);

        // zeige Substitution versteckt an
        GaussViewSubstitution substitution = new GaussViewSubstitution(solutionSection);
        substitution.drawSubstitution(_substitutionStepList);

        // zeige Lösung versteckt an, falls es eine gibt
        GaussViewSolution solution = new GaussViewSolution(solutionSection);
        solution.drawSolution(_solvabilityType, _substitutionStepList);
    }

} // end of class
