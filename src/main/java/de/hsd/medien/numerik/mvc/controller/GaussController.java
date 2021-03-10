package de.hsd.medien.numerik.mvc.controller;

import de.hsd.medien.numerik.mvc.model.gauss.GaussAlgorithm;
import de.hsd.medien.numerik.mvc.model.gauss.GaussNullValueCheck;
import de.hsd.medien.numerik.mvc.model.manager.IAlgorithm;
import de.hsd.medien.numerik.mvc.view.AbstractAlgorithmView;
import de.hsd.medien.numerik.mvc.view.InputAlert;
import de.hsd.medien.numerik.mvc.view.gauss.GaussView;

import de.hsd.medien.numerik.mvc.view.IDs;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * GaussController
 * - steuert GaussView
 * - prüft Userdaten
 * - gibt und zieht Daten von und an GaussAlgorithm
 * - erweitert AbstractController, der für Menu und Header zuständig ist
 * @author Focken, Mareike
 */
public class GaussController extends AbstractAlgorithmController {

    /** IVs für Konstruktor: view, model, stage */
    private GaussView gaussView;
    private GaussAlgorithm gaussAlgorithm;
    private Stage gaussStage;

    /** IV für Userinput-Validation-Ergebnis */
    private boolean isValid = false;
    public boolean isValid() { return isValid; }
    public void setValid(boolean valid) { isValid = valid; }


    /**
     * Konstruktor Gausscontroller
     * @param _actualAlgorithm IAlgorithm
     * @param _view AbstractAlgorithmView
     */
    public GaussController(IAlgorithm _actualAlgorithm, AbstractAlgorithmView _view) {
        super(_actualAlgorithm, _view);
        System.out.println("GaussController-Objekt wurde erstellt");
        this.gaussAlgorithm = (GaussAlgorithm) _actualAlgorithm;
        this.gaussView = (GaussView) _view;
        this.gaussStage = _view.getAlgorithmStage();
        initializeSetOnAction("#"+IDs.Gauss.generateInputFieldButton);
    }


    /** IV Speicherplatz zur Weitergabe der UserInput-Eingabe */
    private float[][] userInputMatrix;
    private String[][] matrix;

    /** Test-Matritzen für Debugging & GUI-Tests */
    float[][] solvableMatrix =  {{4.0f, 2.0f, 2.0f, 8.0f}, {3.0f, -4.0f, 3.0f, -2.0f}, {1.0f, 3.0f, 2.0f, 4.0f}};
    float[][] nonsolvableMatrix = {{1.0f,1.0f,1.0f,3.0f},{1.0f,2.0f,3.0f,4.0f},{3.0f,2.0f,1.0f,0.0f}};
    float[][] allgemeinMatrix = {{1.0f,2.0f,3.0f,4.0f},{5.0f,6.0f,7.0f,8.0f},{9.0f,10.0f,11.0f,12.0f}};


