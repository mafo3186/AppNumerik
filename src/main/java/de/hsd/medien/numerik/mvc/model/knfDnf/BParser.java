package de.hsd.medien.numerik.mvc.model.knfDnf;

import de.hsd.medien.numerik.BoolBaseVisitor;
import de.hsd.medien.numerik.BoolLexer;
import de.hsd.medien.numerik.BoolParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.LinkedList;

/***
 *
 * @author Justin Liersch
 * Parser-Klasse
 */
public class BParser extends BoolBaseVisitor<Void> {
    /* Formel aus der Eingabe --> wird zur KNF */
    private String form;

    /* DNF - Formel <-- wird zur Kontrolle
     * in der Vorlesung erzeugt */
    private String dnf;

    /* Sollten keine Änderungen gemacht werden, werden keine Ergebnisse ausgegeben */
    private boolean show_result = true;

    /* Liste der Umformungen */
    private LinkedList<String> changes = new LinkedList<>();

    /* ANTLR Objekte */
    BoolLexer lex = null;
    CommonTokenStream token = null;
    BoolParser parser = null;
    ParseTree tree = null;

    /***
     * Veranlasst den Parser seine Arbeit zu tun
     */
    public void parse(String _input){
        /* Initalisieren */
        this.form = _input;
        this.dnf = null;
        this.prepareString();
        int passes = this.getNumberOfPasses();
        initialize();

        /* Äquivalenz/Implikation auf der obersten Ebene auflösen */
        this.visit(this.tree);
        /* Baum reinitialisieren mit umgeformten String
         *
         *  Damit erneut der Baum erstellt wird und die nächsten Kriterien erfüllt werden
         * */
        reinitialize(this.form);

        /* De Morgansche Regel anwenden
            NOT(A OR B) = NOT A AND NOT B
            NOT(A AND B) = NOT A OR NOT B */
        reinitialize(this.form);
        this.visit(this.tree);

        /* Distributivgesetz anwenden
            A AND|OR (B AND|OR C) = (A AND|OR B) AND|OR (A AND|OR C)
            Bsp. A AND (B OR C) = (A AND B) OR (A AND C) */
        for(int i = 0; i < passes; i++)
        {
            reinitialize(this.form);
            this.visit(this.tree);
        }
        /* Doppelte Klammern ersetzen
         *
         * Durch das Umformen entstehen manchmal doppelte Klammern. */
        this.removeDoubleBrackets();

        /* Kontrollausgaben */
        System.out.println(this.getChanges());
        System.out.println(this.getKnf());

        /* Die KNF in DNF umformen
         *
         * In der Mathe1 Vorlesung ist der KNF-Algorithmus gegeben.
         * Die Studierenden mussten die DNF ermitteln.
         *
         * Damit das Programm unterstützend zur Vorlesung genutzt werden kann,
         * wird die Umwandlung in KNF vorgeführt und geschildert.
         *
         * Zur Kontrolle wird die DNF ermittelt
         * */
        this.changeKNFtoDNF();
        System.out.println(this.getDnf());
    }

    /***
     * Initialisiert alle nötigen Variablen die für das
     * Parsen benötigt werden
     */
    private void initialize()
    {
        /* Initalisieren der ANTLR - Objekte*/
        this.lex = new BoolLexer(CharStreams.fromString(this.form));
        this.token = new CommonTokenStream(lex);
        this.parser = new BoolParser(token);
        this.tree = parser.expression();
    }

    /***
     * Die Variablen für das Parsen mit neuem String erneuern
     * @param _s Neue Formel
     */
    private void reinitialize(String _s)
    {
        /* Die ANTLR - Objekte mit einem bestimmten String reinitalisieren*/
        this.lex = new BoolLexer(CharStreams.fromString(_s));
        this.token = new CommonTokenStream(lex);
        this.parser = new de.hsd.medien.numerik.BoolParser(token);
        this.tree = parser.expression();
    }

    /***
     * Die Methode ermittelt die Anzahl der Durchgänge die nötig sind,
     * um die Formel vollständig umzuformen
     * @return Anzahl der nötigen Durchläufe
     */
    private int getNumberOfPasses()
    {
        /* Wie viele Äquivalenzen sind in der Formel vorhanden */
        int equi = (this.form.split("AQ", -1).length ) - 1;
        /* Wie viele Implikationen sind in der Formel enthalten */
        int imp = (this.form.split("IMP", -1).length ) - 1;

        return equi + imp;
    }

