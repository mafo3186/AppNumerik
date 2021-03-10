module de.hsd.medien.numerik {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.antlr.antlr4.runtime;
    requires javafx.graphics;
    opens de.hsd.medien.numerik to javafx.fxml;
    opens de.hsd.medien.numerik.mvc.view to javafx.fxml;
    opens de.hsd.medien.numerik.mvc.controller to javafx.fxml;
    exports de.hsd.medien.numerik;
    exports de.hsd.medien.numerik.mvc.model.gauss;
    exports de.hsd.medien.numerik.mvc.model.knfDnf;
}
