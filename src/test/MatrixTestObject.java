import java.util.*;

public class MatrixTestObject {

    private int numberOfVariables;
    public float[][] matrix;
    public enum SolutionType {
        UNIQUE,
        GENERAL,
        NONE,
        ZEROROW,
        ZEROCOLUMN
    }

    EnumMap<SolutionType, Runnable> functionsByMatrixType = new EnumMap<>(SolutionType.class);

    /**
     * Befüllung der EnumMap, bei der jedem Lösungstyp eine Populationsmethode zugewiesen wird
     */
    private void fillEnumList(){
        functionsByMatrixType.put(SolutionType.UNIQUE, this::fillUniqueSolutionMatrix);
        functionsByMatrixType.put(SolutionType.GENERAL, this::fillGeneralSolutionMatrix);
        functionsByMatrixType.put(SolutionType.NONE, this::fillNoSolutionMatrix);
        functionsByMatrixType.put(SolutionType.ZEROROW, this::fillFirstRowZeroMatrix);
        functionsByMatrixType.put(SolutionType.ZEROCOLUMN, this::fillSolutionVectorZero);
    }

    /**
     * Constructor für MatrixTestObject
     * @param _lowerBound als untere mögliche Grenze der Variablenanzahl der Matrix
     * @param _upperBound als obere mögliche Grenze der Variablenanzahl der Matrix
     * @param _solutionType als gewünschter Lösungstyp der Matrix
     */
    public MatrixTestObject(int _lowerBound, int _upperBound, SolutionType _solutionType){
        this.fillEnumList();
        this.numberOfVariables = this.generateRandomAmountOfVariables(_lowerBound, _upperBound);
        this.matrix = new float[numberOfVariables][numberOfVariables+1];
        this.functionsByMatrixType.get(_solutionType).run();
    }

    /**
     * Methode die eine randomisierte Zahl als Variablen-Anzahl für Matrizen der Äquivalenzklassen-Tests ausgibt:
     */
    private int generateRandomAmountOfVariables(int _lowerBound, int _upperBound){
        Random rand = new Random();
        return rand.nextInt(_upperBound - _lowerBound) + _lowerBound;
    }

    public void fillUniqueSolutionMatrix(){
        float number = 1.0f;
        // Grenzzahl bis wohin das Array wiederholend befüllt wird. Ist um 1.0f größer als die Spaltenanzahl (numberOfVariables + 1.0f)
        float upperBoundaryNumber = (float) numberOfVariables + 2.0f;

        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[i].length; j++){
                if (number < upperBoundaryNumber){
                    matrix[i][j] = number;
                    number += 1.0f;
                } else {
                    matrix[i][j] = number;
                    number = 1.0f;
                }
            }
        }
    }

    public void fillGeneralSolutionMatrix(){

        this.fillUniqueSolutionMatrix();

        // Ersetzt die erste Zeile der Matrix mit der letzten Zeile der Matrix
        float[] lineToCopyFrom = matrix[matrix.length - 1];
        float[] lineToSaveTo = matrix[0];
        System.arraycopy(lineToCopyFrom, 0, lineToSaveTo, 0, lineToSaveTo.length);
    }

    public void fillNoSolutionMatrix(){

        this.fillUniqueSolutionMatrix();

        // Ersetzt die erste Zeile der Matrix mit der letzten Zeile der Matrix
        float[] lineToCopyFrom = matrix[matrix.length - 1];
        float[] lineToSaveTo = matrix[0];
        System.arraycopy(lineToCopyFrom, 0, lineToSaveTo, 0, lineToSaveTo.length);

        // Verändert den letzten Wert in der ersten Zeile der Matrix
        matrix[0][matrix.length] = 0.0f;
    }

    public void fillFirstRowZeroMatrix(){

        this.fillUniqueSolutionMatrix();
        for(int i = 0; i <= matrix.length; i++){
            matrix[0][i] = 0.0f;
        }
    }

    public void fillSolutionVectorZero(){

        this.fillUniqueSolutionMatrix();
        for(int i = 0; i < matrix.length; i++){
            matrix[i][matrix.length] = 0.0f;
        }
    }

    public void printMatrix(float[][] m){
        for (float[] x : m) {
            for (float y : x) {
                System.out.print("\t " + y);
            }
            System.out.println();
        }
        System.out.println("\n");

    }


    public static void main(String[] args){

        MatrixTestObject testObject = new MatrixTestObject(2, 4, SolutionType.ZEROROW);
        testObject.printMatrix(testObject.matrix);
    }
}
