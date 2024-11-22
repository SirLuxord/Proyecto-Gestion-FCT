package dad.gestion_fct.controllers.empresa;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.controllers.RootController;
import dad.gestion_fct.models.Empresa;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmpresaController implements Initializable {

    // controllers

    EmpresaModifyController empresaModifyController = new EmpresaModifyController(this);

    // model

    private ListProperty<Empresa> empresas = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<Empresa> empresaSeleccionada = new SimpleObjectProperty<>();
    private BooleanProperty selected = new SimpleBooleanProperty(false);

    // view

    @FXML
    private SplitPane splitEmpresa;

    @FXML
    private BorderPane root;

    @FXML
    private TableColumn<Empresa, String> cpEmpresaColumn;

    @FXML
    private TableColumn<Empresa, String> direccionEmpresaColumn;

    @FXML
    private TableView<Empresa> empresasTable;

    @FXML
    private TableColumn<Empresa, String> localidadEmpresaColumn;

    @FXML
    private TableColumn<Empresa, String> nifColumn;

    @FXML
    private TableColumn<Empresa, String> nombreEmpresaColumn;

    @FXML
    private TableColumn<Empresa, Boolean> publicaColumn;

    @FXML
    private Button modifyButton;

    @FXML
    private Button removeButton;


    public EmpresaController(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/empresa/empresaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        SplitPane.setResizableWithParent(empresaModifyController.getRoot() , false);

        // binding

        empresasTable.itemsProperty().bind(empresas);
        empresaSeleccionada.bind(empresasTable.getSelectionModel().selectedItemProperty());
        modifyButton.disableProperty().bind(selected);
        removeButton.disableProperty().bind(selected);

        empresaSeleccionada.addListener((o , ov ,nv) -> {
            if (nv != null){
                selected.set(true);
            } else {
                selected.set(false);
            }
        });

        // cell values factories

        nifColumn.setCellValueFactory(v -> v.getValue().nifEmpresaProperty());
        nombreEmpresaColumn.setCellValueFactory(v -> v.getValue().nombreProperty());
        direccionEmpresaColumn.setCellValueFactory(v -> v.getValue().dirrecionProperty());
        localidadEmpresaColumn.setCellValueFactory(v -> v.getValue().localidadProperty());
        cpEmpresaColumn.setCellValueFactory(v -> v.getValue().codigoPostalProperty());
        publicaColumn.setCellValueFactory(v -> v.getValue().publicaProperty());

    }

    @FXML
    void onAddEmpresaAction(ActionEvent event) {
        EmpresaCreateDialog createDialog = new EmpresaCreateDialog();
        Optional<Empresa> result = createDialog.showAndWait();
       if (result.isPresent()){
           Empresa empresa = result.get();
           empresa.setIdEmpresa(buscarId(empresa.getNifEmpresa()));
           empresas.add(empresa);
       }
    }

    @FXML
    void onDeleteEmpresaAction(ActionEvent event) {
        empresas.remove(empresaSeleccionada.get());
        Alert borradoAlert = new Alert(Alert.AlertType.WARNING);
        borradoAlert.setTitle("Alerta: Borrado");
        borradoAlert.setHeaderText("Si borras este registro se borran sus contactos, plazas y tutores asociados.");
        borradoAlert.setContentText("¿Estas seguro que quieres borrarlo?");
        // TODO
    }

    @FXML
    void onModifyEmpresaAction(ActionEvent event) {
        empresaModifyController.setEmpresa(empresaSeleccionada.get());
        splitEmpresa.getItems().add(empresaModifyController.getRoot());
    }

    @FXML
    void onSearchAllEmpresaAction(ActionEvent event) {
        String query = "Select * from Empresa";
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Empresa empresa = new Empresa();
                empresa.setIdEmpresa(resultSet.getInt("IdEmpresa"));
                empresa.setNifEmpresa(resultSet.getString("NIFEmpresa"));
                empresa.setNombre(resultSet.getString("NombreEmpresa"));
                empresa.setDirrecion(resultSet.getString("DireccionEmpresa"));
                empresa.setLocalidad(resultSet.getString("LocalidadEmpresa"));
                empresa.setCodigoPostal(resultSet.getString("CPEmpresa"));
                empresa.setPublica(resultSet.getBoolean("PublicaPrivada"));
                empresas.add(empresa);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onSearchEmpresaAction(ActionEvent event) {

    }

    public int buscarId(String nif){
        String query = "Select IdEmpresa from Empresa where NIFEmpresa = ?";
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1 , nif);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getInt("IdEmpresa");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Getters and Setters

    public BorderPane getRoot() {
        return root;
    }

    public SplitPane getSplitEmpresa() {
        return splitEmpresa;
    }
}
