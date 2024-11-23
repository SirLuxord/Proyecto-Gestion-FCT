package dad.gestion_fct.controllers.empresa;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.models.Empresa;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        empresaController.getSplitEmpresa().getItems().remove(getRoot());
    }

    @FXML
    void onModifyAction(ActionEvent event) {

        // Antes de hacer el update se comprueba que ni el nif ni el nombre estén vacíos.

        if (empresa.get().getNifEmpresa().trim().isEmpty()) {
            empresaController.mostrarError("El nif de la empresa no puede estar vacío.");
            throw new IllegalArgumentException("El nif de la empresa no puede estar vacío.");
        }
        if (empresa.get().getNombre().trim().isEmpty()) {
            empresaController.mostrarError("El nombre de la empresa no puede estar vacío.");
            throw new IllegalArgumentException("El nombre de la empresa no puede estar vacío.");
        }

        String query = "Update Empresa set NIFEmpresa = ? , NombreEmpresa = ? , DireccionEmpresa = ? , LocalidadEmpresa = ? , CPEmpresa = ? , EmpresaPublica = ? where IdEmpresa = ?";
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1 , empresa.get().getNifEmpresa());
            statement.setString(2 , empresa.get().getNombre());
            statement.setString(3 , empresa.get().getDirrecion());
            statement.setString(4 , empresa.get().getLocalidad());
            statement.setString(5 , empresa.get().getCodigoPostal());
            statement.setBoolean(6 , empresa.get().isPublica());
            statement.setInt(7 , empresa.get().getIdEmpresa());

            statement.execute();

            // se le pasa al controlador de empresa los datos del registro seleccionado

            empresaController.getEmpresaSeleccionada().setIdEmpresa(empresa.get().getIdEmpresa());
            empresaController.getEmpresaSeleccionada().setNifEmpresa(empresa.get().getNifEmpresa());
            empresaController.getEmpresaSeleccionada().setNombre(empresa.get().getNombre());
            empresaController.getEmpresaSeleccionada().setDirrecion(empresa.get().getDirrecion());
            empresaController.getEmpresaSeleccionada().setLocalidad(empresa.get().getLocalidad());
            empresaController.getEmpresaSeleccionada().setCodigoPostal(empresa.get().getCodigoPostal());
            empresaController.getEmpresaSeleccionada().setPublica(empresa.get().isPublica());

            empresaController.getSplitEmpresa().getItems().remove(getRoot());

        }  catch (SQLException e) {
            empresaController.mostrarError(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

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
