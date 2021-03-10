package de.hsd.medien.numerik.mvc.model.gauss;

import java.util.Arrays;

public class GaussStufenformSteps {

    /**
     * Klasse zum speichern der Matrix zwischen jedem Rechenschritt in der Stufenform im Gauss-Algorithmus.
     * @author Eis, Dietlinde
     * @version 1.0
     */

    // gespeicherte Matrix
    private float[][] matrixStep;

    // Indices der Zeile die aktiv bzw. passiv am Rechenschritt beteiligt ist (zur Einfärbung in der GUI)
    private int activeLineIndex;
    private int passiveLineIndex;

    // Zeilen- u. Spalten-Indices des Faktors mit dem Zeilenwerte erweitert/multipliziert werden (zur Einfärbung in der GUI)
    private int factorLineIndex;
    private int factorColumnIndex;

    // Zeilen- u. Spalten-Indices der neuesten gelösten 0 in der Stufenform (zur Einfärbung in der GUI)
    private int newZeroLineIndex;
    private int newZeroColumnIndex;

    // Hilfstext für GUI
    private String descriptionText;


    /**
     * Methode zum erstellen & übergeben einer deep copy der Matrix, übergeben des Beschreibungstexts
     * @param _matrixStep float[][] als Matrix
     * @param _description String als Beschreibungstext
     * @author Eis, Dietlinde
     */
    GaussStufenformSteps(float[][] _matrixStep, int _activeLineIndex, int _passiveLineIndex, int _factorLineIndex, int _factorColumnIndex, int _newZeroLineIndex, int _newZeroColumnIndex, String _description){
        // Stream zum korrekten klonen eines 2D-Arrays: Jedes innere 1D-Array wird geklont und neu in ein 2D-Array gespeichert:
        this.matrixStep =  Arrays.stream(_matrixStep).map(float[]::clone).toArray(float[][]::new);
        this.activeLineIndex = _activeLineIndex;
        this.passiveLineIndex = _passiveLineIndex;
        this.factorLineIndex = _factorLineIndex;
        this.factorColumnIndex = _factorColumnIndex;
        this.newZeroLineIndex = _newZeroLineIndex;
        this.newZeroColumnIndex = _newZeroColumnIndex;
        this.descriptionText = _description;
        printMatrix(matrixStep);
    }

    /**
     * Debug-Methode zur Überprüfung ob matrixStep oben richtig geklont wurde
     * @param _matrix als matrixStep
     */
    public void printMatrix(float[][] _matrix) {
        for (float[] x : _matrix) {
            for (float y : x) {
                System.out.print("\t " + y);
            }
            System.out.println();
        }
        System.out.println("\n");
    }


    /**
     * Get-Methode für Daten der GaussStufenformSteps
     * @return matrixStep als gespeicherter Stand der Matrix
     */
    public float[][] getSavedMatrix(){
        return this.matrixStep;
    }

    public int getActiveLineIndex() {
        return activeLineIndex;
    }

    public int getPassiveLineIndex() {
        return passiveLineIndex;
    }

    public int getFactorLineIndex(){
        return factorLineIndex;
    }

    public int getFactorColumnIndex(){
        return factorColumnIndex;
    }

    public int getNewZeroLineIndex() {
        return newZeroLineIndex;
    }

    public int getNewZeroColumnIndex() {
        return newZeroColumnIndex;
    }

    public String getDescriptionText(){
        return this.descriptionText;
    }

}