    /***
     * Löst die Implikation innerhalb einer Formel auf
     *
     * @param _left,_right,op  Teilstrings der Formel
     */
    public void resolveImplication(String _org,String _left, String _right, String op)
    {
        /* (NOT F1 OR F2) = (F1 => F2) */
        String form = "NOT" + _left + "OR" + _right;
        String logged = "Die Implikation in " + _org +" wurde aufgelöst:\n " + form +"\n";
        changes.add(logged);

        this.replaceString(_org,form);
    }

    /***
     * Löst die Äquivalenzen innerhalb einer Formel auf
     *
     * @param _left,_right,op  Teilstrings der Formel
     */
    public void resolveEquivalence(String _org,String _left, String _right, String op)
    {
        /* (F1 AND F2) OR (NOT F1 AND NOT F2) = (F1 <=> F2) */
        String form = "(" + _left + "AND" + _right +")OR(NOT"+_left+"ANDNOT"+_right+")";
        String logged = "Die Äquivalenz in " + _org + "wurde aufgelöst:\n" + form +"\n";
        changes.add(logged);

        this.replaceString(_org,form);
    }

    /***
     * Löst Doppelneagtionen in der Formel auf
     *
     * Bearbeitet automatisch die IV form
     */
    public void resolveDoubleNegation()
    {
        /* Aufruf des inneren Teils */
        if(this.form.contains("NOTNOT")){
            String neg = this.internResolveDoubleNegation(this.form);
            this.form = neg;
            String logged = "Doppelnegationen " + neg + " aufgelöst\n";
            changes.add(logged);
        }
    }


    /***
     * Der "innere" Teil zum Auflösen einer Doppelnegation.
     * Hier kann ein String mitgegeben werden
     * @param _form der String der bearbeitet werden soll
     * @return den umgeformten String
     */
    public String internResolveDoubleNegation(String _form)
    {
        if(_form.contains("NOTNOT"))
            _form = _form.replaceAll("NOTNOT","");

        return _form;
    }

    /***
     * Wendet die de Morgansche Regel auf die übergebenen Teilstring an
     * @param _left innerer linker Teil
     * @param _op AND oder OR
     * @param _right innerer rechter Teil
     */
    public void useDeMorgan(String _org,String _left, String _op, String _right)
    {
        /* Aufruf des inneren Teils */
        LinkedList<String> subs = this.getSubIdentifier(_org,_op);
        String form = this.internDeMorgan(subs, _op);
        String logged = "de Morgansche Regeln auf " + _org + "angewendet:\n" + form +"\n";
        changes.add(logged);
        this.replaceString(_org,form);

    }

    /***
     * Der innere Teil zum anwenden der DeMorgan'schen Regel
     */
    public String internDeMorgan(LinkedList<String> _subs, String _op)
    {
        /* Neuen Operator ermitteln */
        String new_op = this.invertOperator(_op);
        /* Die neuen Teilstrings zusammensetzen */
        String form = "(";
        for(int i = 0; i < _subs.size(); i++)
        {
            if(i > 0) form += new_op;
            if(_subs.get(i).contains("AND") || _subs.get(i).contains("OR")) form += "NOT(" + _subs.get(i) + ")";
            else form += "NOT" + _subs.get(i);
        }
        form += ")";
        return form;
    }

    //------------------------------------------------------------------------------------------------------------------
    /***
     * Wendet das Distributivgesetz auf die übergebenen Teilstrings an
     * @param _outer Äußerer Teilstring
     * @param _op Äußerer Operator AND oder OR
     * @param _innerOp Innerer Operator --> Wenn _op = AND dann _innerOp = OR und umgekehrt
     * @param _inner Liste der inneren Expressions oder Variablen
     */
    public void resolveDistribution(String _org,String _outer, String _op, String _innerOp, LinkedList<String> _inner)
    {
        String form = "";
        for(int i = 0; i < _inner.size(); i++)
        {
            form = form + "(" + _outer + _op + _inner.get(i) + ")";
            if(i + 1 < _inner.size()) form = form + _innerOp;
        }
        String logged = "Distributivgesetz auf " + _org + " angewendet:\n" + form +"\n";
        changes.add(logged);
        this.replaceString(_org,form);
    }

    /***
     * Vorbereitung für die Anwendung des Distributivgesetzes
     * @param _left Teilstring links
     * @param _op Operator
     * @param _right Teilstring rechts
     */
    public void prepareDistribution(String _org,String _left, String _op, String _right)
    {
        /* Neuen Operator ermitteln */
        String innerOp = this.invertOperator(_op);
        /* Die inneren Variablen ermitteln */
        LinkedList<String> inner = this.getSubIdentifier(_right,innerOp);
        this.resolveDistribution(_org,_left,_op,innerOp,inner);
    }
    //--------------------------------------------------------------------------------------------------------------

