package dad.gestion_fct.controllers.docente;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public ModifiedDocenteController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/docente/modifiedDocenteView.fxml"));
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
    private BorderPane root;

    @FXML
    private TextField telefonoTextField;

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
