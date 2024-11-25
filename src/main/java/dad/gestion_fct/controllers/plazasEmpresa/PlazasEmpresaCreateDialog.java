package dad.gestion_fct.controllers.plazasEmpresa;

import dad.gestion_fct.models.Ciclos;
import dad.gestion_fct.models.Empresa;
import dad.gestion_fct.models.PlazasEmpresa;
import dad.gestion_fct.models.TutorEmpresa;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import javax.script.Bindings;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlazasEmpresaCreateDialog extends Dialog<PlazasEmpresa> implements Initializable {

    // model

    private ObjectProperty<PlazasEmpresa> plaza = new SimpleObjectProperty<>(new PlazasEmpresa());

    // view

    @FXML
    private ComboBox<Ciclos> cicloCombo;

    @FXML
    private ComboBox<Empresa> empresaCombo;

    @FXML
    private TextField numeroPlazas;

    @FXML
    private GridPane root;

    public PlazasEmpresaCreateDialog(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plazasEmpresa/createPlazasEmpresaView.fxml"));
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
                plaza.get().setIdEmpresa(nv.getIdEmpresa());
                plaza.get().setNombreEmpresa(nv.getNombre());
            }
        });

        cicloCombo.getSelectionModel().selectedItemProperty().addListener((o , ov ,nv) -> {
            if (nv != null){
                plaza.get().setNombreCiclo(nv.toString());
            }
        });

        // populate comboBox
        cicloCombo.getItems().setAll(Ciclos.values());

        numeroPlazas.textProperty().bindBidirectional(plaza.get().numeroPlazasProperty() , new NumberStringConverter());


    }

    private PlazasEmpresa onResult(ButtonType buttonType) {
        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE){
            return plaza.get();
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