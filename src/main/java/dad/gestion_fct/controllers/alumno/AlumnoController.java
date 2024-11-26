package dad.gestion_fct.controllers.alumno;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.models.Alumno;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class AlumnoController implements Initializable {

    // Model
    private ListProperty<Alumno> listaAlumno = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<Alumno> selectedAlumno = new SimpleObjectProperty<>();
    private ModifiedAlumnoController modifiedAlumnoController = new ModifiedAlumnoController(this);

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

        buscarAlumno("","");


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
    void onCreateStudentAction(ActionEvent event) {

    }

    @FXML
    void onSearchStudentAction(ActionEvent event) {
        SearchAlumnoDialog searchDialog = new SearchAlumnoDialog();
        Optional<String> cial = searchDialog.showAndWait();
//        cial.ifPresent(value -> buscarAlumno("1", "%" + value + "%"));

    }

    @FXML
    void onSearchAllStudentAction(ActionEvent event) {
        buscarAlumno("","");
    }

    @FXML
    void onModifiedStudentAction(ActionEvent event) {

        splitAlumno.getItems().add(modifiedAlumnoController.getRoot());
        SplitPane.setResizableWithParent(modifiedAlumnoController.getRoot() , false);
    }

    @FXML
    void onDeleteStudentAction(ActionEvent event) {

    }

    public SplitPane getSplitAlumno() {
        return splitAlumno;
    }

    public BorderPane getRoot() {
        return root;
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

                alumno.setCialAlumno(resultSet.getString("CIALAlumno"));
                alumno.setNombreAlumno(resultSet.getString("NombreAlumno"));
                alumno.setApellidoAlumno(resultSet.getString("ApellidoAlumno"));
                alumno.setCicloAlumno(resultSet.getString("CicloAlumno"));
                alumno.setNussAlumno(resultSet.getString("NussAlumno"));
                alumno.setNombreDocente(resultSet.getString("tutordocente.NombreDocente"));
                alumno.setTutorEmpresa(resultSet.getString("tutorempresa.NombreTE"));

                listaAlumno.add(alumno);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getString(String opcion) {
        String condicion = switch (opcion) {
            case "1" -> "WHERE CIALAlumno LIKE ?";
            case "2" -> "WHERE NombreAlumno LIKE ?";
            case "3" -> "WHERE ApellidoAlumno LIKE ?";
            case "4" -> "WHERE CicloAlumno LIKE ?";
            case "5" -> "WHERE NussAlumno LIKE ?";
            case "6" -> "WHERE tutordocente.NombreDocente LIKE ?";
            case "7" -> "WHERE tutorempresa.NombreTE LIKE ?";
            default -> "WHERE 1 = 1";
        };


        String query = "SELECT CIALAlumno, NombreAlumno, ApellidoAlumno, CicloAlumno, NussAlumno, tutordocente.NombreDocente, " +
                "tutorempresa.NombreTE FROM alumno INNER JOIN tutordocente on " +
                "alumno.IdDocente = tutordocente.IdDocente INNER JOIN tutorempresa on " +
                "alumno.IDTutorE = tutorempresa.IdTE " + condicion ;
        return query;
    }
}
