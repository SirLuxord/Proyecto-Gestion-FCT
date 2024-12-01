package dad.gestion_fct.controllers.empresa;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.models.Empresa;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmpresaController implements Initializable {

    // controllers

    EmpresaModifyController empresaModifyController = new EmpresaModifyController(this);

    // model

    private ListProperty<Empresa> empresas = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<Empresa> empresaSeleccionada = new SimpleObjectProperty<>();

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

    @FXML
    private Button createButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button searchAllButton;

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

        modifyButton.setDisable(true);
        removeButton.setDisable(true);

        // binding

        empresasTable.itemsProperty().bind(empresas);
        empresaSeleccionada.bind(empresasTable.getSelectionModel().selectedItemProperty());
        empresasTable.disableProperty().bind(Bindings.createBooleanBinding(this::onSplitPaneChanged, splitEmpresa.getItems()));

        empresaSeleccionada.addListener((o , ov ,nv) -> {
            modifyButton.setDisable(nv == null);
            removeButton.setDisable(nv == null);
        });

        empresasTable.disableProperty().addListener((o , ov , nv) -> {
            modifyButton.setDisable(nv);
            removeButton.setDisable(nv);
            createButton.setDisable(nv);
            searchButton.setDisable(nv);
            searchAllButton.setDisable(nv);
        });


        // cell values factories

        nifColumn.setCellValueFactory(v -> v.getValue().nifEmpresaProperty());
        nombreEmpresaColumn.setCellValueFactory(v -> v.getValue().nombreProperty());
        direccionEmpresaColumn.setCellValueFactory(v -> v.getValue().dirrecionProperty());
        localidadEmpresaColumn.setCellValueFactory(v -> v.getValue().localidadProperty());
        cpEmpresaColumn.setCellValueFactory(v -> v.getValue().codigoPostalProperty());
        publicaColumn.setCellValueFactory(v -> v.getValue().publicaProperty());

        // cell factories

        publicaColumn.setCellFactory(CheckBoxTableCell.forTableColumn(publicaColumn));

    }

    private Boolean onSplitPaneChanged() {
        if (splitEmpresa.getItems().size() == 2){
            return true;
        }
        return false;
    }

    @FXML
    void onAddEmpresaAction(ActionEvent event) {
        EmpresaCreateDialog createDialog = new EmpresaCreateDialog();
        Optional<Empresa> result = createDialog.showAndWait();
       if (result.isPresent()){
           Empresa empresa = result.get();

           // Antes de hacer el insert se comprueba que ni el nif ni el nombre estén vacíos.

           if (empresa.getNifEmpresa().trim().isEmpty()) {
               mostrarError("El nif de la empresa no puede estar vacío.");
               throw new IllegalArgumentException("El nif de la empresa no puede estar vacío.");
           }
           if (empresa.getNombre().trim().isEmpty()) {
               mostrarError("El nombre de la empresa no puede estar vacío.");
               throw new IllegalArgumentException("El nombre de la empresa no puede estar vacío.");
           }

           String query = "Insert into Empresa (NIFEmpresa, NombreEmpresa, DireccionEmpresa, LocalidadEmpresa, CPEmpresa, EmpresaPublica) VALUES ( ?, ?, ?, ?, ?, ?)";
           try (Connection connection = HikariConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)){

               statement.setString(1 , empresa.getNifEmpresa());
               statement.setString(2 , empresa.getNombre());
               statement.setString(3 , empresa.getDirrecion());
               statement.setString(4 , empresa.getLocalidad());
               statement.setString(5 , empresa.getCodigoPostal());
               statement.setBoolean(6 , empresa.isPublica());

               statement.execute();

           }  catch (SQLException e) {
               mostrarError(e.getLocalizedMessage());
               throw new RuntimeException(e);
           }
           empresa.setIdEmpresa(buscarId(empresa.getNifEmpresa()));
           empresas.add(empresa);
       }
    }

    @FXML
    void onDeleteEmpresaAction(ActionEvent event) {

        int idEmpresa = empresaSeleccionada.get().getIdEmpresa();
        Alert borradoAlert = new Alert(Alert.AlertType.WARNING);
        borradoAlert.setTitle("Alerta: Borrado");
        borradoAlert.setHeaderText("Si borras este registro se borran sus contactos, plazas y tutores asociados.");
        borradoAlert.setContentText("¿Estas seguro que quieres borrarlo?");

        ButtonType botonAfirmativo = new ButtonType("Si" );
        ButtonType botonNegativo = new ButtonType("No" );

        borradoAlert.getButtonTypes().setAll(botonAfirmativo , botonNegativo);

        Optional<ButtonType> result = borradoAlert.showAndWait();
        if (result.get() == botonAfirmativo){
            String[] tablas = {"ContactoEmpresa", "PlazasEmpresas", "TutorEmpresa", "Empresa"};

            try (Connection connection = HikariConnection.getConnection()) {
                for (String tabla : tablas) {
                    String query = "Delete from " + tabla + " where IdEmpresa = ?";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setInt(1, idEmpresa);
                        statement.execute();
                    }
                }
                empresas.remove(empresaSeleccionada.get());
            } catch (SQLException e) {
                mostrarError(e.getLocalizedMessage());
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void onModifyEmpresaAction(ActionEvent event) {

        // se le pasa al controlador de modificar los datos del registro seleccionado

        empresaModifyController.getEmpresa().setIdEmpresa(empresaSeleccionada.get().getIdEmpresa());
        empresaModifyController.getEmpresa().setNifEmpresa(empresaSeleccionada.get().getNifEmpresa());
        empresaModifyController.getEmpresa().setNombre(empresaSeleccionada.get().getNombre());
        empresaModifyController.getEmpresa().setDirrecion(empresaSeleccionada.get().getDirrecion());
        empresaModifyController.getEmpresa().setLocalidad(empresaSeleccionada.get().getLocalidad());
        empresaModifyController.getEmpresa().setCodigoPostal(empresaSeleccionada.get().getCodigoPostal());
        empresaModifyController.getEmpresa().setPublica(empresaSeleccionada.get().isPublica());
        splitEmpresa.getItems().add(empresaModifyController.getRoot());
    }

    @FXML
    void onSearchAllEmpresaAction(ActionEvent event) {
        buscarEmpresas(getSearchQuery("") , "");
    }

    @FXML
    void onSearchEmpresaAction(ActionEvent event) {
        EmpresaSearchDialog searchDialog = new EmpresaSearchDialog();
        Optional<String> result = searchDialog.showAndWait();
        if (result.isPresent()) {
            String campo = result.get();
            TextInputDialog campoDialog = new TextInputDialog();
            campoDialog.setHeaderText("Introduzca el " + campo.toLowerCase());
            campoDialog.setContentText(campo + ":");
            Optional<String> parametroResult = campoDialog.showAndWait();

            if (parametroResult.isPresent()){
                String parametro = parametroResult.get();
                buscarEmpresas( getSearchQuery(campo) , "%" + parametro + "%");
            }
        }

    }

    public void buscarEmpresas(String query , String parametro){
        empresas.clear();
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1 , parametro);
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
            mostrarError(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    private String getSearchQuery(String opcion) {
        String condicion = switch (opcion) {
            case "Nif" -> "WHERE NIFEmpresa LIKE ?";
            case "Nombre" -> "WHERE NombreEmpresa LIKE ?";
            case "Dirección" -> "WHERE DireccionEmpresa LIKE ?";
            case "Localidad" -> "WHERE LocalidadEmpresa LIKE ?";
            case "Código postal" -> "WHERE CPEmpresa LIKE ?";
            default -> "";
        };


        return "Select * from Empresa " + condicion;
    }

    public int buscarId(String nif){
        String query = "Select IdEmpresa from Empresa where NIFEmpresa = ?";
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1 , nif);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("IdEmpresa");

        } catch (SQLException e) {
            mostrarError(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public void mostrarError(String error){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText("Ha habido algún error");
        errorAlert.setContentText("Error: " + error);
        errorAlert.show();
    }

    // Getters and Setters

    public BorderPane getRoot() {
        return root;
    }

    public SplitPane getSplitEmpresa() {
        return splitEmpresa;
    }

    public Empresa getEmpresaSeleccionada() {
        return empresaSeleccionada.get();
    }

    public ObjectProperty<Empresa> empresaSeleccionadaProperty() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresa empresaSeleccionada) {
        this.empresaSeleccionada.set(empresaSeleccionada);
    }
}
