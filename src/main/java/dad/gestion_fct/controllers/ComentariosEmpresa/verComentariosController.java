package dad.gestion_fct.controllers.ComentariosEmpresa;

import dad.gestion_fct.models.ComentariosEmpresa;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class verComentariosController implements Initializable {
    private ObjectProperty<ComentariosEmpresa> comentarioSeleccionado = new SimpleObjectProperty<>();

    @FXML
    private TextArea observacionesTextArea;

    @FXML
    private GridPane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Vincular el TextArea al comentario seleccionado
        comentarioSeleccionado.addListener((o, ov, nv) -> {
            if (ov != null) {
                observacionesTextArea.textProperty().unbindBidirectional(ov.comentariosProperty());
            }
            if (nv != null) {
                observacionesTextArea.textProperty().bindBidirectional(nv.comentariosProperty());
            } else {
                observacionesTextArea.clear();
            }
        });
    }

    // Constructor que carga el FXML y configura el controlador
    public verComentariosController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ComentariosEmpresa/verComentariosView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar el FXML", e);
        }
    }

    // Método para vincular la tabla de comentarios al controlador
    public void bindToCommentsTable(TableView<ComentariosEmpresa> comentariosTable) {
        // Vincula la propiedad del comentario seleccionado a la selección de la tabla
        comentarioSeleccionado.bind(comentariosTable.getSelectionModel().selectedItemProperty());
    }

    // Getter para obtener el GridPane raíz
    public GridPane getRoot() {
        return root;
    }
}