    /**
     * ActionSetter für alle Buttons, die hinzugefügt werden können.
     * @param _id String der GUI-Element-IDs, die mit ActionSetter belegt werden sollen
     */
    @Override
    protected void initializeSetOnAction(String _id){
        System.out.println("initilizeSetOnAction in GaussAlgorithmController mit id als Parameter funktioniert");

        Button button = (Button) gaussStage.getScene().getRoot().lookup(_id);

        try{
        button.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (_id.equals("#" + IDs.Gauss.generateInputFieldButton)) {
                    generateInputFields();
                }
                if (_id.equals("#" + IDs.Gauss.solveMatrixButton)) {
                    solveMatrix();
                    System.out.println("#" + IDs.Gauss.solveMatrixButton);
                }
                if (_id.equals("#" + IDs.Gauss.showSolutionButton)) {
                    showSolution();
                    System.out.println("#" + IDs.Gauss.showSolutionButton);
                }
            }
        });}
        catch(NullPointerException n){
            System.out.println("kein Button mit ID #"+ _id);
        }
    }

    /**
     * - beauftragt View, Inputfelder zu generieren, falls Matrixgröße korrekt ist
     * - initialisiert Actionsetter
     * - setzt Titel
     */
    private void generateInputFields(){
        System.out.println("\t ActionSetter im Gauss läuft");

        int matrixSize = numberOfVariablesUserInput();
        System.out.println("folgende Variablenanzahl wurde eingegeben: " + matrixSize);

        if(matrixSize != -1){
            gaussView.generateMatrixUserInputFields(matrixSize);
        }
        else {
            gaussView.clearSizeInput();
        }

        initializeSetOnAction("#" + IDs.Gauss.solveMatrixButton);
        gaussStage.setTitle("Gauss - LGS Werteeingabe");
    }

    /**
     * solveMatrix()
     * - liest Daten aus der View und
     * - beauftragt View mit Anzeige der Matrix, falls Daten valide sind
     */
    private void solveMatrix(){
        //da ohne Rückgabewert: speichert Ergebnis in Klassenvariable matrix
        getUserInput();
        System.out.println("eingelesene String-Matrix :");
        GaussNullValueCheck.printMatrixString(matrix);

        userInputMatrix = validatedUserInput(matrix);
        //Testmatrizen:
        //userInputMatrix = solvableMatrix;
        //userInputMatrix = nonsolvableMatrix;
        //userInputMatrix = allgemeinMatrix;

        System.out.println("umgewandelte String-Matrix :");
        GaussNullValueCheck.printMatrix(userInputMatrix);

        if(isValid()){
            System.out.println("Eingabewerte sind valide");
            gaussView.drawUserInputAsMatrix(userInputMatrix);
            gaussAlgorithm.setUserInputMatrix(userInputMatrix);
            initializeSetOnAction("#"+ IDs.Gauss.showSolutionButton);
        }

        gaussStage.setTitle("Gauss - Matrixansicht");
    }

    /**
     * showSolution()
     * - beauftragt Model mit Berechnung des Algorithmus
     * - beauftragt View mit Anzeige des Gauss-Lösungsweges und der Lösung
     */
    private void showSolution(){
        try{
            gaussAlgorithm.runAlgorithm();
            gaussView.drawGaussSolution(gaussAlgorithm.getCalculationStepList(), gaussAlgorithm.getSolvabilityType(), gaussAlgorithm.getSubstitutionStepList());
            gaussStage.setTitle("Gauss - Lösung");
            System.out.println("Gauss-Lösung wurde erstellt und an View geschickt");
        } catch(Exception e){
            new InputAlert(InputAlert.AlertType.MATRIX_WRONGINPUT).showAlert();
            clearView();
        }
    }

    /**
     * getUserInput() beauftragt View, UserInput-Werte als String- in der Klassenvariable GaussController.matrix abzuspeichern
     */
    @Override
    void getUserInput() {
        matrix = this.gaussView.getCoefficientAndDeterminantValuesAsString();
        System.out.println("GaussController.getUserInput(): userInputMatrix erfolgreich eingelesen");
    }


    /**
     * isValidMatrix prüft, ob übergebene String-Matrix Werte enthält, die in float umgewandelt werden können,
     * - wandelt Werte in float um und
     * - speichert sie in IV userInputMatrix ab
     * - leere Felder werden zu 0
     * @param _matrix String[][] in View eingelesener UserInput für Koeffizienten und Determinanten
     * @return true, wenn userInput in float umgewandelt werden kann, sonst false
     */
    private float[][] validatedUserInput(String[][] _matrix){
        float[][] floatMatrix = new float[_matrix.length][_matrix.length+1];
        setValid(true);
        for (int m = 0; m < _matrix.length; m++) {
            for (int n = 0; n < (_matrix.length + 1); n++) {
                String userInput = _matrix[m][n];
                try {
                    floatMatrix[m][n] = Float.parseFloat(userInput);
                }catch(NumberFormatException e){
                    setValid(false);
                    if(userInput.isEmpty()){
                        gaussView.updateUserInputTextFieldMatrixAt(m,n);
                        System.out.println("keine Eingabe: also 0 in catch Nullpointer of GaussController.isValidMatrix()");
                    } else {
                        gaussView.clearUserInputTextFieldMatrixAt(m, n);
                        System.out.println("kein float in catch formatException in GaussController.isValidMatrix()");
                    }
                }
            }
        }
        if(!isValid) {
            new InputAlert(InputAlert.AlertType.NOFLOATINPUT).showAlert();
            System.out.println("UserInput ist kein float, isValid" + isValid);
        } else {
            hasZeroRows(floatMatrix);
            System.out.println("UserInput ist korrekter float, isValid=" + isValid);
        }

        System.out.println("Ende der Methode validMatrix");
        return floatMatrix;

    }


    /**
     * - falls Matrix korrekter float ist (isValid = true), auf Nullzeilen prüfen
     * - falls Nullzeilen vorhanden isValid auf false setzen
     * @param floatMatrix zu überprüfende Float-Matrix
     */
    private void hasZeroRows(float[][] floatMatrix) {
        if(isValid) {
            try {
                if (gaussAlgorithm.matrixHasZeroLines(floatMatrix)) {
                    setValid(false);
                    new InputAlert(InputAlert.AlertType.MATRIX_WRONGINPUT).showAlert();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Index-Fehler: Programmabbruch und neue Eingabe.");
            }
        }
    }


    /*------------------------------Anzahl Variablen/Gleichungen---------------------------------*/

    /**
     * numberOfVariablesUserInput liest UserInput aus View
     * @return -1 falls Userinput fehlerhaft, ansonsten UserInput als Int zwischen 2 und 8
     */
    private int numberOfVariablesUserInput(){
        String input = gaussView.getNumberOfVariables();
        if(!isNumberOfVariablesUserInputValid(input)){
            return -1;
        } else {
            return Integer.parseInt(input);
        }
    }

    /**
     * isSizeInputValid checkt, ob die Eingabe des Users nicht leer und eine ganze Zahl zwischen 2 und 8 ist
     * @param _input String Input des Users
     * @return true wenn Input nicht leer und ganze Zahl zwischen 2 und 8, sonst false
     */
    private boolean isNumberOfVariablesUserInputValid(String _input){
        // Falls Eingabefeld leer, Alert auswerfen
        if(_input.isEmpty()) {
            new InputAlert(InputAlert.AlertType.LGS_NOINPUT).showAlert();
            System.out.println("Alert: keine Eingabe");
            return false;
        } else {
            int input;
            // Falls etwas nicht stimmt (parsInt nicht geht, weil z.B. Buchstaben eingegeben werden)
            try {
                input = Integer.parseInt(_input);
            }catch(Exception e){
                new InputAlert(InputAlert.AlertType.WRONGINPUT).showAlert();
                System.out.println("Alert: keine ganze Zahl");
                return false;
            }
            // Falls die Eingabe nicht zwischen 2 und 8 ist, Alert auswerfen und aufhören
            if (input < 2 || input > 8){
                new InputAlert(InputAlert.AlertType.LGS_INVALIDSIZE).showAlert();
                System.out.println("Alert: Eingabe nicht zwischen 2 und 8");
                return false;
            }
        }
        return true;
    }

} // end of class
