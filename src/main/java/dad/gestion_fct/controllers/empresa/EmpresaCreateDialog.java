package dad.gestion_fct.controllers.empresa;

import dad.gestion_fct.models.Empresa;
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

public class EmpresaCreateDialog extends Dialog<Empresa> implements Initializable {

    // model

    private ObjectProperty<Empresa> empresa = new SimpleObjectProperty<>(new Empresa());

    // view

    @FXML
    private TextField adressField;

    @FXML
    private TextField cpField;

    @FXML
    private TextField localityField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField nifField;

    @FXML
    private CheckBox publicCheckBox;

    @FXML
    private GridPane root;

    public EmpresaCreateDialog(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/empresa/modifyEmpresaView.fxml"));
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

        empresa.get().nifEmpresaProperty().bind(nifField.textProperty());
        empresa.get().nombreProperty().bind(nameField.textProperty());
        empresa.get().dirrecionProperty().bind(adressField.textProperty());
        empresa.get().localidadProperty().bind(localityField.textProperty());
        empresa.get().codigoPostalProperty().bind(cpField.textProperty());
        empresa.get().publicaProperty().bind(publicCheckBox.selectedProperty());


    }

    private Empresa onResult(ButtonType buttonType) {
        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE){
            return empresa.get();
        }
        return null;
    }
}
