package de.hsd.medien.numerik.mvc.controller;

import de.hsd.medien.numerik.mvc.model.manager.AlgorithmIdentifier;
import de.hsd.medien.numerik.mvc.model.manager.IAlgorithm;
import de.hsd.medien.numerik.mvc.view.AbstractAlgorithmView;

/**
 * Factory für AlgorithmController
 * - nur createAlgorithmController soll AbstractAlgorithmController zurückliefern
 * - bei Erweiterung um Algorithmus muss case mit AlgorithmIdentifier hinzugefügt werden
 * @author Focken, Mareike
 * @version 1.0
 */
public class AlgorithmControllerFactory {

    /**private Konstruktor, um Objekterstellung zu verhindern */
    private AlgorithmControllerFactory(){}

    /**
     * Fabrik-Methode zur Erstellung der algorithmus-spezifischen Controller, passend zum Identifier
     * @return gibt gewählten AlgorithmusController als AbstractAlgorithmController zurück
     * @param  _model IAlgorithm Objekt
     * @param _view AbstractAlgorithm Objekt
     */
    protected static AbstractAlgorithmController createAlgorithmController(IAlgorithm _model, AbstractAlgorithmView _view) {

        AlgorithmIdentifier algorithmName = _model.getIdentifier();
        AbstractAlgorithmController abstractAlgorithmController;

        switch (algorithmName){
            case GAUSS: abstractAlgorithmController = new GaussController(_model, _view);
                break;
            case EUKLID: abstractAlgorithmController = new EuklidController(_model, _view);
                break;
            case KNFDNF: abstractAlgorithmController = new KnfDnfController(_model, _view);
                break;
            //ToDo: neuen case für weiteren Algorithmus hinzufügen
            default: abstractAlgorithmController = null; System.out.println("kein Controller in Sicht");
        }

        return abstractAlgorithmController;
    }
}

