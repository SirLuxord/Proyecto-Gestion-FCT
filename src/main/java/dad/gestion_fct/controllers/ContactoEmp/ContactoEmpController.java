package dad.gestion_fct.controllers.ContactoEmp;

import dad.gestion_fct.models.ContactoEmp;
import javafx.application.Application;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ContactoEmpController implements Initializable {

    private ListProperty<ContactoEmp> listaContactoEmp = new SimpleListProperty<>();
    private ObjectProperty<ContactoEmp> selectedContactoEmp = new SimpleObjectProperty<>();
    ContactoEmpModifyController contactoEmpModifyController = new ContactoEmpModifyController(this);


    @FXML
    private SplitPane splitTutorEmpresa;

    @FXML
    private BorderPane root;

    @FXML
    private TableColumn<ContactoEmp, String> empresaColumn;

    @FXML
    private TableColumn<ContactoEmp, String> nombreColumn;

    @FXML
    private TableView<ContactoEmp> tutorEmpresaTable;

    @FXML
    private TableColumn<ContactoEmp, String> correoColumn;

    @FXML
    private TableColumn<ContactoEmp, String> telefonoColumn;

    @FXML
    private TableColumn<ContactoEmp, String> apellidoColumn;

    @FXML
    private Button modifyButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button createButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button searchAllButton;




    public ContactoEmpController(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/contactoEmp/ContactoEmpView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SplitPane.setResizableWithParent(contactoEmpModifyController.getRoot() , false);

       // modifyButton.setDisable(true);
       // removeButton.setDisable(true);

        //empresaColumn.setCellValueFactory(v -> v.getValue().();
        nombreColumn.setCellValueFactory(v -> v.getValue().nombreContactoProperty());
        apellidoColumn.setCellValueFactory(v -> v.getValue().apellidoContactoProperty());
        correoColumn.setCellValueFactory(v -> v.getValue().correoContactoProperty());
        telefonoColumn.setCellValueFactory(v -> v.getValue().telefonoContactoProperty());
    }

    public BorderPane getRoot() {
        return root;
    }
    public SplitPane getSplitTutorEmpresa() {
        return splitTutorEmpresa;
    }

    //metodos acciones iinterfaz

    @FXML
    void onAddAction(ActionEvent event) {

    }

    @FXML
    void onDeleteAction(ActionEvent event) {

    }

    @FXML
    void onModifyAction(ActionEvent event) {
        splitTutorEmpresa.getItems().add(contactoEmpModifyController.getRoot());
        SplitPane.setResizableWithParent(contactoEmpModifyController.getRoot(), false);
    }

    @FXML
    void onSearchAction(ActionEvent event) {

    }

    @FXML
    void onSearchAllAction(ActionEvent event) {

    }


}
