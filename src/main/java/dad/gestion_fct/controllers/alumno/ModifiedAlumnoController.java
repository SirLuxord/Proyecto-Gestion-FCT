package dad.gestion_fct.controllers.alumno;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.models.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
        empresaComboBox.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (nv != null){
                alumnoModify.get().setIdempresa(nv.getIdEmpresa());
                alumnoModify.get().setEmpresa(nv.getNombre());
                añadirTutores();
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
    private ComboBox<Empresa> empresaComboBox;

    @FXML
    private ComboBox<TutorEmpresa> tutorComboBox;

    @FXML
    private BorderPane root;

    @FXML
    void onCancelAction(ActionEvent event) {
        alumnoController.getSplitAlumno().getItems().remove(this.getRoot());
        alumnoController.getCreateButton().setDisable(false);
        alumnoController.getModifyButton().setDisable(false);
        alumnoController.getDeleteButton().setDisable(false);
        alumnoController.setModificar(false);
    }

    @FXML
    void onConfirmAction(ActionEvent event) {
        String query;
        int idAlumno = alumnoModify.get().getIdAlumno();
        String newCial = alumnoModify.get().getCialAlumno();
        String newNombre = alumnoModify.get().getNombreAlumno();
        String newApellido = alumnoModify.get().getApellidoAlumno();
        String newCiclo = alumnoModify.get().getCicloAlumno();
        String newNuss = alumnoModify.get().getNussAlumno();
        String newNombreDocente = alumnoModify.get().getNombreDocente();
        int newIdDocente = alumnoModify.get().getIdDocente();
        String newNombreTutor = alumnoModify.get().getTutorEmpresa();
        int newIdTutor = alumnoModify.get().getIdTutor();

        if (newIdTutor != -1){
            query = "UPDATE alumno SET CIALAlumno = ?, NombreAlumno = ?, ApellidoAlumno = ?, CicloAlumno = ?, NussAlumno = ?, IdDocente = ?, IDTutorE = ? WHERE IdAlumno = ?";
        } else {
            query = "UPDATE alumno SET CIALAlumno = ?, NombreAlumno = ?, ApellidoAlumno = ?, CicloAlumno = ?, NussAlumno = ?, IdDocente = ?, IDTutorE = NULL WHERE IdAlumno = ?";
        }

        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, newCial);
            preparedStatement.setString(2, newNombre);
            preparedStatement.setString(3, newApellido);
            preparedStatement.setString(4, newCiclo);
            preparedStatement.setString(5, newNuss);
            preparedStatement.setInt(6, newIdDocente);
            if (newIdTutor != -1){
                preparedStatement.setInt(7, newIdTutor);
                preparedStatement.setInt(8, idAlumno);
            } else {
                preparedStatement.setInt(7, idAlumno);
            }
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (alumnoController.getSelectedAlumno() != null){
            actualizarRegistroCompleto(newCial, newNombre, newApellido, newCiclo, newNuss, newNombreDocente, newIdDocente, newNombreTutor, newIdTutor);
        }
    }

    public void setAlumnoModify(Alumno alumnoModify) {
        this.alumnoModify.set(alumnoModify);
    }

    public Alumno getAlumnoModify() {
        return alumnoModify.get();
    }

    public void setEmpresaComboBox(ComboBox<Empresa> empresaComboBox) {
        this.empresaComboBox = empresaComboBox;
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

    public void añadirEmpresa(){
        try (Connection connection = HikariConnection.getConnection()) {

            String query = "SELECT IdEmpresa, NombreEmpresa FROM `empresa`";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Empresa empresa = new Empresa();
                    empresa.setIdEmpresa(resultSet.getInt("IdEmpresa"));
                    empresa.setNombre(resultSet.getString("NombreEmpresa"));
                    empresaComboBox.getItems().add(empresa);
                }
            }
        } catch (SQLException e) {
            alumnoController.mostrarError(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public void añadirTutores(){
        try (Connection connection = HikariConnection.getConnection()) {

            String query = "SELECT IdTE, NombreTE FROM `tutorempresa`  where IdEmpresa = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, alumnoModify.get().getIdempresa());
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
        empresaComboBox.getItems().clear();
        tutorComboBox.getItems().clear();
        Empresa empresa = new Empresa();
        empresa.setNombre("");
        empresa.setIdEmpresa(-1);
        empresaComboBox.getItems().add(empresa);
        añadirDocente();
        añadirEmpresa();
    }

    private void actualizarRegistroCompleto(String newCial, String newNombre, String newApellido, String newCiclo, String newNuss, String newNombreDocente, int newIdDocente, String newNombreTutor, int newIdTutor) {
        alumnoController.getSelectedAlumno().setCialAlumno(newCial);
        alumnoController.getSelectedAlumno().setNombreAlumno(newNombre);
        alumnoController.getSelectedAlumno().setApellidoAlumno(newApellido);
        alumnoController.getSelectedAlumno().setCicloAlumno(newCiclo);
        alumnoController.getSelectedAlumno().setNussAlumno(newNuss);
        alumnoController.getSelectedAlumno().setNombreDocente(newNombreDocente);
        alumnoController.getSelectedAlumno().setIdDocente(newIdDocente);
        alumnoController.getSelectedAlumno().setTutorEmpresa(newNombreTutor);
        alumnoController.getSelectedAlumno().setIdTutor(newIdTutor);
    }

    public void actualizarRegistro(){

    }

    public ComboBox<Ciclos> getCicloComboBox() {
        return cicloComboBox;
    }

    public ComboBox<Docente> getDocenteComboBox() {
        return docenteComboBox;
    }

    public ComboBox<Empresa> getEmpresaComboBox() {
        return empresaComboBox;
    }

    public ComboBox<TutorEmpresa> getTutorComboBox() {
        return tutorComboBox;
    }

    public ObjectProperty<Alumno> alumnoModifyProperty() {
        return alumnoModify;
    }


}
