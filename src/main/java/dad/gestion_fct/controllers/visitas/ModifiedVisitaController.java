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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ModifiedVisitaController implements Initializable {

    private final VisitaController visitaController;
    private ObjectProperty<Visita> visitaModified = new SimpleObjectProperty<>(new Visita());
    private final StringProperty nombreCompleto = new SimpleStringProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Bindings

        observacionTextArea.textProperty().bindBidirectional(visitaModified.get().observacionProperty());
        fechaDatePicker.valueProperty().bindBidirectional(visitaModified.get().fechaVisitaProperty());

        visitaController.selectedVisitaProperty().addListener((o, ov, nv) -> {
            if (ov != null) {
                alumnoTextField.textProperty().unbindBidirectional(nombreCompleto);
            }
            if (nv != null) {
                nombreCompleto.bind(
                        visitaController.getSelectedVisita().nombreAlumnoProperty()
                                .concat(" ")
                                .concat(visitaController.getSelectedVisita().apellidoAlumnoProperty())
                );
                alumnoTextField.textProperty().bindBidirectional(nombreCompleto);
            }
        });

    }

    public ModifiedVisitaController(VisitaController visitaController) {
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
            visitaModified.get().setIdAlumno(alumnoSeleccionado.getIdAlumno());
            visitaModified.get().setNombreAlumno(alumnoSeleccionado.getNombreAlumno());
            visitaModified.get().setApellidoAlumno(alumnoSeleccionado.getApellidoAlumno());
            visitaModified.get().setNombreDocente(alumnoSeleccionado.getNombreDocente());
            visitaModified.get().setApellidoDocente(alumnoSeleccionado.getApellidoAlumno());
        }
    }

    @FXML
    void onCancelAction(ActionEvent event) {
        visitaController.getSplitVisita().getItems().remove(this.getRoot());
        visitaController.getCreateButton().setDisable(false);
        visitaController.getModifyButton().setDisable(false);
        visitaController.getDeleteButton().setDisable(false);
        visitaController.setModificar(false);
    }


    @FXML
    void onConfirmAction(ActionEvent event) {
        int newIdAlumno = visitaModified.get().getIdAlumno();
        String newNombre = visitaModified.get().getNombreAlumno();
        String newApellido = visitaModified.get().getApellidoAlumno();
        String newDocenteNombre = visitaModified.get().getNombreDocente();
        String newDocenteApellido = visitaModified.get().getApellidoDocente();
        LocalDate fechaVisita  = fechaDatePicker.getValue();
        String observacion = observacionTextArea.getText();

        String query = "UPDATE registrovisitas SET IdAlumno = ?, FechaVisita = ?, Observaciones = ? WHERE IdAlumno = ? AND FechaVisita = ?";
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, newIdAlumno);
            preparedStatement.setDate(2, java.sql.Date.valueOf(fechaVisita));
            preparedStatement.setString(3, observacion);
            preparedStatement.setInt(4, visitaController.getSelectedVisita().getIdAlumno());
            preparedStatement.setDate(5, java.sql.Date.valueOf(visitaController.getSelectedVisita().getFechaVisita()));

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        visitaController.getSelectedVisita().setIdAlumno(newIdAlumno);
        visitaController.getSelectedVisita().setNombreAlumno(newNombre);
        visitaController.getSelectedVisita().setApellidoAlumno(newApellido);
        visitaController.getSelectedVisita().setNombreDocente(newDocenteNombre);
        visitaController.getSelectedVisita().setApellidoDocente(newDocenteApellido);
        visitaController.getSelectedVisita().setFechaVisita(fechaVisita);
        visitaController.getSelectedVisita().setObservacion(observacion);

    }

    public Visita getVisitaModified() {
        return visitaModified.get();
    }

    public ObjectProperty<Visita> visitaModifiedProperty() {
        return visitaModified;
    }

    public BorderPane getRoot() {
        return root;
    }
}
