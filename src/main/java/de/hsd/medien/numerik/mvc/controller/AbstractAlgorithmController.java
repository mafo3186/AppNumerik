package de.hsd.medien.numerik.mvc.controller;

import de.hsd.medien.numerik.mvc.model.manager.AlgorithmManagerModel;
import de.hsd.medien.numerik.mvc.model.manager.IAlgorithm;
import de.hsd.medien.numerik.mvc.view.AbstractAlgorithmView;

import de.hsd.medien.numerik.mvc.view.IDs;
import de.hsd.medien.numerik.mvc.view.StartView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.stage.Stage;


/**
 * AbstractAlgorithmController
 * implementiert IAlgorithmController um Controller-Auswahl zu ermöglichen
 * enthält alle gemeinsamen Eigenschaften aller Algorithmus-Objekte
 * - Actionsetter für das Menu: zurück zur Startseite-Button + neue-Eingabe-Button
 * @author Focken, Mareike
 * @version 1.0
 */
public abstract class AbstractAlgorithmController {

    /**IVs*/
    private AbstractAlgorithmView algorithmView;
    private IAlgorithm algorithm;
    private Scene scene;

    /**
     * Konstruktor AbstractAlgorithmController
     * @param _algorithm IAlgorithm
     * @param _view AbstractAlgorithmView
     */
    public AbstractAlgorithmController(IAlgorithm _algorithm, AbstractAlgorithmView _view){
        this.algorithm = _algorithm;
        this.algorithmView = _view;
        this.scene = this.algorithmView.getAlgorithmScene();
        initializeSetMenuOnAction();
        System.out.println("AbstractAlgorithmController wurde erstellt.");
    }


    /** Actionsetter für Menu: Restart und Clear (Aufruf im Konstruktor der konkreten Controller) */
    private void initializeSetMenuOnAction(){

        System.out.println("initilizeSetOnAction in AbstracAlgorithmController funktioniert");

        Button restartButton = (Button) scene.lookup("#"+ IDs.start);
        restartButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                restartView();
            }
        });

        Button clearButton = (Button) scene.lookup("#"+ IDs.clear);
        clearButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                clearView();
            }
        });

    }

    /**
     * erzeugt in der aktuellen Stage eine neue StartView
     * mit neuem Managermodel und neuem StartController
     */
    protected void restartView() {
        System.out.println("auf zurück zu Start geklickt: ActionSetter in AbstractAlgorithmController hat funktioniert");
        Stage restartStage = this.algorithmView.getAlgorithmStage();

        AlgorithmManagerModel algorithmManagerModel = new AlgorithmManagerModel();
        StartView startView = new StartView(algorithmManagerModel, restartStage);
        StartController startController = new StartController(algorithmManagerModel, startView);
    }

    /**
     * startet Eingabe des Algorithmus-View neu
     * -mit neuem Controller, neuer View und noch aktuellem Model
     */
    protected void clearView() {
        System.out.println("auf neue Eingabe geklickt: ActionSetter in AbstractAlgorithmController hat funktioniert");

        AlgorithmManagerModel algorithmManagerModel = new AlgorithmManagerModel();
        StartView startView = new StartView(algorithmManagerModel, algorithmView.getAlgorithmStage());
        StartController startController = new StartController(algorithmManagerModel, startView);

        startView.setAlgorithmView(this.algorithm.getIdentifier());
        startController.setAlgorithm(this.algorithm.getIdentifier());
    }


    /** beauftragt View, Usereingabe-Daten zu lesen */
    abstract void getUserInput();

    /** Actionsetter für in der View neu generierte Elemente */
    abstract void initializeSetOnAction(String _id);


}
