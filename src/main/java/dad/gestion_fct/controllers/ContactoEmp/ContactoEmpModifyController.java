package dad.gestion_fct.controllers.ContactoEmp;
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
import java.util.ResourceBundle;

public class ContactoEmpModifyController  implements Initializable {
    private ContactoEmpController ContactoEmpController;

    private ObjectProperty<ContactoEmp> contactoEmp = new SimpleObjectProperty<>();


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

    }

    @FXML
    void onModifyAction(ActionEvent event) {

    }
    //recibe instancia
    public ContactoEmpModifyController(ContactoEmpController contactoEmpController) {
        try{
            this.ContactoEmpController = contactoEmpController;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/contactoEmp/modifyContactoEmpView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public BorderPane getRoot() {
        return root;
    }

}
