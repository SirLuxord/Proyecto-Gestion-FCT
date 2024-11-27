package dad.gestion_fct.controllers.ContactoEmp;

import dad.gestion_fct.models.ContactoEmp;
import dad.gestion_fct.models.Empresa;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ContactoEmpCreateDialog extends Dialog<ContactoEmp> implements Initializable {

    // model

    private ObjectProperty<ContactoEmp> contacto = new SimpleObjectProperty<>(new ContactoEmp());

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
    private TextField surnameField;

    @FXML
    private GridPane root;

    public ContactoEmpCreateDialog(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ContactoEmp/createContactoEmpView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTitle("Crear");
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().setAll(new ButtonType("Crear" , ButtonBar.ButtonData.OK_DONE) , ButtonType.CANCEL);
        setResultConverter(this::onResult);

        // bindings
        empresaCombo.getSelectionModel().selectedItemProperty().addListener((o , ov ,nv) -> {
            if (nv != null){
                contacto.get().setIdEmpresa(nv.getIdEmpresa());
                contacto.get().setNombreEmpresa(nv.getNombre());
            }
        });

        contacto.get().nombreContactoProperty().bind(nameField.textProperty());
        contacto.get().apellidoContactoProperty().bind(surnameField.textProperty());
        contacto.get().correoContactoProperty().bind(mailField.textProperty());
        contacto.get().telefonoProperty().bind(phoneNumberField.textProperty());


    }

    private ContactoEmp onResult(ButtonType buttonType) {
        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE){
            return contacto.get();
        }
        return null;
    }

    public ComboBox<Empresa> getEmpresaCombo() {
        return empresaCombo;
    }

    public void setEmpresaCombo(ComboBox<Empresa> empresaCombo) {
        this.empresaCombo = empresaCombo;
    }
}