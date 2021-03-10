package de.hsd.medien.numerik.mvc.model.euklid;

/***
 * Ein Rechenschritt des erweiterten Euklid
 * @author Justin Liersch
 */
public class extendEuklidObj {
    /* Darstellung als Linearkombination */
    /* ggt = s*a + t*b */
    // Koeffizenten
    private int koeS;
    private int koeT;

    private int a;
    private int b;

    /***
     * Konstruktor
     * @param _kS int
     * @param _kT int
     * @param _a int
     * @param _b int
     */
    extendEuklidObj(int _kS, int _kT, int _a, int _b) {
        this.koeS = _kS;
        this.koeT = _kT;
        this.a = _a;
        this.b = _b;
    }

    /***
     * Gibt den Koeffizienten S zurück
     * @return int
     */
    public int getKoeS(){
        return this.koeS;
    }

    /***
     * Gibt den Koeffizienten T zurück
     * @return int
     */
    public int getKoeT(){
        return this.koeT;
    }

    /***
     * Gibt den Wert der Variable A zurück
     * @return int
     */
    public int getA(){
        return this.a;
    }

    /***
     * Gibt den Wert der Variable B zurück
     * @return
     */
    public int getB(){
        return this.b;
    }
}