    /***
     * Holt aus einer Expression alle einzelnen Identifier
     * @param _exp Die Aussage die zerlegt werden soll
     * @param _op der inner Operator an dem getrennt werden soll
     * @return LinkedList<String> mit den einzelnen Identifiern
     */
    public LinkedList<String> getSubIdentifier(String _exp, String _op)
    {
        /* Liste in denen die einzelnen Variablen gespeichert werden */
        LinkedList<String> idens = new LinkedList<>();

        /* Expression wird am Operator gespalten */
        String[] ident = _exp.split(_op,0);

        /* Eventuelle Klammern entfernen und die Liste füllen */
        for(int i = 0; i < ident.length; i++)
        {
            String value = ident[i].replace("NOT(", "");
            value = value.replace("(","");
            value = value.replace(")","");
            idens.add(value);
        }

        return idens;
    }

    /***
     * Dreht den Operator um
     * @param _op zu invertierender Operator
     * @return String invertierten Operator
     */
    public String invertOperator(String _op)
    {
        switch(_op)
        {
            case "AND": return "OR";
            case "OR": return "AND";
            default: return "";
        }
    }

    /***
     * Sucht in der IV this.form einen (Teil)String und ersetzt diesen
     * @param _org zu ersetzender Teilstring
     * @param _new neuer String
     */
    public void replaceString(String _org,String _new)
    {
        if(this.form.compareTo(_org) != 0)
        {
            this.form = this.form.replace(_org,_new);
        }
        else this.form = _new;
    }

    /***
     * Die Methode formt die gebildete KNF in DNF um
     */
    public void changeKNFtoDNF()
    {
        /* Zeichen umwandeln. Bsp ∧ --> AND */
        this.prepareString();
        /* Die KNF in die Disjunktionen spalten */
        String[] sub_form = this.form.split("AND");
        String DNF = "";
        /* Zusammenbauen der DNF */
        for (int i = 0; i < sub_form.length; i++)
        {
            /* Disjunktion von Konjuktionen */
            if(i > 0) DNF += "OR";
            /* Ermittelt die einzelnen Variablen innerhalb der Substrings */
            LinkedList<String> subs = this.getSubIdentifier(sub_form[i],"OR");
            /* Setzt eine Konjunktion zusammen */
            String substring = "(";
            for(int j = 0; j < subs.size(); j++)
            {
                if(j > 0) substring += "AND";
                subs.set(j,this.internResolveDoubleNegation("NOT" + subs.get(j)));
                substring += subs.get(j);
            }
            substring += ")";
            DNF += substring;
        }
        this.dnf = DNF;
    }

    /***
     * Ändert die Zeichen aus der GUI in Strings die der Parser versteht
     */
    public void prepareString()
    {
        this.form = this.form.replaceAll("∧","AND"); //AND
        this.form = this.form.replaceAll("∨","OR");//OR
        this.form = this.form.replaceAll("⇒","IMP");//IMP
        this.form = this.form.replaceAll("⇔","AQ");//AQ
        this.form = this.form.replaceAll("¬","NOT");//NEGATION

        this.form = this.form.replaceAll(" ","");
    }


    /***
     * Bereitet den String für die Ausgabe vor und ersetzt die Ausdrücke durch die Symbole aus der GUI
     */
    public void prepareStringForOutput()
    {
        this.form = this.prepareSpecificStringForOutput(this.form);
    }

    /***
     * Einen speziellen String für die Ausgabe vorbereiten
     * @param s zu ändernden String
     * @return
     */
    public String prepareSpecificStringForOutput(String s)
    {
        s = s.replaceAll("AND","∧"); //AND
        s = s.replaceAll("OR","∨");//OR
        s = s.replaceAll("IMP","⇒");//IMP
        s = s.replaceAll("AQ","⇔");//AQ
        s = s.replaceAll("NOT","¬");//NEGATION

        return s;
    }

