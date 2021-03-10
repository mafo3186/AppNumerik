package de.hsd.medien.numerik.mvc.view;

import de.hsd.medien.numerik.mvc.model.knfDnf.KnfDnfAlgorithm;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.util.LinkedList;

/***
 * Die KnfDnfView erstellt die Ausgabe für den KnfDnf Algorithmus.
 * Die Klasse AbstractAlgorithmView wird für Overrides geerbt.
 * Es werden Elemente gesetzt, erstellt und arrangiert.
 *
 * @author Katharina Stepantschenko
 */
public class KnfDnfView extends AbstractAlgorithmView{

    // erstellt ein KnfDnfAlgorithm Objekt
    KnfDnfAlgorithm knfDnf;
    // Grundgerüst der Ausgabe
    Stage stage;

    /***
     * Der Construktor setzt die IDs und erzeugt erste User-Eingabe-GUI.
     * @param _knfDnf Das aktuelle Objekt wird übergeben.
     * @param _stage Die Bühne wir übergeben.
     */
    public KnfDnfView(KnfDnfAlgorithm _knfDnf, Stage _stage){
        super(_knfDnf, _stage);
        this.knfDnf = _knfDnf;
        this.stage = _stage;

        setElementIds();
        System.out.println("knfDnfView-objekt wurde erstellt");
        updateView();
    }

    /***
     * Überschreibt den Title und die Beschreibung der View.
     */
    @Override
    public void updateHeader() {
        // Name des Algorithmus als Überschrift
        Label title = (Label) this.stage.getScene().getRoot().lookup("#title");
        title.setText(knfDnf.getShortDescription());

        // Beschreibung des Algorithmus
        Text description = (Text) this.stage.getScene().getRoot().lookup("#description");
        description.setText(knfDnf.getAlgorithmDescription());

        // Ausgabe auf die Console
        System.out.println("updateHeader wurde aufgerufen");
    }


    /**
     * Der Button conjunction fügt das Symbol des logischen UNDs auf das Textfeld hinzu.
     */
    public void setConjuction() {
        formInput.appendText("∧");
    }

    /**
     * Der Button disjunction fügt das Symbol des logischen ODERs auf das Textfeld hinzu.
     */
    public void setDisjunction() {
        formInput.appendText("∨");
    }

    /**
     * Der Button negation fügt das Symbol der Negation auf das Textfeld hinzu.
     */
    public void setNegation() {
        formInput.appendText("¬");
    }

    /**
     * Der Button implication fügt das Symbol der Implikation auf das Textfeld hinzu.
     */
    public void setImplication() {
        formInput.appendText("⇒");
    }

    /**
     * Der Button equivalence fügt das Symbol der Äquivalenz auf das Textfeld hinzu.
     */
    public void setEquivalence() {
        formInput.appendText("⇔");
    }


    /* GUI-Elemente für KnfDnf-Algorithmus */
    VBox userinputSizeVBox = new VBox();
    TextFlow comment = new TextFlow();
    Text inputCommentSize = new Text("Füge hier eine zu lösende KNF oder DNF Formel ein:");
    TextField formInput = new TextField();
    Button generateSolveButton = new Button("Berechnen");
    Button conjunctiveButton = new Button("∧");
    Button disjunctionButton = new Button("∨");
    Button negationButton = new Button("¬");
    Button implicationButton = new Button("⇒");
    Button equivalenceButton = new Button("⇔");
    HBox inputButtonsHBox = new HBox();


    /** Getter der GUI-Elemente für Weitergabe an Controller*/
    public String getUserInput() {
        return formInput.getText();
    }



    /**
     * setElementIds versieht IVs mit IDs,
     * Zweck: späteren Zugriff ermöglichen für z.B. Actionsetter
     */
    private void setElementIds(){
        userinputSizeVBox.setId(String.valueOf(IDs.KnfDnf.userinputSizeVBox));
        inputCommentSize.setId(String.valueOf(IDs.KnfDnf.inputCommentSize));
        conjunctiveButton.setId(String.valueOf(IDs.KnfDnf.conjunctiveButton));
        negationButton.setId(String.valueOf(IDs.KnfDnf.negationButton));
        disjunctionButton.setId(String.valueOf(IDs.KnfDnf.disjunctionButton));
        implicationButton.setId(String.valueOf(IDs.KnfDnf.implicationButton));
        equivalenceButton.setId(String.valueOf(IDs.KnfDnf.equivalenceButton));
        generateSolveButton.setId(String.valueOf(IDs.KnfDnf.generateSolveButton));
    }


    /***
     * Diese Methode kreiert die einzelnen Schritte der Berechnung der KNF bzw.
     * DNF Formel und gibt diese zurück. Vor jeder neuen Berechnung wird die
     * Box, in der die vorherige Berechnung stattfand geleert, damit die neue
     * angezeigt wird. Alle Weiter-Buttons werden generiert.
     * @param _getKnf Gibt die Berechnete KNF-Formel zurück.
     * @param _getDnf Gibt die Berechnete DNF-Formel zurück.
     * @param _getChanges Gibt die einzelnen Änderrungen zurück.
     */
    public void create(String _getKnf, String _getDnf, LinkedList<String> _getChanges) {
        // leer das ScrollPane für die Berechnung einer neuen Formel
        solutionSection.getChildren().clear();

        // erstellt einen Weiter-Button und setzt alle weiteren danach
        // auf unsichtbar
        solutionSection.showNextTwoPanesAddVisibleButton();

        // for-Schleife baut jeden Schritt nach Veränderung auf
        for (String o : _getChanges) {
            // erstellt für jeden Schritt ein Textfeld
            TextArea change = new TextArea(o);
            // das Textfeld darf nicht verändert werden
            change.setEditable(false);
            // setzt den Schritt-Knoten auf unsichtbar und fügt den
            // unsichtbaren Schritt-Knoten hinzu
            solutionSection.addStepInvisibleWithoutButton(change);
            // erstellt alle Weiter-Button, und zeigt diese nacheinander
            // auf sichtbar
            solutionSection.showNextTwoPanesAddInvisibleButton();
        }
    }


    /***
     * updateUserinputSection erzeugt erstes User-Eingabe-Feld
     */
    @Override
    public void updateUserinputSection() {
        System.out.println("updateUserinputsection wurde aufgerufen");

        // Größe der userinputVBox
        userinputSizeVBox.setPrefWidth(600);
        userinputSizeVBox.setMaxWidth(1000);
        // Größe des formInput
        formInput.setPrefWidth(300);
        formInput.setMaxWidth(500);

        // Text über dem Eingabefeld
        comment.getChildren().add(inputCommentSize);
        // Button-Liste für das Eingabefeld
        inputButtonsHBox.getChildren().addAll(conjunctiveButton, disjunctionButton, negationButton, implicationButton, equivalenceButton);
        // Berechnen Button
        userinputSizeVBox.getChildren().addAll(comment, formInput,inputButtonsHBox, generateSolveButton);

        // alle Userelemente anhängen
        userInputSection.getChildren().addAll(userinputSizeVBox);


    }


}
