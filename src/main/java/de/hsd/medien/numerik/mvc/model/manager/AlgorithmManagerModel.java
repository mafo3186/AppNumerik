package de.hsd.medien.numerik.mvc.model.manager;

import de.hsd.medien.numerik.mvc.model.euklid.EuklidAlgorithm;
import de.hsd.medien.numerik.mvc.model.gauss.GaussAlgorithm;
import de.hsd.medien.numerik.mvc.model.knfDnf.KnfDnfAlgorithm;

import java.util.ArrayList;

/**
 * Klasse AlgorithmManagermodel
 * - enthält ArrayList<IAlgorithm> mit Algorithmus-Objekten aller in dieser App implementierten Algorithmen
 * - bei Erweiterung der App um einen Algorithmus muss der Konstruktor erweitert werden
 * @author Focken, Mareike
 */
public class AlgorithmManagerModel {

    /** IV enthält eine Instanz jedes Algorithmus' */
    private ArrayList<IAlgorithm> algorithmArrayList = new ArrayList<>();

    /** Konstruktor: füllt algorithmArrayList mit allen Algorithmus-Objekten */
    public AlgorithmManagerModel(){
        this.algorithmArrayList.add(new EuklidAlgorithm());
        this.algorithmArrayList.add(new GaussAlgorithm());
        this.algorithmArrayList.add(new KnfDnfAlgorithm());
        //ToDo: neuen Algorithmus hinzufügen
    }

    /** @return ArrayList<IAlgorithm> mit Algorithmus-Objekten */
    public ArrayList<IAlgorithm> getAlgorithmArrayList() {
        return this.algorithmArrayList;
    }

    /**
     * getAlgorithmModel: gibt zum identifier passendes Algorithmus-Objekt zurück
     * MVC: Model
     * @param _identifier String
     * @return IAlgorithm Algorithmus passend zum identifier
     */
    public IAlgorithm getAlgorithmModel(AlgorithmIdentifier _identifier){
        IAlgorithm algorithm = null;
        // über Liste laufen
        for(IAlgorithm actualAlgorithm : this.algorithmArrayList){
            //falls identifier übereinstimmen
            if(_identifier.equals(actualAlgorithm.getIdentifier())){
                algorithm = actualAlgorithm;
            }
        }
        return algorithm;
    }

} // end of class
