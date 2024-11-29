package dad.gestion_fct.controllers.ComentariosEmpresa;

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
import java.util.ResourceBundle;

public class ComentariosEmpresaModifyController implements Initializable {
    private ComentariosEmpresaController comentariosEmpresaController;
    private ObjectProperty<ComentariosEmpresa> comentario = new SimpleObjectProperty<>();

    @FXML
    private TextField comentarioField;

    @FXML
    private DatePicker fechaComentario;

    @FXML
    private TextField mailField;

    @FXML
    private TextField nombreDocenteField;

    @FXML
    private BorderPane root;

    @FXML
    private Label telefonoDocenteField;

    @FXML
    void onCancelAction(ActionEvent event) {
        comentariosEmpresaController.getSplitComentariosEmpresa().getItems().remove(getRoot());

    }


    public ComboBox<Empresa> getEmpresaCombo() {
        return empresaCombo;
    }

    public void setComentario(ComentariosEmpresa comentario) {
        this.comentario.set(comentario);
    }

    public void getComentario() {
        comentario.get();
    }

    public ComentariosEmpresa getComentariosEmpresa() {
        return comentario.get();
    }

    @FXML
    private ComboBox<Empresa> empresaCombo;

    public void setEmpresaCombo(ComboBox<Empresa> empresaCombo) {
        this.empresaCombo = empresaCombo;
    }

    @FXML
    void onModifyAction(ActionEvent event) {
        if (empresaCombo.getSelectionModel().getSelectedItem() == null) {
            mostrarAlertaError("Selección incompleta", "Debe seleccionar una empresa de la lista.");
            return;
        }

    }


    public BorderPane getRoot() {
        return root;
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


    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de validación");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}