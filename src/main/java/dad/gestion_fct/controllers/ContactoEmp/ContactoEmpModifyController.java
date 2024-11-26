package dad.gestion_fct.controllers.ContactoEmp;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.models.ContactoEmp;
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

public class ContactoEmpModifyController implements Initializable {
    private ContactoEmpController contactoEmpController;

    private ObjectProperty<ContactoEmp> contactoModify = new SimpleObjectProperty<>();


    @FXML
    private ComboBox<?> empresaCombo;

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
        // Limpia la vista actual
        contactoEmpController.getSplitContactoEmp().getItems().remove(getRoot());

    }

    @FXML
    void onModifyAction(ActionEvent event) {

        String query = "Update contactoEmpresa set NombreContacto = ? , ApellidoContacto = ? , Telefono = ? , CorreoContacto = ? where idContacto = ?";
        try (Connection connection = HikariConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, nameField.getText());
            statement.setString(2, surnameField.getText());
            statement.setString(3, phoneNumberField.getText());
            statement.setString(4, mailField.getText());
            statement.setInt(5, contactoModify.get().getIdContacto()); // ID de la empresa del contacto a modificar

            statement.execute();
            System.out.println("ID contancto: " + contactoModify.get().idContactoProperty());



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        /*
        contactoEmpController.getSelectedContactoEmp().setNombreContacto(contactoModify.get().getNombreContacto());
        contactoEmpController.getSelectedContactoEmp().setApellidoContacto(contactoModify.get().getApellidoContacto());
        contactoEmpController.getSelectedContactoEmp().setTelefonoContacto(contactoModify.get().getTelefonoContacto());
        contactoEmpController.getSelectedContactoEmp().setCorreoContacto(contactoModify.get().getCorreoContacto());
        */

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
        contactoModify.addListener((o, ov, nv) -> {
            if (ov != null) {
                nameField.textProperty().unbindBidirectional(ov.nombreContactoProperty());
                surnameField.textProperty().unbindBidirectional(ov.apellidoContactoProperty());
                phoneNumberField.textProperty().unbindBidirectional(ov.telefonoContactoProperty());
                mailField.textProperty().unbindBidirectional(ov.correoContactoProperty());
            }
            if (nv != null) {
                nameField.textProperty().bindBidirectional(nv.nombreContactoProperty());
                surnameField.textProperty().bindBidirectional(nv.apellidoContactoProperty());
                phoneNumberField.textProperty().bindBidirectional(nv.telefonoContactoProperty());
                mailField.textProperty().bindBidirectional(nv.correoContactoProperty());
            }
        });

    }


    public BorderPane getRoot() {
        return root;
    }



    public void setContactoModify(ContactoEmp contacto) {
        this.contactoModify.set(contacto); // Actualiza el contactoModify con el contacto seleccionado

        // Enlazar los campos con los datos del contacto
        nameField.textProperty().set(contacto.getNombreContacto());
        surnameField.textProperty().set(contacto.getApellidoContacto());
        phoneNumberField.textProperty().set(contacto.getTelefonoContacto());
        mailField.textProperty().set(contacto.getCorreoContacto());
    }

}
