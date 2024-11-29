package dad.gestion_fct.controllers.ContactoEmp;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.models.ContactoEmp;
import dad.gestion_fct.models.Empresa;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static dad.gestion_fct.controllers.ContactoEmp.ContactoEmpController.*;

public class ContactoEmpModifyController implements Initializable {
    private ContactoEmpController contactoEmpController;

    private ObjectProperty<ContactoEmp> contacto = new SimpleObjectProperty<>();


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

    @FXML
    void onCancelAction(ActionEvent event) {
        contactoEmpController.getSplitContactoEmp().getItems().remove(getRoot());

    }

    public ComboBox<Empresa> getEmpresaCombo() {
        return empresaCombo;
    }

    public void setEmpresaCombo(ComboBox<Empresa> empresaCombo) {
        this.empresaCombo = empresaCombo;
    }

    public BorderPane getRoot() {
        return root;
    }


    public void setContacto(ContactoEmp contacto) {
        this.contacto.set(contacto); // Actualiza el contactoModify con el contacto seleccionado
    }

    public ContactoEmp getContacto() {
        return contacto.get();
    }

    @FXML  //METODO MODIFICAR
    void onModifyAction(ActionEvent event) {


        if (empresaCombo.getSelectionModel().getSelectedItem() == null ) {
            mostrarAlertaError("Selección incompleta", "Debe seleccionar una empresa de la lista.");
            return;
        } else if (nameField.getText().trim().isEmpty()) {
            mostrarAlertaError("Selección incompleta", "Debe ingresar un nombre de contacto.");
            return;
        }

        if (contacto.get().getNombreContacto().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre no puede estar vacío");
        }
        if (!ContactoEmpController.esTelefonoValido(phoneNumberField.getText())) {
            mostrarAlertaError("Número de teléfono inválido", "Por favor ingrese un número de teléfono válido.");
            return;
        }

        String query = "Update contactoEmpresa set IdEmpresa = ?, NombreContacto = ? , ApellidoContacto = ? , Telefono = ? , CorreoContacto = ? where idContacto = ?";
        try (Connection connection = HikariConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, contacto.get().getIdEmpresa());
            statement.setString(2, contacto.get().getNombreContacto());
            statement.setString(3, contacto.get().getApellidoContacto());
            statement.setString(4, contacto.get().getTelefono());
            statement.setString(5, contacto.get().getCorreoContacto());
            statement.setInt(6, contacto.get().getIdContacto());

            statement.execute();
          //  System.out.println(contacto.get().getIdEmpresa());
             contactoEmpController.getSelectedContactoEmp().setIdContacto(contacto.get().getIdContacto());
            contactoEmpController.getSelectedContactoEmp().setIdEmpresa(contacto.get().getIdEmpresa());
            contactoEmpController.getSelectedContactoEmp().setNombreEmpresa(contacto.get().getNombreEmpresa());
            //contactoEmpController.getSelectedContactoEmp().setNombreEmpresa(empresaCombo.getSelectionModel().getSelectedItem().getNombre());
            contactoEmpController.getSelectedContactoEmp().setNombreContacto(contacto.get().getNombreContacto());
            contactoEmpController.getSelectedContactoEmp().setApellidoContacto(contacto.get().getApellidoContacto());
            contactoEmpController.getSelectedContactoEmp().setTelefono(contacto.get().getTelefono());
            contactoEmpController.getSelectedContactoEmp().setCorreoContacto(contacto.get().getCorreoContacto());

            contactoEmpController.getSplitContactoEmp().getItems().remove(getRoot());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }





    //recibe instancia
    public ContactoEmpModifyController(ContactoEmpController contactoEmpController) {
        try {
            this.contactoEmpController = contactoEmpController;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/contactoEmp/modifyContactoEmpView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        empresaCombo.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) ->{
            if (nv != null) {
                contacto.get().setIdEmpresa(nv.getIdEmpresa());
                contacto.get().setNombreEmpresa(nv.getNombre());
            }
        });

        contacto.addListener((o, ov, nv) -> {
            if (ov != null) {
                nameField.textProperty().unbindBidirectional(ov.nombreContactoProperty());
                surnameField.textProperty().unbindBidirectional(ov.apellidoContactoProperty());
                phoneNumberField.textProperty().unbindBidirectional(ov.telefonoProperty());
                mailField.textProperty().unbindBidirectional(ov.correoContactoProperty());
            }
            if (nv != null) {
                nameField.textProperty().bindBidirectional(nv.nombreContactoProperty());
                surnameField.textProperty().bindBidirectional(nv.apellidoContactoProperty());
                phoneNumberField.textProperty().bindBidirectional(nv.telefonoProperty());
                mailField.textProperty().bindBidirectional(nv.correoContactoProperty());
            }




        });

    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de validación");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


}
