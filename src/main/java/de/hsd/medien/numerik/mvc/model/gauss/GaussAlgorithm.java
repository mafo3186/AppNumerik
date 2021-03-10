package de.hsd.medien.numerik.mvc.model.gauss;

import de.hsd.medien.numerik.mvc.model.manager.AlgorithmIdentifier;
import de.hsd.medien.numerik.mvc.model.manager.IAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Gauss-Algorithmus
 * spezifisches Algorithmus-Objekt, implementiert IAlgorithm
 * - berechnet Algorithmus
 * - speichert Lösungsschritte
 * - prüft Matrix auf nicht erlaubte Nullspalten
 * @author Eis, Dietlinde; Al Ali, Salar; Focken, Mareike
 */
public class GaussAlgorithm implements IAlgorithm {

    public GaussAlgorithm(){
        System.out.println("GaussAlgorithm Objekt erstellt");
    }

    private final String shortDescription = "Gaußsches Eliminationsverfahren";
    private final String description = "Das Gaußsche Eliminationsverfahren ist ein Verfahren zur Lösung linearer Gleichungssysteme. Dafür wird das Gleichungssystem zunächst in Matrixform ausgedrückt und durch Zeilenumformung so umgeformt, dass ihre Werte unterhalb der Hauptdiagonalen zu 0 werden";

    private float [][] userInputMatrix = null;
    private ArrayList<GaussStufenformSteps> calculationStepList;
    private String solvabilityType;
    private ArrayList<GaussSubstitutionSteps> substitutionStepList;
    private GaussStufenform gaussStufenform;
    private GaussSubstitution gaussSubstitution;

    @Override
    public AlgorithmIdentifier getIdentifier() {
        return AlgorithmIdentifier.GAUSS;
    }

    @Override
    public String getShortDescription() {
        return this.shortDescription;
    }

    @Override
    public String getAlgorithmDescription() {
        return this.description;
    }


    /**
     * runAlgorithm()
     * - führt Gauss-Algorithmus aus
     * - speichert Lösungsschritte in IVs ab
     * Precondition: Darf nur aurgerufen werden, wenn userInputMarix != null ist
     */
    @Override
    public void runAlgorithm() {
        System.out.println("GaussAlgorithm.runAlgorithm() aufgerufen");
        // Stufenform-Objekt erzeugen:
        gaussStufenform = new GaussStufenform(userInputMatrix);
        // Stufenform-Algorithmus durchlaufen lassen:
        gaussStufenform.rowEchelonForm();

        // Substitutions-Objekt erstellen:
        gaussSubstitution = new GaussSubstitution(gaussStufenform.getSubstitutionMarix());

        // wenn die Substitution mit Parameter erfolgen soll (Matrix ist allgemein lösbar):
        if(gaussStufenform.getSubstitutionMode()){
            gaussSubstitution.substitute();
        // sonst substituiere die eindeutig lösbare Matrix:
        } else {
            gaussSubstitution.substituteWithParameter();
        }
        //Lösungsschritte speichern
        createSolutionSteps();
    }

    /**
     * speichert Lösungsschritte für Gauss in Instanzvariablen:
     * - CalculationSteplist für GaussStufenform
     * - solvabilityType für Lösbarkeit
     * - substitutionStepList für Substitutionsschritte inklusive Lösung
     */
    @Override
    public void createSolutionSteps() {
        // Übergabe von gaussStufenform.calculationStepList in this.calculationStepList:
        this.calculationStepList = gaussStufenform.getCalculationStepList();
        // Übergabe von gaussStufenform.solvability in this.solvabilityType
        this.solvabilityType = gaussStufenform.solvability;
        // Übergabe von gaussSubstitution.calculationStepList in this.calculationStepList:
        this.substitutionStepList = gaussSubstitution.getSubstitutionStepList();
    }

    /** überträgt User Input aus der View in Model */
    public void setUserInputMatrix(float[][] userInputMatrix) {
        this.userInputMatrix = userInputMatrix;
        System.out.println("GaussAlgorithm.setInputMatrix(float[][] _matrix) aufgerufen");
    }

    /**
     * prüft Matrix auf Nullzeilen
     * @param _matrix float[][] UserinputMatrix
     * @return true, wenn Nullzeilen enthalten sind, false, wenn keine Nullzeilen enthalten sind
     * @throws ArrayIndexOutOfBoundsException Array-Indexfehler
     */
    public boolean matrixHasZeroLines(float[][] _matrix) throws ArrayIndexOutOfBoundsException{
        // genutzte Variablen:
        int rowSize = _matrix[0].length;
        int columnSize = _matrix.length;
        int lastEchelonColumnIndex = _matrix[_matrix.length - 1].length - 2;
        // Vergelichsvariable zur Darstellung einer Null-Zeile, mit der verglichen werden soll:
        float[] emptyLine = new float[rowSize];

        int i = 0;
        int j = 0;

        try {
            // Prüfe jede Zeile
            do {
                // Fülle emptyLine zum Abgleich:
                do {
                    emptyLine[j] = 0.0f;
                    j++;
                } while (j < lastEchelonColumnIndex);
                // Abgleich der aktuellen Zeile mit emptyLine:
                if (Arrays.equals(_matrix[i], emptyLine)) {
                    return true;
                }
                i++;
            } while (i < columnSize);
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Index-Fehler.");
        }
        return false;
    }


    /** Getter für Lösungsschritte der Gaussstufenform */
    public ArrayList<GaussStufenformSteps> getCalculationStepList() { return calculationStepList; }

    /** Getter für Lösbarkeit des LGS */
    public String getSolvabilityType() {return solvabilityType;}

    /** Getter für Lösungsschritte der Substitution mit Lösungsergebnis*/
    public ArrayList<GaussSubstitutionSteps> getSubstitutionStepList() { return substitutionStepList;  }

}
