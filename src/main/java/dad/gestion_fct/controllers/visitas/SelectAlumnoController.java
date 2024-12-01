package dad.gestion_fct.controllers.visitas;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.controllers.alumno.SearchAlumnoDialog;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class SelectAlumnoController implements Initializable {

    // Model
    private ListProperty<Alumno> listaAlumno = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<Alumno> selectedAlumno = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    public SelectAlumnoController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/visitas/alumnoSelectView.fxml"));
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
    void onSearchAllStudentAction(ActionEvent event) {
        buscarAlumno("","");
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
    void onSelectAction(ActionEvent event) {
        Alumno alumno = new Alumno();

        alumno.setIdAlumno(selectedAlumno.get().getIdAlumno());
        alumno.setNombreAlumno(selectedAlumno.get().getNombreAlumno());
        alumno.setApellidoAlumno(selectedAlumno.get().getApellidoAlumno());
        alumno.setNombreDocente(selectedAlumno.get().getNombreDocente());
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onCancelAction(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
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

    public Alumno getSelectedAlumno() {
        return selectedAlumno.get();
    }

    public ObjectProperty<Alumno> selectedAlumnoProperty() {
        return selectedAlumno;
    }

    public BorderPane getRoot() {
        return root;
    }
}
