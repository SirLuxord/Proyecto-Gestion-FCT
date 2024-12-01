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

import static com.sun.javafx.scene.control.skin.resources.ControlResources.getString;

public class ContactoEmpController implements Initializable {


    ContactoEmpModifyController contactoEmpModifyController = new ContactoEmpModifyController(this);

    private ListProperty<ContactoEmp> contactos = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<ContactoEmp> selectedContactoEmp = new SimpleObjectProperty<>();
    private ListProperty<Empresa> empresas = new SimpleListProperty<>(FXCollections.observableArrayList());


    public ContactoEmp getSelectedContactoEmp() {
        return selectedContactoEmp.get();
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

        //cell values
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

    //metodos acciones interfaz

    @FXML
    void onAddAction(ActionEvent event) throws SQLException {
        ContactoEmpCreateDialog dialog = new ContactoEmpCreateDialog();
        seleccionarEmpresas();
        dialog.getEmpresaCombo().getItems().setAll(empresas);
        Optional<ContactoEmp> result = dialog.showAndWait();

        if (result.isPresent()) {
            ContactoEmp contacto = result.get();
                //comprobaciones
            if (contacto.getNombreContacto().trim().isEmpty()) {
                mostrarAlertaError("Campo de nombre vacío");
                throw new IllegalArgumentException("Campo de nombre vacío");
            }

            if (contacto.getTelefono().trim().isEmpty()) {
                mostrarAlertaError("Campo de teléfono vacío");
                throw new IllegalArgumentException("Campo de teléfono vacío");
            }

            if (!esTelefonoValido(contacto.getTelefono())) {
                mostrarAlertaError("numero de telefono invalido");
                return;
            }

            String query = "Insert into contactoEmpresa (IdEmpresa, NombreContacto, ApellidoContacto, Telefono, CorreoContacto) VALUES ( ?, ?, ?, ?, ?)";
            try (Connection connection = HikariConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1 , contacto.getIdEmpresa());
                statement.setString(2, contacto.getNombreContacto());
                statement.setString(3, contacto.getApellidoContacto());
                statement.setString(4, contacto.getTelefono());
                statement.setString(5, contacto.getCorreoContacto());

                statement.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            contacto.setIdContacto(buscarId(contacto.getTelefono()));
            contactos.add(contacto);

            onSearchAllAction(); //para actualizar lista
        }
    }

    @FXML
    void onDeleteAction(ActionEvent event) throws SQLException {
        int idContacto = selectedContactoEmp.get().getIdContacto();
        String query = "Delete from contactoEmpresa where IdContacto =?";

        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, idContacto);
            statement.execute();
            contactos.remove(selectedContactoEmp.get());
            // int rowsAffected = statement.executeUpdate();
        } catch (SQLException e) {
            mostrarAlertaError("Error al eliminar contacto");
            throw new RuntimeException(e);
        }
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

        selectedContactoEmp.bind(contactoEmpresaTable.getSelectionModel().selectedItemProperty());
        System.out.println(selectedContactoEmp.get().getIdContacto());
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
        ContactoEmpSearchDialog searchDialog = new ContactoEmpSearchDialog();
        Optional<String> campo = searchDialog.showAndWait();
        // Se ejecuta solo si el usuario selecciona un valor válido.
        if (campo.isPresent() && !campo.get().isEmpty()) {
            campo.get();
            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setHeaderText("Introduzca el " + campo.get());
            nameDialog.setContentText(campo.get() + ": ");
            Optional<String> result = nameDialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    buscarContactoEmp(campo.get(), "%" + value + "%");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }

    private void buscarContactoEmp(String s, String s1) throws SQLException {
        contactos.clear();

        String query = obtenerString(s);
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, s1);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ContactoEmp contacto = new ContactoEmp();
                contacto.setNombreContacto(resultSet.getString("nombreContacto"));
                contacto.setApellidoContacto(resultSet.getString("apellidoContacto"));
                contacto.setTelefono(resultSet.getString("telefono"));
                contacto.setCorreoContacto(resultSet.getString("correoContacto"));
                contacto.setNombreEmpresa(resultSet.getString("NombreEmpresa"));

                contactos.add(contacto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String obtenerString(String s) {
        System.out.println("Valor de s: " + s);

        String c = switch(s) {
            case "Nombre" -> "WHERE NombreContacto LIKE ?";
            case "Apellido" -> "WHERE ApellidoContacto LIKE ?";
            case "Teléfono" -> "WHERE Telefono LIKE ?";
            case "Correo" -> "WHERE CorreoContacto LIKE ?";
            case "NombreEmpresa" -> "WHERE Empresa.NombreEmpresa LIKE ?";
            default -> "";
        };

        System.out.println(c);
            String query = "SELECT NombreContacto, ApellidoContacto, Telefono, CorreoContacto, Empresa.NombreEmpresa FROM contactoEmpresa " +
                    "INNER JOIN Empresa ON Empresa.IdEmpresa = contactoEmpresa.IdEmpresa " + c;
        return query;
    }

    public int buscarId(String telefono) {
        String query = "Select IdContacto from ContactoEmpresa where Telefono = ?";
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, telefono);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("IdContacto");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //Validar formato telefono
    public static boolean esTelefonoValido(String telefono) {
        // Validar que el número solo contenga dígitos y tenga una longitud mínima
        String regex = "^[0-9]{9,15}$";

        if (telefono == null || telefono.trim().isEmpty()) {
            return false;
        }

        return telefono.matches(regex);
    }


    private void mostrarAlertaError(String titulo) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.showAndWait();
    }

    public SplitPane getSplitContactoEmp() {
        return splitContactoEmpresa;
    }
}
