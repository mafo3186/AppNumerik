package de.hsd.medien.numerik.mvc.view;

import de.hsd.medien.numerik.mvc.model.euklid.EuklidAlgorithm;
import de.hsd.medien.numerik.mvc.model.euklid.euklidObj;
import de.hsd.medien.numerik.mvc.model.euklid.extendEuklidObj;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.LinkedList;

/***
 * @author Justin Liersch
 */
public class EuklidView extends AbstractAlgorithmView {

    EuklidAlgorithm euklid;
    Stage stage;

    /***
     * Konstruktor
     * @param _euklid Algorithmusobjekt
     * @param _stage Stage
     */
    public EuklidView(EuklidAlgorithm _euklid, Stage _stage){
        super(_euklid, _stage);
        System.out.println("EuklidView-Objekt wurder erstellt.");
        this.euklid = _euklid;
        this.stage = _stage;
        updateView();
    }

    @Override
    protected void updateHeader() {
        //Titel und Description auf Werte des Objekts setzen
        Label title = (Label) this.stage.getScene().getRoot().lookup("#" + IDs.title);
        title.setText(euklid.getShortDescription());
        Text description = (Text) this.stage.getScene().getRoot().lookup("#" + IDs.description);
        description.setText(euklid.getAlgorithmDescription());
    }

    @Override
    protected void updateUserinputSection() {
        HBox input = new HBox();

        TextField firstValue = new TextField();
        firstValue.setId("firstValue");
        firstValue.promptTextProperty().set("Positive Zahl");
        firstValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,9}")) {
                    firstValue.setText(oldValue);
                }
            }
        });

        TextField secondValue = new TextField();
        secondValue.setId("secondValue");
        secondValue.promptTextProperty().set("Positive Zahl");
        secondValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,9}")) {
                    secondValue.setText(oldValue);
                }
            }
        });

        Button startCalc = new Button("Berechnen");
        startCalc.setId("calcButton");

        input.getChildren().addAll(firstValue,secondValue,startCalc);
        userInputSection.getChildren().add(input);
    }

    /***
     * Zeigt die Ergebnisse des Euklids auf der View an
     * @param _ggt int
     * @param _euklid LinkedList<euklidObj>
     * @param _extend LinkedList<extendEuklidObj>
     */
    public void showEuklidResult(int _ggt, LinkedList<euklidObj> _euklid, LinkedList<extendEuklidObj> _extend){

        solutionSection.getChildren().remove(0,solutionSection.getChildren().size());

        Label headline = new Label("Berechnung des Euklid");
        solutionSection.getChildren().add(headline);

        Label headEuklid = new Label("Der einfache Euklid:");
        solutionSection.getChildren().add(headEuklid);

        VBox euklidCalcSteps = new VBox();
        for (euklidObj elem : _euklid) {
            HBox form = new HBox();
            form.getChildren().addAll(new Label(String.valueOf(elem.getA())),new Label("=") ,new Label(String.valueOf(elem.getPreb())) ,
                    new Label("*"), new Label(String.valueOf(elem.getB())), new Label("+"), new Label(String.valueOf(elem.getRemain())));
            euklidCalcSteps.getChildren().add(form);
        }
        setColorOnEuklid(euklidCalcSteps);
        solutionSection.getChildren().add(euklidCalcSteps);



        Label headExtend = new Label("Der erweiterte Euklid:");
        solutionSection.getChildren().add(headExtend);

        VBox extendCalcSteps = new VBox();
        for(int i = 0; i < _extend.size(); i++){
            if(i != 0 && i != 1){
                HBox form = new HBox();
                form.getChildren().addAll(new Label(String.valueOf(_ggt)), new Label("="), new Label(String.valueOf(_extend.get(i).getKoeS())), new Label("*"),
                    new Label(String.valueOf(_extend.get(i).getA())), new Label("+"), new Label(String.valueOf(_extend.get(i).getKoeT())),
                    new Label("*"), new Label(String.valueOf(_extend.get(i).getB())));
                extendCalcSteps.getChildren().add(form);
            }
        }
        solutionSection.getChildren().add(extendCalcSteps);
    }

    /***
     * Färbt beim einfachen Euklid die Zahlen ein,
     * um das "Vorrücken" zu verdeutlichen
     * @param _euklid
     */
    public void setColorOnEuklid(VBox _euklid){
        /*HBox form = (HBox) _euklid.getChildren().get(0);
        Label colorLess = (Label) form.getChildren().get(0);
        colorLess.setTextFill(Color.RED);*/
        // farbe der variable a
        Color colorA = null;
        // farbe der variable b
        Color colorB = Color.BLUE;
        // farbe des Rests
        Color colorR = Color.RED;

        for(int i = 0; i < _euklid.getChildren().size(); i++){
            if(i == 0){
                ((Label)((HBox) _euklid.getChildren().get(i)).getChildren().get(4)).setTextFill(colorB);
                ((Label)((HBox) _euklid.getChildren().get(i)).getChildren().get(6)).setTextFill(colorR);
            }
            else{
                // Variable A einfärben
                ((Label)((HBox) _euklid.getChildren().get(i)).getChildren().get(0)).setTextFill(colorA);
                // Variable B einfärben
                ((Label)((HBox) _euklid.getChildren().get(i)).getChildren().get(4)).setTextFill(colorB);
                // Reststelle einfärben
                ((Label)((HBox) _euklid.getChildren().get(i)).getChildren().get(6)).setTextFill(colorR);
            }
            // Tauschen der Farben
            Color dummy = colorA;
            if(dummy == null) {
                dummy = Color.GREEN;
            }
            colorA = colorB;
            colorB = colorR;
            colorR = dummy;
        }
    }
}
