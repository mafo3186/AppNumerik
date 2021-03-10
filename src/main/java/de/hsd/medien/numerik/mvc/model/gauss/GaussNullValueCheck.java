package de.hsd.medien.numerik.mvc.model.gauss;

import java.util.Arrays;

/**
 * Testklasse für Methoden, die auf Nullspalten und Nullzeilen testen
 */
public class GaussNullValueCheck {

    /**
     * Hilfsmethode zum drucken der Matrix beim testen.
     * @param _matrix float[][] als Matrix die gedruck werden soll
     * @author EisDietlinde
     */
    public static void printMatrix(float[][] _matrix) throws NullPointerException{
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

    public static void printMatrixString(String[][] _matrix) throws NullPointerException{
        try {
            for (String[] x : _matrix) {
                for (String y : x) {
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



    static boolean hasSameValueColumns(float[][] _matrix){
        //N = Number of rows
        int rows = _matrix.length;
        //M = Number of cols
        int col = _matrix[0].length;

        for (int i = 0; i < rows; i++) {

            //Vergleichswert: null
            float curr = 0.0f;

            for (int j = 0; j < col; j++) {
                //wenn gelesener Wert NICHT null
                if (_matrix[i][j] != 0.0f) {
                    //und aktueller Wert aber Null
                    if (curr == 0.0f) {
                        //dann nimmt aktuellen Wert den gelesenen WErt an
                        curr = _matrix[i][j];
                    } else if (_matrix[i][j] != curr) {
                        //This row has different values in columns
                        return false;
                    }
                }
            }
        }
        return true; //All rows (in one column?) have the same value
    }

    static boolean hasZeroColumns(float[][] _matrix){
        //N = Number of rows
        int rows = _matrix.length;
        //M = Number of cols
        int col = _matrix[0].length;

        for (int i = 0; i < rows; i++) {

            //Vergleichswert: null
            float curr = 0.0f;

            for (int j = 0; j < col; j++) {
                //wenn gelesener Wert NICHT null
                if (_matrix[i][j] != 0.0f) {
                    //und aktueller Wert aber Null
                    if (curr == 0.0f) {
                        //dann nimmt aktueller Wert den gelesenen WErt an
                        curr = _matrix[i][j];
                    } else if (_matrix[i][j] != curr) {
                        //This row has different values in columns
                        return false;
                    }
                }
            }
        }
        return true; //All rows (in one column?) have the same value
    }


    static boolean hasZeroRows(float[][] _matrix){
        //N = Number of rows
        int rows = _matrix.length;
        //M = Number of cols
        int col = _matrix[0].length;

        //lauf über SPALTEN
        for (int j = 0; j < col; j++) {

            //Vergleichswert: null
            float curr = 0.0f;

            for (int i = 0; i < rows; i++) {
                //wenn gelesener Wert NICHT null
                if (_matrix[i][j] != 0.0f) {
                    //und aktueller Wert aber Null
                    if (curr == 0.0f) {
                        //dann nimmt aktueller Wert den gelesenen WErt an
                        curr = _matrix[i][j];
                    } else if (_matrix[i][j] != curr) {
                        //This column has different values in columns
                        return false;
                    }
                }
            }
        }
        return true; //All columns (in one row?) have the same value
    }


    public static boolean matrixHasZeroLines2(float[][] _matrix){
        // genutzte Variablen:
        int rowSize = _matrix.length;
        int columnSize = _matrix[0].length;

        int lastEchelonColumnIndex = _matrix[_matrix.length - 1].length - 2;
        // Vergelichsvariable zur Darstellung einer Null-Zeile, mit der verglichen werden soll:
        float[] emptyLine = new float[rowSize];

        int i = 0;
        int j = 0;

        // Prüfe jede Zeile
        while (i < columnSize) {
            // Fülle emptyLine zum Abgleich:
            while (j < lastEchelonColumnIndex){
                emptyLine[j] = 0.0f;
                j++;
            }
            // Abgleich der aktuellen Zeile mit emptyLine:
            if(Arrays.equals(_matrix[i], emptyLine)){
                return true;
            }
            i++;
        }
        return false;
    }

    public static boolean matrixHasZeroLinesReparaturVersuch(float[][] _matrix){
        // genutzte Variablen:
        int columnSize = _matrix[0].length;
        int rowSize = _matrix.length;
        int lastEchelonColumnIndex = _matrix[0].length - 1;
        // Vergleichsvariable zur Darstellung einer Null-Zeile, mit der verglichen werden soll:
        float[] emptyLine = new float[rowSize];

        //Zeile
        int i = 0;
        //Spalte
        int j = 0;

        // Prüfe jede Zeile
        while (i < columnSize-1){
            // Fülle emptyLine zum Abgleich:
            while(j < rowSize-1) {
                //do{
                emptyLine[j] = 0.0f;
                j++;
            }
            //} while ();
            // Abgleich der aktuellen Zeile mit emptyLine:
            if(Arrays.equals(_matrix[i], emptyLine)){
                return true;
            }
            i++;
        } //while (i < columnSize-1);
        return false;
    }

    public static boolean matrixHasZeroLinesString(String[][] _matrix){
        // genutzte Variablen:
        int rowSize = _matrix[0].length;
        int columnSize = _matrix.length;
        int lastEchelonColumnIndex = _matrix[_matrix.length - 1].length - 2;
        // Vergelichsvariable zur Darstellung einer Null-Zeile, mit der verglichen werden soll:
        String[] emptyLine = new String[rowSize];

        int i = 0;
        int j = 0;

        // Prüfe jede Zeile
        do {
            // Fülle emptyLine zum Abgleich:
            do{
                emptyLine[j] = "0";
                j++;
            } while (j < lastEchelonColumnIndex);
            // Abgleich der aktuellen Zeile mit emptyLine:
            if(Arrays.equals(_matrix[i], emptyLine)){
                return true;
            }
            i++;
        } while (i < columnSize);
        return false;
    }



    public static void main(String [] args){

    /** Test-Matritzen für Debugging & GUI-Tests */
    float[][] solvableMatrix =  {{4.0f, 2.0f, 2.0f, 8.0f}, {3.0f, -4.0f, 3.0f, -2.0f}, {1.0f, 3.0f, 2.0f, 4.0f}};
    float[][] nonsolvableMatrix = {{1.0f,1.0f,1.0f,3.0f},{1.0f,2.0f,3.0f,4.0f},{3.0f,2.0f,1.0f,0.0f}};
    float [][] allgemeinMatrix = {{1.0f,2.0f,3.0f,4.0f},{5.0f,6.0f,7.0f,8.0f},{9.0f,10.0f,11.0f,12.0f}};

    //ToDo: funktioniert nicht, wenn man noch einen Wert hinzufügt, na toll
    //float[][] nullSpaltenMatrix = {{0.0f, 1.0f},{0.0f,2.0f}};
    //float[][] nullZeilenMatrix = {{0.0f, 0.0f},{3.0f, 4.0f}};

/*
    System.out.println("NullZeilenmatrix");
    printMatrix(nullZeilenMatrix);
    System.out.println("NullSpaltenMatrix");
    printMatrix(nullSpaltenMatrix);

    System.out.println("Hat NullZeilenmatrix Nullspalten?");
    boolean value;
    value = hasZeroColumns(nullZeilenMatrix);
    System.out.println(value);

    System.out.println("Hat NullSpaltenmatrix  Nullspalten?");
    value = hasZeroColumns(nullSpaltenMatrix);
    System.out.println(value);

 */



        GaussAlgorithm algorithm = new GaussAlgorithm();
        float[][] nullSpaltenMatrix = {{0.0f, 1.0f},{0.0f,2.0f}};
        float[][] nullZeilenMatrix = {{0.0f, 0.0f},{3.0f, 4.0f}};

        float[][] test1 =  {{0.0f, 0.0f, 0.0f, 0.0f}, {3.0f, -4.0f, 3.0f, -2.0f}, {1.0f, 3.0f, 2.0f, 4.0f}};
        float[][] test2 = {{0.0f,0.0f,0.0f,0.0f},{1.0f,2.0f,3.0f,4.0f},{3.0f,2.0f,1.0f,0.0f}};
        float[][] test3 = { {0.0f,0.0f,0.0f,0.0f,0.0f,0.0f},
                            {1.0f,1.0f,1.0f,2.0f,3.0f,4.0f},
                            {3.0f,2.0f,1.0f,0.0f,1.0f,1.0f,},
                            {1.0f,1.0f,1.0f,2.0f,3.0f,4.0f},
                            {1.0f,1.0f,1.0f,2.0f,3.0f,4.0f}};
        float[][] test4 = {{0.0f,0.0f,0.0f,0.0f},{1.0f,2.0f,3.0f,4.0f},{3.0f,2.0f,1.0f,0.0f}};

        System.out.println("Nullzeile in Nullspaltenmatrix?:" + algorithm.matrixHasZeroLines(nullSpaltenMatrix));
        System.out.println("Nullzeile in Nullzeilenmatrix?:" + algorithm.matrixHasZeroLines(nullZeilenMatrix));


        String[][] nullSpaltenMatrixString = {{"0","1"},{"0","2"}};
        String[][] nullZeilenMatrixString = {{"0","0"},{"3","4"}};

        String[][] nullMatrixString = {{"0","0"},{"0","0"}};
        System.out.println("NullspalteString erwartet:" + GaussNullValueCheck.matrixHasZeroLinesString(nullSpaltenMatrixString));
        System.out.println("NullzeileString erwartet:" + GaussNullValueCheck.matrixHasZeroLinesString(nullZeilenMatrixString));
        System.out.println("Null erwartet:" + GaussNullValueCheck.matrixHasZeroLinesString(nullMatrixString));

        //System.out.println("test1: Nullzeile in Nullzeilenmatrix?:" + algorithm.matrixHasZeroLines(test1));
        //System.out.println("test3 Nullzeile in Nullzeilenmatrix?:" + algorithm.matrixHasZeroLines(test3));
        System.out.println("test3 Nullzeile in Nullzeilenmatrix?:" + hasZeroRows(test3));




    }

}
