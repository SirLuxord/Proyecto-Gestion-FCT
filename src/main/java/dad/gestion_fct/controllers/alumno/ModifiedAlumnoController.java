package dad.gestion_fct.controllers.alumno;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifiedAlumnoController implements Initializable {

    // View

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public ModifiedAlumnoController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alumno/modifiedAlumnoView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TextField apellidoTextField;

    @FXML
    private TextField ciallTextField;

    @FXML
    private TextField cicloTextField;

    @FXML
    private TextField docenteTextField;

    @FXML
    private TextField nombreTextField;

    @FXML
    void onCancelAction(ActionEvent event) {

    }

    @FXML
    void onConfirmAction(ActionEvent event) {

    }

}
