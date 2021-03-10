package de.hsd.medien.numerik.mvc.controller;

import de.hsd.medien.numerik.mvc.model.knfDnf.KnfDnfAlgorithm;
import de.hsd.medien.numerik.mvc.model.manager.IAlgorithm;
import de.hsd.medien.numerik.mvc.view.AbstractAlgorithmView;
import de.hsd.medien.numerik.mvc.view.IDs;
import de.hsd.medien.numerik.mvc.view.KnfDnfView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/***
 * Die KnfDnfController sorgt für die Interaktion mit der
 * View und dem Model. Lösungsschritte werden hier erstellt.
 * Der Controller nimmt die Informationen des Algorithmus.
 *
 * @author Katharina Stepantschenko
 */
public class KnfDnfController extends AbstractAlgorithmController {

    /** IVs für Konstruktor: stage, view, algorithm */
    private final Stage knfDnfStage;
    private final KnfDnfView knfDnfView;
    private final KnfDnfAlgorithm knfDnfAlgorithm;

    /**
     * KnfDnfController Konstruktor
     * Übergibt den aktuellen Algorithmus und die View.
     * Initialisiert alle OnAction für alle Buttons.
     *
     * @param _actualAlgorithm
     * @param _view
     */
    public KnfDnfController(IAlgorithm _actualAlgorithm, AbstractAlgorithmView _view) {
        super(_actualAlgorithm, _view);
        this.knfDnfAlgorithm = (KnfDnfAlgorithm) _actualAlgorithm;
        this.knfDnfView = (KnfDnfView) _view;
        this.knfDnfStage = _view.getAlgorithmStage();

        // Übergabe der Buttons
        initializeSetOnAction("#" + IDs.KnfDnf.conjunctiveButton);
        initializeSetOnAction("#" + IDs.KnfDnf.disjunctionButton);
        initializeSetOnAction("#" + IDs.KnfDnf.negationButton);
        initializeSetOnAction("#" + IDs.KnfDnf.equivalenceButton);
        initializeSetOnAction("#" + IDs.KnfDnf.implicationButton);
        initializeSetOnAction("#" + IDs.KnfDnf.generateSolveButton);

        // Aufgabe auf die Konsole
        System.out.println("KnfDnfController-Objekt wurde erstellt");
    }


    /***
     * ActionSetter für alle Buttons, die hinzugefügt und erstellt werden.
     * @param _id String der GUI-Element-IDs, die mit ActionSetter belegt
     *            werden sollen.
     */
    @Override
    protected void initializeSetOnAction(String _id){
        System.out.println("initilizeSetOnAction in KnfDnfAlgorithmController mit id als Parameter funktioniert");

        // Button für die knfDnfstage
        Button button = (Button) knfDnfStage.getScene().getRoot().lookup(_id);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //
                if(_id.equals("#"+ IDs.KnfDnf.conjunctiveButton)){
                    knfDnfView.setConjuction();
                }

                if(_id.equals("#"+ IDs.KnfDnf.disjunctionButton)){
                    knfDnfView.setDisjunction();
                }
                if(_id.equals("#"+ IDs.KnfDnf.negationButton)){
                    knfDnfView.setNegation();
                }
                if(_id.equals("#"+ IDs.KnfDnf.implicationButton)){
                    knfDnfView.setImplication();
                }
                if(_id.equals("#"+ IDs.KnfDnf.equivalenceButton)){
                    knfDnfView.setEquivalence();
                }
                if(_id.equals("#"+ IDs.KnfDnf.generateSolveButton)){
                    // holt sich die Eingabe des Users
                    getUserInput();
                    // läuft den Algorithmus durch
                    knfDnfAlgorithm.runAlgorithm();
                    // holt sich die Lösungen
                    knfDnfView.create(knfDnfAlgorithm.getParsedKnf(), knfDnfAlgorithm.getParsedDnf(), knfDnfAlgorithm.getChanges());

                    // Ausgabe auf die Konsole
                    System.out.println("KnfDnf-Lösung wurde an View geschickt");
                }


            }
        });

    }


    /**
     * getUserInput() beauftragt View, sich die Eingabe des Users als String
     * zu holen.
     */
    @Override
    void getUserInput() {
        knfDnfAlgorithm.setUserInput(this.knfDnfView.getUserInput());
    }
}
