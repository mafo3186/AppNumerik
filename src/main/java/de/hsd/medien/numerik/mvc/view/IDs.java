package de.hsd.medien.numerik.mvc.view;

/**
 * enum-Klasse für IDs
 * - Anwendung: Actionsetter
 * @author Focken, Mareike
 */
public enum IDs {
    root,
    header,
    menu,
    start,
    clear,
    title,
    description,
    userinput,
    solution,
    headerAndScrollPane,
    sections;

    public enum Gauss{
        generateInputFieldButton,
        solveMatrixButton,
        showSolutionButton;
    }

    public enum KnfDnf{
        userinputSizeVBox,
        inputCommentSize,
        conjunctiveButton,
        disjunctionButton,
        negationButton,
        implicationButton,
        equivalenceButton,
        generateSolveButton,
        formulaTextField;
    }

    public enum Euklid{
        calcButton;
    }



}
