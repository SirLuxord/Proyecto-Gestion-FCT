package dad.gestion_fct.controllers.alumno;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchAlumnoDialog extends Dialog<String> implements Initializable {


    // Model

    StringProperty campo = new SimpleStringProperty();

    // View

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        fieldComboBox.getItems().addAll("Cial", "Nombre", "Apellido", "Ciclo", "Nuss", "Docente", "Tutor Empresa");

        // bindings

        campo.bind(fieldComboBox.getSelectionModel().selectedItemProperty());

        // init dialog

        setTitle("Buscar");
        setHeaderText("Elija el campo del alumno a buscar:");
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().setAll(
                new ButtonType("Buscar", ButtonBar.ButtonData.OK_DONE),
                ButtonType.CANCEL
        );

        setResultConverter(this::onResult);


    }

    public SearchAlumnoDialog() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SearchView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String onResult(ButtonType buttonType) {
        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE){
            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setHeaderText("Introduzca el " + campo);
            nameDialog.setContentText(campo + ": ");
            //return nameDialog.showAndWait();
        }

        return "";
    }

    @FXML
    private ComboBox<String> fieldComboBox;

    @FXML
    private BorderPane root;

    public BorderPane getRoot() {
        return root;
    }
}
