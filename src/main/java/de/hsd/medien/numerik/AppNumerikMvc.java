package de.hsd.medien.numerik;

import de.hsd.medien.numerik.mvc.controller.StartController;
import de.hsd.medien.numerik.mvc.model.manager.AlgorithmManagerModel;
import de.hsd.medien.numerik.mvc.view.StartView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * AppNumerikMvc startet die App
 * MVC: erstellt Model, View und Controller
 * - übergibt Model und stage an View
 * - übergibt Model und view an Controller
 * @author Focken, Mareike
 * @version 1.0
 */
public class AppNumerikMvc extends Application {
    @Override
    public void start(Stage stage){
        AlgorithmManagerModel algorithmManagerModel = new AlgorithmManagerModel();
        StartView startView = new StartView(algorithmManagerModel, stage);
        StartController startController = new StartController(algorithmManagerModel, startView);
    }

    public static void main(String[] args) {
        launch(args);
    }
}