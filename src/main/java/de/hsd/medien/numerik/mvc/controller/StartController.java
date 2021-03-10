package de.hsd.medien.numerik.mvc.controller;

import de.hsd.medien.numerik.mvc.model.manager.AlgorithmIdentifier;
import de.hsd.medien.numerik.mvc.model.manager.AlgorithmManagerModel;
import de.hsd.medien.numerik.mvc.model.manager.IAlgorithm;
import de.hsd.medien.numerik.mvc.view.AbstractAlgorithmView;

import de.hsd.medien.numerik.mvc.view.StartView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * Klasse StartController
 * kommuniziert mit View: Auswahl der View
 * - setOnAction für Buttons in StartView
 * - setAlgorithmView
 * kommuniziert mit Model: Auswahl des Algorithmus
 * - setAlgorithm)
 * @author Focken, Mareike
 */
public class StartController {

    /** Instanzvariablen für Model und View */
    private StartView view;
    private AlgorithmManagerModel model;

    /**
     * Konstruktor StartController mit Actionsetter für Buttons
     * @param _model AlgorithmManagerModel
     * @param _view StartView
     */
    public StartController(AlgorithmManagerModel _model, StartView _view){
        this.model = _model;
        this.view = _view;
        initializeSetOnAction();
    }

    /**
     * initializeSetOnAction()
     * initialisiert ActionHandler (Buttonreaktion auf Klick), der
     * - zu der View wechselt, die zum Identfier (buttonID) des Buttons passt
     * - das Model (Algorithmus) bestimmt
     * @author Focken, Mareike
     */
    private void initializeSetOnAction(){
        //für alle Buttons in der Liste
        for(Button button : this.view.getAlgorithmButtonList()){
            System.out.println(button.toString());
            //mit Actionsetter versehen
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    //Button-ID lesen für Bestimmung von View und Model
                    AlgorithmIdentifier identifier = AlgorithmIdentifier.valueOf(button.getId());
                    System.out.println("Ich habe auf den Button "+ identifier + " geklickt");
                    setAlgorithm(identifier);
                }
            });
        }
    }


    /**
     * setAlgorithm() identifiziert und setzt algorithmusspezifisch: M, V und C
     * Reihenfolge wichtig:
     * - 1. Algorithmus via identifier setzen
     * - 2. View via identifier setzen
     * - 3. Controller mit Algorithmus als Parameter.
     * @param _identifier
     */
    protected void setAlgorithm(AlgorithmIdentifier _identifier) {
        IAlgorithm algorithmModel = this.model.getAlgorithmModel(_identifier);
        AbstractAlgorithmView algorithmView = view.setAlgorithmView(_identifier);
        AbstractAlgorithmController abstractAlgorithmController = AlgorithmControllerFactory.createAlgorithmController(algorithmModel, algorithmView);
    }

}

