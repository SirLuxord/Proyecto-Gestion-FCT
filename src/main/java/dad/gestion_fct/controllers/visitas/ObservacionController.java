package dad.gestion_fct.controllers.visitas;

import dad.gestion_fct.models.Alumno;
import dad.gestion_fct.models.Visita;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ObservacionController implements Initializable {
    // Model

    private final ObjectProperty<Visita> visitaObservacion = new SimpleObjectProperty<>(new Visita());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        visitaObservacion.addListener((o, ov, nv) -> {
            if (ov != null) {
                observacionesTextArea.textProperty().unbindBidirectional(ov.observacionProperty());
            }
            if (nv != null) {
                observacionesTextArea.textProperty().bindBidirectional(nv.observacionProperty());
            }
        });
    }

    public ObservacionController() {
        try{
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
    private GridPane root;

    public void setVisitaObservacion(Visita visitaObservacion) {
        this.visitaObservacion.set(visitaObservacion);
    }

    public GridPane getRoot() {
        return root;
    }
}
