package dad.gestion_fct.controllers.ContactoEmp;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.models.ContactoEmp;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ContactoEmpController implements Initializable {
    boolean modified = false;

    //private ListProperty<ContactoEmp> listaContactoEmp = new SimpleListProperty<>();
    private ObjectProperty<ContactoEmp> selectedContactoEmp = new SimpleObjectProperty<>();
    ContactoEmpModifyController contactoEmpModifyController = new ContactoEmpModifyController(this);
    private ObservableList<ContactoEmp> listaContactoEmp = FXCollections.observableArrayList();

    public ContactoEmp getSelectedContactoEmp() {
        return selectedContactoEmp.get();
    }

    public ObjectProperty<ContactoEmp> selectedContactoEmpProperty() {
        return selectedContactoEmp;
    }

    public void setSelectedContactoEmp(ContactoEmp selectedContactoEmp) {
        this.selectedContactoEmp.set(selectedContactoEmp);
    }

    @FXML
    private SplitPane splitContactoEmpresa;

    @FXML
    private BorderPane root;

    @FXML
    private TableColumn<ContactoEmp, String> empresaColumn;

    @FXML
    private TableColumn<ContactoEmp, String> nombreColumn;

    @FXML
    private TableView<ContactoEmp> contactoEmpresaTable;

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


    public ContactoEmpController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/contactoEmp/ContactoEmpView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SplitPane.setResizableWithParent(contactoEmpModifyController.getRoot(), false);

         modifyButton.setDisable(true);
         removeButton.setDisable(true);

        //empresaColumn.setCellValueFactory(v -> v.getValue().();
        //SELECT Empresa.NombreEmpresa FROM ContactoEmpresa INNER JOIN Empresa ON ContactoEmpresa.IdEmpresa = Empresa.IdEmpresa;
        nombreColumn.setCellValueFactory(v -> v.getValue().nombreContactoProperty());
        apellidoColumn.setCellValueFactory(v -> v.getValue().apellidoContactoProperty());
        correoColumn.setCellValueFactory(v -> v.getValue().correoContactoProperty());
        telefonoColumn.setCellValueFactory(v -> v.getValue().telefonoContactoProperty());


        // Asignar los datos al TableView
        contactoEmpresaTable.setItems(listaContactoEmp);

        // Cargar los datos de la base de datos
        cargarDatos();

        //Listener actualizar botones
        contactoEmpresaTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Si hay un elemento seleccionado, habilitar los botones; de lo contrario, deshabilitarlos
            boolean hasSelection = newValue != null;
            modifyButton.setDisable(!hasSelection);
            removeButton.setDisable(!hasSelection);

            // Si hay un elemento seleccionado, actualizar el controlador de modificación
            if (hasSelection) {
                contactoEmpModifyController.setContactoModify(newValue);
            }
        });
    }


    private void cargarDatos() {
        listaContactoEmp.clear();  // Limpia la lista actual

        try (Connection connection = HikariConnection.getConnection()) {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM contactoempresa";

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                ContactoEmp contacto = new ContactoEmp();

                contacto.setIdContacto(resultSet.getInt("idContacto"));
                contacto.setNombreContacto(resultSet.getString("NombreContacto"));
                contacto.setApellidoContacto(resultSet.getString("ApellidoContacto"));
                contacto.setTelefonoContacto(resultSet.getString("Telefono"));
                contacto.setCorreoContacto(resultSet.getString("CorreoContacto"));
                listaContactoEmp.add(contacto);  // Agrega el contacto a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BorderPane getRoot() {
        return root;
    }

    public SplitPane getSplitContactoEmpresa() {
        return splitContactoEmpresa;
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
        // Obtener el elemento seleccionado en el TableView
        ContactoEmp selectedContacto = contactoEmpresaTable.getSelectionModel().getSelectedItem();

        if (selectedContacto != null) {
            // Pasar el contacto seleccionado al controlador de modificación
            contactoEmpModifyController.setContactoModify(selectedContacto);

            // Mostrar la vista de modificación si no está ya visible
            if (modified) {
                splitContactoEmpresa.getItems().remove(contactoEmpModifyController.getRoot());
                modified = false;
            } else {
                splitContactoEmpresa.getItems().add(contactoEmpModifyController.getRoot());
                SplitPane.setResizableWithParent(contactoEmpModifyController.getRoot(), false);
                modified = true;
            }
        }

    }


    @FXML
        void onSearchAction(ActionEvent event) {

        }

    @FXML
    void onSearchAllAction(ActionEvent event) {

    }


    public SplitPane getSplitContactoEmp() {
        return splitContactoEmpresa;
    }
}
