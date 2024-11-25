package dad.gestion_fct.controllers.docente;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchDocenteDialog implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public SearchDocenteDialog() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/docente/searchDocenteView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private BorderPane root;

    @FXML
    private TextField telefonoTextField;

    public BorderPane getRoot() {
        return root;
    }
}
