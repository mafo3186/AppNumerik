package de.hsd.medien.numerik.mvc.model.gauss;
import java.util.ArrayList;

public class GaussSubstitution
{
    //Instanz Variablen
    //Anzahl der Variablen in der Matrix, entspricht der Anzahl der Gleichung(Zeilen) in der Matrix.
    private int numberOfVariable;
    //Anzahl der Spalten in der Matrix
    private int numberOfColumns;
    //Speicherplatz für Matrix reservieren.
    private float [][] matrix;

    //Arraylist von Typ GaussSubstitutionSteps, die Rechenschritte speichert.
    private ArrayList<GaussSubstitutionSteps> SubstitutionStepList = new ArrayList<>();


    /**
     * Konstruktor um ein DummyMatrix zu erstellen.
     * @author Alali,Salar
     */
    public GaussSubstitution(float[][] _matrix)
    {
        this.matrix = _matrix;
        this.numberOfVariable = _matrix.length;
    }//Ende Konstruktor.

    /**
     * Method getMatrix, die die Matrix als InstanzVariable der Klasse Matrix zurückgibt,(Testzweck in GausMatrixTest).
     * @return matrix
     * @author Alali,Salar
     */
    public float[][] getMatrix()
    {
        return this.matrix;
    }//Ende getMatrix()

    /**
     * Methode solutions(),die die Lösungen der Variables in einer Liste zurückgibt.(Testzweck in GausMatrixTest).
     * @return List von Variables Lösungen.
     * @author Alali,Salar.
     */
    public ArrayList<Float> solutions()
    {
        ArrayList<Float> solutions = new ArrayList<>();
        //For-Each Schleife über die DataToGUI, und speichere die Lösungen in neuer Arraylist.
        for (GaussSubstitutionSteps a: this.SubstitutionStepList)
        {
            solutions.add(a.getSolutionOfVariable());
        }
        //die neue Arraylist mit der Lösungen zurückgeben.
        return solutions;
    }//Ende solutions()

    /**
     * Methode getSolutionsInArray(), die die Lösungsliste in normalem Array umwandelt.(Testzweck in GausMatrixTest)
     * @return
     * @author Alali,Salar.
     */
    public Float[] getSolutionsInArray()
    {
        //neues Array mit gleichem Groß der Arraylist erstellen
        Float[] arraySolution = new Float[solutions().size()];
        //Durch toArray() die Arraylist in Array umwandelt, und daises zurückgeben.
        return this.solutions().toArray(arraySolution);
    }//Ende getSolutionsInArray()


    /**
     * Methode drawMatrix, die die Matrix auf der Konsole ausgibt.
     * @author Alali,Salar
     */
    public void drawMatrix()
    {

        //Schleife über das 2D-Array, um alle Koeffizienten zu lesen.
        //For-Schleife über alle Zeilen des Arrays
        for(int i = 0; i< numberOfVariable; i++)
        {
            //For-Schleife über alle Spalten des Arrays
            for(int j = 0; j< numberOfVariable; j++)
            {
                //Alle Koeffizienten mit den Variablen ausgeben.
                //Wenn der Koeffizient positiv ist mit, dann "+" ausgeben.
                if(matrix[i][j]>=0)
                    System.out.print(" +"+matrix[i][j]+((char)('A'+j))+" ");
                    //Wenn der Koeffizient negativ ist, dann mit dem eigenen Vorzeichen ausgeben.
                else if(matrix[i][j]<0)
                    System.out.print(" "+matrix[i][j]+((char)('A'+j))+" ");

                //alle Konstante ausgeben,matrix[i][j] |j =variable-1 : ist da wo die Konstante im Array liegt.
                if(j == (numberOfVariable -1))
                {
                    //Wenn die Konstante positiv ist, dann mit "+" ausgeben.
                    if(matrix[i][j+1]>=0)
                        System.out.print(" | " + "+" +  matrix[i][j+1]);
                        //Wenn die Konstante negativ ist, dann eigenen Vorzeichen ausgeben.
                    else if(matrix[i][j+1]<0)
                        System.out.print(" | " + matrix[i][j+1]);
                }

            }
            System.out.println("");

        }
    }//Ende drawMatrix()


