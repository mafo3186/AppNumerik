import de.hsd.medien.numerik.mvc.model.gauss.GaussStufenform;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GaussStufenformTest {

    // BestimmteSpalte(n) = 0
    private final float[][] oneColumnZero = {{4.0f, 0.0f, 2.0f, 8.0f}, {3.0f, 0.0f, 3.0f, -2.0f}, {1.0f, 0.0f, 2.0f, 4.0f}};

    /**
     * Methode zur Rückgabe folgender Werte für die Matrizen-Tests:
     */
    public float[] returnMatrixTestingValues(float[][] _matrix){
        int lastRowIndex = _matrix.length - 1;
        int lastColumnIndex = _matrix[lastRowIndex].length - 1;
        int lastEchelonColumnIndex = _matrix[lastRowIndex].length - 2;
        float lastVariableCoefficient = _matrix[lastRowIndex][lastEchelonColumnIndex];
        float lastVariableValue = _matrix[lastRowIndex][lastColumnIndex];
        float[] matrixTestingValues = {lastVariableCoefficient, lastVariableValue};
        return matrixTestingValues;
    }

    /**
     * Testet Äquivalenzklassen von Matrizen mit eindeutiger, genereller und gar keiner Lösung.
     * @throws Exception
     */
    @Test
    public void testGaussEliminationUniqueSolution_between2And10() throws Exception{
        GaussStufenform unique2To10 = new GaussStufenform(new MatrixTestObject(2, 10, MatrixTestObject.SolutionType.UNIQUE).matrix);
        float[] testValues = returnMatrixTestingValues(unique2To10.rowEchelonForm());
        assertTrue((testValues[1]/testValues[0]) != 0.0f);
    }

    @Test
    public void testGaussEliminationUniqueSolution_between10And20() throws Exception{
        GaussStufenform unique10To20 = new GaussStufenform(new MatrixTestObject(10, 20, MatrixTestObject.SolutionType.UNIQUE).matrix);
        float[] testValues = returnMatrixTestingValues(unique10To20.rowEchelonForm());
        assertTrue((testValues[1]/testValues[0]) != 0.0f);
    }


    @Test
    public void testGaussEliminationGeneralSolution_between2And10() throws Exception{
        GaussStufenform general2To10 = new GaussStufenform(new MatrixTestObject(2, 10, MatrixTestObject.SolutionType.GENERAL).matrix);
        float[] testValues = returnMatrixTestingValues(general2To10.rowEchelonForm());
        assertEquals(testValues[0], testValues[1]);
    }

    @Test
    public void testGaussEliminationGeneralSolution_between10And20() throws Exception{
        GaussStufenform general10To20 = new GaussStufenform(new MatrixTestObject(10, 20, MatrixTestObject.SolutionType.GENERAL).matrix);
        float[] testValues = returnMatrixTestingValues(general10To20.rowEchelonForm());
        assertEquals(testValues[0], testValues[1]);
    }

    @Test
    public void testGaussEliminationNoSolution_between2And10() throws Exception{
        GaussStufenform no2To10 = new GaussStufenform(new MatrixTestObject(2, 10, MatrixTestObject.SolutionType.NONE).matrix);
        float[] testValues = returnMatrixTestingValues(no2To10.rowEchelonForm());
        assertEquals(testValues[0], 0.0f);
        assertNotEquals(testValues[1], 0.0f, 0.0);
        assertNotEquals(testValues[0], testValues[1]);
    }

    @Test
    public void testGaussEliminationNoSolution_between10And20() throws Exception{
        GaussStufenform no2To10 = new GaussStufenform(new MatrixTestObject(10, 20, MatrixTestObject.SolutionType.NONE).matrix);
        float[] testValues = returnMatrixTestingValues(no2To10.rowEchelonForm());
        assertEquals(testValues[0], 0.0f);
        assertNotEquals(testValues[1], 0.0f, 0.0);
        assertNotEquals(testValues[0], testValues[1]);
    }


    @Test
    public void testGaussEliminationOneZeroRow_between2And10() throws Exception{
        GaussStufenform zeroRow2To10 = new GaussStufenform(new MatrixTestObject(2, 10, MatrixTestObject.SolutionType.ZEROROW).matrix);
        float[] testValues = returnMatrixTestingValues(zeroRow2To10.rowEchelonForm());
        List<float[]> listLastRow = Arrays.asList(testValues);
        assertTrue(listLastRow.stream().allMatch(listLastRow.get(0)::equals));
    }

    @Test
    public void rowEchelonForm_oneColumnZero() throws Exception{
        GaussStufenform rowEchelonForm_oneColumnZero = new GaussStufenform(oneColumnZero);
        float[] testValues = returnMatrixTestingValues(rowEchelonForm_oneColumnZero.rowEchelonForm());
        assertEquals(testValues[0], 0.0f);
        assertNotEquals(testValues[1], 0.0f);
    }

    /*
    @Test
    public void testAlgorithmSolutionVectorZero_between2And10() throws Exception{
        int randomVariableNumber = generateRandomAmountOfVariables(2, 10);
        GaussStufenform zeroRow2To10 = new GaussStufenform(new MatrixTestObject(randomVariableNumber, MatrixTestObject.SolutionType.ZEROROW).matrix);
        float[] testValues = returnMatrixTestingValues(zeroRow2To10.rowEchelonForm());
        assertNotEquals(testValues[0], 0.0f);
        assertEquals(testValues[1], 0.0f);
    }
    */
    /*
    @Test
    public void rowEchelonForm_matrixValuesZero() throws Exception{
        GaussStufenform rowEchelonForm_matrixValuesZer0 = new GaussStufenform(matrixValuesZero);
        float[] testValues = returnMatrixTestingValues(rowEchelonForm_matrixValuesZer0.rowEchelonForm());
        assertEquals(testValues[0], 0.0f);
        assertNotEquals(testValues[1], 0.0f);
    }
     */
}
