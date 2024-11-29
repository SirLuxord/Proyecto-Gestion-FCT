package dad.gestion_fct.controllers.visitas;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.controllers.alumno.ModifiedAlumnoController;
import dad.gestion_fct.models.Alumno;
import dad.gestion_fct.models.Visita;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class VisitaController implements Initializable {

    // Model
    private ListProperty<Visita> listaVisitas = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<Visita> selectedVisita = new SimpleObjectProperty<>();
    private ObservacionController observacionController = new ObservacionController(this);
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

    }

    @FXML
    void onDeleteStudentAction(ActionEvent event) {

    }

    @FXML
    void onModifiedStudentAction(ActionEvent event) {

    }

    @FXML
    void onSearchAllStudentAction(ActionEvent event) {

    }

    @FXML
    void onSearchStudentAction(ActionEvent event) {

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

    public BorderPane getRoot() {
        return root;
    }

    public SplitPane getSplitVisita() {
        return splitVisita;
    }

    public void buscarAlumno(String opcion, String parametro){

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
}
