package dad.gestion_fct.controllers.alumno;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchAlumnoController extends Dialog<String> implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public SearchAlumnoController() {
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
    private GridPane root;

    public GridPane getRoot() {
        return root;
    }
}