    /***
     * Ersetzt in der IV form die eventuell auftauchenden überflüssige Klammern
     */
    public void removeDoubleBrackets()
    {
        boolean tooManyBrackets = true;

        while(tooManyBrackets)
        {
            this.form = this.form.replace("((","(");
            this.form = this.form.replace("))",")");
            if(this.form.contains("(("))  tooManyBrackets = true;
            else tooManyBrackets = false;
        }
        if(this.form.contains("AND") && !this.form.contains("OR")
                || !this.form.contains("AND") && this.form.contains("OR"))
        {
            this.form = this.form.replace("(","");
            this.form = this.form.replace(")",")");
        }
    }

    /***
     * Gibt die IV this.form zurück
     * @return IV this.form
     */
    public String getKnf()
    {
        if(this.show_result) {
            this.prepareStringForOutput();
            return this.form;
        }
        return "";
    }

    /***
     * Gibt die DNF zurück
     * @return String output mit dem vorbereiteten String
     */
    public String getDnf()
    {
        if(this.show_result) {
            String output = this.prepareSpecificStringForOutput(this.dnf);
            return output;
        }
        return "";
    }

    /***
     * Gibt die Liste der Änderungen zurück
     * @return LinkedList<String>
     */
    public LinkedList<String> getChanges()
    {
        for(int i = 0; i < this.changes.size(); i++)
        {
            String s = this.prepareSpecificStringForOutput(this.changes.get(i));
            this.changes.set(i,s);
        }
        if(this.changes.isEmpty()) {
            this.changes.add("Nichts zum umformen.\n Entweder bereits in der KNF oder Fehler in der Eingabe");
            this.show_result = false;
        }
        return this.changes;
    }

    /***
     * Die Methode läuft über den geparsten String (Baum) und
     * sucht bzw prüft die Bedingungen die geändert werden müssen
     * @param ctx Expressioncontext wird durch das visit der Mutterklasse übergeben
     * @return rekursiver Aufruf! Arbeitet mit den IV
     */
    @Override
    public Void visitExpression(BoolParser.ExpressionContext ctx) {
        /* Taucht eine Implikation auf? */
        if (ctx.op != null && ctx.op.getText().equals("IMP")) {
            resolveImplication(ctx.getText(),ctx.left.getText(), ctx.right.getText(), ctx.op.getText());
            this.resolveDoubleNegation();
            return super.visitExpression(ctx);
        }

        /* Taucht eine Äquivalenz auf? */
        else if (ctx.op != null && ctx.op.getText().equals("AQ")) {
            resolveEquivalence(ctx.getText(),ctx.left.getText(), ctx.right.getText(), ctx.op.getText());
            this.resolveDoubleNegation();
            return super.visitExpression(ctx);
        }

        /* Kann die deMorgan'sche Regel abgewendet werden */
        else if(ctx.getText().contains("NOT("))
        {
            //ctx.getChild--> if child.start = "(" AND child.stop = ") --> ctx.left + ctx.op + ctx.right"
            //ParseTree test = ctx.getChild(1);
            //if(ctx.getChild(1).getText().startsWith("(") && ctx.getChild(1).getText().endsWith(")"))
            if(ctx.getChild(0).getText().equals("NOT") &&
                    ctx.getChild(1).getText().startsWith("(") && ctx.getChild(1).getText().endsWith(")"))
            {
                this.useDeMorgan(ctx.getText(),ctx.getChild(1).getChild(1).getChild(0).getText(),
                        ctx.getChild(1).getChild(1).getChild(1).getText(),
                        ctx.getChild(1).getChild(1).getChild(2).getText());
                this.resolveDoubleNegation();
                return super.visitExpression(ctx);
            }
        }
        else if(ctx.left != null && ctx.right != null && ctx.op != null) {
            /* Kann das Distributivgesetz angewendet werden? */
            if(((ctx.right.getText().contains("AND") && ctx.op.getText().equals("OR"))
                    || (ctx.left.getText().contains("AND") && ctx.op.getText().equals("OR")))
                    && !this.form.contains("NOT("))
            {
                /* Es wird bestimmt wo welche Teil steht um ihn dann entsprechend umzuformen */
                /* Steht das AND rechts? */
                if(ctx.right.getText().contains("AND"))
                {
                    this.prepareDistribution(ctx.getText(),ctx.left.getText(), ctx.op.getText(), ctx.right.getText());
                    this.resolveDoubleNegation();
                    return super.visitExpression(ctx);
                }
                /* Steht das AND links? */
                else
                {
                    this.prepareDistribution(ctx.getText(),ctx.right.getText(), ctx.op.getText(), ctx.left.getText());
                    this.resolveDoubleNegation();
                    return super.visitExpression(ctx);
                }
            }
        }
        return super.visitExpression(ctx);
    }
}
