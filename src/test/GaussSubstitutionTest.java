import de.hsd.medien.numerik.mvc.model.gauss.GaussStufenform;
import de.hsd.medien.numerik.mvc.model.gauss.GaussSubstitution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Klasse GausMatrixTest, zum Testen der verschiedenen Methoden der Klasse GausMatrix.
 * @author Alali,Salar
 */
public class GaussSubstitutionTest
{

    //null Matrix
    private final float[][] nullMatrix = {{0,0,0},{0,0,0},{0,0,0}};

    //3x4 Matrix mit eindeutigen Lösung :
    private final float[][] uniqueSolutions3x4 = {{2.0f, 8.0f, 2.0f, 4.0f}, {0.0f, -2.0f, 7.0f, -1.0f}, {0.0f, 0.0f, 4.0f, -4.0f}};
    //3x4 Matrix mit allgemeine Lösungen :
    private final float[][] infinitelyManySolutions3x4 = {{1.0f, 2.0f, 3.0f, 4.0f}, {0.0f, -4.0f, -8.0f, -12.0f}, {0.0f, 0.0f, 0.0f, 0.0f}};
    //4x5 Matrix mit eindeutigen Lösung :
    private final float[][] uniqueSolutions4x5 = {{3.0f, 4.0f, 0.0f, -2.0f, 3.0f}, {0.0f, -3.0f, 0.0f , 6.0f, -1.0f}, {0.0f, 0.0f, 4.0f, 7.0f, 52/9f}, {0.0f, 0.0f, 0.0f, 5.0f, 31/9.0f}};

