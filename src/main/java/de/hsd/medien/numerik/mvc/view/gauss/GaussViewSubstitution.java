package de.hsd.medien.numerik.mvc.view.gauss;

import de.hsd.medien.numerik.mvc.model.gauss.GaussSubstitutionSteps;
import de.hsd.medien.numerik.mvc.view.StepPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * GaussViewSubstitution
 * View für Lösungsweg-Abschnitt GaussSubstitution
 * @author Focken, Mareike
 */
public class GaussViewSubstitution {

    StepPane solutionSection;

    public GaussViewSubstitution(StepPane _solutionSection){
        System.out.println("GaussViewSubstitutionObjekt erstellt");
        this.solutionSection = _solutionSection;
    }


    /**
     * Methode drawSubstitution()
     * liest die Substitutions-Schritte aus SubstituteSteps-Objekt aus und zeichnet sie
     * @author Focken, Mareike
     */
    void drawSubstitution(ArrayList<GaussSubstitutionSteps> substitutionStepList){
        // lies SubstituteSteps aus ArrayList solutionMatrix aus GaussStufenform-Objekt newGaussStufenform aus
        // Iterator erstellen
        Iterator<GaussSubstitutionSteps> it = substitutionStepList.iterator();
        // Anzahl Gleichungen für Benennung der Gleichung
        int number = substitutionStepList.size();

        // so lange es nächstes Element in List gibt
        while(it.hasNext()){
            // ermittle Schritt
            GaussSubstitutionSteps step =  it.next();

            // ermittle Index vom Schritt (für Label der Überschrift)
            int index = substitutionStepList.indexOf(step);
            // erstelle GUI-Element für Step-Überschrift
            Label stepLabel = new Label();
            stepLabel.setText("Gauss Substitution Schritt " + (index+1));
            stepLabel.getStyleClass().add("stepTitle");

            Label comment = new Label();
            comment.setText("Auflösen der Gleichung " + number +" nach X" + FormattingElements.intToSubscript(number));

            // erstelle GUI-Element für Gleichung vorher
            ArrayList<Float> _equationBeforeSubstitute = step.getEquationBeforeSubstitute();

            //ArrayList in Text umwandeln und ausgeben:
            String b = equationListToString(_equationBeforeSubstitute, number);
            Text before = new Text("Gleichung " + number + " vor Substitution: " + b);

            // erstelle GUI-Element für Gleichung vorher
            ArrayList<Float> _equationAfterSubstitute = step.getEquationAfterSubstitute();

            //ArrayList in Text umwandeln und ausgeben
            String a = equationListToString(_equationAfterSubstitute, number);
            Text after = new Text("Gleichung " + number + " nach Substitution : " + a);


            // Erstelle GUI-Element für Lösung
            float solutionVariable = step.getSolutionOfVariable();
            Text solution;


            // wenn solutionVariable NaN ist stattdessen [] ausgeben
            if(solutionVariable != solutionVariable){
                //ArrayList in Text umwandeln und ausgeben, mit tiefergestelltem X-Label
                solution = new Text("Lösung der Gleichung: X" + FormattingElements.intToSubscript(number) + " = [ ]");
                // sonst (also bei richtigen floats)
            } else {
                //ArrayList in Text umwandeln und ausgeben, mit tiefergestelltem X-Label
                solution = new Text("Lösung der Gleichung: X" + FormattingElements.intToSubscript(number) + " = " + solutionVariable);
            }


            // erstelle VBox für addStep
            VBox _gaussSolutionSteps = new VBox();

            // packe alle gefundenen Dinge in gaussSolutionSteps
            _gaussSolutionSteps.getChildren().addAll(stepLabel,comment, before, after, solution);

            solutionSection.addStep(_gaussSolutionSteps);

            // Anzahl Gleichungen runterzählen
            number--;
        }

        // Test für Konsole, wie viele Schritte es gibt
        // für den Index der for-Schleife die Schritte
        int stepNumber = substitutionStepList.size()-1;
        for (int i = 0; i< stepNumber; i++) {
            System.out.println("Substitution " + i);
            System.out.println("Anzahl Substitutionsschritte " + substitutionStepList.get(i));
        }

    } // Ende drawSubstitution


    /**
     * Methode equationListToString() macht aus Werten einer ArrayList einen String
     * in Form einer Gleichung
     * @param list ArrayList<Float> mit Werten aus SubstituteSteps
     * @param _number int Zahl der Gleichungen
     * @return s String für die Gleichung
     * @author Focken,Mareike
     */
    private String equationListToString(ArrayList<Float> list, int _number){
        String s = "";

        // Test für Debugging: Ausgabe der Listenlänge auf Konsole zur Prüfung, ob alle in der GUI ausgelesen werden
        int listSize = list.size();
        System.out.println("Länge der ArrayList pro Gleichung für Substitution: " + listSize);

        //starte mit dem X-Label der Gleichungsnummer
        int xLabel = _number;

        int m;
        // laufe über ganze Liste
        for(m = 0; m < (list.size()); m++) {
            // so lange Liste mehr als zwei Einträge hat (also mehr als 1x= z, also Index größer null, kleiner als Größe-2)
            // hänge tiefergestelltes Label und "+" an String an
            if(m >=0 && (m<list.size()-2)) {
                // bei String-Erstellung Label tieferstellen
                s += list.get(m) + " X" + FormattingElements.intToSubscript(xLabel) + " + " + "\t";
            }
            // beim vorletzten Eintrag nur tiefergestelltes Label anhängen
            else if(m== (list.size()-2)){
                // bei String-Erstellung Label tieferstellen und plus am Ende weglassen
                s += list.get(m) + " X" + FormattingElements.intToSubscript(xLabel) +  "\t";
            }
            // beim letzten Eintrag schreib Lösung mit = davor anhängen
            else if (m == (list.size()-1)){
                s += " = " + "\t" + list.get(m);
            }
            // zähle X-Label hoch
            xLabel++;
        }
        return s;
    }
}
