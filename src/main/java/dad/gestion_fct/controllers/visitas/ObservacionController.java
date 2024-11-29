package dad.gestion_fct.controllers.visitas;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ObservacionController implements Initializable {
    VisitaController visitaController = new VisitaController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public ObservacionController(VisitaController visitaController) {
        try{
            this.visitaController = visitaController;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/visitas/observacionesView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TextArea observacionesTextArea;

    @FXML
    private BorderPane root;
}