    /**
     * Methode substitute(), die die Matrix in Stufenform bekommt, speichert die Koeffizienten und die Konstante in jeder
     * Zeile in einer ArrayList von Typ float, und erzeugt beim ersten Aufruf eine andere ArrayList von Typ float
     * ,die die Lösungen der Variablen speichert, und gibt die beiden Listen in jeder Zeile zu der Methode solveVariable().
     * Am Anfang wird der Koeffizient und die Konstant in der letzte Zeile gespeichert, und sie werden an solveVariable()
     * übergeben.
     * Danach wird die Liste, die die Koeffizienten und Konstanten beinhaltet geleert, um die Koeffizienten und
     * Konstanten der nächsten höheren Zeile zu speichern.
     * @author Alali,Salar
     */
    public void substitute()
    {
        //Arraylist um die Lösungen zu speichern
        ArrayList<Float> solutions = new ArrayList<Float>();
        //Arraylist um die Koeffizienten und Konstanten in jeder Zeile zu speichern.
        ArrayList<Float> equation = new ArrayList<Float>();

        //Schleife über alle Zeilen der Matrix, beginnen von der letzte Zeile(Wo nur ein Koeffizient und eine Konstant sind)
        for(int i = numberOfVariable - 1; i >= 0 ; i--)
        {
            //Schleife über alle spalten der aktuelle Zeile
            for(int j = 0; j <= numberOfVariable; j++)
            {
                //Die Koeffizienten, die nach dem Stufenform '0' geworden sind, werden ignoriert.
                if(j < i)
                {
                    continue;
                }
                //Ansonsten, speichere(einfügen) jede Zahl in dieser Zeile in der Liste.
                else
                {
                    equation.add(matrix[i][j]);
                }
            }
            // rufe die Methode findSolutionOfVariable(), und gebe ihr die Liste, die die Koeffizienten und Konstant beinhaltet.
            //findSolutionOfVariable() gibt die aktualisierte Lösungliste zurück , dann speicher diese in der variable solutions.
            solutions = findSolutionOfVariable(equation, solutions);

            // leere die Liste equation, um die Koeffizienten und Konstant der nächste Zeile zu speichern.
            equation.clear();
        }
        return;
    }//Ende substitute()


    /**
     * Methode findSolutionOfVariable, die die Koeffizienten und Konstant der einzelnen Zeilen der Matrix von substitute() erhaltet,
     * dann ersetzt die Lösungen der Variable, die ggf. schon berechnet und in der Lösungsliste schon gespeichert  wurden,
     * und diese mit der entsprechende Koeffizient multipliziert. Dann löst sie der aktuellen Variable dadurch, dass sie
     * der neu berechnete Konstante auf die Koeffizient teilt. Danach speichert die neue berechnete Lösung in der Lösungsliste.
     * @param _equation die Koeffizienten und Konstant der aktuelle Zeile
     * @param _solution die aktuelle Lösungsliste
     * @return die neue Lösungsliste.
     * @author Alali,Salar
     */
    public ArrayList<Float> findSolutionOfVariable(ArrayList<Float> _equation, ArrayList<Float> _solution)
    {
        //Liste newSolutions, die anfangs die aktuelle(n) Lösung(en) speichert.
        ArrayList<Float> newSolutions = _solution;

        //Variable tempSolution, die die Zahl auf der rechten Seite der Gleichung ( nach der Addition bzw. Substraktion)
        //später speichern wird. z.B.:(2Y = 4 | 4 ist hier das tempSolution )
        float tempSolution = 0.0f;

        //Variable coefficient, die die  Koeffizient der aktuell gesuchten Variable speichert.(diese hat immer der
        // Index '0', da jede Zeile in der Matrix(,die in Stuffenform gebracht wurde) mit der Koeffizient der aktuelle
        // gesuchte Variable anfängt )
        float coefficient = _equation.get(0);


        //Wenn gerade die erste (untere) Zeile (Gleichung) gelöst wird, dann ist die Lösungliste noch leer, dann.. :
        if(newSolutions.size() == 0)
        {
            //der tempSolution ist in diesem Fall die Konstant, da die erste (untere) Zeile immer in der Form
            // (z.B.: 6Z = 8) ist.
            tempSolution = _equation.get(1);

        }
        //Ansonsten, wenn die Lösungsliste nicht leer ist, dann :
        else
        {
            //Schleife über die _equation Liste, die die Zahlen(Koeffizienten und Konstant) der Gleichung beinhaltet.
            //der Schleife fängt bei der zweiten Index'1' an, da  die Koeffizienten in der ersten Index schon in Variable
            //coefficient gespeichert wurde.
            for(int i = 1; i < _equation.size(); i++)
            {
                //Versuch folgendes zu machen:
                try
                {
                    //Addiere die aktuelle tempSolution(die noch '0' ist) auf [ Lösungen der Variable *(mal)
                    // ihre Koeffizienten ]
                    // die Zuweisung fängt direkt mit (-) an, da die Zahlen immer auf der rechte Seite der Gleichung
                    // gebracht werden sollen.
                    tempSolution += - _solution.get( (_solution.size()-1) - (i-1) )*_equation.get(i);
                }
                //die vorherige Zeile (Befehl) wird fehlerlos laufen, bis i gleich der index der Konstant wird.
                //Da wird keine Lösungen in Lösungsliste mehr geben, die mit der Konstant multipliziert werden
                // können, da wird ein Fehler aufgetaucht.
                // Wenn so, dann mach folgendes:
                catch (Exception e)
                {
                    //Addiere die aktuelle tempSolution (die durch den vorherigen Befehl berechnet wurde)
                    // auf die Konstant.
                    tempSolution += _equation.get(i);
                }
            }
        }

        // nun wird die Lösung der aktuellen Variable berechnet und den Wert in der Lösungsliste gespeichert.
        newSolutions.add(tempSolution/coefficient);

        //Neue Arraylist von Typ Float, um die Gleichung nach Substitution zu speichern.
        ArrayList<Float> equationAfterSubstitute = new ArrayList<>();
        //die Koeffizient der Variable erstens einfügen.
        equationAfterSubstitute.add(coefficient);
        //Dann die Konstant einfügen.
        equationAfterSubstitute.add(tempSolution);

        //die Lösung rechnen und in Variable speichern.
        float variableSolution = tempSolution/coefficient;

        /*Neue Variable, die die Gleichung vor der Substitution speichert, da hier die Werte der Arraylist per Referenz
         * aufgerufen werden und da in Zeile 282 die Arraylist _equation bei Auslesen jede Gleichung geleert wird,
         * dann "klone" ich eine Kopie von _equation bevor sie geleert wird, wobei diese Kopie von einer bestimmten
         * Datentyp sein soll, daher wird sie mit "casting" in Datentyp Arraylist<Float> gespeichert.
         */
        ArrayList<Float> equationBeforeSubstitute = (ArrayList<Float>) _equation.clone();

        //Aufrufe die Methode sendToGUI(), mit Übergabe der Daten, um die Daten zu GaussSubstitutionSteps zu senden.
        sendToSubstitutionStepList(equationBeforeSubstitute, equationAfterSubstitute,variableSolution);

        //Gebe die neue Lösungsliste zurück.
        return newSolutions;
    }//Ende findSolutionOfVariable()