    //Matrix mit wenigster zugelassener Variablenanzahl 2x3 :
    private final float[][] minUniqueSolutions2x3 = {{2, 2, 2}, {0, 2, -2}};
    //Matrix mit höchster zugelassener Variablenanzahl 8x9 :
    private final float[][] maxUniqueSolutions8x9 = {{5.0f, 4.0f, 0.0f, -2.0f, 4.0f, 3.0f, 4.0f, 1.0f, 5.0f},
            {0.0f, -7/5f, 0.0f, 26/5f, -2/5f, -4/5f, 63/5f, 2/5f, 0.0f},
            {0.0f, 0.0f, 4.0f, 48/7f, 41/7f, -30/7f, 12.0f, 22/7f, 5.0f},
            {0.0f, 0.0f, 0.0f, 137/7f, 25/7f, 29/7f, 48.0f, 17/7f, 3.0f},
            {0.0f, 0.0f, 0.0f, 0.0f, -1027/548f, -297/274f, -908/137f, 355/274f, 595/548f},
            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 3300/427f, -1351/261f, -818/1027f, 544/1027f},
            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 3998/199f, -2258/385f, -2500/241f},
            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -366/2267f, -1048/287f}};

    //3x4 Matrix mit Lösungsvektor = 0 :
    private final float[][] solutionVectorZero = {{2.0f, 6.0f, 2.0f, 0.0f}, {0.0f, 1.0f, 5.0f, 0.0f}, {0.0f, 0.0f, 2.0f, 0.0f}};
    //3x4 Matrix mit nur negativen Koeffizienten und Konstanten  :
    private final float[][] allNumbersNegativUniqueSolution3x4 = {{-2.0f, -12.0f, -2.0f, -8.0f}, {0.0f, -2.0f, -6.0f, -10.0f}, {0.0f, 0.0f, -8.0f, -16.0f}};
    //3x4 Matrix mit nur geraden (Zahlen) Koeffizienten und Konstanten  :
    private final float[][] allUnevenNumbersUniqueSolution3x4 = {{3.0f, 5.0f, 7.0f, 9.0f}, {0.0f, 11.0f, 13.0f, 15.0f}, {0.0f, 0.0f, 17.0f, 19.0f}};
    //3x4 Matrix mit nur ungeraden (Zahlen) Koeffizienten und Konstanten  :
    private final float[][] allEvenNumbersUniqueSolution3x4 = {{2.0f, 4.0f, 6.0f, 8.0f}, {0.0f, 10.0f, 12.0f, 14.0f}, {0.0f, 0.0f, 16.0f, 18.0f}};

    @Mock
    private GaussStufenform mockGaussStufenform;

    @Mock
    private GaussSubstitution mockGaussSubstitution;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Testet den Aufruf der Methode substitute().
     * @throws Exception
     */
    @Test
    public void invokeSubstitute() throws Exception{
        final Answer answer = invocationOnMock -> null;
        doAnswer(answer).when(mockGaussSubstitution).substitute();
    }

    /**
     * Testet den Aufruf der Methode findSolutionOfVariable().
     * @throws Exception
     */
    @Test
    public void invokeFindSolutionOfVariable() throws Exception{
        final ArrayList<Float> mockEquation = mock(ArrayList.class);
        final ArrayList<Float> mockSolution = mock(ArrayList.class);
        final Answer answer = invocationOnMock -> null;
        doAnswer(answer).when(mockGaussSubstitution).findSolutionOfVariable(mockEquation, mockSolution);
    }

    /**
     * Testet den return-Wert der Methode findSolutionOfVariable().
     * @throws Exception
     */
    @Test
    public void testFindSolutionOfVariable() throws Exception{
        final ArrayList<Float> mockSolution = mock(ArrayList.class);
        final ArrayList<Float> mockEquation = mock(ArrayList.class);
        final ArrayList<Float> returnList = mock(ArrayList.class);
        when(mockGaussSubstitution.findSolutionOfVariable(mockEquation, mockSolution)).thenReturn(returnList);
    }

    /**
     * Testet den Aufruf der Methode sendToSubstitutionStepList().
     * @throws Exception
     */
    @Test
    public void invokeSendToSubstitutionStepList() throws Exception{
        final ArrayList<Float> mockEquationBeforeSubstitution = mock(ArrayList.class);
        final ArrayList<Float> mockEquationAfterSubstitution = mock(ArrayList.class);
        final float mockVariableSolution = 2.0f;
        final Answer answer = invocationOnMock -> null;
        doAnswer(answer).when(mockGaussSubstitution).sendToSubstitutionStepList(mockEquationBeforeSubstitution, mockEquationAfterSubstitution, mockVariableSolution);
    }

    /**
     * Testet den Aufruf der Methode substituteWithParameter().
     * @throws Exception
     */
    @Test
    public void invokeSubstituteWithParameter() throws Exception{
        final Answer answer = invocationOnMock -> null;
        doAnswer(answer).when(mockGaussSubstitution).substituteWithParameter();
    }

    /**
     * Methode getMatrixTest(), die als Test die Methode getMatrix() in der Klasse GaussMatrix testet.
     * @throws Exception
     */
    @Test
    void getMatrixTest() throws Exception
    {
        final float[][] matrix = mockGaussStufenform.getSubstitutionMarix();
        assertEquals(mockGaussStufenform.getSubstitutionMarix(), matrix);
    }

    /**
     * Methode substituteMinUniqueSolutions2x3(), Matrix mit wenigster zugelassener Variablenanzahl testet.
     * @throws Exception
     */
    @Test
    void substituteMinUniqueSolutions2x3() throws Exception
    {
        GaussSubstitution mminUniqueSolutions2x3Matrix = new GaussSubstitution(minUniqueSolutions2x3);
        mminUniqueSolutions2x3Matrix.substitute();

        Float[] actual = mminUniqueSolutions2x3Matrix.getSolutionsInArray();
        Float[] expected = { -1f, 2f,};

        assertArrayEquals(expected, actual);
    }//Ende substituteMinUniqueSolutions2x3()

    /**
     * Methode substituteUniqueSolutions4x5(), die eine Matrix mit eindeutige Lösung und 4 Variablen hat.
     * @throws Exception
     */
    @Test
    void substituteUniqueSolutions4x5() throws Exception
    {
        GaussSubstitution uniqueSolutions4x5Matrix = new GaussSubstitution(uniqueSolutions4x5);
        uniqueSolutions4x5Matrix.substitute();

        Float[] actual = uniqueSolutions4x5Matrix.getSolutionsInArray();
        Float[] expected = { 0.6888888889f,0.23888886f, 1.7111111111f,-0.82222205f };

        assertArrayEquals(expected, actual);
    }//Ende substituteUniqueSolutions4x5()

    /**
     * Methode substituteMaxUniqueSolutions8x9(), die eine Matrix mit höchster zugelassener Variablenanzahl testet.
     * @throws Exception
     */
    @Test
    void substituteMaxUniqueSolutions8x9() throws Exception
    {
        GaussSubstitution maxUniqueSolutions8x9Matrix = new GaussSubstitution(maxUniqueSolutions8x9);
        maxUniqueSolutions8x9Matrix.substitute();

        Float[] actual = maxUniqueSolutions8x9Matrix.getSolutionsInArray();
        Float[] expected = { 22.617773f, 6.0863895f, 6.4760733f, -10.213179f, -17.087633f, 16.406467f, -3.0111873f, -8.53387f};

        assertArrayEquals(expected, actual);
    }//Ende substituteMaxUniqueSolutions8x9()


    /**
     * Methode substituteSolutionVectorZero(), die eine  Matrix mit Lösungsvektor = 0 testet.
     * @throws Exception
     */
    @Test
    void substituteSolutionVectorZero() throws Exception
    {
        GaussSubstitution solutionVectorZeroMatrix = new GaussSubstitution(solutionVectorZero);
        solutionVectorZeroMatrix.substitute();

        Float[] actual = solutionVectorZeroMatrix.getSolutionsInArray();
        Float[] expected = {0.0f, 0.0f, 0.0f};

        assertArrayEquals(expected, actual);
    }//Ende substituteSolutionVectorZero()

    /**
     * Methode substituteAllNumbersNegativUniqueSolution3x4(), die eine Matrix mit 3 Variablen und  nur negativen
     * Koeffizienten und Konstanten.
     * @throws Exception
     */
    @Test
    void substituteAllNumbersNegativUniqueSolution3x4() throws Exception
    {
        GaussSubstitution allNumbersNegativUniqueSolution3x4Matrix = new GaussSubstitution(allNumbersNegativUniqueSolution3x4);
        allNumbersNegativUniqueSolution3x4Matrix.substitute();

        Float[] actual = allNumbersNegativUniqueSolution3x4Matrix.getSolutionsInArray();
        Float[] expected = {2.0f, -1.0f, 8.0f};

        assertArrayEquals(expected, actual);
    }//Ende substituteAllNumbersNegativUniqueSolution3x4()

    /**
     * Methode substituteAllUnevenNumbersUniqueSolution3x4(), die eine  Matrix mit 3 Variablen und mit nur geraden
     * (Zahlen) Koeffizienten und Konstanten testet.
     * @throws Exception
     */
    @Test
    void substituteAllUnevenNumbersUniqueSolution3x4() throws Exception
    {
        GaussSubstitution allUnevenNumbersUniqueSolution3x4Matrix = new GaussSubstitution(allUnevenNumbersUniqueSolution3x4);
        allUnevenNumbersUniqueSolution3x4Matrix.substitute();

        Float[] actual = allUnevenNumbersUniqueSolution3x4Matrix.getSolutionsInArray();
        Float[] expected = {1.117647f, 0.04278079f, 0.32085547f};

        assertArrayEquals(expected, actual);
    }//Ende substituteAllUnevenNumbersUniqueSolution3x4()

    /**
     * Methode substituteAllEvenNumbersUniqueSolution3x4(), die eine  Matrix mit 3 Variablen und mit nur geraden
     * (Zahlen) Koeffizienten und Konstanten testet.
     * @throws Exception
     */
    @Test
    void substituteAllEvenNumbersUniqueSolution3x4() throws Exception
    {
        GaussSubstitution allEvenNumbersUniqueSolution3x4Matrix = new GaussSubstitution(allEvenNumbersUniqueSolution3x4);
        allEvenNumbersUniqueSolution3x4Matrix.substitute();

        Float[] actual = allEvenNumbersUniqueSolution3x4Matrix.getSolutionsInArray();
        Float[] expected = {1.125f, 0.05f, 0.5250001f};

        assertArrayEquals(expected, actual);
    }//Ende substituteAllEvenNumbersUniqueSolution3x4()

    /**
     * Methode findSolutionOfVariableTest(), die die Methode findSolutionOfVariableTest() in der Klasse GaussMatrix testet, mit einer Matrix
     * mit eindeutigen Lösung und 2 Variablen.
     * @throws Exception
     */
    @Test
    void findSolutionOfVariableTest() throws Exception
    {
        //Dummy-Objekt, um solveVariable() aufrufen zu können.
        GaussSubstitution matrix = new GaussSubstitution(nullMatrix);

        //Erstellen die aktuelle Parametern der Methode solveVariable();
        //ArrayList, die zu lösende Gleichung enthält.
        ArrayList<Float> equation = new ArrayList<>();
        //Ein paar beliebige Zahlen zu der Gleichung einfügen.
        equation.add(2f);
        equation.add(10f);

        //Der andere Parameter der Methode solveVariable(), die die vorherige Lösung der anderen Variable enthält.
        ArrayList<Float> solution = new ArrayList<>();

        ArrayList<Float> actual = matrix.findSolutionOfVariable(equation, solution);

        ArrayList<Float> expected = new ArrayList<>();
        expected.add(5f);

        assertEquals(actual, expected);
    }//Ende findSolutionOfVariableTest()

    /**
     * Methode substituteWithParameterTest(), die die Methode substituteWithParameterTest() in der Klasse GaussMatrix, wo
     * es allgemeine Lösungen für die Matrix geben.
     * @throws Exception
     */
    @Test
    void substituteWithParameterTest() throws Exception
    {
        GaussSubstitution infinitelyManySolutions3x4Matrix = new GaussSubstitution(infinitelyManySolutions3x4);
        infinitelyManySolutions3x4Matrix.substituteWithParameter();

        Float[] actual = infinitelyManySolutions3x4Matrix.getSolutionsInArray();
        Float[] expected = {1.0f, 1.0f, -1.0f};

        assertArrayEquals(expected, actual);
    }//Ende substituteWithParameterTest()
}//Ende GaussMatrixTest
