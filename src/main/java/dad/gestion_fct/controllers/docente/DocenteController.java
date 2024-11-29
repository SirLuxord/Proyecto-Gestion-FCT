package dad.gestion_fct.controllers.docente;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.controllers.alumno.CreateAlumnoDialog;
import dad.gestion_fct.models.Alumno;
import dad.gestion_fct.models.Docente;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class DocenteController implements Initializable {


    // Model
    private ListProperty<Docente> listaDocentes = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<Docente> selectedDocente = new SimpleObjectProperty<>();
    private ModifiedDocenteController modifiedDocenteController = new ModifiedDocenteController(this);
    private boolean modificar = false;

    // View

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Bindings

        selectedDocente.bind(docenteTable.getSelectionModel().selectedItemProperty());

        // Cell bindings

        nombreDocColumn.setCellValueFactory(v -> v.getValue().nombreDocenteProperty());
        apellidoDocColumn.setCellValueFactory(v -> v.getValue().apellidoDocenteProperty());
        TelDocColumn.setCellValueFactory(v -> v.getValue().emailDocenteProperty());
        emailDocColumn.setCellValueFactory(v -> v.getValue().telefonoDocenteProperty());

        // Table binding

        docenteTable.itemsProperty().bind(listaDocentes);

        //Listener de selectedDocente

        selectedDocente.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Actualizar los datos del controlador modificado
                modifiedDocenteController.getDocenteModify().setIdDocente(newValue.getIdDocente());
                modifiedDocenteController.getDocenteModify().setNombreDocente(newValue.getNombreDocente());
                modifiedDocenteController.getDocenteModify().setApellidoDocente(newValue.getApellidoDocente());
                modifiedDocenteController.getDocenteModify().setEmailDocente(newValue.getEmailDocente());
                modifiedDocenteController.getDocenteModify().setTelefonoDocente(newValue.getTelefonoDocente());
            }
        });

        // Buttons listener

        selectedDocente.addListener((o , ov ,nv) -> {
            if (!modificar) {
                modifyButton.setDisable(nv == null);
                deleteButton.setDisable(nv == null);
            }
        });

        modifyButton.setDisable(true);
        deleteButton.setDisable(true);

        buscarDocente("","");
        SplitPane.setResizableWithParent(modifiedDocenteController.getRoot() , false);
    }

    public DocenteController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/docente/docenteView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TableView<Docente> docenteTable;

    @FXML
    private TableColumn<Docente, String> nombreDocColumn;

    @FXML
    private TableColumn<Docente, String> apellidoDocColumn;

    @FXML
    private TableColumn<Docente, String> TelDocColumn;

    @FXML
    private TableColumn<Docente, String> emailDocColumn;

    @FXML
    private Button createButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button modifyButton;

    @FXML
    private BorderPane root;

    @FXML
    private SplitPane splitDocente;

    @FXML
    void onCreateDocAction(ActionEvent event) {

        CreateDocenteDialog createDialog = new CreateDocenteDialog();
        Optional<Docente> result = createDialog.showAndWait();
        result.ifPresent(docente -> {
            docente = result.get();

            // Antes de hacer el insert se comprueba que los cambios obligatorios no estén vacíos.

            if (docente.getNombreDocente().trim().isEmpty()) {
                mostrarError("El nombre del docente no puede estar vacío.");
                throw new IllegalArgumentException("El nombre del docente no puede estar vacío.");
            }
            if (docente.getApellidoDocente().trim().isEmpty()) {
                mostrarError("El apellido del docente no puede estar vacío.");
                throw new IllegalArgumentException("El apellido del docente no puede estar vacío.");
            }
            if (docente.getTelefonoDocente().trim().isEmpty()) {
                mostrarError("El télefono del docente no puede estar vacío.");
                throw new IllegalArgumentException("El ciclo del docente no puede estar vacío.");
            }

            String query = "Insert into tutordocente (NombreDocente, ApellidoDocente, EmailDocente, TelefonoDocente) VALUES ( ?, ?, ?, ? )";
            try (Connection connection = HikariConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)){

                statement.setString(1 , docente.getNombreDocente());
                statement.setString(2 , docente.getApellidoDocente());
                if (docente.getEmailDocente() != null) {
                    statement.setString(3 , docente.getEmailDocente());
                } else {
                    statement.setNull(3, java.sql.Types.VARCHAR);
                }
                statement.setString(4 , docente.getTelefonoDocente());
                statement.execute();

            }  catch (SQLException e) {
                mostrarError(e.getLocalizedMessage());
                throw new RuntimeException(e);
            }
            listaDocentes.add(docente);
        });


    }

    @FXML
    void onDeleteDocAction(ActionEvent event) {
        eliminarDocente();
    }

    @FXML
    void onModifiedDocAction(ActionEvent event) {
        modificar = true;
        createButton.setDisable(true);
        modifyButton.setDisable(true);
        deleteButton.setDisable(true);
        splitDocente.getItems().add(modifiedDocenteController.getRoot());
    }

    @FXML
    void onSearchDocAction(ActionEvent event) {
        SearchDocenteDialog searchDialog = new SearchDocenteDialog();
        Optional<String> campo = searchDialog.showAndWait();
        if (!campo.get().equals("")){
            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setHeaderText("Introduzca el " + campo.get());
            nameDialog.setContentText(campo.get() + ": ");
            Optional<String> result = nameDialog.showAndWait();
            result.ifPresent(value -> buscarDocente(campo.get(), "%" + value + "%"));
        }
    }

    @FXML
    void onSearchAllDocAction(ActionEvent event) {
        buscarDocente("","");
    }

    public Button getCreateButton() {
        return createButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getModifyButton() {
        return modifyButton;
    }

    public SplitPane getSplitDocente() {
        return splitDocente;
    }

    public Docente getSelectedDocente() {
        return selectedDocente.get();
    }

    public ObjectProperty<Docente> selectedDocenteProperty() {
        return selectedDocente;
    }

    public void setSelectedDocente(Docente selectedDocente) {
        this.selectedDocente.set(selectedDocente);
    }

    public void setModificar(boolean modificar) {
        this.modificar = modificar;
    }

    public BorderPane getRoot() {
        return root;
    }

    public void buscarDocente(String opcion, String parametro){

        listaDocentes.clear();
        String query = getString(opcion);
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, parametro);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Docente docente = new Docente();
                docente.setIdDocente(resultSet.getInt("IdDocente"));
                docente.setNombreDocente(resultSet.getString("NombreDocente"));
                docente.setApellidoDocente(resultSet.getString("ApellidoDocente"));
                docente.setEmailDocente(resultSet.getString("EmailDocente"));
                docente.setTelefonoDocente(resultSet.getString("TelefonoDocente"));
                listaDocentes.add(docente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getString(String opcion) {
        String condicion = switch (opcion) {
            case "Nombre" -> "WHERE NombreDocente LIKE ?";
            case "Apellido" -> "WHERE ApellidoDocente LIKE ?";
            case "Email" -> "WHERE EmailDocente LIKE ?";
            case "Teléfono" -> "WHERE TelefonoDocente LIKE ?";
            default -> "WHERE 1 = 1";
        };


        String query = "SELECT IdDocente, NombreDocente, ApellidoDocente, EmailDocente, TelefonoDocente" +
                " FROM tutordocente " + condicion ;
        return query;
    }

    public void eliminarDocente(){
        int docenteEliminado = selectedDocente.get().getIdDocente();

        Alert alertEliminar = new Alert(Alert.AlertType.WARNING);
        alertEliminar.setTitle("Eliminación de docente");
        alertEliminar.setHeaderText("Si borrás el registro se borrarán " +
                "los registros asociados.");
        alertEliminar.setContentText("Tablas afectadas :\n" +
                "- Comentarios captacion de empresa");
        ButtonType botonAfirmativo = new ButtonType("Borrar");
        ButtonType botonNegativo = new ButtonType("Cancelar");

        alertEliminar.getButtonTypes().setAll(botonAfirmativo , botonNegativo);
        Optional<ButtonType> result = alertEliminar.showAndWait();

        if (result.get() == botonAfirmativo){

            try (Connection connection = HikariConnection.getConnection()) {
                String[] tablas = {"comentarioscaptacionempresa", "tutordocente"};
                for (String tabla : tablas) {
                    String query = "Delete from " + tabla + " where IdDocente = ?";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setInt(1, docenteEliminado);
                        statement.execute();
                    }
                }
                listaDocentes.remove(selectedDocente.get());

            } catch (SQLException e) {
                mostrarError(e.getLocalizedMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public void mostrarError(String error){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText("Ha habido algún error");
        errorAlert.setContentText("Error: " + error);
        errorAlert.show();
    }

}