    /**
     * Methode sendToGui(), die die Rechenschritte der einzelnen Zeile in der Matrix während der Substation speichert,
     * indem sie ein Objekt der Klasse GaussSubstitutionSteps erstellt, und die Daten in den Objekten speichert.
     * @param _equationBeforeSubstitute die Gleichung vor Substation
     * @param _equationAfterSubstitute die Gleichung nach der Substation
     * @param _variableSolution die Lösung der Variable.
     * @author Alali,Salar
     */
    public void sendToSubstitutionStepList(ArrayList<Float> _equationBeforeSubstitute, ArrayList<Float> _equationAfterSubstitute, float _variableSolution)
    {
        // Erstellen ein Objekt von GaussSubstitutionSteps mit Übergabe der Daten
        GaussSubstitutionSteps data = new GaussSubstitutionSteps(_equationBeforeSubstitute, _equationAfterSubstitute, _variableSolution);
        //Das erstellte Objekt an SubstitutionStepList einfügen.
        SubstitutionStepList.add(data);
    }//Ende sendToGUI()

    /**
     * Methode getSubstitutionStepList(), die die Arraylist SubstitutionStepList zurückgibt, (Kapselung)
     * @return ArrayList SubstitutionStepList
     * @author Alali,Salar
     */
    public ArrayList<GaussSubstitutionSteps> getSubstitutionStepList()
    {
        return this.SubstitutionStepList;
    }//Ende getSubstitutionStepList()


