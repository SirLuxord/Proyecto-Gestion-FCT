package dad.gestion_fct.controllers.alumno;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AlumnoController extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

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
    private TableView<?> alumnoTable;

    @FXML
    private TableColumn<?, ?> apellidoAlumColumn;

    @FXML
    private TableColumn<?, ?> cialAlumnoColumn;

    @FXML
    private TableColumn<?, ?> cicloAlumColumn;

    @FXML
    private TableColumn<?, ?> docenteAlumColumn;

    @FXML
    private TableColumn<?, ?> nombreAlumColumn;

    @FXML
    private TableColumn<?, ?> nussAlumColumn;

    @FXML
    void onCreateStudentAction(ActionEvent event) {

    }

    @FXML
    void onDeleteStudentAction(ActionEvent event) {

    }

    @FXML
    void onModifiedStudentAction(ActionEvent event) {

    }

    public BorderPane getRoot() {
        return root;
    }
}
