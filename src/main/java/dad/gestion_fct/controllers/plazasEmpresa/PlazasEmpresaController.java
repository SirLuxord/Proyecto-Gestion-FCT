package dad.gestion_fct.controllers.plazasEmpresa;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.controllers.tutorEmpresa.TutorEmpresaCreateDialog;
import dad.gestion_fct.controllers.tutorEmpresa.TutorEmpresaModifyController;
import dad.gestion_fct.controllers.tutorEmpresa.TutorEmpresaSearchDialog;
import dad.gestion_fct.models.Empresa;
import dad.gestion_fct.models.PlazasEmpresa;
import dad.gestion_fct.models.TutorEmpresa;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class PlazasEmpresaController implements Initializable {

    // controllers

    PlazasEmpresaModifyController plazasEmpresaModifyController = new PlazasEmpresaModifyController(this);

    // model

    private ListProperty<PlazasEmpresa> plazas = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<PlazasEmpresa> plazaSeleccionada = new SimpleObjectProperty<>();

    private ListProperty<Empresa> empresas = new SimpleListProperty<>(FXCollections.observableArrayList());

    // view

    @FXML
    private TableColumn<PlazasEmpresa, String> cicloColumn;

    @FXML
    private Button createButton;

    @FXML
    private TableColumn<PlazasEmpresa, String> empresaColumn;

    @FXML
    private TableView<PlazasEmpresa> plazasTable;

    @FXML
    private Button modifyButton;

    @FXML
    private TableColumn<PlazasEmpresa, Number> numeroPlazasColumn;

    @FXML
    private Button removeButton;

    @FXML
    private BorderPane root;

    @FXML
    private Button searchAllButton;

    @FXML
    private Button searchButton;

    @FXML
    private SplitPane splitPlazas;

    public PlazasEmpresaController(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plazasEmpresa/plazasEmpresaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        SplitPane.setResizableWithParent(plazasEmpresaModifyController.getRoot() , false);

        modifyButton.setDisable(true);
        removeButton.setDisable(true);

        // binding

        plazasTable.itemsProperty().bind(plazas);
        plazaSeleccionada.bind(plazasTable.getSelectionModel().selectedItemProperty());
        plazasTable.disableProperty().bind(Bindings.createBooleanBinding(this::onSplitPaneChanged, splitPlazas.getItems()));
        plazasEmpresaModifyController.getEmpresaCombo().itemsProperty().bind(empresas);


        plazaSeleccionada.addListener((o , ov , nv) -> {
            modifyButton.setDisable(nv == null);
            removeButton.setDisable(nv == null);
        });

        plazasTable.disableProperty().addListener((o , ov , nv) -> {
            modifyButton.setDisable(nv);
            removeButton.setDisable(nv);
            createButton.setDisable(nv);
            searchButton.setDisable(nv);
            searchAllButton.setDisable(nv);
        });


        // cell values factories

        empresaColumn.setCellValueFactory(v -> v.getValue().nombreEmpresaProperty());
        cicloColumn.setCellValueFactory(v -> v.getValue().nombreCicloProperty());
        numeroPlazasColumn.setCellValueFactory(v -> v.getValue().numeroPlazasProperty());


    }

    private Boolean onSplitPaneChanged() {
        return splitPlazas.getItems().size() == 2;
    }


    @FXML
    void onAddAction(ActionEvent event) {
        PlazasEmpresaCreateDialog createDialog = new PlazasEmpresaCreateDialog();
        seleccionarEmpresas();
        createDialog.getEmpresaCombo().getItems().setAll(empresas);
        Optional<PlazasEmpresa> result = createDialog.showAndWait();
        if (result.isPresent()){
            PlazasEmpresa plaza = result.get();

            String query = "Insert into PlazasEmpresas (NombreCiclo, IdEmpresa, NumeroPlazas) VALUES ( ?, ?, ?)";
            try (Connection connection = HikariConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)){

                statement.setString(1 , plaza.getNombreCiclo());
                statement.setInt(2 , plaza.getIdEmpresa());
                statement.setInt(3 , plaza.getNumeroPlazas());

                statement.execute();

            }  catch (SQLException e) {
                mostrarError(e.getLocalizedMessage());
                throw new RuntimeException(e);
            }
            plazas.add(plaza);
        }
    }

    @FXML
    void onDeleteAction(ActionEvent event) {

        String nombreCiclo = plazaSeleccionada.get().getNombreCiclo();
        int idEmpresa = plazaSeleccionada.get().getIdEmpresa();
        String deleteQuery = "Delete from PlazasEmpresas where NombreCiclo = ? and IdEmpresa = ?";

        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {


            deleteStatement.setString(1, nombreCiclo);
            deleteStatement.setInt(2, idEmpresa);

            deleteStatement.execute();
            plazas.remove(plazaSeleccionada.get());

        } catch (SQLException e) {
            mostrarError(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onModifyAction(ActionEvent event) {

        seleccionarEmpresas();

        // se le pasa al controlador de modificar los datos del registro seleccionado

        plazasEmpresaModifyController.setNombreCicloAnterior(plazaSeleccionada.get().getNombreCiclo());
        plazasEmpresaModifyController.setIdEmpresaAntiguo(plazaSeleccionada.get().getIdEmpresa());;
        plazasEmpresaModifyController.getPlaza().setNumeroPlazas(plazaSeleccionada.get().getNumeroPlazas());

        splitPlazas.getItems().add(plazasEmpresaModifyController.getRoot());
    }

    @FXML
    void onSearchAllAction(ActionEvent event) {
        buscarPlazas(getSearchQuery("") , "");
    }

    @FXML
    void onSearchAction(ActionEvent event) {
        PlazasEmpresaSearchDialog searchDialog = new PlazasEmpresaSearchDialog();
        Optional<String> result = searchDialog.showAndWait();

        if (result.isPresent()){
            String campo = result.get();
            TextInputDialog campoDialog = new TextInputDialog();
            campoDialog.setHeaderText("Introduzca el " + campo);
            campoDialog.setContentText(campo + ":");
            Optional<String> parametroResult = campoDialog.showAndWait();

            if (parametroResult.isPresent()){
                String parametro = parametroResult.get();
                buscarPlazas( getSearchQuery(campo) , "%" + parametro + "%");
            }
        }

    }

    public void buscarPlazas(String query , String parametro){
        plazas.clear();
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1 , parametro);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PlazasEmpresa plaza = new PlazasEmpresa();
                plaza.setNombreCiclo(resultSet.getString("PlazasEmpresas.NombreCiclo"));
                plaza.setIdEmpresa(resultSet.getInt("Empresa.IdEmpresa"));
                plaza.setNombreEmpresa(resultSet.getString("Empresa.NombreEmpresa"));
                plaza.setNumeroPlazas(resultSet.getInt("PlazasEmpresas.NumeroPlazas"));
                plazas.add(plaza);
            }

        } catch (SQLException e) {
            mostrarError(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }


    public void seleccionarEmpresas (){
        empresas.setAll(FXCollections.observableArrayList());
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
                empresa.setPublica(resultSet.getBoolean("EmpresaPublica"));
                empresas.add(empresa);
            }

        } catch (SQLException e) {
            mostrarError(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    private static String getSearchQuery(String opcion) {
        String condicion = switch (opcion) {
            case "Nombre ciclo" -> "WHERE NombreCiclo LIKE ?";
            case "Nombre empresa" -> "WHERE NombreEmpresa LIKE ?";
            default -> "";
        };


        String query = "Select PlazasEmpresas.* , Empresa.NombreEmpresa , Empresa.IdEmpresa from PlazasEmpresas inner join Empresa on Empresa.IdEmpresa = PlazasEmpresas.IdEmpresa " + condicion ;
        return query;
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

    public PlazasEmpresa getPlazaSeleccionada() {
        return plazaSeleccionada.get();
    }

    public ObjectProperty<PlazasEmpresa> plazaSeleccionadaProperty() {
        return plazaSeleccionada;
    }

    public void setPlazaSeleccionada(PlazasEmpresa plazaSeleccionada) {
        this.plazaSeleccionada.set(plazaSeleccionada);
    }

    public SplitPane getSplitPlazas() {
        return splitPlazas;
    }

    public void setSplitPlazas(SplitPane splitPlazas) {
        this.splitPlazas = splitPlazas;
    }

    public ObservableList<Empresa> getEmpresas() {
        return empresas.get();
    }

    public ListProperty<Empresa> empresasProperty() {
        return empresas;
    }

    public void setEmpresas(ObservableList<Empresa> empresas) {
        this.empresas.set(empresas);
    }
}
