package dad.gestion_fct.controllers.alumno;

import dad.gestion_fct.HikariConnection;
//import dad.gestion_fct.models.*;

import dad.gestion_fct.models.Alumno;
import dad.gestion_fct.models.Ciclos;
import dad.gestion_fct.models.Docente;
import dad.gestion_fct.models.TutorEmpresa;
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

public class AlumnoController implements Initializable {

    // Model
    private ListProperty<Alumno> listaAlumno = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<Alumno> selectedAlumno = new SimpleObjectProperty<>();
    private ModifiedAlumnoController modifiedAlumnoController = new ModifiedAlumnoController(this);
    private boolean modificar = false;


    // View

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Bindings

        selectedAlumno.bind(alumnoTable.getSelectionModel().selectedItemProperty());

        // Cell bindings

        cialAlumnoColumn.setCellValueFactory(v -> v.getValue().cialAlumnoProperty());
        nombreAlumColumn.setCellValueFactory(v -> v.getValue().nombreAlumnoProperty());
        apellidoAlumColumn.setCellValueFactory(v -> v.getValue().apellidoAlumnoProperty());
        cicloAlumColumn.setCellValueFactory(v -> v.getValue().cicloAlumnoProperty());
        nussAlumColumn.setCellValueFactory(v -> v.getValue().nussAlumnoProperty());
        docenteAlumColumn.setCellValueFactory(v -> v.getValue().nombreDocenteProperty());
        tutorAlumColumn.setCellValueFactory(v -> v.getValue().tutorEmpresaProperty());

        // Table binding

        alumnoTable.itemsProperty().bind(listaAlumno);

        //Listener de selectedAlumno

        selectedAlumno.addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                // Actualizar los datos del controlador modificado
                modifiedAlumnoController.getAlumnoModify().setIdAlumno(newValue.getIdAlumno());
                modifiedAlumnoController.getAlumnoModify().setCialAlumno(newValue.getCialAlumno());
                modifiedAlumnoController.getAlumnoModify().setNombreAlumno(newValue.getNombreAlumno());
                modifiedAlumnoController.getAlumnoModify().setApellidoAlumno(newValue.getApellidoAlumno());
                modifiedAlumnoController.getAlumnoModify().setNussAlumno(newValue.getNussAlumno());

                // Actualizar ciclo
                modifiedAlumnoController.getCicloComboBox()
                        .getItems()
                        .stream()
                        .filter(ciclo -> ciclo.toString().equals(newValue.getCicloAlumno()))
                        .findFirst()
                        .ifPresent(ciclo ->
                                modifiedAlumnoController.getCicloComboBox().getSelectionModel().select(ciclo)
                        );

                // Actualizar docente
                modifiedAlumnoController.getDocenteComboBox()
                        .getItems()
                        .stream()
                        .filter(docente -> docente.getNombreDocente().equals(newValue.getNombreDocente()))
                        .findFirst()
                        .ifPresent(docente ->
                                modifiedAlumnoController.getDocenteComboBox().getSelectionModel().select(docente)
                        );

