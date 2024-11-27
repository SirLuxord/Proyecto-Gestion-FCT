package dad.gestion_fct.controllers.ContactoEmp;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.models.ContactoEmp;
import dad.gestion_fct.models.Empresa;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class ContactoEmpController implements Initializable {


    ContactoEmpModifyController contactoEmpModifyController = new ContactoEmpModifyController(this);

    private ListProperty<ContactoEmp> contactos = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<ContactoEmp> selectedContactoEmp = new SimpleObjectProperty<>();
    //private ListProperty<Ciclos> ciclos = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ListProperty<Empresa> empresas = new SimpleListProperty<>(FXCollections.observableArrayList());


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
        onSearchAllAction();
        modifyButton.setDisable(true);
        removeButton.setDisable(true);

        contactoEmpresaTable.itemsProperty().bind(contactos);
        selectedContactoEmp.bind(contactoEmpresaTable.getSelectionModel().selectedItemProperty());
        contactoEmpresaTable.disableProperty().bind(Bindings.createBooleanBinding(this::onSplitPaneChanged, splitContactoEmpresa.getItems()));
        contactoEmpModifyController.getEmpresaCombo().itemsProperty().bind(empresas);

        selectedContactoEmp.addListener((o, ov, nv) -> {
            modifyButton.setDisable(nv == null);
            removeButton.setDisable(nv == null);
        });


        contactoEmpresaTable.disableProperty().addListener((o, ov, nv) -> {
            modifyButton.setDisable(nv);
            removeButton.setDisable(nv);
            createButton.setDisable(nv);
            searchButton.setDisable(nv);
            searchAllButton.setDisable(nv);
        });
        //empresaColumn.setCellValueFactory(v -> v.getValue().();
        //SELECT Empresa.NombreEmpresa FROM ContactoEmpresa INNER JOIN Empresa ON ContactoEmpresa.IdEmpresa = Empresa.IdEmpresa;
        nombreColumn.setCellValueFactory(v -> v.getValue().nombreContactoProperty());
        apellidoColumn.setCellValueFactory(v -> v.getValue().apellidoContactoProperty());
        correoColumn.setCellValueFactory(v -> v.getValue().correoContactoProperty());
        telefonoColumn.setCellValueFactory(v -> v.getValue().telefonoProperty());
        empresaColumn.setCellValueFactory(v -> v.getValue().nombreEmpresaProperty());


    }


    private Boolean onSplitPaneChanged() {
        if (splitContactoEmpresa.getItems().size() == 2) {
            return true;
        }
        return false;
    }

    public BorderPane getRoot() {
        return root;
    }

    public SplitPane getSplitContactoEmpresa() {
        return splitContactoEmpresa;
    }

    //metodos acciones iinterfaz

    @FXML
    void onAddAction(ActionEvent event) throws SQLException {
        ContactoEmpCreateDialog dialog = new ContactoEmpCreateDialog();
        seleccionarEmpresas();
        dialog.getEmpresaCombo().getItems().setAll(empresas);
        Optional<ContactoEmp> result = dialog.showAndWait();

        if (result.isPresent()) {
            ContactoEmp contacto = result.get();

            if (contacto.getNombreContacto().trim().isEmpty()) {
                mostrarAlertaError("Campo de nombre vacío");
                throw new IllegalArgumentException("Campo de nombre vacío");
            }

            if (contacto.getTelefono().trim().isEmpty()) {
                mostrarAlertaError("Campo de teléfono vacío");
                throw new IllegalArgumentException("Campo de teléfono vacío");
            }

            String query = "Insert into contactoEmpresa (IdEmpresa, NombreContacto, ApellidoContacto, Telefono, CorreoContacto) VALUES ( ?, ?, ?, ?, ?)";
            try (Connection connection = HikariConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, contacto.getIdEmpresa());
                statement.setString(2, contacto.getNombreContacto());
                statement.setString(3, contacto.getApellidoContacto());
                statement.setString(4, contacto.getTelefono());
                statement.setString(5, contacto.getCorreoContacto());

                statement.execute();

            }  catch (SQLException e) {
                 throw new RuntimeException(e);
            }
            contacto.setIdContacto(buscarId(contacto.getTelefono()));
            contactos.add(contacto);

        }
    }

    @FXML
    void onDeleteAction(ActionEvent event) {

    }

    @FXML
    void onModifyAction(ActionEvent event) {
        seleccionarEmpresas();

        contactoEmpModifyController.setContacto(new ContactoEmp());
        contactoEmpModifyController.getContacto().setIdContacto(selectedContactoEmp.get().getIdContacto());
        contactoEmpModifyController.getContacto().setIdEmpresa(selectedContactoEmp.get().getIdEmpresa());
        contactoEmpModifyController.getContacto().setNombreContacto(selectedContactoEmp.get().getNombreContacto());
        contactoEmpModifyController.getContacto().setApellidoContacto(selectedContactoEmp.get().getApellidoContacto());
        contactoEmpModifyController.getContacto().setTelefono(selectedContactoEmp.get().getTelefono());
        contactoEmpModifyController.getContacto().setCorreoContacto(selectedContactoEmp.get().getCorreoContacto());
        splitContactoEmpresa.getItems().add(contactoEmpModifyController.getRoot());

    }


    private void seleccionarEmpresas() {
        empresas.setAll(FXCollections.observableArrayList());
        String query = "Select * from Empresa";
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Empresa empresa = new Empresa();
                empresa.setIdEmpresa(resultSet.getInt("IdEmpresa"));
                empresa.setNifEmpresa(resultSet.getString("NIFEmpresa"));
                empresa.setNombre(resultSet.getString("NombreEmpresa"));
                empresa.setDirrecion(resultSet.getString("DireccionEmpresa"));
                empresa.setLocalidad(resultSet.getString("LocalidadEmpresa"));
                empresa.setCodigoPostal(resultSet.getString("CPEmpresa"));
                empresa.setPublica(resultSet.getBoolean("EmpresaPublica"));
                empresas.add(empresa);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private void onSearchAllAction() {
        //contactos.clear();  // Limpiar los datos anteriores
        contactos.setAll(FXCollections.observableArrayList());
        try (Connection connection = HikariConnection.getConnection()) {
            Statement statement = connection.createStatement();
            String query = "SELECT contactoEmpresa.*, Empresa.NombreEmpresa, Empresa.IdEmpresa FROM contactoEmpresa" +
                    " INNER JOIN Empresa ON Empresa.IdEmpresa = contactoEmpresa.IdEmpresa;";

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                ContactoEmp contacto = new ContactoEmp();
                contacto.setIdContacto(resultSet.getInt("IdContacto"));
                contacto.setIdEmpresa(resultSet.getInt("Empresa.IdEmpresa"));
                contacto.setNombreContacto(resultSet.getString("nombreContacto"));
                contacto.setApellidoContacto(resultSet.getString("apellidoContacto"));
                contacto.setTelefono(resultSet.getString("telefono"));
                contacto.setCorreoContacto(resultSet.getString("correoContacto"));
                contacto.setNombreEmpresa(resultSet.getString("NombreEmpresa"));

                contactos.add(contacto);  // Agregar el contacto a la lista
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    @FXML
    void onSearchAction(ActionEvent event) {

    }

    public int buscarId(String telefono){
        String query = "Select IdContacto from ContactoEmpresa where Telefono = ?";
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1 , telefono);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("IdContacto");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void mostrarAlertaError(String titulo) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de validación");
        alert.setHeaderText(titulo);
        alert.showAndWait();
    }

    public SplitPane getSplitContactoEmp() {
        return splitContactoEmpresa;
    }
}
