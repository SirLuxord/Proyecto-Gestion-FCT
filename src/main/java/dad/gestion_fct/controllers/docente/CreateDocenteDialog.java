package dad.gestion_fct.controllers.docente;

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

public class CreateDocenteDialog implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
    private TextField apellidoTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField nombreTextField;

    @FXML
    private GridPane root;

    @FXML
    private TextField telTextField;

    public GridPane getRoot() {
        return root;
    }
}
