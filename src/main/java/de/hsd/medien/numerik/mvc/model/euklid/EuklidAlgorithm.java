package de.hsd.medien.numerik.mvc.model.euklid;

import de.hsd.medien.numerik.mvc.model.manager.AlgorithmIdentifier;
import de.hsd.medien.numerik.mvc.model.manager.IAlgorithm;

import java.util.LinkedList;

/**
 * spezifisches Algorithmus-Objekt: Methoden werden implementiert mit Daten aus Model
 * @author Justin Liersch
 */
public class EuklidAlgorithm implements IAlgorithm {
    private String shortDescription = "Euklidischer und Erweiterter Euklidischer Algorithmus";
    private String algorithmDescription = "Der Euklidische Algorithmus berechnet den größten gemeinsamen Teiler (ggT) zweier Zahlen";

    /* Ergebnis */
    private int ggt;
    /* erster Wert aus der View */
    private int firstValue;
    /* zweiter Wert aus der View */
    private int secondValue;

    /* Listen mit den gespeicherten Rechenschritte */
    private LinkedList<euklidObj> euklidCalcs = new LinkedList<>();

    private LinkedList<extendEuklidObj> extendCalcs = new LinkedList<>();


    /* Rechnungen */

    /***
     * Berechnung des einfachen Euklids
     * @param _a Erster Wert (int)
     * @param _b Zweiter Wert (int)
     * @return den Ggt (int)
     */
    private int euklid(int _a, int _b){

        euklidObj eObj = new euklidObj(_a,_b);
        if(_b == 0){
            eObj.setRemain(0);
            this.appendOnEuklidCalc(eObj);
            return _a;
        }
        else {
            eObj.setRemain(_a % _b);
            eObj.setPreb((int) Math.floor(_a /_b));
            this.appendOnEuklidCalc(eObj);
            return euklid(_b, _a % _b);
        }
    }

    /***
     * Errechent aus den Rechenschritten des einfachen Euklids
     * den erweiterten
     */
    private void extendEuklid(){
        int s = 1;
        int t = 0;
        int copyT = 0;

        for(int i = euklidCalcs.size() - 1; i >= 0; i-- ){
            euklidObj obj = euklidCalcs.get(i);
            if(i < euklidCalcs.size() - 1) {
                int q = obj.getPreb();
                t = s - q * copyT;
                s = copyT;
            }
            int a = obj.getA();
            int b = obj.getB();
            extendEuklidObj exObj = new extendEuklidObj(s,t,a,b);
            appendOnextendCalcs(exObj);
            copyT = t;
        }
    }

    /***
     * Setzt den ersten Wert
     * @param _value int
     */
    public void setFirstValue(int _value){this.firstValue = _value;}

    /***
     * Setzt den zweiten Wert
     * @param _value int
     */
    public void setSecondValue(int _value){this.secondValue = _value;}

    /***
     * Hängt ein Objekt des einfachen Euklids an die Liste an
     * @param _obj
     */
    public void appendOnEuklidCalc(euklidObj _obj){
        euklidCalcs.add(_obj);
    }

    /***
     * Hängt ein Objekt des erweiterten Euklids an die Liste an
     * @param _obj
     */
    public void appendOnextendCalcs(extendEuklidObj _obj){
        extendCalcs.add(_obj);
    }

    /***
     * Gibt den ermittelten Ggt zurück
     * @return int
     */
    public int getGgt(){return this.ggt;};

    /***
     * Gibt die Liste des einfachen Euklids zurück
     * @return LinkedList<euklidObj>
     */
    public LinkedList<euklidObj> getEuklidCalcs(){return this.euklidCalcs;}

    /***
     * Gibt die Liste des erweiterten Euklids zurück
     * @return LinkedList<extendEuklidObj>
     */
    public LinkedList<extendEuklidObj> getExtendCalcs(){return this.extendCalcs;}

    /***
     * Löscht alle Rechenschritte / Ergebnisse
     */
    public void clearResults()
    {
        this.euklidCalcs.clear();
        this.extendCalcs.clear();
    }

    /***
     * Konstruktor
     */
    public EuklidAlgorithm(){
        System.out.println("EuklidAlgorithm Objekt erstellt");
    }

    @Override
    public AlgorithmIdentifier getIdentifier() {
        return AlgorithmIdentifier.EUKLID;
    }


    @Override
    public String getShortDescription() {
        return shortDescription;
    }

    @Override
    public String getAlgorithmDescription() {
        return algorithmDescription;
    }

    /**
     * Methode runAlgorithm()
     * zum Aufruf/Ausführung der Berechnung im Model der Algorithmen
     */
    @Override
    public void runAlgorithm() {
        this.ggt = this.euklid(this.firstValue,this.secondValue);
        this.extendEuklid();
    }


    @Override
    public void createSolutionSteps() {

    }
}
