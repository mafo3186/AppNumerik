package de.hsd.medien.numerik.mvc.model.gauss;

import java.util.ArrayList;

/**
 * Klasse GaussSubstitutionSteps, die die RechnenSchritte der Substitution jeder Gleichung in unterschiedliche Objekte speichert,
 *  um Daten für GUI zur Verfügung stellen.
 * @version 1.0
 * @author Alali,Salar
 */
public class GaussSubstitutionSteps
    {
        //Die Gleichung bevor Substitution
        private final ArrayList<Float> equationBeforeSubstitute;
        //Die Gleichung nach der Substitution
        private final ArrayList<Float> equationAfterSubstitute;
        //Lösung der Variable aktueller Gleichung
        private final float solutionOfVariable;

        /**
         * Konstruktor der GaussSubstitutionSteps.
         * @param _equationBeforeSubstitute die Gleichung vor Substitution
         * @param _equationAfterSubstitute  die Gleichung nach Substitution
         * @param _solutionOfVariable Lösung der Variable
         * @author Alali,Salar
         */
        public GaussSubstitutionSteps(ArrayList<Float> _equationBeforeSubstitute, ArrayList<Float> _equationAfterSubstitute, float _solutionOfVariable)
            {
                this.equationBeforeSubstitute = _equationBeforeSubstitute;
                this.equationAfterSubstitute = _equationAfterSubstitute;
                this.solutionOfVariable = _solutionOfVariable;
            }//Ende Konstruktor

        /**
         * Methode getEquationBeforeSubstitute(), die die Gleichung vor der Substitution und IV von außen zur Verfügung
         * stellt.
         * @return die Gleichung vor der Substitution
         * @author Alali,Salar
         */
        public ArrayList<Float> getEquationBeforeSubstitute()
            {
                return this.equationBeforeSubstitute;
            }//Ende getEquationBeforeSubstitute

        /**
         *Methode getEquationAfterSubstitute(), die die Gleichung nach der Substitution und IV von außen zur Verfügung
         * stellt
         * @return die Gleichung nach der Substitution
         * @author Alali,Salar
         */
        public ArrayList<Float> getEquationAfterSubstitute()
            {
                return this.equationAfterSubstitute;
            }//Ende getEquationAfterSubstitute()

        /**
         * Methode getSolutionOfVariable(), die die Lösung der Variable nach der Substitution und IV von außen zur Verfügung
         * stellt
         * @return Lösung der Variable
         * @author Alali,Salar
         */
        public float getSolutionOfVariable()
            {
                return this.solutionOfVariable;
            }//Ende getSolutionOfVariable()
    }//Ende Klasse GaussSubstitutionSteps
