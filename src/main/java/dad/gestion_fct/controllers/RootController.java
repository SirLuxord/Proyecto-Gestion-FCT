package dad.gestion_fct.controllers;

import dad.gestion_fct.controllers.ComentariosEmpresa.ComentariosEmpresaController;
import dad.gestion_fct.controllers.ContactoEmp.ContactoEmpController;
import dad.gestion_fct.controllers.alumno.AlumnoController;
import dad.gestion_fct.controllers.docente.DocenteController;
import dad.gestion_fct.controllers.empresa.EmpresaController;
import dad.gestion_fct.controllers.plazasEmpresa.PlazasEmpresaController;
import dad.gestion_fct.controllers.tutorEmpresa.TutorEmpresaController;
import dad.gestion_fct.controllers.visitas.VisitaController;
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
    ComentariosEmpresaController comentariosEmpresaController = new ComentariosEmpresaController();

    EmpresaController empresaController = new EmpresaController();
    TutorEmpresaController tutorEmpresaController = new TutorEmpresaController();
    PlazasEmpresaController plazasEmpresaController = new PlazasEmpresaController();
    AlumnoController alumnoController = new AlumnoController();
    DocenteController docenteController = new DocenteController();
    VisitaController visitaController = new VisitaController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactoTab.setContent(contactoEmpController.getRoot());
        comentariosTab.setContent(comentariosEmpresaController.getRoot());
        empresasTab.setContent(empresaController.getRoot());
        tutorEmpTab.setContent(tutorEmpresaController.getRoot());
        plazasTab.setContent(plazasEmpresaController.getRoot());
        alumnoTab.setContent(alumnoController.getRoot());
        docenteTab.setContent(docenteController.getRoot());
        visitasTab.setContent(visitaController.getRoot());
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

    @FXML
    private Tab plazasTab;

    public TabPane getRoot() {
        return root;
    }
}
