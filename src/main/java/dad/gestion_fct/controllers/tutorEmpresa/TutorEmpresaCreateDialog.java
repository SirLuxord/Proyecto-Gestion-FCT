package dad.gestion_fct.controllers.tutorEmpresa;

import dad.gestion_fct.models.Empresa;
import dad.gestion_fct.models.TutorEmpresa;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TutorEmpresaCreateDialog extends Dialog<TutorEmpresa> implements Initializable {

    // model

    private ObjectProperty<TutorEmpresa> tutor = new SimpleObjectProperty<>(new TutorEmpresa());

    // view

    @FXML
    private ComboBox<Empresa> empresaCombo;

    @FXML
    private TextField mailField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField surnameField;

    @FXML
    private GridPane root;

    public TutorEmpresaCreateDialog(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tutorEmpresa/createTutorEmpresaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // init dialog

        setTitle("Crear");
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().setAll(new ButtonType("Crear" , ButtonBar.ButtonData.OK_DONE) , ButtonType.CANCEL);
        setResultConverter(this::onResult);

        // bindings

        empresaCombo.getSelectionModel().selectedItemProperty().addListener((o , ov ,nv) -> {
            if (nv != null){
                tutor.get().setIdEmpresa(nv.getIdEmpresa());
                tutor.get().setNombreEmpresa(nv.getNombre());
            }
        });

        tutor.get().nombreProperty().bind(nameField.textProperty());
        tutor.get().apellidosProperty().bind(surnameField.textProperty());
        tutor.get().correoProperty().bind(mailField.textProperty());
        tutor.get().telefonoProperty().bind(phoneNumberField.textProperty());


    }

    private TutorEmpresa onResult(ButtonType buttonType) {
        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE){
            return tutor.get();
        }
        return null;
    }

    public ComboBox<Empresa> getEmpresaCombo() {
        return empresaCombo;
    }

    public void setEmpresaCombo(ComboBox<Empresa> empresaCombo) {
        this.empresaCombo = empresaCombo;
    }
}
