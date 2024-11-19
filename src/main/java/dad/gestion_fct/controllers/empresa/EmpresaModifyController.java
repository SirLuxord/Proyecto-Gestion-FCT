package dad.gestion_fct.controllers.empresa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmpresaModifyController implements Initializable {

    // view

    @FXML
    private TextField adressField;

    @FXML
    private TextField cpField;

    @FXML
    private TextField localityField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField nifField;

    @FXML
    private CheckBox publicCheckBox;

    @FXML
    private BorderPane root;

    public EmpresaModifyController(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/empresa/modifyEmpresaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void onCancelAction(ActionEvent event) {

    }

    @FXML
    void onModifyAction(ActionEvent event) {

    }

    public BorderPane getRoot() {
        return root;
    }
}
