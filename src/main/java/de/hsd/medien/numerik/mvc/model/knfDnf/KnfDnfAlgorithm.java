package de.hsd.medien.numerik.mvc.model.knfDnf;

import de.hsd.medien.numerik.mvc.model.manager.AlgorithmIdentifier;
import de.hsd.medien.numerik.mvc.model.manager.IAlgorithm;

import java.util.LinkedList;

public class KnfDnfAlgorithm implements IAlgorithm {

    public KnfDnfAlgorithm(){
        System.out.println("KnfDnfAlgorithm Objekt erstellt");
        userInput = "";
    }

    private String userInput;
    private String shortDescription = "Konjunktive und Disjunktive Normalform";
    private String algorithmDescription = "Eine Formel F ist in " +
            "konjunktiver Normalform (KNF), falls sie eine Konjunktion von " +
            "Disjunktionen von Literalen ist. Eine Formel F ist in " +
            "disjunktiver Normalform (DNF), falls sie eine Disjunktion von " +
            "Konjunktionen von Literalen ist.";

    BParser parser = new BParser();

    @Override
    public AlgorithmIdentifier getIdentifier() {
        return AlgorithmIdentifier.KNFDNF;
    }

    @Override
    public String getShortDescription() { return shortDescription; }

    @Override
    public String getAlgorithmDescription() { return algorithmDescription; }

    public void setUserInput(String _input){
        this.userInput = _input;
    }

    /***
     * Stößt über das Model den Parser an
     */
    @Override
    public void runAlgorithm() {
        this.parser.parse(this.userInput);
    }

    @Override
    public void createSolutionSteps() {

    }

    /***
     * Parser-bezogene Methoden
     * @author Justin Liersch
     */
    //-------------------------------------------------------------------------------------------------------
    public String getParsedKnf(){
        return this.parser.getKnf();
    }

    public String getParsedDnf(){
        return this.parser.getDnf();
    }

    public LinkedList<String> getChanges() {return this.parser.getChanges();}
    //-------------------------------------------------------------------------------------------------------

}