                // Actualizar tutor
                modifiedAlumnoController.getTutorComboBox()
                        .getItems()
                        .stream()
                        .filter(tutor -> tutor.getNombre().equals(newValue.getTutorEmpresa()))
                        .findFirst()
                        .ifPresent(tutor ->
                                modifiedAlumnoController.getTutorComboBox().getSelectionModel().select(tutor)
                        );
            }
        });

        // Buttons listener

        selectedAlumno.addListener((o , ov ,nv) -> {
            if (!modificar) {
                modifyButton.setDisable(nv == null);
                deleteButton.setDisable(nv == null);
            }
        });

        modifyButton.setDisable(true);
        deleteButton.setDisable(true);
        buscarAlumno("","");

        SplitPane.setResizableWithParent(modifiedAlumnoController.getRoot() , false);

    }

    public AlumnoController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alumno/alumnoView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private BorderPane root;

    @FXML
    private TableView<Alumno> alumnoTable;

    @FXML
    private TableColumn<Alumno, String> cialAlumnoColumn;

    @FXML
    private TableColumn<Alumno, String> nombreAlumColumn;

    @FXML
    private TableColumn<Alumno, String> apellidoAlumColumn;

    @FXML
    private TableColumn<Alumno, String> cicloAlumColumn;

    @FXML
    private TableColumn<Alumno, String> nussAlumColumn;

    @FXML
    private TableColumn<Alumno, String> docenteAlumColumn;

    @FXML
    private TableColumn<Alumno, String> tutorAlumColumn;

    @FXML
    private SplitPane splitAlumno;

    @FXML
    private Button createButton;

    @FXML
    private Button modifyButton;

    @FXML
    private Button deleteButton;

    @FXML
    void onCreateStudentAction(ActionEvent event) {

        CreateAlumnoDialog createDialog = new CreateAlumnoDialog();
        Optional<Alumno> result = createDialog.showAndWait();
        result.ifPresent(alumno -> {
             alumno = result.get();

            // Antes de hacer el insert se comprueba que ni el nif ni el nombre estén vacíos.

            if (alumno.getCialAlumno().trim().isEmpty()) {
                mostrarError("El cial del alumno no puede estar vacío.");
                throw new IllegalArgumentException("El cial del alumno no puede estar vacío.");
            }
            if (alumno.getNombreAlumno().trim().isEmpty()) {
                mostrarError("El nombre del alumno no puede estar vacío.");
                throw new IllegalArgumentException("El nombre del alumno no puede estar vacío.");
            }
            if (alumno.getApellidoAlumno().trim().isEmpty()) {
                mostrarError("El apellido del alumno no puede estar vacío.");
                throw new IllegalArgumentException("El apellido del alumno no puede estar vacío.");
            }
            if (alumno.getCicloAlumno().trim().isEmpty()) {
                mostrarError("El ciclo del alumno no puede estar vacío.");
                throw new IllegalArgumentException("El ciclo del alumno no puede estar vacío.");
            }
            if (alumno.getIdDocente() == null) {
                mostrarError("El docente del alumno debe ser seleccionado.");
                throw new IllegalArgumentException("El docente del alumno debe ser seleccionado.");
            }

            String query = "Insert into alumno (CIALAlumno, NombreAlumno, ApellidoAlumno, CicloAlumno, NussAlumno, IdDocente, IDTutorE) VALUES ( ?, ?, ?, ?, ?, ?, ?)";
            try (Connection connection = HikariConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)){

                statement.setString(1 , alumno.getCialAlumno());
                statement.setString(2 , alumno.getNombreAlumno());
                statement.setString(3 , alumno.getApellidoAlumno());
                statement.setString(4 , alumno.getCicloAlumno());
                statement.setString(5 , alumno.getNussAlumno());
                if (alumno.getNussAlumno() != null) {
                    statement.setString(5, alumno.getNussAlumno());
                } else {
                    statement.setNull(5, java.sql.Types.VARCHAR);
                }
                statement.setInt(6, alumno.getIdDocente());
                if (alumno.getIdTutor() != null && alumno.getIdTutor() != 0) {
                    statement.setInt(7, alumno.getIdTutor());
                } else {
                    statement.setNull(7, java.sql.Types.INTEGER);
                }
                statement.execute();

            }  catch (SQLException e) {
                mostrarError(e.getLocalizedMessage());
                throw new RuntimeException(e);
            }
            listaAlumno.add(alumno);
        });
    }

    @FXML
    void onSearchStudentAction(ActionEvent event) {
        SearchAlumnoDialog searchDialog = new SearchAlumnoDialog();
        Optional<String> campo = searchDialog.showAndWait();
        if (!campo.get().equals("")){
            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setHeaderText("Introduzca el " + campo.get());
            nameDialog.setContentText(campo.get() + ": ");
            Optional<String> result = nameDialog.showAndWait();
            result.ifPresent(value -> buscarAlumno(campo.get(), "%" + value + "%"));
        }
    }

    @FXML
    void onSearchAllStudentAction(ActionEvent event) {
        buscarAlumno("","");
    }

    @FXML
    void onModifiedStudentAction(ActionEvent event) {
        modificar = true;
        createButton.setDisable(true);
        modifyButton.setDisable(true);
        deleteButton.setDisable(true);

        modifiedAlumnoController.addRemoveCombBox();
        Optional<Ciclos> matchingCiclo = modifiedAlumnoController.getCicloComboBox()
                .getItems()
                .stream()
                .filter(ciclo -> ciclo.toString().equals(selectedAlumno.get().getCicloAlumno()))
                .findFirst();
        matchingCiclo.ifPresent(ciclo ->
                modifiedAlumnoController.getCicloComboBox().getSelectionModel().select(ciclo)
        );
        Optional<Docente> matchingDocente = modifiedAlumnoController.getDocenteComboBox()
                .getItems()
                .stream()
                .filter(docente -> docente.getNombreDocente().equals(selectedAlumno.get().getNombreDocente()))
                .findFirst();
        matchingDocente.ifPresent(docente ->
                modifiedAlumnoController.getDocenteComboBox().getSelectionModel().select(docente)
        );
        Optional<TutorEmpresa> matchingTutor = modifiedAlumnoController.getTutorComboBox()
                .getItems()
                .stream()
                .filter(tutor -> tutor.getNombre().equals(selectedAlumno.get().getTutorEmpresa()))
                .findFirst();
        matchingTutor.ifPresent(tutor ->
                modifiedAlumnoController.getTutorComboBox().getSelectionModel().select(tutor)
        );
        splitAlumno.getItems().add(modifiedAlumnoController.getRoot());
    }

    @FXML
    void onDeleteStudentAction(ActionEvent event) {
        eliminarAlumno();
    }

    public SplitPane getSplitAlumno() {
        return splitAlumno;
    }

    public BorderPane getRoot() {
        return root;
    }

    public Alumno getSelectedAlumno() {
        return selectedAlumno.get();
    }

    public ObjectProperty<Alumno> selectedAlumnoProperty() {
        return selectedAlumno;
    }

    public Button getCreateButton() {
        return createButton;
    }

    public Button getModifyButton() {
        return modifyButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setModificar(boolean modificar) {
        this.modificar = modificar;
    }

    public void buscarAlumno(String opcion, String parametro){

        listaAlumno.clear();
        String query = getString(opcion);
        try (Connection connection = HikariConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, parametro);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                Alumno alumno = new Alumno();
                alumno.setIdAlumno(resultSet.getInt("IdAlumno"));
                alumno.setCialAlumno(resultSet.getString("CIALAlumno"));
                alumno.setNombreAlumno(resultSet.getString("NombreAlumno"));
                alumno.setApellidoAlumno(resultSet.getString("ApellidoAlumno"));
                alumno.setCicloAlumno(resultSet.getString("CicloAlumno"));
                alumno.setNussAlumno(resultSet.getString("NussAlumno"));
                alumno.setNombreDocente(resultSet.getString("tutordocente.NombreDocente"));
                alumno.setIdDocente(resultSet.getInt("tutordocente.IdDocente"));
                alumno.setTutorEmpresa(resultSet.getString("tutorempresa.NombreTE"));
                alumno.setIdTutor(resultSet.getInt("tutorempresa.IdTE"));


                listaAlumno.add(alumno);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getString(String opcion) {
        String condicion = switch (opcion) {
            case "Cial" -> "WHERE CIALAlumno LIKE ?";
            case "Nombre" -> "WHERE NombreAlumno LIKE ?";
            case "Apellido" -> "WHERE ApellidoAlumno LIKE ?";
            case "Ciclo" -> "WHERE CicloAlumno LIKE ?";
            case "Nuss" -> "WHERE NussAlumno LIKE ?";
            case "Docente" -> "WHERE tutordocente.NombreDocente LIKE ?";
            case "Tutor Empresa" -> "WHERE tutorempresa.NombreTE LIKE ?";
            default -> "WHERE 1 = 1";
        };


        String query = "SELECT IdAlumno, CIALAlumno, NombreAlumno, ApellidoAlumno, CicloAlumno, NussAlumno, tutordocente.NombreDocente, tutordocente.IdDocente," +
                "tutorempresa.NombreTE, tutorempresa.IdTE FROM alumno INNER JOIN tutordocente on " +
                "alumno.IdDocente = tutordocente.IdDocente INNER JOIN tutorempresa on " +
                "alumno.IDTutorE = tutorempresa.IdTE " + condicion ;
        return query;
    }

    public void eliminarAlumno(){
        int alumnoEliminado = selectedAlumno.get().getIdAlumno();

        Alert alertEliminar = new Alert(Alert.AlertType.WARNING);
        alertEliminar.setTitle("Eliminación de alumno");
        alertEliminar.setHeaderText("Si eliminas el alumno tambien eliminará " +
                "en otras tablas.");
        alertEliminar.setContentText("Tablas afectadas :\n" +
                "- Registro de visitas");

        ButtonType botonAfirmativo = new ButtonType("Borrar");
        ButtonType botonNegativo = new ButtonType("Cancelar");

        alertEliminar.getButtonTypes().setAll(botonAfirmativo , botonNegativo);
        Optional<ButtonType> result = alertEliminar.showAndWait();

        if (result.get() == botonAfirmativo){
            String[] tablas = {"registrovisitas", "alumno"};
            try (Connection connection = HikariConnection.getConnection()) {
                for (String tabla : tablas) {
                    String query = "Delete from " + tabla + " where IdAlumno = ?";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setInt(1, alumnoEliminado);
                        statement.execute();
                    }
                }
                listaAlumno.remove(selectedAlumno.get());
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
