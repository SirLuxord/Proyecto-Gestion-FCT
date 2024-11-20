package dad.gestion_fct.controllers.empresa;

import dad.gestion_fct.models.Empresa;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmpresaModifyController implements Initializable {

    // controller

    private final EmpresaController empresaController;

    // model

    private ObjectProperty<Empresa> empresa = new SimpleObjectProperty<>();

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
    private BorderPane root;

    public EmpresaModifyController(EmpresaController empresaController){
        try{
            this.empresaController = empresaController;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/empresa/modifyEmpresaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Bindings

        empresa.addListener( (o , ov , nv) -> {
            if (ov != null){
                nifField.textProperty().unbindBidirectional(ov.nifEmpresaProperty());
                nameField.textProperty().unbindBidirectional(ov.nombreProperty());
                localityField.textProperty().unbindBidirectional(ov.localidadProperty());
                cpField.textProperty().unbindBidirectional(ov.codigoPostalProperty());
                adressField.textProperty().unbindBidirectional(ov.dirrecionProperty());
                publicCheckBox.selectedProperty().unbindBidirectional(ov.publicaProperty());
            }
            if (nv != null){
                nifField.textProperty().bindBidirectional(nv.nifEmpresaProperty());
                nameField.textProperty().bindBidirectional(nv.nombreProperty());
                localityField.textProperty().bindBidirectional(nv.localidadProperty());
                cpField.textProperty().bindBidirectional(nv.codigoPostalProperty());
                adressField.textProperty().bindBidirectional(nv.dirrecionProperty());
                publicCheckBox.selectedProperty().bindBidirectional(nv.publicaProperty());
            }
        });

    }

    @FXML
    void onCancelAction(ActionEvent event) {
        empresaController.getSplitEmpresa().getItems().remove(this);
    }

    @FXML
    void onModifyAction(ActionEvent event) {
        // TODO
    }

    public BorderPane getRoot() {
        return root;
    }

    public Empresa getEmpresa() {
        return empresa.get();
    }

    public ObjectProperty<Empresa> empresaProperty() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa.set(empresa);
    }
}
