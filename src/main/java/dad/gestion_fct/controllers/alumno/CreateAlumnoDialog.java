package dad.gestion_fct.controllers.alumno;

import dad.gestion_fct.models.Alumno;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateAlumnoDialog extends Dialog<Alumno>  implements Initializable {


    ObjectProperty<Alumno> alumno = new SimpleObjectProperty<>(new Alumno());

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

        alumno.get().cialAlumnoProperty().bind(cialTextField.textProperty());
        alumno.get().nombreAlumnoProperty().bind(nombreTextField.textProperty());
        alumno.get().apellidoAlumnoProperty().bind(apellidoTextField.textProperty());
        alumno.get().cicloAlumnoProperty().bind(cicloComboBox.getSelectionModel().selectedItemProperty());
        alumno.get().nussAlumnoProperty().bind(nussTextField.textProperty());
        alumno.get().nombreDocenteProperty().bind(docenteComboBox.getSelectionModel().selectedItemProperty());

    }

    private Alumno onResult(ButtonType buttonType) {
        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE){
            return alumno.get();
        }

        return null;
    }

    public CreateAlumnoDialog() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alumno/createAlumnoView.fxml"));
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
    private ComboBox<String> cicloComboBox; // CAMBIAR EL STRING DESPUÉS!

    @FXML
    private TextField nussTextField;

    @FXML
    private ComboBox<String> docenteComboBox;

    @FXML
    private GridPane root;

    public GridPane getRoot() {
        return root;
    }
}
