package de.hsd.medien.numerik.mvc.model.manager;

/**
 * Interface IAlgorithm
 * wird von jedem Algorithmus-Model-Objekt implementiert, damit jeder konkrete Algorithmus
 * - identifiziert wird (Voraussetzung für Erstellung von Controllern und Views)
 * - kurz und lang beschrieben
 * - ausgeführt und
 * - in Lösungsschritten abgespeichert wird.
 * @author Focken, Mareike
 */
public interface IAlgorithm {

    /** @return AlgorithmIdentifier zur Identifizierung und Setzen von View und Controller-Elementen*/
    AlgorithmIdentifier getIdentifier();

    /** @return String Kurzbeschreibung für z.B. Button-Text in Startansicht */
    String getShortDescription();

    /** @return String Algorithmus-Beschreibung/Erklärung in Algorithmus-View */
    String getAlgorithmDescription();

    /** Aufruf/Ausführung der Berechnung im Algorithmus-Model */
    void runAlgorithm();

    /** Abspeicherung der Lösungsschritte im Model zur Bereitstellung für View */
    void createSolutionSteps();

}
