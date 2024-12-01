package dad.gestion_fct.controllers.visitas;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.models.Alumno;
import dad.gestion_fct.models.Visita;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateVisitaController implements Initializable {

    private ObjectProperty<Visita> visita = new SimpleObjectProperty<>(new Visita());
    private final StringProperty nombreCompleto = new SimpleStringProperty();
    private VisitaController visitaController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        visita.get().fechaVisitaProperty().bind(fechaDatePicker.valueProperty());
        visita.get().observacionProperty().bind(observacionTextArea.textProperty());
    }

    public CreateVisitaController(VisitaController visitaController) {
        try{
            this.visitaController = visitaController;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/visitas/modifiedVisitaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private DatePicker fechaDatePicker;

    @FXML
    private TextField alumnoTextField;

    @FXML
    private TextArea observacionTextArea;

    @FXML
    private BorderPane root;

    @FXML
    void onSelectAction(ActionEvent event) {
        // Crea la nueva ventana
        Stage secondaryStage = new Stage();
        secondaryStage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana principal mientras esta está abierta

        // Crea el controlador para la ventana secundaria
        SelectAlumnoController selectAlumnoController = new SelectAlumnoController();

        // Configura la escena con el contenido de la ventana secundaria
        Scene scene = new Scene(selectAlumnoController.getRoot());
        secondaryStage.setScene(scene);

        // Muestra la ventana secundaria y espera hasta que se cierre
        secondaryStage.showAndWait();

        // Recupera el objeto seleccionado
        Alumno alumnoSeleccionado = selectAlumnoController.getSelectedAlumno();
        if (alumnoSeleccionado != null) {
            alumnoTextField.textProperty().unbindBidirectional(nombreCompleto);
            alumnoTextField.textProperty().set(alumnoSeleccionado.getNombreAlumno().concat(" ").concat(alumnoSeleccionado.getApellidoAlumno()));
            visita.get().setIdAlumno(alumnoSeleccionado.getIdAlumno());
            visita.get().setNombreAlumno(alumnoSeleccionado.getNombreAlumno());
            visita.get().setApellidoAlumno(alumnoSeleccionado.getApellidoAlumno());
            visita.get().setNombreDocente(alumnoSeleccionado.getNombreDocente());
            visita.get().setApellidoDocente(alumnoSeleccionado.getApellidoAlumno());
        }
    }

    @FXML
    void onCancelAction(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onConfirmAction(ActionEvent event) {

        if (fechaDatePicker.getValue() == null) {
            visitaController.mostrarError("La fecha no puede estar vacía.");
            throw new IllegalArgumentException("La fecha no puede estar vacía.");
        }
        if (alumnoTextField.getText().isEmpty()) {
            visitaController.mostrarError("El alumno no puede estar vacío.");
            throw new IllegalArgumentException("El alumno no puede estar vacío.");
        }
        if (observacionTextArea.getText().isEmpty()) {
            visitaController.mostrarError("La observación del docente no puede estar vacía.");
            throw new IllegalArgumentException("La observación del docente no puede estar vacía.");
        }

        String query = "Insert into registrovisitas (FechaVisita, IdAlumno, Observaciones) VALUES ( ?, ?, ?)";
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.setDate(1 , java.sql.Date.valueOf(visita.get().getFechaVisita()));
            statement.setInt(2 , visita.get().getIdAlumno());
            statement.setString(3 , visita.get().getObservacion());

            statement.execute();

        }  catch (SQLException e) {
            visitaController.mostrarError(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        visitaController.getListaVisitas().add(visita.get());

        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    public BorderPane getRoot() {
        return root;
    }
}
