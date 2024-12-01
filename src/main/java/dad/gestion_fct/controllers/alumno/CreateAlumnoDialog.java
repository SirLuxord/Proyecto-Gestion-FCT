package dad.gestion_fct.controllers.alumno;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.models.Alumno;
import dad.gestion_fct.models.Ciclos;
import dad.gestion_fct.models.Docente;
import dad.gestion_fct.models.TutorEmpresa;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateAlumnoDialog extends Dialog<Alumno>  implements Initializable {


    private ObjectProperty<Alumno> alumno = new SimpleObjectProperty<>(new Alumno());
    private AlumnoController alumnoController = new AlumnoController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Rellenamos las comboBox

        cicloComboBox.getItems().setAll(Ciclos.values());
        addRemoveCombBox();

        // init dialog

        setTitle("Crear");
        setHeaderText("Introduzca los datos del alumno a buscar:");
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().setAll(
                new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE),
                ButtonType.CANCEL
        );
        setResultConverter(this::onResult);

        // bindings

        alumno.get().cialAlumnoProperty().bind(cialTextField.textProperty());
        alumno.get().nombreAlumnoProperty().bind(nombreTextField.textProperty());
        alumno.get().apellidoAlumnoProperty().bind(apellidoTextField.textProperty());
        cicloComboBox.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (nv != null) {
                alumno.get().setCicloAlumno(nv.toString());
            }
        });
        alumno.get().nussAlumnoProperty().bind(nussTextField.textProperty());
        docenteComboBox.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (nv != null) {
                alumno.get().setNombreDocente(nv.getNombreDocente());
                alumno.get().setIdDocente(nv.getIdDocente());
            }
        });
        tutorComboBox.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (nv != null) {
                alumno.get().setTutorEmpresa(nv.getNombre());
                alumno.get().setIdTutor(nv.getId());
            }
        });
    }

    private Alumno onResult(ButtonType buttonType) {
        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE){
            return alumno.get();
        }
        return null;
    }

    public CreateAlumnoDialog() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alumno/createAlumnoView.fxml"));
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
    private GridPane root;

    public GridPane getRoot() {
        return root;
    }

    public void addRemoveCombBox(){

        docenteComboBox.getItems().clear();
        tutorComboBox.getItems().clear();
        TutorEmpresa tutorVacio = new TutorEmpresa();
        tutorVacio.setNombre("");
        tutorVacio.setId(-1);
        tutorComboBox.getItems().add(tutorVacio);
        añadirDocente();
        añadirTutores();
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
}
