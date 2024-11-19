package dad.gestion_fct.controllers.alumno;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchAlumnoDialog extends Dialog<String> implements Initializable {



    // Model

    StringProperty cialProperty = new SimpleStringProperty();

    // View

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // init dialog

        setTitle("Buscar");
        setHeaderText("Introduzca el cial del alumno a buscar:");
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().setAll(
                new ButtonType("Buscar", ButtonBar.ButtonData.OK_DONE),
                ButtonType.CANCEL
        );
        setResultConverter(this::onResult);

        // bindings

        cialProperty.bind(cialTextField.textProperty());

    }

    public SearchAlumnoDialog() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alumno/searchAlumnoView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TextField cialTextField;

    @FXML
    private BorderPane root;

    private String onResult(ButtonType buttonType) {
        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE){
            String cial = cialProperty.get();
            return cial;
        }
        return  null;
    }
}