    /**
     * Methode substituteWithParameter(),die die Matrix löst in Fall der allgemeine Lösung.
     * die Methode bekommt die Matrix in Stufenform , speichert die Koeffizienten und die
     * Konstante in jeder Zeile (außer die letzte Zeile, wo alle Koeffizienten und die
     *  Konstante '0' sind )in einer ArrayList von Typ float, und erzeugt beim ersten Aufruf eine andere ArrayList
     * von Typ float,die die Lösungen der Variablen speichert und enthaltet schon '1' als Lösung der letzte Variable
     * , und gibt die beiden Listen in jeder Zeile zu der Methode solveVariable().
     * solveVariable() erhaltet hier die Lösungliste im Gegenteil von substitute() mit '1' als Lösung für die letzte
     * Variable, und daher berechnet sie danach EINE Lösung von der allgemeine Lösung der anderen Variablen.
     * Danach wird die Liste, die die Koeffizienten und Konstanten beinhaltet geleert, um die Koeffizienten und
     * Konstanten der nächsten höheren Zeile zu speichern.
     * @author Alali,Salar
     */
    public void substituteWithParameter()
    {
        //Arraylist um die Lösungen zu speichern
        ArrayList<Float> solutions = new ArrayList<Float>();
        //Arraylist um die Koeffizienten und Konstanten in jeder Zeile zu speichern.
        ArrayList<Float> equation = new ArrayList<Float>();
        //Füge '1' auf die Lösungsliste als Lösung der letzten Variable im Fall einer allgemeine Lösung ein.
        solutions.add(1.0f);

        //Füge die letzte Zeile in der Matrix im Fall einer allgemeine Lösung.
        //Die Zahlen in der letzten Zeile werden eingefügt bevor der Lösung anfängt, denn sie habe drauf keine Auswirkung.
        //die Zahlen werden Zeilenweise in der Arraylist eingefügt, indem sie von 1D-Array zu Arraylist umgewandelt.
        //Das geschieht mit Hilfe der Methode asList().
        ArrayList<Float> bevor = new ArrayList<Float>();
        ArrayList<Float> after = new ArrayList<Float>();

        //Schleife über die Letzte Zeile
        for(int i = numberOfVariable - 1; i >= numberOfVariable -1 ; i--)
        {
            //Schleife über alle Zahlen.
            for(int j = 0; j <= numberOfVariable; j++)
            {
                //Die Koeffizienten, die nach dem Stufenform '0' geworden sind, werden ignoriert.
                if(j < i)
                {
                    continue;
                }
                //Füge die Koeffizienten und Konstanten an die Before und After Listen ein.
                else
                {
                    bevor.add(matrix[i][j]);
                    after.add(matrix[i][j]);
                }
            }
        }

        //Füge die beiden Listen an SubstitutionStepList-Liste als die Daten der letzte Zeile, um an GaussSubstitutionSteps zu senden.
        sendToSubstitutionStepList(bevor,after ,1.0f);

        //Schleife über alle restlichen Zeilen der Matrix.
        for(int i = numberOfVariable - 2; i >= 0 ; i--)
        {
            //Schleife über alle spalten der aktuelle Zeile
            for(int j = 0; j <= numberOfVariable; j++)
            {
                //Die Koeffizienten, die nach dem Stufenform '0' geworden sind, werden ignoriert.
                if(j < i)
                {
                    continue;
                }
                //Ansonsten, speichere(einfügen) jede Zahl in dieser Zeile in der Liste.
                else
                {
                    equation.add(matrix[i][j]);
                }
            }
            // rufe die Methode solveVariable(), und gebe ihr die Liste, die die Koeffizienten und Konstant beinhaltet.
            solutions = findSolutionOfVariable(equation, solutions);

            // leere die Liste equation, um die Koeffizienten und Konstant der nächste Zeile zu speichern.
            equation.clear();
        }
        return;
    }//Ende substituteWithParameter()





    /**
     * Methode main, die ausgeführt wird, wenn die Klasse GaussSubstitution ausgeführt wird.
     * @author Alali,Salar
     */
    public static void main(String[] args)
    {
        System.out.println("Eindeutige Lösungmatrix : ");
        float [][] uniqueSolution = {{2,2,4,4},{0,2,1,2}, {0,0,5,10}};


        //Matrix erstellen und zeigen:
        GaussSubstitution uniqueSolutionMatrix = new GaussSubstitution(uniqueSolution);


        uniqueSolutionMatrix.drawMatrix();
        //Lösungen berechnen
        uniqueSolutionMatrix.substitute();
        //Die Lösungen Schritt für Schritt von unteren Zeile bis zur Oberer.
        System.out.println("Die Lösungen sind : " + uniqueSolutionMatrix.solutions() + "\n");


        System.out.println("Allgemeine Lösungmatrix");
        float [][] infinitelyManySolutions = {{1,2,3,4},{0,-4,-8,-12}, {0,0,0,0}};

        //Matrix erstellen und zeigen:
        GaussSubstitution infinitelyManySolutionsMatrix = new GaussSubstitution(infinitelyManySolutions);
        infinitelyManySolutionsMatrix.drawMatrix();
        //Die Lösungen berechnen
        infinitelyManySolutionsMatrix.substituteWithParameter();
        //Die Lösungen Schritt für Schritt von unteren Zeile bis zur Oberer.
        System.out.println("Eine von unendlichen Lösungen ist : " + infinitelyManySolutionsMatrix.solutions());

    }//Ende Main()
}//Ende GaussSubstitution
