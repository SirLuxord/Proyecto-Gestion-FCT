package dad.gestion_fct.controllers.alumno;

import dad.gestion_fct.models.Alumno;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifiedAlumnoController implements Initializable {

    // Model

    AlumnoController alumnoController;
    ObjectProperty<Alumno> alumnoModify = new SimpleObjectProperty<>();

    // View

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Bindings

        alumnoModify.addListener((ObservableValue<? extends Alumno> o, Alumno ov, Alumno nv) -> {
            if (ov != null){
                cialTextField.textProperty().unbindBidirectional(ov.cialAlumnoProperty());
                nombreTextField.textProperty().unbindBidirectional(ov.nombreAlumnoProperty());
                apellidoTextField.textProperty().unbindBidirectional(ov.apellidoAlumnoProperty());
                cicloComboBox.valueProperty().unbindBidirectional(ov.cialAlumnoProperty());
                nussTextField.textProperty().unbindBidirectional(ov.nussAlumnoProperty());
                docenteComboBox.valueProperty().unbindBidirectional(ov.nombreDocenteProperty());
                tutorComboBox.valueProperty().unbindBidirectional(ov.tutorDocenteProperty());

            }

            if (nv != null){
                cialTextField.textProperty().bindBidirectional(nv.cialAlumnoProperty());
                nombreTextField.textProperty().bindBidirectional(nv.nombreAlumnoProperty());
                apellidoTextField.textProperty().bindBidirectional(nv.apellidoAlumnoProperty());
                cicloComboBox.valueProperty().bindBidirectional(nv.cialAlumnoProperty());
                nussTextField.textProperty().bindBidirectional(nv.nussAlumnoProperty());
                docenteComboBox.valueProperty().bindBidirectional(nv.nombreDocenteProperty());
                tutorComboBox.valueProperty().bindBidirectional(nv.tutorDocenteProperty());
            }
        });
    }

    public ModifiedAlumnoController(AlumnoController alumnoController) {
        try{
            this.alumnoController = alumnoController;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alumno/modifiedAlumnoView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TextField cialTextField;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField apellidoTextField;

    @FXML
    private ComboBox<String> cicloComboBox; // CAMBIAR EN UN FUTURO EL STRING

    @FXML
    private TextField nussTextField;

    @FXML
    private ComboBox<String> docenteComboBox;

    @FXML
    private ComboBox<String> tutorComboBox;

    @FXML
    private BorderPane root;

    @FXML
    void onCancelAction(ActionEvent event) {

    }

    @FXML
    void onConfirmAction(ActionEvent event) {

    }

    public BorderPane getRoot() {
        return root;
    }
}
