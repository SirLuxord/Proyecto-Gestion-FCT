package dad.gestion_fct.controllers.alumno;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.models.Alumno;
import dad.gestion_fct.models.Ciclos;
import dad.gestion_fct.models.Docente;
import dad.gestion_fct.models.TutorEmpresa;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifiedAlumnoController implements Initializable {

    // Model

    private final AlumnoController alumnoController;
    private final ObjectProperty<Alumno> alumnoModify = new SimpleObjectProperty<>(new Alumno());

    // View

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cicloComboBox.getItems().setAll(Ciclos.values());
        // Bindings

        cialTextField.textProperty().bindBidirectional(alumnoModify.get().cialAlumnoProperty());
        nombreTextField.textProperty().bindBidirectional(alumnoModify.get().nombreAlumnoProperty());
        apellidoTextField.textProperty().bindBidirectional(alumnoModify.get().apellidoAlumnoProperty());
        cicloComboBox.getSelectionModel().selectedItemProperty().addListener( (o, ov , nv) -> {
            if (nv != null){
                alumnoModify.get().setCicloAlumno(nv.toString());
            }
        });
        nussTextField.textProperty().bindBidirectional(alumnoModify.get().nussAlumnoProperty());
        docenteComboBox.getSelectionModel().selectedItemProperty().addListener((o, ov , nv) -> {
            if (nv != null){
                alumnoModify.get().setIdDocente(nv.getIdDocente());
                alumnoModify.get().setNombreDocente(nv.getNombreDocente());
            }
        });
        tutorComboBox.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (nv != null){
                alumnoModify.get().setIdTutor(nv.getId());
                alumnoModify.get().setTutorEmpresa(nv.getNombre());
            }
        });
    }

    public ModifiedAlumnoController(AlumnoController alumnoController) {
        try{
            this.alumnoController = alumnoController;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alumno/modifiedAlumnoView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TextField cialTextField;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField apellidoTextField;

    @FXML
    private ComboBox<Ciclos> cicloComboBox;

    @FXML
    private TextField nussTextField;

    @FXML
    private ComboBox<Docente> docenteComboBox;

    @FXML
    private ComboBox<TutorEmpresa> tutorComboBox;

    @FXML
    private BorderPane root;

    @FXML
    void onCancelAction(ActionEvent event) {
        alumnoController.getSplitAlumno().getItems().remove(this.getRoot());
    }

    @FXML
    void onConfirmAction(ActionEvent event) {

        // UPDATE alumno SET CIALAlumno, NombreAlumno, ApellidoAlumno, CicloAlumno, NussAlumno, IdDocente, IDTutorE WHERE IdAlumno = ?

        alumnoController.getSelectedAlumno().setCialAlumno(alumnoModify.get().getCialAlumno());
        alumnoController.getSelectedAlumno().setNombreAlumno(alumnoModify.get().getNombreAlumno());
        alumnoController.getSelectedAlumno().setApellidoAlumno(alumnoModify.get().getApellidoAlumno());
        alumnoController.getSelectedAlumno().setCicloAlumno(alumnoModify.get().getCicloAlumno());
        alumnoController.getSelectedAlumno().setNussAlumno(alumnoModify.get().getNussAlumno());
        alumnoController.getSelectedAlumno().setNombreDocente(alumnoModify.get().getNombreDocente());
        alumnoController.getSelectedAlumno().setIdDocente(alumnoModify.get().getIdDocente());
        alumnoController.getSelectedAlumno().setTutorEmpresa(alumnoModify.get().getTutorEmpresa());
        alumnoController.getSelectedAlumno().setIdTutor(alumnoModify.get().getIdTutor());
    }

    public void setAlumnoModify(Alumno alumnoModify) {
        this.alumnoModify.set(alumnoModify);
    }

    public Alumno getAlumnoModify() {
        return alumnoModify.get();
    }

    public ObjectProperty<Alumno> alumnoModifyProperty() {
        return alumnoModify;
    }

    public BorderPane getRoot() {
        return root;
    }

    public void añadirDocente(){
        try (Connection connection = HikariConnection.getConnection()) {

            String query = "SELECT IdDocente, NombreDocente FROM `tutordocente`";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Docente docente = new Docente();
                    docente.setIdDocente(resultSet.getInt("IdDocente"));
                    docente.setNombreDocente(resultSet.getString("NombreDocente"));
                    docenteComboBox.getItems().add(docente);
                }
            }
        } catch (SQLException e) {
            alumnoController.mostrarError(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public void añadirTutores(){
        try (Connection connection = HikariConnection.getConnection()) {

            String query = "SELECT IdTE, NombreTE FROM `tutorempresa`";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    TutorEmpresa tutor = new TutorEmpresa();
                    tutor.setId(resultSet.getInt("IdTE"));
                    tutor.setNombre(resultSet.getString("NombreTE"));
                    tutorComboBox.getItems().add(tutor);
                }
            }
        } catch (SQLException e) {
            alumnoController.mostrarError(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public void addRemoveCombBox(){
        docenteComboBox.getItems().clear();
        tutorComboBox.getItems().clear();
        añadirDocente();
        añadirTutores();
    }

    public ComboBox<Ciclos> getCicloComboBox() {
        return cicloComboBox;
    }

    public ComboBox<Docente> getDocenteComboBox() {
        return docenteComboBox;
    }

    public ComboBox<TutorEmpresa> getTutorComboBox() {
        return tutorComboBox;
    }
}
