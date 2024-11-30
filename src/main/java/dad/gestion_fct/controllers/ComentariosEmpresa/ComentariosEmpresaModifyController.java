package dad.gestion_fct.controllers.ComentariosEmpresa;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.models.ComentariosEmpresa;
import dad.gestion_fct.models.Empresa;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ComentariosEmpresaModifyController implements Initializable {
    private final ComentariosEmpresaController comentariosEmpresaController;
    private final ObjectProperty<ComentariosEmpresa> comentario = new SimpleObjectProperty<>();

    @FXML
    private ComboBox<Empresa> empresaCombo;
    @FXML
    private TextField comentarioField;

    @FXML
    private DatePicker fechaComentario;

    @FXML
    private TextField nombreDocenteField;

    @FXML
    private BorderPane root;

    @FXML
    private TextField telefonoDocenteField;

    @FXML
    void onCancelAction(ActionEvent event) {

        comentariosEmpresaController.getSplitComentariosEmpresa().getItems().remove(getRoot());
    }

    public ComboBox<Empresa> getEmpresaCombo() {
        return empresaCombo;
    }

    public void setEmpresaCombo(ComboBox<Empresa> empresaCombo) {
        this.empresaCombo = empresaCombo;
    }

    public BorderPane getRoot() {
        return root;
    }

    public void setComentario(ComentariosEmpresa comentario) {
        this.comentario.set(comentario);
    }

    public ComentariosEmpresa getComentario() {
        return comentario.get();
    }

    @FXML
    void onModifyAction(ActionEvent event) {
        //verificaciones
        if (empresaCombo.getSelectionModel().getSelectedItem() == null) {
            mostrarAlertaError("Selección incompleta", "Debe seleccionar una empresa de la lista.");
            return;
        } else if (nombreDocenteField.getText().trim().isEmpty()) {
            mostrarAlertaError("Selección incompleta", "Debe ingresar el nombre del docente.");
            return;
        } else if (comentarioField.getText().trim().isEmpty()) {
            mostrarAlertaError("Comentario vacío", "Debe ingresar un comentario.");
            return;
        }
        comentariosEmpresaController.buscarIdDocente(comentario.get().getTelefonoDocente());

        String query = "UPDATE ComentariosCaptacionEmpresa SET FechaComentario = ?, IdEmpresa = ?, IdDocente = ?, Comentarios = ? WHERE IdComentario = ?";
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, java.sql.Date.valueOf(getComentario().getFechaComentario()));
            statement.setInt(2, comentario.get().getIdEmpresa());
            statement.setInt(3, comentario.get().getIdDocente());
            statement.setString(4, getComentario().getComentarios());
            statement.setInt(5, comentario.get().getIdComentario());

            statement.execute();

            comentariosEmpresaController.getSelectedComentariosEmpresa().setIdComentario(comentario.get().getIdComentario());
            comentariosEmpresaController.getSelectedComentariosEmpresa().setFechaComentario(comentario.get().getFechaComentario());
            comentariosEmpresaController.getSelectedComentariosEmpresa().setIdEmpresa(comentario.get().getIdEmpresa());
            comentariosEmpresaController.getSelectedComentariosEmpresa().setIdDocente(comentario.get().getIdDocente());
            comentariosEmpresaController.getSelectedComentariosEmpresa().setComentarios(comentario.get().getComentarios());
            comentariosEmpresaController.getSelectedComentariosEmpresa().setNombreEmpresa(comentario.get().getNombreEmpresa());

            comentariosEmpresaController.getSplitComentariosEmpresa().getItems().remove(getRoot());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ComentariosEmpresaModifyController(ComentariosEmpresaController comentariosEmpresaController) {
        try {
            this.comentariosEmpresaController = comentariosEmpresaController;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/comentariosEmpresa/modifyComentariosEmpresaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        empresaCombo.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (nv != null) {
                comentario.get().setIdEmpresa(nv.getIdEmpresa());
                comentario.get().setNombreEmpresa(nv.getNombre());
            }
        });

        comentario.addListener((o, ov, nv) -> {
            if (ov != null) {
                nombreDocenteField.textProperty().unbindBidirectional(ov.nombreDocenteProperty());
                telefonoDocenteField.textProperty().unbindBidirectional(ov.telefonoDocenteProperty());
                comentarioField.textProperty().unbindBidirectional(ov.comentariosProperty());
                fechaComentario.valueProperty().unbindBidirectional(ov.fechaComentarioProperty());

            }

            if (nv != null) {
                nombreDocenteField.textProperty().bindBidirectional(nv.nombreDocenteProperty());
                telefonoDocenteField.textProperty().bindBidirectional(nv.telefonoDocenteProperty());
                comentarioField.textProperty().bindBidirectional(nv.comentariosProperty());
                fechaComentario.valueProperty().bindBidirectional(nv.fechaComentarioProperty());


            }
        });

    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de validación");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

