package dad.gestion_fct.controllers.tutorEmpresa;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.controllers.empresa.EmpresaController;
import dad.gestion_fct.controllers.empresa.EmpresaModifyController;
import dad.gestion_fct.models.Empresa;
import dad.gestion_fct.models.TutorEmpresa;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
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
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class TutorEmpresaController implements Initializable {

    // controllers

    TutorEmpresaModifyController tutorEmpresaModifyController = new TutorEmpresaModifyController(this);

    // model

    private ListProperty<TutorEmpresa> tutores = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<TutorEmpresa> tutorSeleccionado = new SimpleObjectProperty<>();

    private ListProperty<Empresa> empresas = new SimpleListProperty<>(FXCollections.observableArrayList());

    // view

    @FXML
    private SplitPane splitTutorEmpresa;

    @FXML
    private BorderPane root;

    @FXML
    private TableColumn<TutorEmpresa, String> empresaColumn;

    @FXML
    private TableColumn<TutorEmpresa, String> nombreColumn;

    @FXML
    private TableView<TutorEmpresa> tutorEmpresaTable;

    @FXML
    private TableColumn<TutorEmpresa, String> correoColumn;

    @FXML
    private TableColumn<TutorEmpresa, String> telefonoColumn;

    @FXML
    private TableColumn<TutorEmpresa, String> apellidoColumn;

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

    public TutorEmpresaController(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tutorEmpresa/tutorEmpresaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        SplitPane.setResizableWithParent(tutorEmpresaModifyController.getRoot() , false);

        modifyButton.setDisable(true);
        removeButton.setDisable(true);

        // binding

        tutorEmpresaTable.itemsProperty().bind(tutores);
        tutorSeleccionado.bind(tutorEmpresaTable.getSelectionModel().selectedItemProperty());
        tutorEmpresaTable.disableProperty().bind(Bindings.createBooleanBinding(this::onSplitPaneChanged, splitTutorEmpresa.getItems()));
        tutorEmpresaModifyController.getEmpresaCombo().itemsProperty().bind(empresas);


        tutorSeleccionado.addListener((o , ov , nv) -> {
            modifyButton.setDisable(nv == null);
            removeButton.setDisable(nv == null);
        });

        tutorEmpresaTable.disableProperty().addListener((o , ov , nv) -> {
            modifyButton.setDisable(nv);
            removeButton.setDisable(nv);
            createButton.setDisable(nv);
            searchButton.setDisable(nv);
            searchAllButton.setDisable(nv);
        });


        // cell values factories

        empresaColumn.setCellValueFactory(v -> v.getValue().nombreEmpresaProperty());
        nombreColumn.setCellValueFactory(v -> v.getValue().nombreProperty());
        apellidoColumn.setCellValueFactory(v -> v.getValue().apellidosProperty());
        correoColumn.setCellValueFactory(v -> v.getValue().correoProperty());
        telefonoColumn.setCellValueFactory(v -> v.getValue().telefonoProperty());

        buscarTutores(getSearchQuery(""),"");
    }

    private Boolean onSplitPaneChanged() {
        if (splitTutorEmpresa.getItems().size() == 2){
            return true;
        }
        return false;
    }

    @FXML
    void onAddAction(ActionEvent event) {
        TutorEmpresaCreateDialog createDialog = new TutorEmpresaCreateDialog();
        seleccionarEmpresas();
        createDialog.getEmpresaCombo().getItems().setAll(empresas);
        Optional<TutorEmpresa> result = createDialog.showAndWait();
        if (result.isPresent()){
            TutorEmpresa tutor = result.get();

            // Antes de hacer el insert se comprueba que ni el nombre ni el teléfono estén vacíos.

            if (tutor.getNombre().trim().isEmpty()) {
                mostrarError("El nombre del tutor no puede estar vacío.");
                throw new IllegalArgumentException("El nombre del tutor no puede estar vacío.");
            }
            if (tutor.getTelefono().trim().isEmpty()) {
                mostrarError("El teléfono no puede estar vacío.");
                throw new IllegalArgumentException("El teléfono no puede estar vacío.");
            }

            String query = "Insert into TutorEmpresa (IdEmpresa, NombreTE, ApellidoTE, CorreoTE, TelefonoTE) VALUES ( ?, ?, ?, ?, ?)";
            try (Connection connection = HikariConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)){

                statement.setInt(1 , tutor.getIdEmpresa());
                statement.setString(2 , tutor.getNombre());
                statement.setString(3 , tutor.getApellidos());
                statement.setString(4 , tutor.getCorreo());
                statement.setString(5 , tutor.getTelefono());

                statement.execute();

            }  catch (SQLException e) {
                mostrarError(e.getLocalizedMessage());
                throw new RuntimeException(e);
            }
            tutor.setId(buscarId(tutor.getTelefono()));
            tutores.add(tutor);
        }
    }

    @FXML
    void onDeleteAction(ActionEvent event) {

        int idTutor = tutorSeleccionado.get().getId();
        String deleteQuery = "Delete from TutorEmpresa where IdTE = ?";
        String updateQuery = "Update Alumno set IDTutorE = null where IDTutorE = ?";

        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            updateStatement.setInt(1 , idTutor);
            updateStatement.execute();

            deleteStatement.setInt(1, idTutor);
            deleteStatement.execute();
            tutores.remove(tutorSeleccionado.get());

        } catch (SQLException e) {
            mostrarError(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onModifyAction(ActionEvent event) {

        seleccionarEmpresas();

        // se le pasa al controlador de modificar los datos del registro seleccionado

        tutorEmpresaModifyController.getTutor().setId(tutorSeleccionado.get().getId());
        tutorEmpresaModifyController.getTutor().setNombre(tutorSeleccionado.get().getNombre());
        tutorEmpresaModifyController.getTutor().setApellidos(tutorSeleccionado.get().getApellidos());
        tutorEmpresaModifyController.getTutor().setCorreo(tutorSeleccionado.get().getCorreo());
        tutorEmpresaModifyController.getTutor().setTelefono(tutorSeleccionado.get().getTelefono());

        Optional<Empresa> matchingEmpresa = tutorEmpresaModifyController.getEmpresaCombo()
                .getItems()
                .stream()
                .filter(empresa -> empresa.getNombre().equals(tutorSeleccionado.get().getNombreEmpresa()))
                .findFirst();
        matchingEmpresa.ifPresent(empresa ->
                tutorEmpresaModifyController.getEmpresaCombo().getSelectionModel().select(empresa)
        );

        splitTutorEmpresa.getItems().add(tutorEmpresaModifyController.getRoot());
    }

    @FXML
    void onSearchAllAction(ActionEvent event) {
        buscarTutores(getSearchQuery("") , "");
    }

    @FXML
    void onSearchAction(ActionEvent event) {
        TutorEmpresaSearchDialog searchDialog = new TutorEmpresaSearchDialog();
        Optional<String> result = searchDialog.showAndWait();
        if (result.isPresent()) {
            String campo = result.get();
            TextInputDialog campoDialog = new TextInputDialog();
            campoDialog.setHeaderText("Introduzca el " + campo.toLowerCase());
            campoDialog.setContentText(campo + ":");
            Optional<String> parametroResult = campoDialog.showAndWait();

            if (parametroResult.isPresent()){
                String parametro = parametroResult.get();
                buscarTutores( getSearchQuery(campo) , "%" + parametro + "%");
            }
        }

    }

    public void buscarTutores(String query , String parametro){
        tutores.clear();
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1 , parametro);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){

                TutorEmpresa tutor = new TutorEmpresa();
                tutor.setId(resultSet.getInt("TutorEmpresa.IdTE"));
                tutor.setIdEmpresa(resultSet.getInt("Empresa.IdEmpresa"));
                tutor.setNombreEmpresa(resultSet.getString("Empresa.NombreEmpresa"));
                tutor.setNombre(resultSet.getString("TutorEmpresa.NombreTE"));
                tutor.setApellidos(resultSet.getString("TutorEmpresa.ApellidoTE"));
                tutor.setCorreo(resultSet.getString("TutorEmpresa.CorreoTE"));
                tutor.setTelefono(resultSet.getString("TutorEmpresa.TelefonoTE"));

                tutores.add(tutor);
            }
        } catch (SQLException e) {
            mostrarError(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public String getSearchQuery(String opcion) {
        String condicion = switch (opcion) {
            case "Nombre empresa" -> "WHERE NombreEmpresa LIKE ?";
            case "Nombre" -> "WHERE NombreTE LIKE ?";
            case "Apellido" -> "WHERE ApellidoTE LIKE ?";
            case "Correo" -> "WHERE CorreoTE LIKE ?";
            case "Teléfono" -> "WHERE TelefonoTE LIKE ?";
            default -> "";
        };


        return "Select Empresa.IdEmpresa , Empresa.NombreEmpresa , TutorEmpresa.* from TutorEmpresa inner join Empresa on Empresa.IdEmpresa = TutorEmpresa.IdEmpresa " + condicion;
    }

    public int buscarId(String telefono){
        String query = "Select IdTE from TutorEmpresa where TelefonoTE = ?";
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1 , telefono);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("IdTE");

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

    public SplitPane getSplitTutorEmpresa() {
        return splitTutorEmpresa;
    }

    public TutorEmpresa getTutorSeleccionado() {
        return tutorSeleccionado.get();
    }

    public ObjectProperty<TutorEmpresa> tutorSeleccionadoProperty() {
        return tutorSeleccionado;
    }

    public void setTutorSeleccionado(TutorEmpresa tutorSeleccionado) {
        this.tutorSeleccionado.set(tutorSeleccionado);
    }

    public TutorEmpresaModifyController getTutorEmpresaModifyController() {
        return tutorEmpresaModifyController;
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