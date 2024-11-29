package dad.gestion_fct.controllers;

import dad.gestion_fct.controllers.alumno.AlumnoController;
import dad.gestion_fct.controllers.docente.DocenteController;
import dad.gestion_fct.controllers.empresa.EmpresaController;
import dad.gestion_fct.controllers.tutorEmpresa.TutorEmpresaController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {

    EmpresaController empresaController = new EmpresaController();
    TutorEmpresaController tutorEmpresaController = new TutorEmpresaController();
    AlumnoController alumnoController = new AlumnoController();
    DocenteController docenteController = new DocenteController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        empresasTab.setContent(empresaController.getRoot());
        tutorEmpTab.setContent(tutorEmpresaController.getRoot());
        alumnoTab.setContent(alumnoController.getRoot());
        docenteTab.setContent(docenteController.getRoot());
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
