package de.hsd.medien.numerik.mvc.view.gauss;

import de.hsd.medien.numerik.mvc.model.gauss.GaussStufenformSteps;
import de.hsd.medien.numerik.mvc.view.StepPane;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * GaussViewStufenform
 * View des Lösungsweg-Abschnitts GaussStufenform inkl. Lösbarkeit
 * @author Focken, Mareike
 */
public class GaussViewStufenform{

    /** IV für Lösungsabschnitt*/
    StepPane solutionSection;

    /**
     * Konstruktor GaussStufenform
     * @param _solutionSection StepPane Lösungsabschnitt
     */
    public GaussViewStufenform(StepPane _solutionSection){
        this.solutionSection = _solutionSection;
    }


    /**
     * erzeugt GUI-Elemente für GaussStufenform-Lösungsschritte im Lösungsabschnitt
     * @param _calculationStepList Lösungsschritte aus Model
     */
    void drawGaussStufenform(ArrayList<GaussStufenformSteps> _calculationStepList){

        // lies GaussstufenformSteps aus ArrayList calculationStepList GaussStufenform aus
        // Iterator erstellen
        Iterator<GaussStufenformSteps> it = _calculationStepList.iterator();
        // wenn es nächstes Element in List gibt
        while(it.hasNext()){
            // ermittle Schritt
            GaussStufenformSteps step =  it.next();

            /* Werte fürs GUI-Aufhübschen ermitteln */

            // ermittle Index vom Schritt (für Label der Überschrift)
            int index = _calculationStepList.indexOf(step);
            // ermittle aktive (geänderte) Zeile
            int activeLineIndex = step.getActiveLineIndex();
            // ermittle passive (für Änderung verwendete) Zeile
            int passiveLineIndex = step.getPassiveLineIndex();
            // ermittle newZeroLineIndex & newZeroColumnIndex
            int newZeroLineIndex = step.getNewZeroLineIndex();
            int newZeroColumnIndex = step.getNewZeroColumnIndex();

            /*GUI-Elemente erstellen*/

            // erstelle GUI-Element für Step-Überschrift
            Label stepLabel = new Label();
            stepLabel.setText("Gauss Stufenform Schritt " + (index+1));
            stepLabel.getStyleClass().add("stepTitle");

            // erstelle GUI-Element für Kommentar
            String descriptionText = step.getDescriptionText();
            Text comment = new Text(descriptionText);

            // estelle GUI-Element für Matrix
            float[][] _matrix = step.getSavedMatrix();

            // erstelle GUI-Element für Klammern um Matrix und gefärbten Zeilen
            HBox boxWithBrackets = FormattingElements.matrixToBracketHBox(_matrix, activeLineIndex, passiveLineIndex, newZeroLineIndex, newZeroColumnIndex);

            // erstelle VBox für addStep
            VBox _gaussSolutionSteps = new VBox();

            // packe alle gefundenen Dinge in gaussSolutionSteps
            _gaussSolutionSteps.getChildren().addAll(stepLabel,comment, boxWithBrackets);

            solutionSection.addStep(_gaussSolutionSteps);
        }

        // für den Index der for-Schleife die Schritte
        int stepNumber = _calculationStepList.size()-1;
        GaussStufenformSteps lastStep = _calculationStepList.get(stepNumber);

        // Test für Konsole, wie viele Schritte es gibt: 0-11
        for (int i = 0; i< stepNumber; i++) {
            System.out.println("Stufenform " +i);
            System.out.println("Anzahl Stufenformschritte " + _calculationStepList.get(i));
        }

    } // Ende drawGaussStufenform



    /*-------------------------------------------------solvability --------------------------*/
    /**
     * erzeugt GUI-Elemente für LGS-Lösbarkeit im Lösungsabschnitt
     * @param solvabilitytype String, der als Lösbarkeit übergeben wird
     * @author Focken, Mareike
     */
    void drawSolvabilityType(String solvabilitytype){
        // erstelle GUI-Element für Solvability-Überschrift
        Label stepLabel = new Label();
        stepLabel.setText("Lösbarkeit des Linearen Gleichungssystems ");
        stepLabel.getStyleClass().add("stepTitle");

        // erstelle GUI-Elemente für Kommentar
        Text comment;
        switch (solvabilitytype){
            case "solvable": comment = new Text("Matrix ist eindeutig lösbar."); break;
            case "nonSolvable": comment = new Text("Matrix ist nicht lösbar."); break;
            case "substituteWithParameter": comment = new Text("Matrix ist allgemein lösbar."); break;
            default: comment = new Text ("Die Lösbarkeit konnte nicht ermittelt werden");
        }

        // erstelle VBox für addStep
        VBox _solvability = new VBox();

        // packe alle gefundenen Dinge in _solvability VBox
        _solvability.getChildren().addAll(stepLabel, comment);

        solutionSection.addStep(_solvability);
    } // Ende drawSolvabilityType

}
