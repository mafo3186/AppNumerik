package de.hsd.medien.numerik.mvc.view;

import de.hsd.medien.numerik.mvc.model.euklid.EuklidAlgorithm;
import de.hsd.medien.numerik.mvc.model.gauss.GaussAlgorithm;
import de.hsd.medien.numerik.mvc.model.knfDnf.KnfDnfAlgorithm;
import de.hsd.medien.numerik.mvc.model.manager.AlgorithmIdentifier;
import de.hsd.medien.numerik.mvc.model.manager.IAlgorithm;
import de.hsd.medien.numerik.mvc.view.gauss.GaussView;
import javafx.stage.Stage;

/**
 * Factory für AlgorithmView
 * - nur createAlgorithmView soll AbstractAlgorithmView zurückliefern
 * - bei Erweiterung um Algorithmus muss case mit AlgorithmIdentifier hinzugefügt werden
 * @author Focken, Mareike
 * @version 1.0
 */
public class AlgorithmViewFactory {

    /**private Konstruktor, um Objekterstellung zu verhindern */
    private AlgorithmViewFactory(){}

    /**
     * Fabrik-Methode zur Erstellung der algorithmus-spezifischen Controller, passend zum Identifier
     * @return gibt gewählten AlgorithmusController als AbstractAlgorithmController zurück
     * @param  _model IAlgorithm Objekt
     * @param _stage Stage
     */
    protected static AbstractAlgorithmView createAlgorithmView(IAlgorithm _model, Stage _stage) {

        AlgorithmIdentifier algorithmName = _model.getIdentifier();

        AbstractAlgorithmView abstractAlgorithmView;

        switch (algorithmName){
            case GAUSS: abstractAlgorithmView = new GaussView((GaussAlgorithm) _model, _stage);
                break;
            case EUKLID: abstractAlgorithmView = new EuklidView((EuklidAlgorithm) _model, _stage);
                break;
            case KNFDNF: abstractAlgorithmView = new KnfDnfView((KnfDnfAlgorithm) _model, _stage);
                break;
            //ToDo: neuen case für weiteren Algorithmus hinzufügen
            default: abstractAlgorithmView = null; System.out.println("keine View in Sicht");
        }

        return abstractAlgorithmView;
    }

}
