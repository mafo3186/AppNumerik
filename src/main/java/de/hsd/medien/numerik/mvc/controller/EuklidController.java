package de.hsd.medien.numerik.mvc.controller;

import de.hsd.medien.numerik.mvc.model.euklid.EuklidAlgorithm;
import de.hsd.medien.numerik.mvc.model.euklid.euklidObj;
import de.hsd.medien.numerik.mvc.model.euklid.extendEuklidObj;
import de.hsd.medien.numerik.mvc.model.manager.IAlgorithm;
import de.hsd.medien.numerik.mvc.view.AbstractAlgorithmView;
import de.hsd.medien.numerik.mvc.view.EuklidView;
import de.hsd.medien.numerik.mvc.view.IDs;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.LinkedList;

/***
 * @author Justin Liersch
 */
public class EuklidController extends AbstractAlgorithmController {

    private Stage euklidStage;
    private EuklidView euklidView;
    private EuklidAlgorithm euklidAlgorithm;
    private boolean correctInput = false;

    /**
     * Model und View an Controller übergeben
     * MVC: Controller
     *
     * @param _algorithm
     * @param _view
     */
    public EuklidController(IAlgorithm _algorithm, AbstractAlgorithmView _view) {
        super(_algorithm, _view);
        this.euklidView = (EuklidView) _view;
        this.euklidAlgorithm = (EuklidAlgorithm) _algorithm;
        this.euklidStage = this.euklidView.getAlgorithmStage();
        initializeSetOnAction("#" + IDs.Euklid.calcButton);
    }

    /**
     * Methode getUserInput()
     * MVC: Controller
     * ruft Methode in View auf, Usereingabe-Daten zu lesen
     */
    @Override
    public void getUserInput() {
        TextField firstValue = (TextField) this.euklidStage.getScene().getRoot().lookup("#firstValue");
        TextField secondValue = (TextField) this.euklidStage.getScene().getRoot().lookup("#secondValue");
        if(!firstValue.getText().equals("") && !secondValue.getText().equals(""))
        {
            this.euklidAlgorithm.setFirstValue(Integer.valueOf(firstValue.getText()));
            this.euklidAlgorithm.setSecondValue(Integer.valueOf(secondValue.getText()));
            this.correctInput = true;

        }
        //this.euklidAlgorithm.setFirstValue(Integer.valueOf(((TextField) this.euklidStage.getScene().getRoot().lookup("#firstValue")).getText()));
        //this.euklidAlgorithm.setSecondValue(Integer.valueOf(((TextField) this.euklidStage.getScene().getRoot().lookup("#secondValue")).getText()));
    }

    /**
     * Actionsetter für in der View neu generierte Elemente
     */
    @Override
    protected void initializeSetOnAction(String _id) {
        Button calc = (Button) this.euklidStage.getScene().getRoot().lookup(_id);
        calc.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getUserInput();
                if (correctInput == true){
                    euklidAlgorithm.clearResults();
                    euklidAlgorithm.runAlgorithm();
                    euklidView.showEuklidResult(euklidAlgorithm.getGgt(), euklidAlgorithm.getEuklidCalcs(), euklidAlgorithm.getExtendCalcs());
                    correctInput = false;
                }
            }});
    }


}
