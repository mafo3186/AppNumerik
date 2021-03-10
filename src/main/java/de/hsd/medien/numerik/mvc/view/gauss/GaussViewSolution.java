package de.hsd.medien.numerik.mvc.view.gauss;

import de.hsd.medien.numerik.mvc.model.gauss.GaussSubstitutionSteps;
import de.hsd.medien.numerik.mvc.view.StepPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * GaussViewSolution extends GaussView
 * View des Abschnitts Lösung nach Lösungsweg
 * @author Focken, Mareike
 */
public class GaussViewSolution {

    StepPane solutionSection;
    /**
     * Konstruktor:
     * @param _solutionSection StepPane
     */
    public GaussViewSolution(StepPane _solutionSection){
        System.out.println("GaussViewSolutionObjekt erstellt");
        this.solutionSection = _solutionSection;
    }

    /**
     * Methode drawSolution()
     * liest Lösung aus und zeichnet Lösung der Variablen
     * @author Focken, Mareike
     */
    void drawSolution(String _solvabilityType, ArrayList<GaussSubstitutionSteps> _substitutionStepList){

        if(!_solvabilityType.equals("nonSolvable")) {
            // Lösungs-Überschrift und Kommentar

            VBox comment = createSolutionComment(_solvabilityType);
            VBox solutionVariables = createSolutionVariables(_substitutionStepList);

            // füge alles hinzu
            VBox solution = new VBox();
            solution.getChildren().addAll(comment, solutionVariables);
            this.solutionSection.addStep(solution);
        }

    }

    /**
     * Methode drawSolutionComment()
     * erstellt Lösungs-Kommentar
     * - liest solvabilityType aus IV aus
     *
     * @author Focken, Mareike
     */
    private VBox createSolutionComment(String _solvabilityType) {

        // erstelle GUI-Element für Lösungs-Überschrift
        Label stepLabel = new Label();
        stepLabel.setText("Lösung des Linearen Gleichungssystems");
        stepLabel.getStyleClass().add("stepTitle");

        // erstelle GUI-Elemente für Kommentar
        Text comment;
        switch (_solvabilityType) {
            case "solvable":
                comment = new Text ("Eindeutige Lösung:");
                break;
            case "nonSolvable":
                comment = new Text ("Keine Lösung.");
                break;
            case "substituteWithParameter":
                comment = new Text ("Allgemeine Lösung:");
                break;
            default:
                comment = new Text ("Die Lösbarkeit konnte nicht ermittelt werden");
        }
        // erstelle VBox für addStep
        VBox _solutionComment = new VBox();
        // füge Kommentar  hinzu
        _solutionComment.getChildren().addAll(stepLabel, comment);
        return _solutionComment;
    }

    /**
     * Methode createSolutionVariables()
     * liest die Lösung aus dataToGUI aus und macht GUI-Element daraus
     * @return VBox mit nach X aufgelöster Gleichung (Also Variable mit Lösung)
     * @author Focken, Mareike
     */
    private VBox createSolutionVariables(ArrayList<GaussSubstitutionSteps> _substitutionStepList){

        // erstelle VBox für addStep
        VBox _solutionVariables = new VBox();

        // Zähler für Variablen
        int number = 1;

        // lies SubstituteSteps aus ArrayList solutionMatrix aus GaussStufenform-Objekt newGaussStufenform aus
        // Iterator erstellen, der hinten anfängt
        ListIterator<GaussSubstitutionSteps> it = _substitutionStepList.listIterator(_substitutionStepList.size());

        // so lange es nächstes Element in List gibt
        while(it.hasPrevious()){
            // ermittle Schritt
            GaussSubstitutionSteps step =  it.previous();

            // estelle GUI-Element für Lösung
            float solutionVariable = step.getSolutionOfVariable();
            //ArrayList in Text umwandeln und ausgeben, mit tiefergestelltem X-Label
            Text solution = new Text("X" + FormattingElements.intToSubscript(number) + " = " + solutionVariable);

            // packe alle gefundenen Dinge in _solutionVariables
            _solutionVariables.getChildren().addAll(solution);

            // Zähler hochzählen
            number++;
        }
        return _solutionVariables;
    }

}
