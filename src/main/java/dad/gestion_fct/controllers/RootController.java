package dad.gestion_fct.controllers;

import dad.gestion_fct.controllers.ContactoEmp.ContactoEmpController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {

    ContactoEmpController contactoEmpController = new ContactoEmpController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactoTab.setContent(contactoEmpController.getRoot());
    }

    public RootController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/rootView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private Tab alumnoTab;

    @FXML
    private Tab asignacionesTab;

    @FXML
    private Tab comentariosTab;

    @FXML
    private Tab contactoTab;

    @FXML
    private Tab docenteTab;

    @FXML
    private Tab empresasTab;

    @FXML
    private TabPane root;

    @FXML
    private Tab tutorEmpTab;

    @FXML
    private Tab visitasTab;

    public TabPane getRoot() {
        return root;
    }
}
