package dad.gestion_fct.controllers.visitas;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.controllers.alumno.CreateAlumnoDialog;
import dad.gestion_fct.controllers.alumno.ModifiedAlumnoController;
import dad.gestion_fct.controllers.alumno.SearchAlumnoDialog;
import dad.gestion_fct.models.Alumno;
import dad.gestion_fct.models.Visita;
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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class VisitaController implements Initializable {

    // Model
    private ListProperty<Visita> listaVisitas = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<Visita> selectedVisita = new SimpleObjectProperty<>();
    private ModifiedVisitaController modifiedVisitaController = new ModifiedVisitaController(this);
    private ObservacionController observacionController = new ObservacionController();
    private boolean modificar = false;

    // View

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Bindings

        selectedVisita.bind(visitasTable.getSelectionModel().selectedItemProperty());

        // Cell bindings

        fechaVisitaColumn.setCellValueFactory(v -> v.getValue().fechaVisitaProperty());
        nombreAlumColumn.setCellValueFactory(v -> v.getValue().nombreAlumnoProperty());
        apellidoAlumColumn.setCellValueFactory(v -> v.getValue().apellidoAlumnoProperty());
        docenteColumn.setCellValueFactory(v -> v.getValue().nombreDocenteProperty());
        apellDocColumn.setCellValueFactory(v -> v.getValue().apellidoDocenteProperty());
        observacionesColumn.setCellValueFactory(v -> v.getValue().observacionProperty());

        // Table binding

        visitasTable.itemsProperty().bind(listaVisitas);

        // Buttons listener

        selectedVisita.addListener((o , ov ,nv) -> {


            if (!modificar) {
                modifyButton.setDisable(nv == null);
                deleteButton.setDisable(nv == null);
                if (nv != null){
                    if (!splitVisita.getItems().contains(observacionController.getRoot())) {
                        splitVisita.getItems().add(observacionController.getRoot());
                    }
                    modifiedVisitaController.getVisitaModified().setIdAlumno(nv.getIdAlumno());
                    modifiedVisitaController.getVisitaModified().setFechaVisita(nv.getFechaVisita());
                    modifiedVisitaController.getVisitaModified().setObservacion(nv.getObservacion());
                    observacionController.setVisitaObservacion(nv);
                } else {
                    splitVisita.getItems().remove(observacionController.getRoot());
                    observacionController.setVisitaObservacion(new Visita());
                }
            }
        });

        modifyButton.setDisable(true);
        deleteButton.setDisable(true);
        buscarVisita("","");
        SplitPane.setResizableWithParent(observacionController.getRoot() , false);
        SplitPane.setResizableWithParent(modifiedVisitaController.getRoot() , false);
    }

    public VisitaController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/visitas/visitasView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TableView<Visita> visitasTable;

    @FXML
    private TableColumn<Visita, LocalDate> fechaVisitaColumn;

    @FXML
    private TableColumn<Visita, String> nombreAlumColumn;

    @FXML
    private TableColumn<Visita, String> apellidoAlumColumn;

    @FXML
    private TableColumn<Visita, String> docenteColumn;

    @FXML
    private TableColumn<Visita, String> apellDocColumn;

    @FXML
    private TableColumn<Visita, String> observacionesColumn;

    @FXML
    private Button createButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button modifyButton;

    @FXML
    private BorderPane root;

    @FXML
    private SplitPane splitVisita;

    @FXML
    void onCreateStudentAction(ActionEvent event) {
        // Crea la nueva ventana
        Stage secondaryStage = new Stage();
        secondaryStage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana principal mientras esta está abierta

        // Crea el controlador para la ventana secundaria
        CreateVisitaController createVisitaController = new CreateVisitaController(this);

        // Configura la escena con el contenido de la ventana secundaria
        Scene scene = new Scene(createVisitaController.getRoot());
        secondaryStage.setScene(scene);

        // Muestra la ventana secundaria y espera hasta que se cierre
        secondaryStage.showAndWait();
    }

    @FXML
    void onDeleteStudentAction(ActionEvent event) {
        eliminarAlumno();
    }

    @FXML
    void onModifiedStudentAction(ActionEvent event) {
        modificar = true;
        createButton.setDisable(true);
        modifyButton.setDisable(true);
        deleteButton.setDisable(true);
        splitVisita.getItems().remove(observacionController.getRoot());
        splitVisita.getItems().add(modifiedVisitaController.getRoot());
    }

    @FXML
    void onSearchAllStudentAction(ActionEvent event) {
        buscarVisita("","");
    }

    @FXML
    void onSearchStudentAction(ActionEvent event) {

        SearchVisitaDialog searchDialog = new SearchVisitaDialog();
        Optional<String> campo = searchDialog.showAndWait();
        if (!campo.get().equals("")){
            if (campo.get().equals("Fecha")) {

                // Diálogo personalizado
                Dialog<LocalDate> dateDialog = new Dialog<>();
                dateDialog.setTitle("Seleccionar Fecha");
                dateDialog.setHeaderText("Introduzca la fecha");

                // Añadimos el DatePicker al diálogo
                DatePicker datePicker = new DatePicker();
                dateDialog.getDialogPane().setContent(datePicker);

                // Añadir botones al diálogo
                ButtonType confirmButton = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
                dateDialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);

                // Procesar el resultado del DatePicker
                dateDialog.setResultConverter(dialogButton -> {
                    if (dialogButton == confirmButton) {
                        return datePicker.getValue();
                    }
                    return null;
                });

                Optional<LocalDate> dateResult = dateDialog.showAndWait();
                dateResult.ifPresent(date -> buscarVisita(campo.get(), date.toString()));

            } else {
                TextInputDialog nameDialog = new TextInputDialog();
                nameDialog.setHeaderText("Introduzca el " + campo.get());
                nameDialog.setContentText(campo.get() + ": ");
                Optional<String> result = nameDialog.showAndWait();
                result.ifPresent(value -> buscarVisita(campo.get(), "%" + value + "%"));
            }
        }

    }

    public Visita getSelectedVisita() {
        return selectedVisita.get();
    }

    public ObjectProperty<Visita> selectedVisitaProperty() {
        return selectedVisita;
    }

    public boolean isModificar() {
        return modificar;
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

    public ObservableList<Visita> getListaVisitas() {
        return listaVisitas.get();
    }

    public ListProperty<Visita> listaVisitasProperty() {
        return listaVisitas;
    }

    public void setModificar(boolean modificar) {
        this.modificar = modificar;
    }

    public BorderPane getRoot() {
        return root;
    }

    public SplitPane getSplitVisita() {
        return splitVisita;
    }

    public void buscarVisita(String opcion, String parametro){

        listaVisitas.clear();
        String query = getString(opcion);
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, parametro);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                Visita visita = new Visita();
                visita.setFechaVisita(resultSet.getObject("FechaVisita", java.time.LocalDate.class));
                visita.setIdAlumno(resultSet.getInt("alumno.IdAlumno"));
                visita.setNombreAlumno(resultSet.getString("alumno.NombreAlumno"));
                visita.setApellidoAlumno(resultSet.getString("alumno.ApellidoAlumno"));
                visita.setIdDocente(resultSet.getInt("tutordocente.IdDocente"));
                visita.setNombreDocente(resultSet.getString("tutordocente.NombreDocente"));
                visita.setApellidoDocente(resultSet.getString("tutordocente.ApellidoDocente"));
                visita.setObservacion(resultSet.getString("Observaciones"));
                listaVisitas.add(visita);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getString(String opcion) {
        String condicion = switch (opcion) {
            case "Fecha" -> "WHERE FechaVisita = ?";
            case "Alumno" -> "WHERE alumno.NombreAlumno LIKE ?";
            case "Apellido Alumno" -> "WHERE alumno.ApellidoAlumno LIKE ?";
            case "Docente" -> "WHERE tutordocente.NombreDocente LIKE ?";
            case "Apellido Docente" -> "WHERE tutordocente.ApellidoDocente LIKE ?";
            case "Observaciones" -> "WHERE Observaciones LIKE ?";
            default -> "WHERE 1 = 1";
        };


        String query = "SELECT FechaVisita, alumno.IdAlumno, alumno.NombreAlumno, alumno.ApellidoAlumno, tutordocente.IdDocente, tutordocente.NombreDocente, " +
                "tutordocente.ApellidoDocente, Observaciones FROM `registrovisitas` INNER JOIN alumno ON " +
                "alumno.IdAlumno = registrovisitas.IdAlumno INNER JOIN tutordocente ON tutordocente.IdDocente =" +
                " alumno.IdDocente " + condicion ;
        return query;
    }

    public void mostrarError(String error){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText("Ha habido algún error");
        errorAlert.setContentText("Error: " + error);
        errorAlert.show();
    }

    public void eliminarAlumno(){
        int idVisitaEliminada = selectedVisita.get().getIdAlumno();
        LocalDate fechaVisitaEliminada = selectedVisita.get().getFechaVisita();

        Alert alertEliminar = new Alert(Alert.AlertType.WARNING);
        alertEliminar.setTitle("Eliminación de visita");
        alertEliminar.setHeaderText("¿Estás seguro de eliminar este registro?.");
        ButtonType botonAfirmativo = new ButtonType("Borrar");
        ButtonType botonNegativo = new ButtonType("Cancelar");

        alertEliminar.getButtonTypes().setAll(botonAfirmativo , botonNegativo);
        Optional<ButtonType> result = alertEliminar.showAndWait();

        if (result.get() == botonAfirmativo){
            try (Connection connection = HikariConnection.getConnection()) {
                String query = "Delete from registrovisitas where IdAlumno = ? AND FechaVisita = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, idVisitaEliminada);
                    statement.setDate(2, java.sql.Date.valueOf(fechaVisitaEliminada));
                    statement.execute();
                }
                listaVisitas.remove(selectedVisita.get());
            } catch (SQLException e) {
                mostrarError(e.getLocalizedMessage());
                throw new RuntimeException(e);
            }
        }
    }

}
