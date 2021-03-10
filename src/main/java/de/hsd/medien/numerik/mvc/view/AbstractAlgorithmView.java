package de.hsd.medien.numerik.mvc.view;

import de.hsd.medien.numerik.AppNumerikMvc;
import de.hsd.medien.numerik.mvc.model.manager.IAlgorithm;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

/**
 * AbstractAlgorithmView
 * abstrake Klasse, die von allen algorithmus-spezifischen Views geerbt wird
 * erbt von StepPane (VBox-Subklasse für schrittweise Lösungsanzeige)
 * - enthält Header mit Menu-Buttons, Titel und Beschreibung
 * - Userinput-Section (leer)
 * - Solution-Section (leer)
 * @author Focken, Mareike
 * @version 1.0
 */
public abstract class AbstractAlgorithmView extends StepPane{

    /** über Konstruktor initialisierte IVs*/
    protected IAlgorithm algorithm;
    protected Stage algorithmStage;


    /** Instanzvariablen */
    private Scene algorithmScene;
    private String stageTitle = "Algorithmen lernen";

    private AnchorPane root = new AnchorPane();
    private VBox header = new VBox();
    private HBox menu = new HBox();

    private VBox headerAndScrollPane = new VBox();
    private ScrollPane sectionScrollPane = new ScrollPane();

    protected VBox sections = new VBox();
    protected StepPane userInputSection = new StepPane();
    protected StepPane solutionSection = new StepPane();

    private Button startButton = new Button("zurück zur Startseite");
    private Button clearButton = new Button("neue Eingabe");
    protected Label title = new Label("Algo-Titel");
    protected Text description = new Text ("Algo-Beschreibung Algorithmus-Beschreibung: Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore");


    /**
     * Konstruktor AbstractAlgorithm erzeugt Template für View-Ansicht
     * @param _algorithm IAlgorithm-Objekt
     * @param _stage aktuelle Stage
     */
    protected AbstractAlgorithmView(IAlgorithm _algorithm, Stage _stage)  {
        this.algorithm = _algorithm;
        this.algorithmStage = _stage;
        initializeView();
    }

    /**
     * setElementIds versieht IVs mit IDs,
     * Zweck: späteren Zugriff ermöglichen für z.B. Actionsetter oder CSS
     */
    private void setElementIds(){
        root.setId(IDs.root.name());
        header.setId(IDs.header.name());
        menu.setId(IDs.menu.name());
        startButton.setId(IDs.start.name());
        clearButton.setId(IDs.clear.name());
        title.setId(IDs.title.name());
        description.setId(IDs.description.name());
        userInputSection.setId(IDs.userinput.name());
        solutionSection.setId(IDs.solution.name());
        headerAndScrollPane.setId(IDs.headerAndScrollPane.name());
        sections.setId(IDs.sections.name());
    }


    /**
     * Initialisierung der View (via Konstruktor)
     * setzt Header mit Menu-Buttons, Titel, Beschreibung, und ScrollPane-Bereich mit userInput- und solution-section
     */
    void initializeView()  {

        //Element-Größen und Anker setzen für AnchorPane root (notwendig für ScrollPane-Funktionalität)
        AnchorPane.setBottomAnchor(headerAndScrollPane, 0.0);
        AnchorPane.setLeftAnchor(headerAndScrollPane, 0.0);
        AnchorPane.setRightAnchor(headerAndScrollPane, 0.0);
        AnchorPane.setTopAnchor(headerAndScrollPane, 0.0);

        // GUI-Elemente verschachteln
        menu.getChildren().addAll(startButton, clearButton);
        header.getChildren().addAll(menu,title,new TextFlow(description));
        sections.getChildren().addAll(userInputSection, solutionSection);
        sectionScrollPane = new ScrollPane(sections);
        headerAndScrollPane.getChildren().addAll(header, sectionScrollPane);
        root.getChildren().addAll(headerAndScrollPane);

        // Elemente für Actionsetter und CSS vorbereiten
        setElementIds();

        // Scene und Stage füllen
        algorithmScene = new Scene(root,900,700);
        algorithmScene.getStylesheets().add(AppNumerikMvc.class.getResource("styleAlgorithmView.css").toExternalForm());
        algorithmStage.setTitle(stageTitle);
        algorithmStage.setScene(algorithmScene);
        //algorithmStage.setFullScreen(true);
        algorithmStage.show();
    }


    /**
     * updateView() wird in allen konkreten Algorithmus-Views implementiert:
     * - ersetzt Platzhalter in Titel/Description des abstrakten Templates durch Daten des Models
     * - updated userinputSection algorithmusspezifisch
     */
    protected void updateView() {
        updateHeader();
        updateUserinputSection();
    }

    /** Algorithmusspezifisches Update von Titel und Beschreibung im Header */
    protected void updateHeader(){
        this.title.setText(algorithm.getShortDescription());
        this.description.setText(algorithm.getAlgorithmDescription());
        System.out.println("updateHeader wurde in AbstractAlgorithmView aufgerufen");
    }

    /** updated Eingabe-Section algorithmusspezifisch im StepPane userInputSection */
    protected abstract void updateUserinputSection();

    /** @return Stage für algorithmusspezifische View via Controller  */
    public Stage getAlgorithmStage(){return algorithmStage;}

    /** @return Scene für algorithmusspezifische View via Controller  */
    public Scene getAlgorithmScene() {return algorithmScene;}

}
