package de.hsd.medien.numerik.mvc.view;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


/**
 * Klasse StepPane erweitert Klasse VBox
 * für GUI Allgemein
 * Zweck: Anzeige des nächsten Lösungsschrittes
 * - Methode showNextPane(): wechselt beim nächsten Child die Sichtbarkeit von false auf true, falls es ein unsichtbares Child gibt
 * - Methode addStep(): fügt Element in StepPane ein inklusive neuem Weiter-Button, der nächsten Schritt einblendet
 * @author Focken, Mareike
 * @author Duhme, Pia
 */
public class StepPane extends VBox {

    /**
     * Methode addStep() fügt den übergebenen Knoten unsichtbar hinzu
     * darüber erscheint der Button "weiter", der den nächsten unsichtbaren Schritt anzeigen soll.
     * @param step übergebener Knoten, der hinzugefügt werden soll (z.B. Lösungsschritt, Eingabefeld, etc.)
     */
    public void addStep(Node step) {
        addButton("Weiter");
        //setze den Schritt-Knoten auf Unsichtbar
        step.setVisible(false);
        // füge den unsichtbaren Schritt-Knoten hinzu
        getChildren().add(step);
    }

    /**
     * Methode addStepButton() fügt nur Button "weiter" hinzu,
     * der den nächsten unsichtbaren Schritt anzeigen soll
     */
    public void addButton(String _text) {
        // erstelle Weiter-Button
        Button btn = new Button(_text);
        // fügt dem Button die Fuktionalität zu, die Methode StepPane.showNextPane() aufzurufen
        // (also nächstes unsichtbare Element sichtbar zu machen)
        btn.setOnAction(actionEvent -> showNextPane());
        // füge Weiter-Button hinzu: eventuell besser vor dem Lösungsschritt einfügen?
        getChildren().add(btn);
    }


    /**
     * Methode addStepVisible() fügt den übergebenen Knoten sichtbar hinzu
     * @param step übergebener Knoten, der hinzugefügt werden soll (z.B. Lösungsschritt, Eingabefeld, etc.)
     */
    public void addStepVisible(Node step) {
        getChildren().add(step);
    }


    /**
     * Methode showNextPane()
     * zeigt das nächste Pane an
     * nächstes Pane: erstes, nicht sichtbare Child in <StepPane></StepPane> (erweitert VBox)
     * Hinweis: nur auf der direkt darunter liegenden Node-Ebene (tiefer liegende unsichtbare Elemente werden nicht berücksichtigt)
     * Zweck: Anzeige des nächsten Zwischenschritts des Lösungsweges eines Algorithmus'
     */
    public void showNextPane()  {
        // Liste von Children, die aktuellen Knoten ausliest --
        ObservableList<Node> list = this.getChildren();
        // Liste mit forEach-Schleife durchlaufen
        for(Node nextChild:list) {
            // falls Element unsichtbar ist
            if(!nextChild.isVisible()) {
                // setze child (Node) auf "sichtbar"
                nextChild.setVisible(true);
                break;
            }
        }
    }


    /**
     * Methode showAllPanes()
     * zeigt alle unsichtbaren Panes in StepPane an
     * Zweck: Anzeige des gesamten Lösungsweges eines Algorithmus'
     */
    public void showAllPanes()  {
        // Liste von Children, die aktuellen Knoten ausliest --
        ObservableList<Node> list = this.getChildren();
        // Liste mit forEach-Schleife durchlaufen
        for(Node nextChild:list) {
            // falls Element unsichtbar ist
            if(!nextChild.isVisible()) {
                // setze child (Node) auf "sichtbar"
                nextChild.setVisible(true);
                // nicht abbrechen!
                //break;
            }
        }
    }

    /**
     * Methode addShowAllButton() fügt nur Button "Ganze Lösung anzeigen" hinzu,
     * der den nächsten unsichtbaren Schritt anzeigen soll
     * @author Focken, Mareike
     */
    public void addShowAllButton(Node step) {
        Button btn = new Button("Ganze Lösung anzeigen");
        btn.setOnAction(actionEvent -> showAllPanes());
        getChildren().add(step);
        getChildren().add(btn);
    }


    /**
     * addStepInvisibleWithoutButton unsichtbar ohne Button
     * @param step Node
     */
    public void addStepInvisibleWithoutButton(Node step) {
        step.setVisible(false);
        getChildren().add(step);
    }


    /**
     * showNextTwoPanesAddInvisibleButton
     * - macht zwei StepPanes sichtbar
     * - löscht letzten sichtbaren Button?
     * - fügt weiteren Button unsichtbar hinzu
     */
    public void showNextTwoPanesAddInvisibleButton() {

        Button btn = new Button("Weiter");
        btn.setOnAction(actionEvent -> {
            showNextPane();
            showNextPane();
            getChildren().remove(btn);
        });
        getChildren().add(btn);
        btn.setVisible(false);
    }


    /**
     * showNextTwoPanesAddVisibleButton
     * - macht zwei StepPanes sichtbar
     * - löscht einen sichtbaren Button?
     * - fügt weiteren Button sichtbar hinzu
     */
    public void showNextTwoPanesAddVisibleButton(){
        Button btn = new Button("Weiter");
        btn.setOnAction(actionEvent -> {
            showNextPane();
            showNextPane();
            getChildren().remove(btn);
        });
        getChildren().add(btn);
    }

}

