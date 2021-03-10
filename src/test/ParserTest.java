import de.hsd.medien.numerik.mvc.model.knfDnf.BParser;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParserTest {
    private static BParser parser;

    @BeforeAll
    @DisplayName("Init Parser")
    public static void initParser(){
        parser = new BParser();
    }

    @Test
    @DisplayName("Test Resolve")
    public void testResolve(){
        String exampleInput = "NOTNOTNOTNOTTEST";
        assertEquals("TEST",parser.internResolveDoubleNegation(exampleInput));
        exampleInput = "NTONTOTESTEST";
        assertEquals(exampleInput,parser.internResolveDoubleNegation(exampleInput));
    }

    @Test
    @DisplayName("Test Preparations")
    public void testPreparations(){
        String exampleInput = "A AND B";
        assertEquals("A ∧ B", parser.prepareSpecificStringForOutput(exampleInput));
    }

    @Test
    @DisplayName("Test Invert")
    public void testInvert(){
        assertEquals("AND",parser.invertOperator("OR"));
        assertEquals("OR",parser.invertOperator("AND"));

    }

    @Test
    @DisplayName("Complete Parse")
    public void testParse(){
        String exampleInput= "A ⇒ (B ⇔ (A ∨ (B ∧ ¬C)))";
        parser.parse(exampleInput);
        assertEquals("(¬A∨B)∧(¬A∨A∨B)∧(¬A∨¬C∨¬B)∧(¬A∨¬A)∧(¬A∨¬B∨C)",parser.getKnf());
        assertEquals("(A∧¬B)∨(A∧¬A∧¬B)∨(A∧C∧B)∨(A∧A)∨(A∧B∧¬C)",parser.getDnf());

    }
}
