package dad.gestion_fct.controllers.tutorEmpresa;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.models.Empresa;
import dad.gestion_fct.models.TutorEmpresa;
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
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TutorEmpresaModifyController implements Initializable {

    // controller

    private final TutorEmpresaController TutorEmpresaController;

    // model

    private ObjectProperty<TutorEmpresa> tutor = new SimpleObjectProperty<>(new TutorEmpresa());

    // view

    @FXML
    private ComboBox<Empresa> empresaCombo;

    @FXML
    private TextField mailField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private BorderPane root;

    @FXML
    private TextField surnameField;

    public TutorEmpresaModifyController(TutorEmpresaController tutorEmpresaController){
        try{
            this.TutorEmpresaController = tutorEmpresaController;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tutorEmpresa/modifyTutorEmpresaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Bindings
        empresaCombo.getSelectionModel().selectedItemProperty().addListener((o , ov ,nv) -> {
            if (nv != null){
                tutor.get().setIdEmpresa(nv.getIdEmpresa());
                tutor.get().setNombreEmpresa(nv.getNombre());
            }
        });


        nameField.textProperty().bindBidirectional(tutor.get().nombreProperty());
        surnameField.textProperty().bindBidirectional(tutor.get().apellidosProperty());
        mailField.textProperty().bindBidirectional(tutor.get().correoProperty());
        phoneNumberField.textProperty().bindBidirectional(tutor.get().telefonoProperty());

    }


    @FXML
    void onCancelAction(ActionEvent event) {
        TutorEmpresaController.getSplitTutorEmpresa().getItems().remove(getRoot());
    }

    @FXML
    void onModifyAction(ActionEvent event) {

        // Antes de hacer el update se comprueba que ni el nombre y el teléfono estén vacíos.

        if (tutor.get().getNombre().trim().isEmpty()) {
            TutorEmpresaController.mostrarError("El nombre del tutor no puede estar vacío.");
            throw new IllegalArgumentException("El nombre del tutor no puede estar vacío.");
        }
        if (tutor.get().getTelefono().trim().isEmpty()) {
            TutorEmpresaController.mostrarError("El teléfono no puede estar vacío.");
            throw new IllegalArgumentException("El teléfono no puede estar vacío.");
        }

        String query = "Update TutorEmpresa set IdEmpresa = ?, NombreTE = ?, ApellidoTE = ?, CorreoTE = ?, TelefonoTE = ? where IdTE = ?";
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1 , tutor.get().getIdEmpresa());
            statement.setString(2 , tutor.get().getNombre());
            statement.setString(3 , tutor.get().getApellidos());
            statement.setString(4 , tutor.get().getCorreo());
            statement.setString(5 , tutor.get().getTelefono());
            statement.setInt(6 , tutor.get().getId());

            statement.execute();

            // se le pasa al controlador de empresa los datos del registro seleccionado

            TutorEmpresaController.getTutorSeleccionado().setIdEmpresa(tutor.get().getIdEmpresa());
            TutorEmpresaController.getTutorSeleccionado().setNombreEmpresa(tutor.get().getNombreEmpresa());
            TutorEmpresaController.getTutorSeleccionado().setCorreo(tutor.get().getCorreo());
            TutorEmpresaController.getTutorSeleccionado().setTelefono(tutor.get().getTelefono());
            TutorEmpresaController.getTutorSeleccionado().setNombre(tutor.get().getNombre());
            TutorEmpresaController.getTutorSeleccionado().setApellidos(tutor.get().getApellidos());

            TutorEmpresaController.getSplitTutorEmpresa().getItems().remove(getRoot());

        }  catch (SQLException e) {
            TutorEmpresaController.mostrarError(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

    }

    public BorderPane getRoot() {
        return root;
    }

    public TutorEmpresa getTutor() {
        return tutor.get();
    }

    public ObjectProperty<TutorEmpresa> tutorProperty() {
        return tutor;
    }

    public void setTutor(TutorEmpresa tutor) {
        this.tutor.set(tutor);
    }

    public ComboBox<Empresa> getEmpresaCombo() {
        return empresaCombo;
    }

    public void setEmpresaCombo(ComboBox<Empresa> empresaCombo) {
        this.empresaCombo = empresaCombo;
    }
}
