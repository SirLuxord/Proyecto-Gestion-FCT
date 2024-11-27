package dad.gestion_fct.controllers.docente;

import dad.gestion_fct.models.Alumno;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
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

public class DocenteController implements Initializable {


    // Model
    private ListProperty<Alumno> listaAlumno = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<Alumno> selectedAlumno = new SimpleObjectProperty<>();
    private ModifiedDocenteController modifiedDocenteController = new ModifiedDocenteController(this);

    // View

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Bindings
        //selectedAlumno.bind(alumnoTable.getSelectionModel().selectedItemProperty());
    }

    public DocenteController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/docente/docenteView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TableColumn<?, ?> TelDocColumn;

    @FXML
    private TableColumn<?, ?> apellidoDocColumn;

    @FXML
    private TableView<?> docenteTable;

    @FXML
    private TableColumn<?, ?> emailDocColumn;

    @FXML
    private TableColumn<?, ?> nombreDocColumn;

    @FXML
    private BorderPane root;

    @FXML
    private SplitPane splitDocente;

    @FXML
    void onCreateDocAction(ActionEvent event) {

    }

    @FXML
    void onDeleteDocAction(ActionEvent event) {

    }

    @FXML
    void onModifiedDocAction(ActionEvent event) {

    }

    @FXML
    void onSearchDocAction(ActionEvent event) {

    }

    public BorderPane getRoot() {
        return root;
    }
}
