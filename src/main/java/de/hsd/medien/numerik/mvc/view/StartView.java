package de.hsd.medien.numerik.mvc.view;

import de.hsd.medien.numerik.AppNumerikMvc;
import de.hsd.medien.numerik.mvc.model.manager.AlgorithmIdentifier;
import de.hsd.medien.numerik.mvc.model.manager.AlgorithmManagerModel;
import de.hsd.medien.numerik.mvc.model.manager.IAlgorithm;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Klasse StartView
 * enthält Startansicht der Applikation mit Buttons für jeden Algorithmus - generiert aus dem Managermodel
 * @author Focken, Mareike
 * version 1.0
 */
public class StartView {

    /** StartView IVs*/
    private Stage stage;
    private Scene scene;
    private AlgorithmManagerModel algorithmManagerModel;
    private ArrayList<Button> algorithmButtonList;


    /** IVs für Textinhalte*/
    final String stageTitle = "Algorithmen lernen";
    final String appTitle = "Algorithmen verstehen";
    final String appIntroduction = "Diese Applikation soll das Erlernen und Verstehen von Algorithmen unterstützen. Bitte wählen Sie den Algorithmus aus, den Sie sich ansehen möchten.";
    final String teamIP1 = "HSD - SoSe2020 - IP1: Al Ali, Salar; Duhme, Pia; Eis, Dietlinde; Focken, Mareike; Liersch, Justin; Pahlitzsch, Dominik; Stepantschenko, Katharina";
    final String teamIP2 = "HSD - WiSe2020 - IP2: Al Ali, Salar; Eis, Dietlinde; Focken, Mareike; Liersch, Justin; Stepantschenko, Katharina";

    /**
     * Konstruktor für Startansicht
     * @param _algorithmManagerModel AlgorithmManagermodel enthält alle implementierten Algorithmusobjekte
     * @param _stage Stage
     */
    public StartView(AlgorithmManagerModel _algorithmManagerModel, Stage _stage) {
        this.algorithmManagerModel = _algorithmManagerModel;
        this.stage = _stage;
        this.algorithmButtonList = new ArrayList<>();
        this.initializeView();
    }

    /**
     * startView()
     * erstellt Startseite prozedural
     * generiert automatisch Buttons aus AlgorithmMangerModel-ArrayList
     * @author Focken, Mareike
     */
    public void initializeView(){
        // Elemente für Startseite erzeugen
        Label title = new Label(appTitle);
        title.setId("title");
        TextFlow introduction = new TextFlow(new Text(appIntroduction));
        introduction.setId("intro");
        VBox buttonVBox = new VBox();
        buttonVBox.setId("buttonBox");
        //für jeden Algorithmus in der Liste
        for (IAlgorithm algorithm : algorithmManagerModel.getAlgorithmArrayList()){
            //Button erstellen und Buttonliste hinzufügen
            Button actualButton = createAlgorithmButton(algorithm.getIdentifier(), algorithm.getShortDescription());
            algorithmButtonList.add(actualButton);
            //ToDo:Test-Sop: kann später mal weg
            System.out.println("Buttonliste - Dieser Algorithmus in der Liste heißt: " + algorithm.getIdentifier());
            //an die buttonVBox pro Algorithmus in der Liste einen Button mit entsprechendem Text und ID erstellen und anhängen
            buttonVBox.getChildren().add(actualButton);
        }
        System.out.println();

        TextFlow author1 = new TextFlow(new Text(teamIP1));
        TextFlow author2 = new TextFlow(new Text(teamIP2));

        //root-Element für Scene erzeugen und Elemente anhängen
        VBox root = new VBox();
        root.setId("root");
        root.getChildren().addAll(title, introduction, buttonVBox, author1, author2);
        //Scene setzen und mit Stylesheets versehen
        scene = new Scene(root,700,700);
        scene.getStylesheets().add(AppNumerikMvc.class.getResource("style.css").toExternalForm());
        //Stage mit Titel und Scene bestücken und anzeigen
        stage.setTitle(stageTitle);
        stage.setScene(scene);
        stage.show();
    }


    /** Methode getButtonList(): auto-generated getter für die Buttonliste*/
    public ArrayList<Button> getAlgorithmButtonList() {
        return algorithmButtonList;
    }

    /**
     * Methode createAlgorithmButton()
     * - erzeugt neue Buttons mit Text und ID
     * - kreiiert auch Zugriffsmöglichkeit über styleClass mit generiertem Namen aus String _identifier + "Button"
     * @param _identifier String für zu setzende Button-ID
     * @param _shortDescription String für Text als Aufschrift des buttons
     * @return Button mit ID und Aufschrift,
     */
    private Button createAlgorithmButton(AlgorithmIdentifier _identifier, String _shortDescription){
        //neuen Button erzeugen
        Button button = new Button(_identifier.name());
        // mit algorithmName Button-ID setzen
        button.setId(_identifier.name());
        //mit ButtonText versehen
        button.setText(_shortDescription);
        // mit identifier Styleclass setzen
        button.getStyleClass().add(_identifier + "Button");
        return button;
    }


    /**
     * setAlgorithmView setzt anhand des Identifiers die algorithmus-spezifische View
     * MVC: View
     * @param _identifier String Algorithmus-Identifier
     */
    public AbstractAlgorithmView setAlgorithmView(AlgorithmIdentifier _identifier){

        System.out.println("Folgende View wird durch setAlgorithmView() aufgerufen: " + _identifier);
        IAlgorithm algorithm = algorithmManagerModel.getAlgorithmModel(_identifier);
        //ToDo Abfangen, wenn view = null
        return AlgorithmViewFactory.createAlgorithmView(algorithm, this.stage);

    }


} // end of class
