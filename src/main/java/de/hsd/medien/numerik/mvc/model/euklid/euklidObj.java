package de.hsd.medien.numerik.mvc.model.euklid;

/***
 * Ein Rechenschritt des einfachen Euklid
 * @author Justin Liersch
 */
public class euklidObj{
    /* a = preb * b + remain */
    private int a;
    private int preb;
    private int b;
    private int remain;

    /***
     * Konstruktor
     * @param _a Wert A (vor dem gleich)
     * @param _b Wert B (preb * b + remain)
     */
    euklidObj(int _a, int _b){
        this.a = _a;
        this.b = _b;
    }

    /***
     * Setzt den Vorfaktor von B
     * @param _preb int
     */
    public void setPreb(int _preb){
        this.preb = _preb;
    }

    /***
     * Setzt den Rest der Gleichung
     * @param _remain int
     */
    public void setRemain(int _remain){
        this.remain = _remain;
    }

    /***
     * Gibt den Wert A zurück
     * @return int
     */
    public int getA(){
        return this.a;
    }

    /***
     * Gibt den Wert B zurück
     * @return int
     */
    public int getB(){
        return this.b;
    }

    /***
     * Gibt den Vorfaktor von B zurück
     * @return int
     */
    public int getPreb(){
        return this.preb;
    }

    /***
     * Gibt den Rest zurück
     * @return int
     */
    public int getRemain(){
        return this.remain;
    }
}