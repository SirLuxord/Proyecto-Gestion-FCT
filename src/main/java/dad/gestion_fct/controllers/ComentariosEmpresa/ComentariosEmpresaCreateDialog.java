package dad.gestion_fct.controllers.ComentariosEmpresa;

import dad.gestion_fct.models.ComentariosEmpresa;
import dad.gestion_fct.models.ContactoEmp;
import dad.gestion_fct.models.Empresa;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ComentariosEmpresaCreateDialog extends Dialog<ComentariosEmpresa> implements Initializable {

    private ObjectProperty<ComentariosEmpresa> comentario = new SimpleObjectProperty<>(new ComentariosEmpresa());


    @FXML
    private TextField comentarioField;

    @FXML
    private ComboBox<Empresa> empresaCombo;

    @FXML
    private DatePicker fechaComentario;

    @FXML
    private TextField nombreDocenteField;

    @FXML
    private TextField telefonoDocenteField;
    @FXML
    private GridPane root;

    @FXML
    void onFechaComentario(ActionEvent event) {
    }

    public ComentariosEmpresaCreateDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/comentariosEmpresa/createComentariosEmpresaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTitle("Crear");
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().setAll(new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);
        setResultConverter(this::onResult);


        // bindings
        empresaCombo.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (nv != null) {

                comentario.get().setIdEmpresa(nv.getIdEmpresa());
                comentario.get().setNombreEmpresa(nv.getNombre());
            }
        });

        comentario.get().fechaComentarioProperty().bind(fechaComentario.valueProperty());
        comentario.get().comentariosProperty().bind(comentarioField.textProperty());
        comentario.get().nombreDocenteProperty().bind(nombreDocenteField.textProperty());
        comentario.get().telefonoDocenteProperty().bind(telefonoDocenteField.textProperty());
        //terminar
    }
    private ComentariosEmpresa onResult(ButtonType buttonType) {
        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE){
            return comentario.get();
        }
        return null;
    }
    public ComboBox<Empresa> getEmpresaCombo() {
        return empresaCombo;
    }
}
