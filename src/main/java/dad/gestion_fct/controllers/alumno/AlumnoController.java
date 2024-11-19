package dad.gestion_fct.controllers.alumno;

import dad.gestion_fct.models.Alumno;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AlumnoController implements Initializable {

    // Model

    private ObjectProperty<Alumno> selectedAlumno = new SimpleObjectProperty<>();

    // View

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Bindings
        selectedAlumno.bind(alumnoTable.getSelectionModel().selectedItemProperty());
    }

    public AlumnoController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alumno/alumnoView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private BorderPane root;

    @FXML
    private TableView<Alumno> alumnoTable;

    @FXML
    private TableColumn<Alumno, String> apellidoAlumColumn;

    @FXML
    private TableColumn<Alumno, String> cialAlumnoColumn;

    @FXML
    private TableColumn<Alumno, String> cicloAlumColumn;

    @FXML
    private TableColumn<Alumno, String> docenteAlumColumn;

    @FXML
    private TableColumn<Alumno, String> nombreAlumColumn;

    @FXML
    private TableColumn<Alumno, String> nussAlumColumn;

    @FXML
    private SplitPane splitAlumno;

    @FXML
    void onCreateStudentAction(ActionEvent event) {

    }

    @FXML
    void onSearchStudentAction(ActionEvent event) {
        SearchAlumnoDialog searchDialog = new SearchAlumnoDialog();
        if (searchDialog.showAndWait().isPresent()) {
            // Consulta
        }
    }

    @FXML
    void onSearchAllStudentAction(ActionEvent event) {

    }

    @FXML
    void onModifiedStudentAction(ActionEvent event) {

    }

    @FXML
    void onDeleteStudentAction(ActionEvent event) {

    }

    public BorderPane getRoot() {
        return root;
    }



}
