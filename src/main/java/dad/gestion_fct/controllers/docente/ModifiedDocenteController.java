package dad.gestion_fct.controllers.docente;

import dad.gestion_fct.models.Alumno;
import dad.gestion_fct.models.Docente;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;



import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifiedDocenteController implements Initializable {

    // Model

    private final DocenteController docenteController;
    private final ObjectProperty<Docente> docenteModify = new SimpleObjectProperty<>();

    // View

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bindings

        docenteModify.addListener((ObservableValue<? extends Docente> o, Docente ov, Docente nv) -> {
            if (ov != null){

                nombreTextField.textProperty().unbindBidirectional(ov.nombreDocenteProperty());
                apellidoTextField.textProperty().unbindBidirectional(ov.apellidoDocenteProperty());
                emailTextField.textProperty().unbindBidirectional(ov.emailDocenteProperty());
                telefonoTextField.textProperty().unbindBidirectional(ov.telefonoDocenteProperty());


            }

            if (nv != null){

                nombreTextField.textProperty().bindBidirectional(nv.nombreDocenteProperty());
                apellidoTextField.textProperty().bindBidirectional(nv.apellidoDocenteProperty());
                emailTextField.textProperty().bindBidirectional(nv.emailDocenteProperty());
                telefonoTextField.textProperty().bindBidirectional(nv.telefonoDocenteProperty());

            }
        });
    }

    public ModifiedDocenteController(DocenteController docenteController) {
        try{
            this.docenteController = docenteController;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/docente/modifiedDocenteView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField apellidoTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField telefonoTextField;

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
