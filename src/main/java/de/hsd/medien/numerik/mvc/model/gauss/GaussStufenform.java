package de.hsd.medien.numerik.mvc.model.gauss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GaussStufenform {

    /**
     * Klasse zur Implementierung des Rechenschrittes der Stufenform im Gauss-Algorithmus.
     * Diese Klasse wird am Ende mit den restlichen Schritten des Gauss-Algorithmus zusammengeführt.
     * - rowEchelonForm() als Stufenform-Algorithmus und Container-Methode, die alle folgenden Methoden aneinandergereiht ausführt.
     * - transformToIdealRowPosition() zum prüfen, ob sich der aktuelle Aufbau der Matrix für die Umformung in die Stufenform eignet.
     * - rowReduction() als Methode die die Matrix in die Stufenform umrechnet.
     * - determineSolvability() als Methode zur Fallunterscheidung zwischen den Lösbarkeiten des LGS/der Matrix
     * @author EisDietlinde
     * @version 1.0
     */

    // 2D-Matrix mit Werten des LGS
    private float [][] matrix;

    // Speichervariable für die Matrix:
    private float [][] updatedMatrix;

    // Matrix die am Ende nach ihrer bestimmbaren Lösbarkeit via GaussController an die GaussSubstitution geleitet wird:
    private boolean substitutionMode;

    // Speichervariable für die Zeile mit der in der Stufenform verrechnet wird:
    private float[] savedLineValues;

    // Liste welche die Speicherschritte enthält
    ArrayList<GaussStufenformSteps> calculationStepList;

    // Array in das die Matrix am Ende der Stufenform zur Substitution gespeichert wird
    private float[][] substitutionMatrix;

    /**
     * IV für Lösbarkeit (in dieser KLasse anstatt in GaussStufenformSteps
     * ist im Konstruktor auf null gesetzt
     * wird durch Methode determineSolvability() geändert
     * @author Focken, Mareike
     */
    public String solvability;

    /**
     * IVs die lediglich als Referenzwerte für die Matrix dienen (Indices & Längen):
     */
    // rowSize und columnSize als Länge der Zeilen bzw. Spalten
    private int rowSize;
    private int columnSize;

    // lastRowIndex und lastColumnIndex als Index der letzten Zeile bzw. Spalte der Matrix
    private int lastRowIndex;
    private int lastColumnIndex;

    // lastEchelonColumnIndex als Index der letzten Spalte IN DER STUFENFORM ( != letzte Spalte in der ganzen Matrix)
    private int lastEchelonColumnIndex;

    /**
     * IVs der Indices für GaussStufenform – Siehe Darstellung zur besseren Übersicht:
     *
     *                    | current column | ...
     *  --------------------------------------------------
     *  formerLine        | [formerLineIndex][currentColumnIndex] | ...
     *  transformableLine | [currentLineIndex][currentColumnIndex] | ...
     *
     */

    private int currentColumnIndex;
    private int currentLineIndex;
    private int formerLineIndex;

    /**
     * IVs der Zeilen für GaussStufenform – Siehe Darstellung Im Kommentar oben zur besseren Übersicht:
     * (Werte werden erst in der Methode rowReduction initialisiert, da sie abhängig von jeweils referenzierten Zeilen sind)
     */
    // formerLine als Zeile mit der in der in aktuell betrachteten Spalte gerechnet wird
    private float[] formerLine;

    // formerLineValue als Wert an der Stelle [formerLineIndex][currentColumnIndex]
    private float formerLineValue;

    // transformableLine als Zeile die in die Stufenform gebracht werden soll
    private float[] transformableLine;

    // transformableLineValue als Wert an der Stelle [currentLineIndex][currentColumnIndex]
    private float transformableLineValue;


    /**
     * Hashmap mit eingetragenen Werten zur besseren Lesbarkeit in der GUI
     */
    private static final HashMap<String, String> textValues = new HashMap<String, String>();


    /**
     * Konstruktor für GaussStufenform:
     * @param userMatrix Matrix aus User-Eingaben in der GUI (Koeffizienten des zu lösenden LGS)
     */
    public GaussStufenform(float[][] userMatrix) {
        this.matrix = userMatrix;
        this.rowSize = matrix[0].length;
        this.columnSize = matrix.length;
        this.savedLineValues = new float[rowSize];
        this.lastRowIndex = matrix.length - 1;
        this.lastColumnIndex = matrix[lastRowIndex].length - 1;
        this.lastEchelonColumnIndex = matrix[lastRowIndex].length - 2;
        this.currentColumnIndex = 0;
        this.formerLineIndex = 0;
        this.currentLineIndex = 1;
        // Speicherplatz für Lösungen aus GaussStufenform:
        this.calculationStepList = new ArrayList<>();
        // Speicherplatz für Lösungen aus Substitution:
        // Eintrag von Werten und Verschriftlichungen in HashMap
        this.setValueTexts();
    }

    /**
     * Methode zum Eintragen der Werten und ihrer zugehörigen Verschriftlichungen,
     * zur besseren Lesbarkeit der Beschreibungstexte in der GUI
     * @author Eis, Dietlinde
     */
    private void setValueTexts(){
        // Key & Value für Ordnungszahlen mit -e Endung
        textValues.put("1e", "erste");
        textValues.put("2e", "zweite");
        textValues.put("3e", "dritte");
        textValues.put("4e", "vierte");
        textValues.put("5e", "fünfte");
        textValues.put("6e", "sechste");
        textValues.put("7e", "siebte");
        textValues.put("8e", "achte");
        textValues.put("9e", "neunte");
        // Key & Value für Ordnungszahlen mit -en Endung
        textValues.put("1en", "ersten");
        textValues.put("2en", "zweiten");
        textValues.put("3en", "dritten");
        textValues.put("4en", "vierten");
        textValues.put("5en", "fünften");
        textValues.put("6en", "sechsten");
        textValues.put("7en", "siebten");
        textValues.put("8en", "achten");
        textValues.put("9en", "neunten");
    }


    /**
     * Umfassende Methode um den Algorithmus für die Umformung der Matrix in die Stufenform auszuführen.
     * Wird in der main-Methode aufgerufen.
     * @return updatedMatrix
     */
    public float[][] rowEchelonForm() {
        System.out.println("execute rowEchelonForm");

        // Führe den Stufenform-Algorithmus für alle Spalten in der Stufenform (== bis zur Ergebnisspalte) aus.
        updatedMatrix = rowReduction(this.matrix, currentColumnIndex, formerLineIndex, currentLineIndex);
        return updatedMatrix;
    }

    /**
     * Methode um die Matrix in die zulässige bzw. beste Ausgangslage vor Anwendung der Stufenform bringen soll.
     * Prüft ob erster Koeffizient 0 in erster Zeile ist (nicht zulässig), ruft ggf. Untermethoden auf, um die Zeilen zu tauschen.
     * - führt ERST searchForPrioritizedValue() aus, um die mögliche präferierte Form zu erreichen (an der aktuellen Stelle für formerLineValue == 1)
     * - führt DANN preconditionRowReduction() aus, um die Precondition für Stufenform zu erreichen (an der aktuellen Stelle für formerLineValue != 0)
     * @param _matrix float[][] als aktuelle übergebene Matrix
     * @author EisDietlinde
     */
    private float[][] transformToIdealRowPosition(float[][] _matrix) {
        // Rufe Methode auf, um die Stelle an der aktuellen Stelle für formerLineValue wenn möglich durch Zeilentausch mit dem Wert 1 zu besetzen
        updatedMatrix = searchForPrioritizedValue(_matrix);
        // Wenn der Koeffizient an der aktuellen Stelle für formerLineValue in der Matrix == 0.0f ist
        if (_matrix[formerLineIndex][currentColumnIndex] == 0.0f) {
            // Rufe Methode, um jene Stelle mit einem Wert != 0.0f zu besetzen
            updatedMatrix = preconditionRowReduction(_matrix);
        } else {
            // Sonst übergebe gleiche Matrix
            updatedMatrix = _matrix;
        }
        System.out.println("\n" + "Print transformToIdealRowPosition: "); printMatrix(updatedMatrix);
        return updatedMatrix;
    }

    /**
     * Submethode von transformToIdealRowPosition()
     * um die Matrix wenn möglich in die günstigste Form für die Stufenform zu bringen.
     * Sucht nach einer Zeile mit Koeffizient 1 an erster Stelle und tauscht sie mit der obersten Zeile tauscht.
     * @param _matrix float[][] als aktuelle übergebene Matrix
     * @return updatedMatrix mit umgeformten Zeilen oder null
     * @author EisDietlinde
     */
    private float[][] searchForPrioritizedValue(float[][] _matrix) {
        // Leeres 1D-Array als Zwischenspeicher für tauschbare Zeile erstellen
        float[] swappableLine;
        // Zuweisung der Matrix, damit updatedMatrix nicht null bleibt beim Speichern in GaussStufenformSteps.
        updatedMatrix = _matrix;
        // Laufvariable = currentLineIndex, da immer nur der Bereich der Spalte, der noch umgefromt werden muss, betrachtet wird
        int m = currentLineIndex;
        // Variable die den Index der zu tauschenden Zeile für saveCalculationStep übernimmt (weil m selbst sich immer ändert)
        // Für die erste Spalte (ermittelt via Schleife durch jede Zeile)
        while (m <= lastRowIndex) {
            // prüfen ob irgend ein Koeffizient == 1 ist
            if (_matrix[m][currentColumnIndex] == 1.0) {
                // firstLine mit dieser Zeile tauschen:
                // diese Zeile als zu tauschende Zeile
                swappableLine = _matrix[m];
                // erste Zeile in diese Zeile speichern
                _matrix[m] = _matrix[formerLineIndex];
                // zu tauschender Zeile in erste Zeile speichern
                _matrix[formerLineIndex] = swappableLine;
                // Matrix speichern
                updatedMatrix = _matrix;
                // Daten & Zwischenstand der Matrix für die GUI in Liste eintragen
                saveCalculationStep(
                        updatedMatrix,
                        -1000,
                        -1000,
                        -1000,
                        -1000,
                        -1000,
                        -1000,
                        textValues.get(String.valueOf(currentLineIndex + 1) + "e") + " Zeile mit der " + textValues.get(String.valueOf(m + 1) + "en") + " Zeile vertauscht:");
                break;
            } else {
                m++;
            }
        }
        return updatedMatrix;
    }

    /**
     * Submethode von transformToIdealRowPosition()
     * um die Matrix in in die zulässige Form zu bringen.
     * Sucht nach einer Zeile mit Koeffizient != 0.0f an erster Stelle und tauscht sie mit der obersten Zeile tauscht.
     * @param _matrix float[][] als übergebene Matrix
     * @return updatedMatrix float[][] entweder mit umgeformten Zeilen oder in ursprünglicher Form
     * @author EisDietlinde
     */
    private float[][] preconditionRowReduction (float[][] _matrix) {
        // Leeres 1D-Array als Zwischenspeicher für tauschbare Zeile erstellen
        float[] swappableLine;
        // Zuweisung der Matrix, damit updatedMatrix nicht null bleibt beim Speichern in GaussStufenformSteps.
        updatedMatrix = _matrix;
        // In der untersten Zeile angefangen, rückwärts durch die Spalte zählen
        int m = lastRowIndex;
        // Variable die den Index der zu tauschenden Zeile für saveCalculationStep übernimmt (weil m selbst sich immer ändert)
        while (m >= 1) {
            // und prüfen ob der Koeffizient != 0 ist
            if (_matrix[m][currentColumnIndex] != 0.0) {
                // firstLine mit unterster Zeile tauschen:
                // unterste Zeile als zu tauschende Zeile
                swappableLine = _matrix[m];
                // erste Zeile in unterste Zeile speichern
                _matrix[m] = _matrix[formerLineIndex];
                // zu tauschender Zeile in erste Zeile speichern
                _matrix[formerLineIndex] = swappableLine;
                // Matrix speichern
                updatedMatrix = _matrix;
                // Daten & Zwischenstand der Matrix für die GUI in Liste eintragen
                saveCalculationStep(updatedMatrix,
                        -1000,
                        -1000,
                        -1000,
                        -1000,
                        -1000,
                        -1000,
                        textValues.get(String.valueOf(currentLineIndex + 1) + "e") + " Zeile mit der " + textValues.get(String.valueOf(m + 1) + "en") + " Zeile vertauscht:");
                break;
            } else {
                m--;
            }
        }
        return updatedMatrix;
    }

    /**
     * Hilfsmethode zum drucken der Matrix beim testen.
     * @param _matrix float[][] als Matrix die gedruck werden soll
     * @author EisDietlinde
     */
    public void printMatrix(float[][] _matrix) throws NullPointerException{
        try {
            for (float[] x : _matrix) {
                for (float y : x) {
                    System.out.print("\t " + y);
                }
                System.out.println();
            }
            System.out.println("\n");
        }
        catch(NullPointerException e) {
            System.out.println("Null-Pointer-Exception beim Versuch, printMatrix() außerhalb des regulären Programmflusses aufzurufen");
        }
    }

    /**
     * Hilfsmethode zum drucken von einer Array-Zeile beim testen.
     * @param _line float[] als Zeile die gedruckt werden soll
     * @author EisDietlinde
     */
    public void printLine(float[] _line) throws NullPointerException{
        try {
            for (float x : _line) {
                System.out.print("\t " + x);
            }
            System.out.println();
        } catch (NullPointerException e){
            System.out.println("Null-Pointer-Exception beim Versuch, printLine() außerhalb des regulären Programmflusses aufzurufen");
        }
    }

    /**
     * Methode um die Matrix in die Stufenform zu bringen.
     * @param _matrix float[][]
     * @param _currentColumnIndex int als Index der aktuell betrachteten Spalte
     * @param _formerLineIndex int als Index der Zeile die verrechnet wird
     * @param _currentLineIndex int als Index der Zeile die in die Stufenform gebracht werden soll
     * @return updatedMatrix float[][] als umgeformte Matrix in Stufenform
     * - führt ERST transformToIdealRowPosition() aus um die Matrix in die beste/zulässige Form für die Berechnung zu bringen.
     * - führt DANN saveLineValues() aus um die Werte der verrechnenden Zeile vor ihrer Erweiterung zu speichern (damit sie weiter untern zurückgesetzt werden kann).
     * - führt DANN extendRow() mit der verrechnenden Zeile und der umzuwandelnden Zeile um sie jeweils zu erweitern.
     * - führt DANN subtractRow() um die verrechnende Zeile von der umzuwandelnden Zeile abzuziehen.
     * - führt DANN resetLineValues() aus um die verrechnende Zeile auf ihre Werte vor Erweiterung zurückzusetzen.
     * - führt DANN moveToNextLine() bzw. moveToNextColumn() aus um eine Zeile/Spalte weiterzugehen.
     * @author EisDietlinde
     */
    private float[][] rowReduction(
            float[][] _matrix,
            int _currentColumnIndex,
            int _formerLineIndex,
            int _currentLineIndex) {

        this.matrix = transformToIdealRowPosition(_matrix);

        // relevante Zeilen und Werte für die Umformung setzen
        this.formerLine = _matrix[_formerLineIndex];
        this.formerLineValue = _matrix[_formerLineIndex][_currentColumnIndex];
        this.transformableLine = _matrix[_currentLineIndex];
        this.transformableLineValue = _matrix[_currentLineIndex][_currentColumnIndex];

        // die Werte von formerLine speichern, um sie später wieder zurücksetzen zu können
        saveLineValues(formerLine);

        // Folgender Ablauf wird ausgeführt, solange die aktuell betrachtete Zeile nicht die letzte Zeile erreicht hat.
        // Die Abfrage hierzu wird am Ende der Zeilenumformung in der Methode moveToNextLine() abgefangen.

        // Erweiterung der ersten Zeile für die Subtraktion:
        formerLine = extendRow(formerLine, transformableLineValue);
        System.out.println("erweitere Zeile " + formerLineIndex + " mit Faktor " + transformableLineValue);
        System.out.print("erweiterte formerLine: "); printLine(formerLine);
        // Daten & Zwischenstand der Matrix updaten und für die GUI in Liste eintragen
        updatedMatrix = _matrix;
        saveCalculationStep(updatedMatrix,
                _formerLineIndex,
                -1000,
                _currentLineIndex,
                _currentColumnIndex,
                -1000,
                -1000,
                "Erweiterung der " + textValues.get(String.valueOf(_formerLineIndex + 1) + "en") + " Zeile mit dem " + textValues.get(String.valueOf(_currentColumnIndex + 1) + "en") +" Koeffizient " + (transformableLineValue) + " der " + textValues.get(String.valueOf(_currentLineIndex + 1) + "en") + " Zeile:");

        // Erweiterung der aktuell betrachteten Zeile für die Subtraktion:
        transformableLine = extendRow(transformableLine, formerLineValue);
        System.out.print("erweiterte transformableLine: "); printLine(transformableLine);
        // Daten & Zwischenstand der Matrix updaten und für die GUI in Liste eintragen
        updatedMatrix = _matrix;
        saveCalculationStep(updatedMatrix,
                _currentLineIndex,
                -1000,
                _formerLineIndex,
                _currentColumnIndex,
                -1000,
                -1000,
                "Erweiterung der " + textValues.get(String.valueOf(_currentLineIndex + 1) + "en") + " Zeile mit dem " + textValues.get(String.valueOf(_currentColumnIndex + 1) + "en") + " Koeffizient " + (formerLineValue) + " der " + textValues.get(String.valueOf(_formerLineIndex + 1) + "en") + " Zeile:");

        // Subtraktion der ersten Zeile von der aktuell betrachteten Zeile:
        subtractRow(transformableLine, formerLine);

        // update veränderte Zeilen in der Matrix
        _matrix[_currentLineIndex] = transformableLine;
        // Daten & Zwischenstand der Matrix updaten und für die GUI in Liste eintragen
        updatedMatrix = _matrix;
        saveCalculationStep(updatedMatrix,
                _currentLineIndex,
                _formerLineIndex,
                -1000,
                -1000,
                _currentLineIndex,
                _currentColumnIndex,
                "Subtrahiere von der " + textValues.get(String.valueOf(_currentLineIndex + 1) + "en") + " Zeile die " + textValues.get(String.valueOf(_formerLineIndex + 1) + "e") + " Zeile:");

        System.out.println("STUFENFORM ZWISCHENSCHRITT:");
        printMatrix(updatedMatrix);

        // die Werte von formerLine zurücksetzen, um sie für die nächste Stufenform-Rechnung verwenden zu können
        resetLineValues(formerLine);

        if (_currentLineIndex == lastRowIndex && _currentColumnIndex == lastEchelonColumnIndex){
            determineSolvability(updatedMatrix);
            return updatedMatrix;
        } else {
            // In allen anderen Fällen (Ende der Stufenform noch nicht erreicht) soll der Zwischenstand der Matrix beim zurücksetzen der Werte von formerLine sollen in der GUI ausgegeben werden:
            updatedMatrix = _matrix;
            saveCalculationStep(updatedMatrix,
                    -1000,
                    -1000,
                    -1000,
                    -1000,
                    _currentLineIndex,
                    _currentColumnIndex,
                    "Die " + textValues.get(String.valueOf(formerLineIndex + 1) + "e") + " Zeile zurücksetzen:");

            // Solange die letzte Zeile in dieser Spalte nicht erreicht worden ist
            if (_currentLineIndex <= lastRowIndex - 1) {
                // gehe eine Zeile weiter
                moveToNextLine(_matrix, _currentColumnIndex, formerLineIndex, currentLineIndex);

                // Sonst, solange dies nicht die letzte Spalte in der Stufenform-Matrix ist
            } else if (_currentColumnIndex <= lastEchelonColumnIndex - 1){
                // gehe eine Spalte weiter
                moveToNextColumn(_matrix, _currentColumnIndex, formerLineIndex);
            }
        }
        return updatedMatrix;
    }

    /**
     * Methode um die Werte einer Zeile der Matrix zu speichern.
     * @param _lineToSave float[] als Zeile deren Werte gespeichert werden sollen
     * @return savedLineValues float[] als Zeile mit gespeicherten Werten
     * @author EisDietlinde
     */
    private float[] saveLineValues(float[] _lineToSave) {
        System.arraycopy(_lineToSave, 0, savedLineValues, 0, _lineToSave.length);
        return savedLineValues;
    }

    /**
     * Methode um Werte einer Zeile durch andere zu ersetzen/überschreiben
     * @param _lineToRestore float[] als Zeile deren Werte ersetzt werden sollen
     * @return _lineToRestore float[] als Zeile mit ersetzten Werten
     * @author EisDietlinde
     */
    private float[] resetLineValues(float[] _lineToRestore) {
        System.arraycopy(savedLineValues, 0, _lineToRestore, 0, _lineToRestore.length);
        System.out.println("Zeile in Index " + formerLineIndex + " zurückgesetzt.");
        return _lineToRestore;
    }

    /**
     * Methode um in der Stufenform zur nächsten betrachteten Zeile zu springen
     * @param _matrix als Matrix übergeben aus rowReduction
     * @param _currentColumnIndex als Index aktueller Spalte übergeben aus rowReduction
     * @param _formerLineIndex als Index zu verrechnender Zeile übergeben aus rowReduction
     * @param _currentLineIndex als Index aktuell betrachteter Zeile übergeben aus rowReduction
     * @author EisDietlinde
     */
    private void moveToNextLine(
            float[][] _matrix,
            int _currentColumnIndex,
            int _formerLineIndex,
            int _currentLineIndex
    ) {
        // Hier werden für die Verschiebung die Indices um eins erhöht bzw. die Zeilen um eins nach unten gesetzt
        // Für die betrachtete Zeile eins weitergehen und speichern
        currentLineIndex = ++_currentLineIndex;
        System.out.println("gehe eine Zeile weiter: Index " + currentLineIndex + "\n");
        //transformableLine = _matrix[currentLineIndex];
        // Wert in neuer betrachteten Zeile / aktuelle Spalte setzen
        transformableLineValue = _matrix[currentLineIndex][_currentColumnIndex];
        System.out.println("neuer Wert nach dem gelöst wird: " + transformableLineValue + "\n");

        // Fallunterscheidung: letzte Zeile noch nicht erreicht
        // Wenn der neue Wert an transformableLineValue erweiterbar ist (!= 0.0f) und es noch eine Zeile danach gibt
        // ACHTUNG: Hier muss <= stehen damit der Algorithmus (bei einer lösbaren Matrix) weiterläuft:
        // Da dieser Fall zu einer weiteren Verrechnung führt, steht hier eine "weiche" Unterscheidung,
        // damit der Zugriffsbereich für die aufgerufene Methode integer aber inklusiv genug ist.
        if (transformableLineValue != 0.0f && currentLineIndex <= lastRowIndex) {
            // wiederhole den Stufenform-Algorithmus: Rekursiver Aufruf
            rowReduction(_matrix, _currentColumnIndex, _formerLineIndex, currentLineIndex);

            // Sonst wenn der neue Wert an transformableLineValue bereits 0.0f ist und es noch eine Zeile danach gibt
            // ACHTUNG: Hier muss < stehen damit kein OutOfBounds-Error entsteht:
            // Da dieser Fall zu keiner weiteren Verrechnung (sondern lediglich Wechsel zur darauffolgenden Zeile) stattfindet,
            // ist keine Inklusivität notwendig, abner eine "harte" Unterscheidung.
        } else if (transformableLineValue == 0.0f && currentLineIndex < lastRowIndex) {
            // gehe eine Zeile weiter: Rekursiver Aufruf
            System.out.println("transformable Line Value == 0.0f --> Moved to next Line");
            this.moveToNextLine(_matrix, _currentColumnIndex, _formerLineIndex, currentLineIndex);

            // Fallunterscheidung: letzte Zeile erreicht
            // Sonst wenn der neue Wert an transformableLineValue bereits 0.0f ist das die letzte Zeile ist
        } else if (transformableLineValue == 0.0f && currentLineIndex == lastRowIndex) {
            // gehe eine Spalte weiter
            this.moveToNextColumn(_matrix, _currentColumnIndex, formerLineIndex);
        }
    }

    /**
     * Methode um in der Stufenform zur nächsten betrachteten Spalte zu springen
     * @param _matrix float[][]
     * @param _currentColumnIndex int als Index der aktuell betrachteten Spalte
     * @param _formerLineIndex int als Index der Zeile die verrechnet wird
     * - ruft die Methode determineSolvability() auf, wenn die letzte Stelle in der
     * @author EisDietlinde
     */
    private void moveToNextColumn(
            float[][] _matrix,
            int _currentColumnIndex,
            int _formerLineIndex
    ){
        // Hier werden die Indices in der neuen Spalte gesetzt
        // setze Index für die nächste Spalte
        currentColumnIndex = ++_currentColumnIndex;
        System.out.println("currentColumnIndex: " + currentColumnIndex);

        // Falls die Spalte eine Null-Spalte ist
        if(checkZeroColumn(_matrix)){
            saveCalculationStep(updatedMatrix,
                    -1000,
                    -1000,
                    -1000,
                    -1000,
                    -1000,
                    -1000,
                    textValues.get(String.valueOf(currentColumnIndex) + "e") + " Spalte ist leer. Die Stufenform wird in der " + textValues.get(String.valueOf(currentColumnIndex + 1) + "en") + " Spalte fortgesetzt:");
            // Gehe eine Spalte weiter (während der aktuelle Spaltenindex bis zu diesem Punkt hier einmal erhöht wurde)
            moveToNextColumn(_matrix, currentColumnIndex, _formerLineIndex);
            System.out.println("Nullspalte übersprungen, neuer Spaltenindex: " + currentColumnIndex);
        }

        // Index erste Zeile hochzählen
        formerLineIndex = ++_formerLineIndex;
        // Index der betrachteten Zeile befindet sich in der neuen Spalte direkt unter formerLine
        currentLineIndex = _formerLineIndex + 1;
        System.out.println("gehe zur nächsten Spalte… " + currentColumnIndex);
        // erste Zeile setzen
        formerLine = _matrix[_formerLineIndex];
        // Wert erste Zeile / neue Spalte setzen
        formerLineValue = _matrix[_formerLineIndex][currentColumnIndex];

        // Hier werden die Zeilen und Spalten in Abhängigkeit zu den neuen Indices gesetzt
        // Falls es noch eine Zeile danach gibt
        if (currentLineIndex <= lastRowIndex){
            // neue betrachtete Zeile speichern
            transformableLine = _matrix[currentLineIndex];
            // Wert in neuer betrachteten Zeile / neue Spalte setzen
            transformableLineValue = _matrix[currentLineIndex][currentColumnIndex];
        } else {
            // update Matrix und setze weiter mit rowReduction fort
            updatedMatrix = _matrix;
        }

        // Wenn letzte Zeile noch nicht erreicht ist und der neue Wert an transformableLineValue erweiterbar (!= 0.0f) ist:
            // ACHTUNG: Hier muss <= stehen damit der Algorithmus (bei einer lösbaren Matrix) weiterläuft:
            // Da dieser Fall zu einer weiteren Verrechnung führt, steht hier eine "weiche" Unterscheidung,
            // damit der Zugriffsbereich für die aufgerufene Methode integer aber inklusiv genug ist.
        if (currentLineIndex <= lastRowIndex && transformableLineValue != 0.0f) {
            rowReduction(_matrix, currentColumnIndex, formerLineIndex, currentLineIndex);

            // Wenn letzte Zeile noch nicht erreicht ist und der neue Wert an transformableLineValue bereits == 0.0f ist:
                // ACHTUNG: Hier muss < stehen damit kein OutOfBounds-Error entsteht:
                // Da dieser Fall zu keiner weiteren Verrechnung (sondern lediglich Wechsel zur darauffolgenden Zeile) stattfindet,
                // ist keine Inklusivität notwendig, abner eine "harte" Unterscheidung.
        } else if (currentLineIndex < lastRowIndex) {
            // gehe eine Zeile weiter
            this.moveToNextLine(_matrix, currentColumnIndex, formerLineIndex, currentLineIndex);

            // Wenn die letzte Spalte erreicht ist aber noch nicht die letzte Zeile:
                // WICHTIG: Frage nach letzter Spalte hat im Ablauf eine schwächere Position und muss daher zuerst abgefragt werden
                // --> else if-Blöcke in dieser Reihenfolge setzen!
                // Operator >= wählen, da Index in Methode als Erstes erhöht wird und Vergleichswert bereits überschritten haben kann
        } else if (currentLineIndex != lastRowIndex && currentColumnIndex == lastEchelonColumnIndex){
            updatedMatrix = _matrix;
            // Stufenform zu Ende, Lösbarkeit der Matrix ermitteln:
            determineSolvability(updatedMatrix);
            // Sonst wenn letzte Zeile aber noch nicht letzte Spalte erreicht ist:
        } else if (currentLineIndex == lastRowIndex && currentColumnIndex != lastEchelonColumnIndex) {
            // zur nächsten Spalte gehen
            this.moveToNextColumn(_matrix, currentColumnIndex, formerLineIndex);
        }
    }

    private boolean checkZeroColumn(float[][] _matrix) {
        // temporäres Array zum speichern der zu überprüfenden Spalten:
        float[] checkedColumn = new float[columnSize];
        // Vergelichsvariable zur Darstellung einer Null-Spalte, mit der verglichen werden soll.
        float[] emptyColumn = new float[columnSize];

        int i = 0;
        do {
            checkedColumn[i] = _matrix[i][currentColumnIndex];
            emptyColumn[i] = 0.0f;
            i++;
        } while (i <= lastRowIndex);
        // Wenn die Werte der zu überprüfenden Spalte die der Vergelichsvariable entsprechen
        if (Arrays.equals(checkedColumn, emptyColumn)) {
            System.out.println("NULLSPALTE IN SPALTE " + currentColumnIndex + " GEFUNDEN.");
            return true;
        }
        return false;
    }

    /**
     * Submethode von rowReduction() um eine Zeile mit einem Faktor zu erweitern
     * @param _line float[] als übergebene Zeile
     * @param _factor float als Faktor für die Erweiterung
     * @return extendedLine float[]
     * @author EisDietlinde
     */
    private float[] extendRow(float[] _line, float _factor) {
        // lokaler Zwischenspeicher für erweiterte Zeile
        float[] extendedLine = null;
        // Multipliziere alle Koeffizienten in transformableLine mit firstLineValue
        for (int i = 0; i < _line.length; i++) {
            _line[i] = _line[i] * _factor;
            // erweiterte Zeile speichern und mögliche -0.0f normalisieren
            extendedLine = normalizeZero(_line);
        }
        return extendedLine;
    }

    /**
     * Hilfsmethode von extendRow() um alle unglücklichen Werte -0.0f in 0.0f nach der Erweiterung umzuwandeln
     * @param _extendedLine float[] als übergebene Zeile
     * @return _extendedLine float[]
     * @author EisDietlinde
     */
    private float[] normalizeZero(float[] _extendedLine){
        // jeden Wert -0.0f in der Zeile mit 0.0f ersetzen
        for (int i = 0; i < _extendedLine.length; i++){
            if (_extendedLine[i] == -0.0f){
                _extendedLine[i] = 0.0f;
            }
        }
        return _extendedLine;
    }

    /**
     * Submethode von rowReduction() um eine Zeile von einer anderen Zeile abzuziehen
     * @param _minuendLine float[] als Minuend (Zeile von der subtrahiert wird)
     * @param _subtrahendLine float[] als Subtrahend (Zeile die subtrahiert wird)
     * @return difference float[] als Differenz beider Zeilen
     * @author EisDietlinde
     */
    private float[] subtractRow(float[] _minuendLine, float[] _subtrahendLine) {
        // lokaler Zwischenspeicher für Differenz-Zeile
        float[] difference = null;
        // Minuend-Zeile - Subtrahend-Zeile
        for (int i = 0; i < _minuendLine.length; i++ ) {
            _minuendLine[i] = _minuendLine[i] - _subtrahendLine[i];
            difference = _minuendLine;
        }
        System.out.print("Differenz: "); printLine(difference);
        return difference;
    }

    /**
     * Methode zum speichern eines Zwischenschrittes
     * @param _matrixCurrentForm als übergebene Matrix mit aktuellem Stand
     * @param _description als Beschreibungstext zum Zwischenschritt für die GUI
     * @author EisDietlinde
     */
    private void saveCalculationStep(float[][] _matrixCurrentForm, int _activeLineIndex, int _passiveLineIndex, int _factorLineIndex, int _factorColumnIndex, int _newZeroLineIndex, int _newZeroColumnIndex, String _description){
        // Daten & Zwischenstand der Matrix updaten und für die GUI in Liste eintragen
        calculationStepList.add(new GaussStufenformSteps(_matrixCurrentForm, _activeLineIndex, _passiveLineIndex, _factorLineIndex, _factorColumnIndex, _newZeroLineIndex, _newZeroColumnIndex, _description));
    }

    /**
     * Methode zur bestimmung der Lösbarkeit des LGS, abhängig von der Lösung der letzten Variable in der Matrix
     * @param _matrix float[][] als übergebene Matrix
     * @author EisDietlinde
     */
    public void determineSolvability(float[][] _matrix){
        // Wert der Matrix für die Substitution setzen:
        this.setSubstitutionMatrix(_matrix);
        // wenn letzte Variable != Ergebnisstelle und beide != 0.0f --> Matrix eindeutig lösbar
        if (_matrix[lastRowIndex][lastEchelonColumnIndex] != _matrix[lastRowIndex][lastColumnIndex]
                && _matrix[lastRowIndex][lastEchelonColumnIndex] != 0.0f
                && _matrix[lastRowIndex][lastColumnIndex] != 0.0f) {

            System.out.println("LÖSBAR");
            // Speicherpunk erstellen
            saveCalculationStep(_matrix,
                    -1000,
                    -1000,
                    -1000,
                    -1000,
                    -1000,
                    -1000,
                    "Die Matrix ist lösbar.");
            this.solvability = "solvable";
            this.setSubstitutionMode(false);
        }
        // wenn letzte Variable == 0.0 && Ergebnisstelle != 0.0f --> Matrix nicht lösbar
        else if (_matrix[lastRowIndex][lastEchelonColumnIndex] == 0.0f && _matrix[lastRowIndex][lastColumnIndex] != 0.0f) {
            System.out.println("NICHT LÖSBAR");
            // Speicherpunk erstellen
            saveCalculationStep(_matrix,
                    -1000,
                    -1000,
                    -1000,
                    -1000,
                    -1000,
                    -1000,
                    "Die Matrix ist nicht lösbar.");
            this.solvability = "nonSolvable";
        }
        // wenn letzte Variable == Ergebnisstelle --> Matrix allgemein lösbar
        else if (_matrix[lastRowIndex][lastEchelonColumnIndex] == _matrix[lastRowIndex][lastColumnIndex]) {
            System.out.println("ALLGEMEIN LÖSBAR");
            this.solvability = "substituteWithParameter";
            // Speicherpunk erstellen
            saveCalculationStep(_matrix,
                    -1000,
                    -1000,
                    -1000,
                    -1000,
                    -1000,
                    -1000,
                    "Die Matrix ist allgemein lösbar.");
            this.setSubstitutionMode(true);
        }
    }

    /**
     * Getter und Setter die den Wert der Matrix zur Substitution in GaussAlgorithm
     * @param _matrix float[][]
     */
    private void setSubstitutionMatrix(float[][] _matrix){
        this.substitutionMatrix = _matrix;
    }

    public float[][] getSubstitutionMarix(){
        return this.substitutionMatrix;
    }

    /**
     * Getter und Setter die den Wert der Matrix zur Substitution in GaussAlgorithm
     * @param _withParameter boolean
     */
    private void setSubstitutionMode(boolean _withParameter){
        this.substitutionMode = _withParameter;
    }
    public boolean getSubstitutionMode(){
        return this.substitutionMode;
    }

    /**
     * Getter von GaussStufenformSteps für GaussAlgorithm
     * @return calculationStepList ArrayList<GaussStufenformSteps>
     */
    public ArrayList<GaussStufenformSteps> getCalculationStepList() {
        return calculationStepList;
    }



    public static void main (String[] args) {

        // Testmatrix instantiieren
        float [][] dummyValues = {{4.0f, 0.0f, 2.0f, 8.0f}, {3.0f, 0.0f, 2.0f, -2.0f}, {1.0f, 0.0f, 2.0f, 4.0f}};
        // float [][] zeroColumnMatrix = {{1.0f, 0.0f, 0.0f, 2.0f, 1.0f}, {2.0f, 0.0f, 0.0f, 1.0f, 2.0f}, {4.0f, 0.0f, 0.0f, 3.0f, 3.0f}, {4.0f, 0.0f, 0.0f, 2.0f, 1.0f}};

        GaussStufenform testGauss = new GaussStufenform(dummyValues);

        System.out.println("testGauss: ");
        testGauss.printMatrix(testGauss.matrix);

        // Gib ihm!
        testGauss.rowEchelonForm();

        // Testmethode zum debuggen der GaussStufenformStep-Objekte und der Liste für die GUI
        for (GaussStufenformSteps step : testGauss.calculationStepList){
            System.out.println("DEBUG MATRIX LIST:");
            System.out.println(testGauss.calculationStepList.indexOf(step));
            System.out.println(testGauss.calculationStepList.get(testGauss.calculationStepList.indexOf(step)).getDescriptionText());
            testGauss.printMatrix(testGauss.calculationStepList.get(testGauss.calculationStepList.indexOf(step)).getSavedMatrix());
        }
    }
}
