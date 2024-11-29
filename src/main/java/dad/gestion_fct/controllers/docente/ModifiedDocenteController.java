package dad.gestion_fct.controllers.docente;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.models.Alumno;
import dad.gestion_fct.models.Docente;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;



import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifiedDocenteController implements Initializable {

    // Model

    private final DocenteController docenteController;
    private final ObjectProperty<Docente> docenteModify = new SimpleObjectProperty<>(new Docente());

    // View

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Bindings

        nombreTextField.textProperty().bindBidirectional(docenteModify.get().nombreDocenteProperty());
        apellidoTextField.textProperty().bindBidirectional(docenteModify.get().apellidoDocenteProperty());
        emailTextField.textProperty().bindBidirectional(docenteModify.get().emailDocenteProperty());
        telefonoTextField.textProperty().bindBidirectional(docenteModify.get().telefonoDocenteProperty());

    }

    public ModifiedDocenteController(DocenteController docenteController) {
        try{
            this.docenteController = docenteController;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/docente/modifiedDocenteView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField apellidoTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField telefonoTextField;

    @FXML
    private BorderPane root;

    @FXML
    void onCancelAction(ActionEvent event) {
        docenteController.getSplitDocente().getItems().remove(this.getRoot());
        docenteController.getCreateButton().setDisable(false);
        docenteController.getModifyButton().setDisable(false);
        docenteController.getDeleteButton().setDisable(false);
        docenteController.setModificar(false);
    }

    @FXML
    void onConfirmAction(ActionEvent event) {
        String query;
        int idDocente = docenteModify.get().getIdDocente();
        String newNombre = docenteModify.get().getNombreDocente();
        String newApellido = docenteModify.get().getApellidoDocente();
        String newEmail = docenteModify.get().getEmailDocente();
        String newTelefono = docenteModify.get().getTelefonoDocente();

        query = "UPDATE tutordocente SET NombreDocente = ?, ApellidoDocente = ?, EmailDocente = ?, TelefonoDocente = ? WHERE IdDocente = ?";

        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, newNombre);
            preparedStatement.setString(2, newApellido);
            preparedStatement.setString(3, newEmail);
            preparedStatement.setString(4, newTelefono);
            preparedStatement.setInt(5, idDocente);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (docenteController.getSelectedDocente() != null){
            actualizarRegistroCompleto(newNombre, newApellido, newEmail, newTelefono);
        }
    }

    private void actualizarRegistroCompleto(String newNombre, String newApellido, String newEmail, String newTelefono) {
        docenteController.getSelectedDocente().setNombreDocente(newNombre);
        docenteController.getSelectedDocente().setApellidoDocente(newApellido);
        docenteController.getSelectedDocente().setEmailDocente(newEmail);
        docenteController.getSelectedDocente().setTelefonoDocente(newTelefono);
    }

    public Docente getDocenteModify() {
        return docenteModify.get();
    }

    public ObjectProperty<Docente> docenteModifyProperty() {
        return docenteModify;
    }

    public BorderPane getRoot() {
        return root;
    }
}
