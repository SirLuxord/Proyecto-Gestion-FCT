package dad.gestion_fct.controllers.docente;

import dad.gestion_fct.controllers.alumno.AlumnoController;
import dad.gestion_fct.controllers.alumno.ModifiedAlumnoController;
import dad.gestion_fct.models.Alumno;
import dad.gestion_fct.models.Docente;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateDocenteDialog extends Dialog<Docente> implements Initializable {

    ObjectProperty<Docente> docente = new SimpleObjectProperty<>(new Docente());
    DocenteController docenteController = new DocenteController();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // init dialog

        setTitle("Crear");
        setHeaderText("Introduzca los datos del alumno a buscar:");
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().setAll(
                new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE),
                ButtonType.CANCEL
        );
        setResultConverter(this::onResult);

        // bindings

        docente.get().nombreDocenteProperty().bind(nombreTextField.textProperty());
        docente.get().apellidoDocenteProperty().bind(apellidoTextField.textProperty());
        docente.get().emailDocenteProperty().bind(emailTextField.textProperty());
        docente.get().telefonoDocenteProperty().bind(telTextField.textProperty());

    }

    public CreateDocenteDialog() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/docente/createDocenteView.fxml"));
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
    private GridPane root;

    @FXML
    private TextField telTextField;

    public GridPane getRoot() {
        return root;
    }


    private Docente onResult(ButtonType buttonType) {
        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE){
            return docente.get();
        }
        return null;
    }
}
